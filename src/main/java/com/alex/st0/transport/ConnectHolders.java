package com.alex.st0.transport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alex.st0.registry.DiscoveryService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 客户端连接持有类
 * 
 * @author xuykj
 *
 */
public class ConnectHolders {
	private Map<String, Channel> connectHolds = new ConcurrentHashMap<String, Channel>();
	private static ConnectHolders ch = new ConnectHolders();

	private ConnectHolders() {

	}

	public void addConnect(final String ipPort, Channel ch) {
		connectHolds.put(ipPort, ch);
		ch.closeFuture().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				connectHolds.remove(ipPort);
			}
		});

	}

	public Channel getConnect(String registerKey) throws Exception {
		List<String> address = DiscoveryService.getInstance().getServices(registerKey);
		if (address.size() == 0)
			throw new RuntimeException(registerKey + " 未发现服务");
		int index = (int) (Math.random() * address.size());
		String path = address.get(index);
		if (!connectHolds.containsKey(path)) {
			Client client = new Client();
			String args[] = path.split(":");
			client.connect(args[0], Integer.parseInt(args[1]));
		}
		return connectHolds.get(path);
	}

	public static ConnectHolders getInstance() {
		return ch;
	}
}
