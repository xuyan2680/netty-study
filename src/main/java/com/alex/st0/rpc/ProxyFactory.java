package com.alex.st0.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
/**
 * 代理工厂
 * @author xuykj
 *
 */
public class ProxyFactory {
	public static Object getObject(Class<?> iface) throws Exception {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { iface },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						RpcRequest request = new RpcRequest();
						request.setRequestId(UUID.randomUUID().toString());
						request.setCreateMillisTime(System.currentTimeMillis());
						request.setClassName(method.getDeclaringClass().getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);

						if (request.getRegistryKey() == null) {
							request.setRegistryKey(request.getClassName());
						}
						// send
						RpcResponse response = NettyRpcClient.send(request);
						// valid response
						if (response == null) {
							throw new Exception("rpc 响应为空");
						}
						if (response.isError()) {
							throw response.getError();
						} else {
							return response.getResult();
						}

					}
				});
	}
}
