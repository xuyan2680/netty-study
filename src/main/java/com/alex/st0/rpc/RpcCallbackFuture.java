package com.alex.st0.rpc;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

/**
 * rpc回调
 * @author xuykj
 *
 */
public class RpcCallbackFuture {
	public static ConcurrentMap<String, RpcCallbackFuture> futurePool = new ConcurrentHashMap<String, RpcCallbackFuture>();	// 过期，失效
	
	// net codec
	private RpcRequest request;
	private RpcResponse response;
	// future lock
	private boolean isDone = false;
	private Object lock = new Object();
	
	public RpcCallbackFuture(RpcRequest request) {
		this.request = request;
		futurePool.put(request.getRequestId(), this);
	}
	public RpcResponse getResponse() {
		return response;
	}
	public void setResponse(RpcResponse response) {
		this.response = response;
		// notify future lock
		synchronized (lock) {
			isDone = true;
			lock.notifyAll();
		}
	}

	public RpcResponse get(long timeoutMillis) throws InterruptedException, TimeoutException{
		if (!isDone) {
			synchronized (lock) {
				try {
					lock.wait(timeoutMillis);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		
		if (!isDone) {
			throw new TimeoutException(MessageFormat.format("rpc 请求超时:{0}, request:{1}", System.currentTimeMillis(), request.toString()));
		}
		futurePool.remove(response.getRequestId());
		return response;
	}
}
