package com.alex.st0.spring.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alex.st0.spring.support.annotation.RpcService;
import com.alex.st0.transport.Server;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
/**
 * spring netty rpc 服务启动类
 * @author xuykj
 *
 */
public class SpringServer implements InitializingBean, ApplicationContextAware {
	// 本地提供服务的bean
	public static Map<String, Object> serviceBean = new HashMap<String, Object>();
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(SpringServer.class);

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		Map<String, Object> serviceMap = arg0.getBeansWithAnnotation(RpcService.class);
		if (serviceMap != null && serviceMap.size() > 0) {
			for (Object sBean : serviceMap.values()) {
				String interfaceName = sBean.getClass().getAnnotation(RpcService.class).value().getName();
				serviceBean.put(interfaceName, sBean);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Server server = new Server(20000, serviceBean);
		logger.info("正在启动服务....");
		server.init();
	}

}
