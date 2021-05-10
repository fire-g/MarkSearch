package com.mark.search.index;

import com.mark.search.annotation.Component;
import com.mark.search.util.Constant;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author HaoTian
 */
@Component
public class IndexContentSyn implements Runnable {
    private Date date=new Date();
    private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public void run() {

        File config = new File(Constant.configDir + File.separator + "index.properties");
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //声明配置文件
            InputStream inStream = new FileInputStream(config);
            Properties prop = new Properties();
            prop.load(inStream);
            inStream.close();
            while (true) {
                Thread.sleep(1000*3);
                if(IndexContent.id==0){
                    continue;
                }
                prop.setProperty("id",""+IndexContent.id);
                prop.setProperty("master",IndexContent.master+"");
                prop.setProperty("time",IndexContent.time+"");
                prop.setProperty("status",IndexContent.status+"");
                OutputStream outputStream=new FileOutputStream(config);
                date=new Date();
                prop.store(outputStream,format.format(date));
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
