package com.mark.search.index.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.index.service.SearchService;
import com.mark.search.index.subject.MarkDoc;
import com.mark.search.util.Constant;
import com.mark.search.index.core.AnalyzerFactory;
import com.mark.search.index.core.SearchFactory;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import com.mark.search.util.Util;

import java.io.IOException;
import java.util.*;

/**
 * @author haotian
 */
@Service(name = "index")
public class SearchServiceImpl implements SearchService {
    private IndexSearcher searcher;

    public SearchServiceImpl() {
        searcher = SearchFactory.getSearcher();
    }

    @Override
    public MarkDoc[] search(String word) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        try {
            addShouldQuery("synopsis", word, builder);
            addShouldQuery("title", word, builder);
            addShouldQuery("content", word, builder);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BooleanQuery query = builder.build();
        TopDocs results;
        try {
            results = searcher.search(query, Integer.MAX_VALUE);
            ScoreDoc[] hits = results.scoreDocs;
            List<MarkDoc> markDocs = new ArrayList<>();
            for (ScoreDoc doc : hits) {
                MarkDoc markDoc = new MarkDoc();
                markDoc.doc = doc.doc;
                markDoc.node = Constant.indexNode;
                markDoc.score = doc.score;
                markDocs.add(markDoc);
            }
            return markDocs.toArray(new MarkDoc[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MarkDoc[0];
    }

    private void addShouldQuery(String fid, String value, BooleanQuery.Builder builder) throws ParseException {
        Query content = new QueryParser(fid, AnalyzerFactory.getAnalyzer()).parse(value);
        builder.add(content, BooleanClause.Occur.SHOULD);
    }

    @Override
    public List<Map<String, Object>> getDocument(MarkDoc[] docs) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarkDoc i : docs) {
            Map<String, Object> m = new HashMap<>(4);
            try {
                Document document = searcher.doc(i.doc);
                m.put("score", i.score);
                m.put("id", Util.combineInt2Long(i.doc,i.node));
                for (IndexableField field : document.getFields()) {
                    String s = field.stringValue();
                    if (s != null) {
                        m.put(field.name(), s);
                    }
                    Number n = field.numericValue();
                    if (n != null) {
                        m.put(field.name(), n);
                    }
                }
                list.add(m);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
