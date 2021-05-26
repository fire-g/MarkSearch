package com.mark.search.index.core;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * @author HaoTian
 */
public class DirectoryFactory {
    private static Directory directory;
    private static long time;
    private final static Object O =new Object();

    static {
        initDirectory();
    }

    /**
     * 创建索引文件夹对象
     */
    public static void initDirectory() {
        try {
            directory = FSDirectory.open(new File(System.getProperty("user.dir") +
                    File.separator + "data" + File.separator + "i").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload(){
        try {
            Directory directory = FSDirectory.open(new File(System.getProperty("user.dir") +
                    File.separator + "data" + File.separator + "i").toPath());
            synchronized (O){
                DirectoryFactory.directory = directory;
                time = System.currentTimeMillis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Directory getDirectory() {
        if((System.currentTimeMillis() - time)>1000*30){
            reload();
        }
        return directory;
    }
}
