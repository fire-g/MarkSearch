package com.mark.search.index.service;

import com.mark.search.index.subject.MarkDoc;

import java.util.List;
import java.util.Map;

/**
 * @author haotian
 */
public interface SearchService {
    /**
     * 获取所有的
     * @param query query
     * @return 所有满足条件的返回值
     */
    MarkDoc[] search(String query);

    /**
     * 获取文档
     * @param docs 文档id
     * @return document
     */
    List<Map<String,Object>> getDocument(MarkDoc[] docs);
}
