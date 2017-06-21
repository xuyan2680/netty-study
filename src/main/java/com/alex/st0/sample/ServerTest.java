package com.alex.st0.sample;

import java.util.HashMap;
import java.util.Map;

import com.alex.st0.transport.Server;

/**
 * main启动netty rpc服务
 * 
 * @author xuykj
 *
 */
public class ServerTest {

	public static void main(String[] args) {
		// 本地提供服务的bean
		Map<String, Object> serverBean = new HashMap<String, Object>();
		// 此处可以通spring容器初始化
		serverBean.put(IHello.class.getName(), new HelloImpl());
		// InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
		Server server = new Server(20000, serverBean);
		server.init();
	}

}
