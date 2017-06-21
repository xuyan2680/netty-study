package com.alex.st0.handler;

import java.io.UnsupportedEncodingException;

import com.alex.st0.codec.ResponsePacket;
import com.alex.st0.rpc.RpcCallbackFuture;
import com.alex.st0.rpc.RpcResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 客户端业务处理
 * 
 * @author xuykj
 *
 */
public class BizClientHandler extends ChannelInboundHandlerAdapter {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(BizClientHandler.class);
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("客户端连接成功");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ResponsePacket r = (ResponsePacket) msg;
		RpcResponse response = null;
		try {
			response = JSON.parseObject(new String(r.getData(), "utf-8"), new TypeReference<RpcResponse>() {
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RpcCallbackFuture future = RpcCallbackFuture.futurePool.get(response.getRequestId());
		future.setResponse(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		Channel ch = ctx.channel();
		ch.close();
	}
}
