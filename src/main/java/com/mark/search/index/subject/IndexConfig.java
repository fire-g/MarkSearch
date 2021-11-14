package com.mark.search.index.subject;

import java.util.List;

/**
 * 索引配置信息
 * @author HaoTian
 */
public class IndexConfig {
    /**
     * 索引Id
     */
    public Long id;

    /**
     * 是否允许索引列表中不存在的索引项目作为索引的一部分,true标识允许,false标识不允许
     */
    public boolean unknown;

    /**
     * 索引文件地址(可使用相对地址和绝对地址)
     */
    public String path;

    /**
     * 列索引列表
     */
    public List<IndexItem> indexItems;

    /**
     * 索引项信息项
     */
    static class IndexItem {
        /**
         * 索引名
         */
        String name;

        /**
         * 索引是否存储
         */
        boolean store;

        /**
         * 是否添加索引
         */
        boolean index;

        /**
         * 是否分词(仅限字符串型数据)
         */
        boolean participle;

        /**
         * 是否必须存在
         */
        boolean mast;
    }
}
