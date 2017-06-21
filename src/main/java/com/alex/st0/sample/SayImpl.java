package com.alex.st0.sample;

import org.springframework.stereotype.Service;

import com.alex.st0.spring.support.annotation.RpcService;

@RpcService(ISay.class)
@Service
public class SayImpl implements ISay {

	@Override
	public String say(String i) {
		System.out.println(i);
		return i;
	}

}
