package com.mark.search.index.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.index.IndexContent;
import com.mark.search.index.core.MarkIndexFactory;
import com.mark.search.index.service.SearchService;
import com.mark.search.index.subject.MarkDoc;
import com.mark.search.index.subject.MarkIndex;
import org.apache.lucene.queryparser.classic.ParseException;
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
        //searcher = SearchFactory.getSearcher();
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
                markDoc.node = IndexContent.id;
                markDoc.score = doc.score;
                markDocs.add(markDoc);
            }
            return markDocs.toArray(new MarkDoc[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MarkDoc[0];
    }

    @Override
    public MarkDoc[] search(List<Integer> indexes, String word) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        try {
            addShouldQuery("synopsis", word, builder);
            addShouldQuery("title", word, builder);
            addShouldQuery("content", word, builder);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BooleanQuery query = builder.build();
        return search(indexes,query);
    }

    @Override
    public MarkDoc[] search(List<Integer> indexes, BooleanQuery query) {
        List<MarkDoc> markDocs = new ArrayList<>();
        for(int i:indexes){
            MarkIndex index = MarkIndexFactory.get(i);
            if(index == null)
                continue;
            try {
                TopDocs results = index.searcher.search(query,Integer.MAX_VALUE);
                ScoreDoc[] hits = results.scoreDocs;

                for (ScoreDoc doc : hits) {
                    MarkDoc markDoc = new MarkDoc();
                    markDoc.index = i;
                    markDoc.doc = doc.doc;
                    markDoc.node = IndexContent.id;
                    markDoc.score = doc.score;
                    markDocs.add(markDoc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return markDocs.toArray(new MarkDoc[0]);
    }

    @Override
    public MarkDoc[] searchImage(String hash) {
        char[] chars = hash.toCharArray();
        int[] ints=new int[8];
        for(int i=0;i<8;i=i+2){
            ints[i]=Integer.parseInt(String.valueOf(chars[i]) + chars[i + 1],16);
        }
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        return new MarkDoc[0];
    }

    private void addShouldQuery(String fid, String value, BooleanQuery.Builder builder) throws ParseException {
        //Query content = new QueryParser(fid, AnalyzerFactory.getAnalyzer()).parse(value);
        //builder.add(content, BooleanClause.Occur.SHOULD);
    }

    @Override
    public List<Map<String, Object>> getDocument(MarkDoc[] docs) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarkDoc i : docs) {
            Map<String, Object> m = new HashMap<>(4);
            try {
                Document document = searcher.doc(i.doc);
                m.put("score", i.score);
                m.put("id", Util.combineInt2Long(i.doc, i.node));
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
