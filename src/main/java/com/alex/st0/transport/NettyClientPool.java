package com.alex.st0.transport;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.alex.st0.registry.DiscoveryService;

/**
 * 连接池
 * 
 * @author xuykj
 *
 */
public class NettyClientPool {

	private GenericObjectPool<Client> pool;

	public NettyClientPool(String host, int port) {
		pool = new GenericObjectPool<Client>(new NettyClientPoolFactory(host, port));
		pool.setTestOnBorrow(true);
		pool.setMaxTotal(2);
	}

	public GenericObjectPool<Client> getPool() {
		return this.pool;
	}

	// serverAddress : [Client, Client]
	private static ConcurrentHashMap<String, NettyClientPool> clientPoolMap = new ConcurrentHashMap<String, NettyClientPool>();

	public static GenericObjectPool<Client> getPool(String className) throws Exception {
		List<String> address = DiscoveryService.getInstance().getServices(className);
		if (address.size() == 0)
			throw new RuntimeException(className + " 未发现服务");
		int index = (int) (Math.random() * address.size());
		String serverAddress = address.get(index);
		if (serverAddress == null || serverAddress.trim().length() == 0) {
			throw new IllegalArgumentException(">>>>>>>>>>>> serverAddress is null");
		}
		// get from pool
		NettyClientPool clientPool = clientPoolMap.get(serverAddress);
		if (clientPool != null) {
			return clientPool.getPool();
		}

		// init pool
		String[] array = serverAddress.split(":");
		String host = array[0];
		int port = Integer.parseInt(array[1]);

		clientPool = new NettyClientPool(host, port);
		clientPoolMap.put(serverAddress, clientPool);
		return clientPool.getPool();
	}

}
