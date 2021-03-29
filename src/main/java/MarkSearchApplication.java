import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

/**
 * @author HaoTian
 */
public class MarkSearchApplication {

    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(new File(System.getProperty("user.dir")+ File.separator+"data").toPath());
        IndexSearcher searcher=new IndexSearcher(DirectoryReader.open(directory));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(directory,indexWriterConfig);
    }
}
