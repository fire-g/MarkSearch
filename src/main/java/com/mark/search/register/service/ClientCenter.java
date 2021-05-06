package com.mark.search.register.service;

import com.mark.search.register.entity.ClientNode;

/**
 * @author haotian
 */
public interface ClientCenter {

    /**
     * 客户端注册
     *
     * @param node 客户端
     * @return 操作结果
     */
    boolean register(ClientNode node);

    /**
     * 获取所有客户端
     *
     * @return 客户端列表
     */
    ClientNode[] list();
}
