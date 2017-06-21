package com.alex.st0.rpc;

import java.lang.reflect.Method;
import java.util.Map;

import com.alex.st0.codec.Header;
import com.alex.st0.codec.RequestPacket;
import com.alex.st0.codec.ResponsePacket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 底层通信协议与rpc协议转换
 * 
 * @author xuykj
 *
 */
public class ProcessorImpl implements IProcessor {
	public Map<String, Object> serverBean;

	public ProcessorImpl(Map<String, Object> serverBean) {
		this.serverBean = serverBean;
	}

	@Override
	public ResponsePacket handle(RequestPacket requestPacket) throws Exception {
		RequestPacket request = requestPacket;
		ResponsePacket response = new ResponsePacket();
		RpcResponse rpcResponse = new RpcResponse();
		Header header = new Header();
		header.setType(Header.RESPONSE);
		response.setHeader(header);
		RpcRequest rpcRequest = null;
		try {
			rpcRequest = JSON.parseObject(new String(request.getData(), "utf-8"), new TypeReference<RpcRequest>() {
			});
			rpcResponse.setRequestId(rpcRequest.getRequestId());
			Class<?> cls = Class.forName(rpcRequest.getClassName());
			Method m = cls.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
			Object target = serverBean.get(rpcRequest.getClassName());
			Object result = m.invoke(target, rpcRequest.getParameters());
			rpcResponse.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			rpcResponse.setError(e);
		}
		String rpcResponseStr = JSON.toJSONString(rpcResponse);
		response.setData(rpcResponseStr.getBytes("utf-8"));
		return response;
	}

}
