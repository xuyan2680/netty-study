package com.alex.st0.sample;

import org.springframework.stereotype.Service;

import com.alex.st0.spring.support.annotation.RpcService;

@RpcService(IHello.class)
@Service
public class HelloImpl implements IHello {

	@Override
	public String say(String str) {
		System.out.println("远程调用成功:" + str);
		return "SS:" + str;
	}

}
