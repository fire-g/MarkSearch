package com.mark.search.index.service;

import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;

/**
 * 注册中心修改，集群结点修改接口
 * @author haotian
 */
public interface IndexService {

    /**
     * 报告新增中心结点
     * @param regNode 中心结点
     * @return 操作结果
     */
    boolean addRegNode(RegNode regNode);

    /**
     * 报告死亡中心结点
     * @param regNode 中心结点
     * @return 操作结果
     */
    boolean deadRegNode(RegNode regNode);

    /**
     * 添加集群结点
     * @param indexNode 结点
     * @return 操作结果
     */
    boolean addNode(IndexNode indexNode);

    /**
     *报告死亡集群结点
     * @param indexNode 结点
     * @return 操作结果
     */
    boolean deadNode(IndexNode indexNode);
}
