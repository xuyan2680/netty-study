package com.alex.st0.transport;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alex.st0.codec.ProtocolDecoder;
import com.alex.st0.codec.ProtocolEncoder;
import com.alex.st0.handler.BizServerHandler;
import com.alex.st0.registry.RegistryService;
import com.alex.st0.rpc.IProcessor;
import com.alex.st0.rpc.ProcessorImpl;
import com.alex.st0.utils.IpUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 服务启动类
 * 
 * @author xuykj
 *
 */
public class Server {
	private int port;
	private Map<String, Object> serviceBean;

	public Server(int port, Map<String, Object> serviceBean) {
		this.port = port;
		this.serviceBean = serviceBean;
	}

	public void init() {
		final IProcessor iProcessor = new ProcessorImpl(this.serviceBean);
		EventLoopGroup boss = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap().group(boss, worker).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 256).childOption(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast("log", new LoggingHandler(LogLevel.INFO));
							ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
							ch.pipeline().addLast("decoder", new ProtocolDecoder());
							ch.pipeline().addLast("encoder", new ProtocolEncoder());
							ch.pipeline().addLast(new AcceptorIdleStateTrigger());
							ch.pipeline().addLast(new BizServerHandler(iProcessor));
						}
					});
			ChannelFuture future = bootstrap.bind(this.port).sync();
			Set<String> keys = this.serviceBean.keySet();
			for (String key : keys) {
				RegistryService.getInstance().register(key, IpUtil.getAddress(this.port));
			}
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
