package com.alex.st0.registry;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 服务注册
 * 
 * @author xuykj
 *
 */
public class RegistryService {
	private static RegistryService instance = new RegistryService();
	private ZkClient zkClient = null;

	private RegistryService() {
		String ZKServers = "172.28.0.45:2181";
		zkClient = new ZkClient(ZKServers, 10000, 10000, new SerializableSerializer());
	}

	public static RegistryService getInstance() {
		return instance;
	}

	public void register(String registerKey, String address) {
		String servicePath = ZkEnv.ZK_RPC.concat("/").concat(registerKey);
		zkClient.createPersistent(servicePath, true);
		String realPath = servicePath.concat("/").concat(address);
		zkClient.createEphemeral(realPath);
	}
}
