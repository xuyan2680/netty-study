package com.alex.st0.spring.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.alex.st0.rpc.ProxyFactory;

public class SpringClient<T> implements FactoryBean<T>, InitializingBean {
	private Class<T> interfaceClass;
	private transient T proxy;

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		proxy = (T) ProxyFactory.getObject(interfaceClass);
	}

	@Override
	public T getObject() throws Exception {
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public T getProxy() {
		return proxy;
	}

	public void setProxy(T proxy) {
		this.proxy = proxy;
	}

}
