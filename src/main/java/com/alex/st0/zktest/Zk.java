package com.alex.st0.zktest;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.WatchedEvent;

/**
 * 经典单例模型
 * 
 * @author xuykj
 *
 */
public class Zk {
	private Zk() {
		init();
	}

	private ZkClient zkClient = null;

	public void init() {
		String ZKServers = "172.28.0.45:2181";
		zkClient = null;
		final Zk that = this;
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		zkClient = new ZkClient(ZKServers, 10000, 10000, new SerializableSerializer()) {
			@Override
			public void process(WatchedEvent watchedEvent) {
				super.process(watchedEvent);
				countDownLatch.countDown();
				if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
					that.init();
				}
				if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
					System.out.println("收到连接事件");
				}
			}
		};
		try {
			countDownLatch.await();
			System.out.println("连接成功");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class ZkContain {
		private static Zk zk = new Zk();
	}

	public static Zk getInstance() {
		return ZkContain.zk;
	}

	public static void main(String[] args) {
		Zk zk = Zk.getInstance();
		List<String> dirs = zk.zkClient.getChildren("/");
		System.out.println(dirs);
	}
}
