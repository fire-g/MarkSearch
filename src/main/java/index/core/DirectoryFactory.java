package index.core;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * @author HaoTian
 */
public class DirectoryFactory {
    private static Directory directory;

    static {
        initDirectory();
    }

    /**
     * 创建索引文件夹对象
     */
    public static void initDirectory() {
        try {
            directory = FSDirectory.open(new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "i").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Directory getDirectory() {
        return directory;
    }
}
