package core;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * @author HaoTian
 */
public class DirectoryService {
    private static Directory directory;

    static {
        try {
            directory= FSDirectory.open(new File(System.getProperty("user.dir")+ File.separator+"data").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Directory getDirectory() {
        return directory;
    }
}
