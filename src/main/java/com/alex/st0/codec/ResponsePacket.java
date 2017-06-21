package com.alex.st0.codec;

/**
 * 通信协议响应包
 * 
 * @author xuykj
 *
 */
public class ResponsePacket extends Packet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 协议头
	private Header header;
	// 请求数据
	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
}
