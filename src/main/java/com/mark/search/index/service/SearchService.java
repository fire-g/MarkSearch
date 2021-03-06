package com.mark.search.index.service;

import com.mark.search.index.subject.MarkDoc;
import org.apache.lucene.search.BooleanQuery;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author haotian
 */
public interface SearchService {
    /**
     * 获取所有的
     *
     * @param query query
     * @return 所有满足条件的返回值
     */
    MarkDoc[] search(String query);

    /**
     *
     * @param indexes 索引id列表
     * @param query 搜索条件
     * @return 满足条件的文档
     */
    MarkDoc[] search(List<Integer> indexes,String query);


    MarkDoc[] search(List<Integer> indexes, BooleanQuery query);

    /**
     * 根据图片Hash查找图片
     * @param hash 图片hash
     * @return 返回图片结构
     */
    MarkDoc[] searchImage(String hash);

    /**
     * 获取文档
     *
     * @param docs 文档id
     * @return document
     */
    List<Map<String, Object>> getDocument(MarkDoc[] docs);
}
