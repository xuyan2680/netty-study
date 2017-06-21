package com.alex.st0.rpc;

import com.alex.st0.codec.RequestPacket;
import com.alex.st0.codec.ResponsePacket;
/**
 * 底层通信协议与rpc协议转换
 * @author xuykj
 *
 */
public interface IProcessor {

	public ResponsePacket handle(RequestPacket requestPacket) throws Exception;
}
