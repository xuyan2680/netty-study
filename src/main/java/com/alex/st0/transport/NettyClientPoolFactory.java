package com.alex.st0.transport;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 连接对象工厂
 * 
 * @author xuykj
 *
 */
public class NettyClientPoolFactory extends BasePooledObjectFactory<Client> {

	private String host;
	private int port;

	public NettyClientPoolFactory(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public Client create() throws Exception {
		Client client = new Client();
		client.connect(host, port);
		return client;
	}

	@Override
	public PooledObject<Client> wrap(Client arg0) {
		return new DefaultPooledObject<Client>(arg0);
	}

	@Override
	public void destroyObject(PooledObject<Client> p) throws Exception {
		Client client = p.getObject();
		client.close();
	}

	@Override
	public boolean validateObject(PooledObject<Client> p) {
		Client client = p.getObject();
		return client.isValidate();
	}

}
