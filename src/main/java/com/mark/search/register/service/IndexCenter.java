package com.mark.search.register.service;

import com.mark.search.register.entity.IndexNode;

/**
 * @author haotian
 */
public interface IndexCenter {

    /**
     * 获取索引服务增加和删除列表
     */
    void modify();

    /**
     * 获取所有索引服务结点
     *
     * @return 所有的索引结点
     */
    IndexNode[] list();

    /**
     * 注册索引服务
     * 当服务注册为新结点之后会同步到客户端
     * 当服务注册为
     *
     * @param indexNode Node结点的信息
     * @return 注册是否成功
     */
    boolean register(IndexNode indexNode);

    /**
     * 注册索引服务,是否是新node结点或副本结点由注册中心决定
     *
     * @param ip   新服务的ip
     * @param port 新服务监听的端口
     * @return Node
     */
    IndexNode register(String ip, int port);

    /**
     * 心跳包服务
     *
     * @param indexNode node
     * @return node[]
     */
    IndexNode[] heartBeat(IndexNode indexNode);
}
