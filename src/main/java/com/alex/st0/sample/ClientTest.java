package com.alex.st0.sample;

import com.alex.st0.rpc.ProxyFactory;

public class ClientTest {

	public static void main(String[] args) {
		try {
			IHello ihello = (IHello)ProxyFactory.getObject(IHello.class);
			for(int i=0;i<1000;i++){
				String reqStr = "rpc 测试"+i;
				System.out.println(reqStr);
				String str = ihello.say(reqStr);
				System.out.println(str);
//				Thread.sleep(400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
