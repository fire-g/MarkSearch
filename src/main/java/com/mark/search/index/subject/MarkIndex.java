package com.mark.search.index.subject;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

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
     * 分词器
     */
    private Analyzer analyzer;

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

    /**
     * 索引模型,承载了索引文档的标准
     */
    private MarkDocModel docModel;

    public void open(){

    }

    public void create(MarkDocModel docModel) throws IOException {
        analyzer = new StandardAnalyzer();
        directory = FSDirectory.open(new File(System.getProperty("user.dir") +
                File.separator + "data" + File.separator + "i" + docModel.id).toPath());
        initWriter();
        searcher = new IndexSearcher(DirectoryReader.open(directory));
    }

    /**
     * 创建IndexWriter对象，writer对象创建完成之后需要进行commit，否则创建IndexSearcher对象会报索引无法找到的错误
     */
    public void initWriter() {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            writer = new IndexWriter(directory, indexWriterConfig);
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
            writer = null;
        }
    }

    public MarkDocModel getDocModel() {
        return docModel;
    }
}
