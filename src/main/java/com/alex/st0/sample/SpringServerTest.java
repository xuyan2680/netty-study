package com.alex.st0.sample;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * spring 启动netty rpc服务
 * @author xuykj
 *
 */
public class SpringServerTest {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "classpath:spring-provider.xml" });
	}
}
