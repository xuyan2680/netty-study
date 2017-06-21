package com.alex.st0.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 协议编码类
 * 
 * @author xuykj
 *
 */
@ChannelHandler.Sharable
public class ProtocolEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
		if (msg instanceof RequestPacket) {
			doEncodeRequest((RequestPacket) msg, out);
		} else if (msg instanceof ResponsePacket) {
			doEncodeResponse((ResponsePacket) msg, out);
		} else if (msg instanceof HeartBeatPacket) {
			doEncodeBeat((HeartBeatPacket) msg, out);
		} else {
			throw new IllegalArgumentException();
		}
	}

	private void doEncodeBeat(HeartBeatPacket request, ByteBuf out) {
		byte type = request.getHeader().getType();
		int length = 0;
		out.writeByte(type).writeInt(length);
	}

	private void doEncodeRequest(RequestPacket request, ByteBuf out) {
		byte type = request.getHeader().getType();
		byte[] bytes = request.getData();
		int length = bytes.length;
		out.writeByte(type).writeInt(length).writeBytes(bytes);
	}

	private void doEncodeResponse(ResponsePacket response, ByteBuf out) {
		byte type = response.getHeader().getType();
		byte[] bytes = response.getData();
		int length = bytes.length;
		out.writeByte(type).writeInt(length).writeBytes(bytes);
	}

}
