package com.alex.st0.codec;

/**
 * 心跳包
 * 
 * @author xuykj
 *
 */
public class HeartBeatPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	/**
	 * 构建心跳包
	 * 
	 * @return
	 */
	public static HeartBeatPacket buildHeartBeat() {
		HeartBeatPacket hb = new HeartBeatPacket();
		Header h = new Header();
		h.setType(Header.HEARTBEAT);
		h.setLength(0);
		hb.setHeader(h);
		return hb;
	}
}
