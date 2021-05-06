package com.mark.search.index.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class AnalyzerFactory {
    private static Analyzer analyzer;

    static {
        initAnalyzer();
    }

    public static void initAnalyzer() {
        analyzer = new StandardAnalyzer();
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }
}
