package com.mark.search.register.service;

import com.mark.search.register.entity.RegNode;

/**
 * 注册中心
 * 注册中心管理索引(index)客户端、注册中心并将服务接口注入
 *
 * @author haotian
 */
public interface RegisterCenter {

    /**
     * 当前方法用于验证注册中心是否
     *
     * @return hello
     */
    String hello();

    /**
     * 获取注册中心列表
     *
     * @return 所有注册中心
     */
    RegNode[] list();

    /**
     * 注册中心结点
     *
     * @param regNode 需要注册的中心结点
     * @return 中心结点
     */
    boolean register(RegNode regNode);
}
