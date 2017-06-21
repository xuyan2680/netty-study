package com.alex.st0.rpc;

import com.alex.st0.codec.Header;
import com.alex.st0.codec.RequestPacket;
import com.alex.st0.transport.ConnectHolders;
import com.alibaba.fastjson.JSON;

import io.netty.channel.Channel;

/**
 * rpc 客户端
 * 
 * @author xuykj
 *
 */
public class NettyRpcClient {
	public static synchronized Channel getChannel(String registerKey) throws Exception {
		Channel ch = ConnectHolders.getInstance().getConnect(registerKey);
		if (ch == null) {
			throw new RuntimeException(registerKey + " 服务连接池为空");
		}
		return ch;
	}

	/**
	 * 发送rpc请求包
	 * 
	 * @param request
	 * @return
	 */
	public static RpcResponse send(RpcRequest request) {
		try {
			RpcCallbackFuture future = new RpcCallbackFuture(request);
			Channel ch = getChannel(request.getClassName());
			RequestPacket requestInfo = new RequestPacket();
			Header header = new Header();
			header.setType(Header.REQUEST);
			String reqdata = JSON.toJSONString(request);
			byte[] data = reqdata.getBytes("utf-8");
			requestInfo.setHeader(header);
			requestInfo.setData(data);
			ch.writeAndFlush(requestInfo).sync();
			return future.get(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
