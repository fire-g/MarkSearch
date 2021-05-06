package com.mark.search.rpc;

/**
 * 远程服务对象
 *
 * @author HaoTian
 */
public class RemoteService {
    /**
     * 服务名称
     */
    private String service;

    /**
     * 服务调用方法
     */
    private String method;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameters;

    /**
     * 实参列表
     */
    private Object[] arguments;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
