package com.mark.search.index.service;

import java.util.List;

/**
 * @author haotian
 */
public interface WriterService {
    /**
     * 添加一个对象到索引
     * @param o 需要添加的索引对象
     */
    long execute(Object o);

    /**
     * 添加一系列对象到索引
     * @param list 多个对象
     */
    long execute(List<?> list);
}
