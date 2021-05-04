package com.mark.search.index.subject;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索结果
 * @author HaoTian
 */
public class SearchResult implements Serializable {
    /**
     * 搜索到的总结果数量
     */
    private long total;
    /**
     * 当前页数
     */
    private int page;
    /**
     * 当前业的搜索结果
     */
    private List<?> list;

    public final long getTotal() {
        return total;
    }

    public final void setTotal(long total) {
        this.total = total;
    }

    public final int getPage() {
        return page;
    }

    public final void setPage(int page) {
        this.page = page;
    }

    public final List<?> getList() {
        return list;
    }

    public final void setList(List<?> list) {
        this.list = list;
    }
}
