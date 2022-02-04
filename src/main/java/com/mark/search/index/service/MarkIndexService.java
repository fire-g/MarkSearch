package com.mark.search.index.service;

import com.mark.search.index.subject.MarkDocModel;

/**
 *  索引相关服务接口
 */
public interface MarkIndexService {
    /**
     * 获取所有索引
     * @return 索引列表
     */
    MarkDocModel[] list();

    /**
     * 创建索引
     * @param docModel 索引文档模型
     * @return 是否创建成功
     */
    boolean create(MarkDocModel docModel);

    /**
     * 删除索引
     * @param id 索引id
     * @return 是否删除成功
     */
    boolean delete(long id);
}
