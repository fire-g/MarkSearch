package com.mark.search.rpc;

/**
 * 远程服务对象
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
    private Class<?> parameters;

    /**
     * 实参列表
     */
    private Object[] arguments;
}
