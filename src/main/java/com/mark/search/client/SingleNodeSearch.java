package com.mark.search.client;

import com.mark.search.register.entity.IndexNode;

/**
 * @author haotian
 */
public class SingleNodeSearch implements Runnable {

    private final IndexNode indexNode;
    private final String word;

    public SingleNodeSearch(IndexNode indexNode, String word) {
        this.indexNode = indexNode;
        this.word = word;
    }

    public SingleNodeSearch(IndexNode indexNode, String word, AutoClient autoClient) {
        this.indexNode = indexNode;
        this.word = word;
    }

    @Override
    public void run() {

    }
}
