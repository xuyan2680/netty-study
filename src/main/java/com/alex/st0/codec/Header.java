package com.alex.st0.codec;
/**
 * 消息头定义
 * @author xuykj
 *
 */
public class Header {
	public static final byte REQUEST = 0x01; // Request
	public static final byte RESPONSE = 0x02; // Response
	public static final byte PUBLISH_SERVICE = 0x03; // 发布服务
	public static final byte PUBLISH_CANCEL_SERVICE = 0x04; // 取消发布服务
	public static final byte SUBSCRIBE_SERVICE = 0x05; // 订阅服务
	public static final byte OFFLINE_NOTICE = 0x06; // 通知下线
	public static final byte ACK = 0x07; // Acknowledge
	public static final byte HEARTBEAT = 0x0f; // Heartbeat
	private byte type;// 消息类型
	private int length;// 消息长度

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
