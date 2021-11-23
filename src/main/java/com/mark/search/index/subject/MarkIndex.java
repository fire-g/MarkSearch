package com.mark.search.index.subject;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

/**
 * 索引结构
 * @author HaoTian
 */
public class MarkIndex {
    /**
     * 索引唯一标识id
     */
    public long id;

    /**
     * 搜索对象
     */
    public IndexSearcher searcher;

    /**
     * 索引写对象
     */
    public IndexWriter writer;

    /**
     * 索引文件对象
     */
    public Directory directory;
}
