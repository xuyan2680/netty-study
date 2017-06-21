package com.alex.st0.rpc;

import java.io.Serializable;
import java.util.Arrays;

/**
 * rpc 请求业务包定义
 * @author xuykj
 *
 */
public class RpcRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	private String registryKey;
	private String requestId;
	private long createMillisTime;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

	public String getRegistryKey() {
		return registryKey;
	}
	public void setRegistryKey(String registryKey) {
		this.registryKey = registryKey;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public long getCreateMillisTime() {
		return createMillisTime;
	}
	public void setCreateMillisTime(long createMillisTime) {
		this.createMillisTime = createMillisTime;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return "RpcRequest{" +
				"registryKey='" + registryKey + '\'' +
				", requestId='" + requestId + '\'' +
				", createMillisTime=" + createMillisTime +
				", className='" + className + '\'' +
				", methodName='" + methodName + '\'' +
				", parameterTypes=" + Arrays.toString(parameterTypes) +
				", parameters=" + Arrays.toString(parameters) +
				'}';
	}

}
