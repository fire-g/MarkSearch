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

    /**
     * 打开索引
     * @throws IOException 打开索引涉及IO，可能引发IO问题
     */
    public void open() throws IOException{
        this.analyzer = new StandardAnalyzer();
        this.directory = FSDirectory.open(new File(System.getProperty("user.dir") +
                File.separator + "data" + File.separator + "i"+File.separator + docModel.id).toPath());
        initWriter();
        this.searcher = new IndexSearcher(DirectoryReader.open(directory));
    }

    /**
     * 创建索引
     * @param docModel 索引模板
     * @throws IOException 创建索引设计IO，可能发生IO问题
     */
    public void create(MarkDocModel docModel) throws IOException {
        this.analyzer = new StandardAnalyzer();
        this.docModel =docModel;
        this.directory = FSDirectory.open(new File(System.getProperty("user.dir") +
                File.separator + "data" + File.separator + "i"+File.separator + docModel.id).toPath());
        initWriter();
        this.searcher = new IndexSearcher(DirectoryReader.open(directory));
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

    public long getId() {
        return docModel.id;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public IndexSearcher getSearcher() {
        return searcher;
    }

    public void setSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public IndexWriter getWriter() {
        return writer;
    }

    public void setWriter(IndexWriter writer) {
        this.writer = writer;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public void setDocModel(MarkDocModel docModel) {
        this.docModel = docModel;
    }
}
