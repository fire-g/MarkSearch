package index.core;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.IOException;

/**
 * @author HaoTian
 */
public class WriterFactory {
    private static IndexWriter writer=null;

    static {
        initWriter();
    }

    /**
     * 创建IndexWriter对象，writer对象创建完成之后需要进行commit，否则创建IndexSearcher对象会报索引无法找到的错误
     */
    public static void initWriter(){
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(AnalyzerFactory.getAnalyzer());
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            writer = new IndexWriter(DirectoryFactory.getDirectory(),indexWriterConfig);
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
            writer=null;
        }
    }

    public static IndexWriter getWriter() {
        return writer;
    }
}
