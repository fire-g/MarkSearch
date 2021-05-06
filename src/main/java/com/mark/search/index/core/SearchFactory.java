package com.mark.search.index.core;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * @author HaoTian
 */
public class SearchFactory {
    private static IndexSearcher searcher;

    static {
        initReader();
    }

    public static void initReader() {
        Directory directory = DirectoryFactory.getDirectory();
        try {
            searcher = new IndexSearcher(DirectoryReader.open(directory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IndexSearcher getSearcher() {
        return searcher;
    }
}
