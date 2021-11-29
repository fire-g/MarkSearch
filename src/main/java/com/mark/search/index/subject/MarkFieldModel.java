package com.mark.search.index.subject;

/**
 * 索引Item模板
 * @author HaoTian
 */
public class MarkFieldModel {

    /**
     * 键
     */
    protected String name;

    /**
     * 数据类型
     */
    private Type type;

    /**
     * 是否索引
     */
    private boolean index;

    /**
     * 是否存储
     */
    private boolean store;

    /**
     * 是否分词
     */
    private boolean participle;

    /**
     * 是否必须
     */
    private boolean necessary;
}
