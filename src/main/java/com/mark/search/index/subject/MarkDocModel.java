package com.mark.search.index.subject;

import java.util.List;

/**
 * 文档规则模板，一个索引支持一个文档
 * @author HaoTian
 */
public class MarkDocModel {
    /**
     * 文档类型标识ID，索引ID
     */
    protected Long id;

    /**
     * 是否可扩展
     * 是否支持模板中未声明的field
     */
    protected boolean extensible;

    /**
     * 文档规则模板列表
     */
    protected List<MarkFieldModel> models;

    /**
     * 添加一个field模板
     * @param model 需要添加的模板
     */
    public void addModel(MarkFieldModel model){
        models.add(model);
    }

}
