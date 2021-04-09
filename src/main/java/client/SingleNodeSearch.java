package client;

import register.entity.IndexNode;

/**
 * @author haotian
 */
public class SingleNodeSearch implements Runnable {

    private IndexNode indexNode;
    private String word;
    private Client client;

    public SingleNodeSearch(IndexNode indexNode, String word) {
        this.indexNode = indexNode;
        this.word = word;
    }

    public SingleNodeSearch(IndexNode indexNode, String word, Client client) {
        this.indexNode = indexNode;
        this.word = word;
        this.client = client;
    }

    @Override
    public void run() {

    }
}
