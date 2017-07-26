package com.alex.st0.rpc;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.alex.st0.codec.Header;
import com.alex.st0.codec.RequestPacket;
import com.alex.st0.transport.Client;
import com.alex.st0.transport.NettyClientPool;
import com.alibaba.fastjson.JSON;

/**
 * rpc 客户端
 * 
 * @author xuykj
 *
 */
public class NettyRpcClient {
	/**
	 * 发送rpc请求包
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static RpcResponse send(RpcRequest request) throws Exception {
		GenericObjectPool<Client> pool = NettyClientPool.getPool(request.getClassName());
		Client client = null;
		try {
			client = pool.borrowObject();
			RpcCallbackFuture future = new RpcCallbackFuture(request);
			RequestPacket requestInfo = new RequestPacket();
			Header header = new Header();
			header.setType(Header.REQUEST);
			String reqdata = JSON.toJSONString(request);
			byte[] data = reqdata.getBytes("utf-8");
			requestInfo.setHeader(header);
			requestInfo.setData(data);
			client.getChannel().writeAndFlush(requestInfo).sync();
			return future.get(5000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnObject(client);
		}
		return null;
	}
}
