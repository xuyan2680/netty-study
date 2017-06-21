package com.alex.st0.handler;

import com.alex.st0.codec.RequestPacket;
import com.alex.st0.codec.ResponsePacket;
import com.alex.st0.rpc.IProcessor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 服务端业务处理
 * 
 * @author xuykj
 *
 */
public class BizServerHandler extends ChannelInboundHandlerAdapter {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(BizServerHandler.class);
	private IProcessor iProcessor;

	public BizServerHandler(IProcessor iProcessor) {
		super();
		this.iProcessor = iProcessor;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("客户端链路建立成功");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		RequestPacket request = (RequestPacket) msg;
		ResponsePacket response = null;
		try {
			response = iProcessor.handle(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ctx.channel().writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		Channel ch = ctx.channel();
		ch.close();
	}
}
