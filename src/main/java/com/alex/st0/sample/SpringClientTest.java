package com.alex.st0.sample;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * spring rpc 客户端测试
 * @author xuykj
 *
 */
public class SpringClientTest {

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "classpath:spring-consumer.xml" });
		IHello ihello = (IHello)ctx.getBean("iHello");
		long start = System.currentTimeMillis();
		for(int i=0;i<1000;i++){
			ihello.say("rpc"+i);
//			Thread.sleep(200);
		}
		ISay isay = (ISay)ctx.getBean("iSay");
		for(int i=0;i<1000;i++){
			isay.say("otherrpc"+i);
//			Thread.sleep(200);
		}
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000);
	}

}
