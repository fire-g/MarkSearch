package com.mark.search.util;

import java.io.*;
import java.util.Properties;

/**
 * 配置文件
 *
 * @author haotian
 */
public class Property {
    private File file;
    private Properties properties;

    public Property() {
        file = new File("./config.properties");
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Property(String f) {
        file = new File(f);
        if (!file.exists()) {
            System.out.println("配置文件不存在");
        }
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ReadProperty() {

    }

    public void saveProperty() {
        try {
            OutputStream stream = new FileOutputStream(file);
            properties.store(stream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
