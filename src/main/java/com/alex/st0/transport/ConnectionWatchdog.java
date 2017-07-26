/*
 * Copyright (c) 2015 The Jupiter Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alex.st0.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 断线重连
 * @author xuykj
 *
 */
@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter
		implements TimerTask, ChannelHandlerHolder {

	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ConnectionWatchdog.class);

	private static final int ST_STARTED = 1;
	private static final int ST_STOPPED = 2;

	private final Bootstrap bootstrap;
	private final Timer timer;
	private final SocketAddress remoteAddress;

	private volatile int state;
	private int attempts;

	public ConnectionWatchdog(Bootstrap bootstrap, Timer timer, SocketAddress remoteAddress) {
		this.bootstrap = bootstrap;
		this.timer = timer;
		this.remoteAddress = remoteAddress;
		start();
	}

	public boolean isStarted() {
		return state == ST_STARTED;
	}

	public void start() {
		state = ST_STARTED;
	}

	public void stop() {
		state = ST_STOPPED;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();

		attempts = 0;

		logger.info("已连接 {}.", ch);

		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		boolean doReconnect = isReconnectNeeded();
		if (doReconnect) {
			if (attempts < 12) {
				attempts++;
			}
			long timeout = 2 << attempts;
			timer.newTimeout(this, timeout, TimeUnit.SECONDS);
		}

		logger.warn("断开连接 {}, 地址: {}, 重连: {}.", ctx.channel(), remoteAddress, doReconnect);

		ctx.fireChannelInactive();
	}

	@Override
	public void run(Timeout timeout) throws Exception {
		if (!isReconnectNeeded()) {
			logger.warn("取消重连 {}.", remoteAddress);
			return;
		}

		ChannelFuture future;
		synchronized (bootstrap) {
			bootstrap.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(handlers());
				}
			});
			future = bootstrap.connect(remoteAddress);
		}

		future.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture f) throws Exception {
				boolean succeed = f.isSuccess();

				logger.warn("重连 {}, {}.", remoteAddress, succeed ? "succeed" : "failed");

				if (!succeed) {
					f.channel().pipeline().fireChannelInactive();
				}
			}
		});
	}

	private boolean isReconnectNeeded() {
		return isStarted();
	}
}
