package com.alex.st0.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import com.alex.st0.codec.ProtocolDecoder;
import com.alex.st0.codec.ProtocolEncoder;
import com.alex.st0.handler.BizClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;

/**
 * 客户端连接类
 * 
 * @author xuykj
 *
 */
public class Client {
	private EventLoopGroup group;
	final HashedWheelTimer timer = new HashedWheelTimer();

	private Bootstrap init() {
		group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true);
		return bootstrap;
	}

	private SocketAddress getSocketAddress(String ip, int port) {
		return InetSocketAddress.createUnresolved(ip, port);
	}

	public void connect(String ip, int port) throws Exception {
		Bootstrap bootstrap = init();
		SocketAddress socketAddress = getSocketAddress(ip, port);
		final ConnectionWatchdog watchdog = new ConnectionWatchdog(bootstrap, timer, socketAddress) {
			@Override
			public ChannelHandler[] handlers() {
				return new ChannelHandler[] { this, new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS),
						new ProtocolDecoder(), new ProtocolEncoder(), new ConnectorIdleStateTrigger(),
						new BizClientHandler() };
			}
		};
		watchdog.start();
		bootstrap.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(watchdog.handlers());
			}
		});
		ChannelFuture cf = bootstrap.connect(socketAddress).sync();
		ConnectHolders.getInstance().addConnect(ip + ":" + port, cf.channel());
	}
}
