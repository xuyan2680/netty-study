package com.alex.st0.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.Signal;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
/**
 * 协议解码类
 * @author xuykj
 *
 */
public class ProtocolDecoder extends ReplayingDecoder<ProtocolDecoder.State> {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ProtocolDecoder.class);
	private final Header header = new Header();

	public ProtocolDecoder() {
		super(State.HEADER_TYPE);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch (state()) {
		case HEADER_TYPE:
			header.setType(in.readByte());
			checkpoint(State.HEADER_LENGTH);
		case HEADER_LENGTH:
			header.setLength(in.readInt());
			checkpoint(State.BODY);
		case BODY:
			switch (header.getType()) {
			case Header.HEARTBEAT:
				logger.info("心跳包");
				break;
			case Header.REQUEST:
				byte[] bytes = new byte[header.getLength()];
				in.readBytes(bytes);
				RequestPacket req = new RequestPacket();
				req.setData(bytes);
				out.add(req);
				break;
			case Header.RESPONSE:
				byte[] bytesr = new byte[header.getLength()];
				in.readBytes(bytesr);
				ResponsePacket resp = new ResponsePacket();
				resp.setData(bytesr);
				out.add(resp);
				break;
			default:
				throw Signal.valueOf("协议数据错误");
			}

			checkpoint(State.HEADER_TYPE);
			break;
		default:
			throw Signal.valueOf("协议数据错误");
		}
	}

	enum State {
		HEADER_TYPE, HEADER_ID, HEADER_LENGTH, BODY
	}
}
