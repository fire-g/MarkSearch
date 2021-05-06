package com.mark.search.client.service;

import com.mark.search.register.entity.IndexNode;

/**
 * 客户端服务
 *
 * @author haotian
 */
public interface ClientService {

    /**
     * 添加一个索引结点
     *
     * @param indexNode 索引结点
     * @return 返回执行情况
     */
    boolean addNode(IndexNode indexNode);

    /**
     * 删除一个死亡索引结点
     *
     * @param indexNode 索引结点
     * @return 返回执行情况
     */
    boolean deadNode(IndexNode indexNode);
}
