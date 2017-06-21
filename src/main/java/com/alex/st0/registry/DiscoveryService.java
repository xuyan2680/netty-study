package com.alex.st0.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 服务发现
 * 
 * @author xuykj
 *
 */
public class DiscoveryService {
	private static DiscoveryService instance = new DiscoveryService();
	private ZkClient zkClient = null;
	private Map<String, List<String>> connectHolds = new ConcurrentHashMap<String, List<String>>();

	private DiscoveryService() {
		String ZKServers = "172.28.0.45:2181";
		zkClient = new ZkClient(ZKServers, 10000, 10000, new SerializableSerializer());
	}

	public static DiscoveryService getInstance() {
		return instance;
	}

	public List<String> getServices(final String registerKey) {
		if (!connectHolds.containsKey(registerKey)) {
			List<String> paths = zkClient.getChildren(ZkEnv.ZK_RPC.concat("/" + registerKey));
			connectHolds.put(registerKey, paths);
			zkClient.subscribeChildChanges(ZkEnv.ZK_RPC.concat("/" + registerKey), new IZkChildListener() {
				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					connectHolds.put(registerKey, currentChilds);
				}
			});
		}
		return connectHolds.get(registerKey);
	}
}
