package com.mark.search.util;

import com.mark.search.annotation.Component;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 参数同步线程
 * @author HaoTian
 */
@Component
public class ConstantSyn implements Runnable {
    private Date date=new Date();
    private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void run() {
        while (true){
            File config = new File(Constant.configDir+File.separator+"config.properties");
            if(!config.exists()){
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
                prop.setProperty("port",""+Constant.port);
                prop.setProperty("http.port",""+Constant.http);
                prop.setProperty("register",Constant.regNode.getIp()+":"+Constant.regNode.getPort());
                List<String> list=new ArrayList<>();
                if(Constant.client){
                    list.add("client");
                }
                if(Constant.index){
                    list.add("index");
                }
                if(Constant.register){
                    list.add("register");
                }
                if(list.size()==1){
                    prop.setProperty("as",list.get(0));
                }else if(list.size()==2){
                    prop.setProperty("as",list.get(0)+"|"+list.get(1));
                }else if(list.size()==3){
                    prop.setProperty("as",list.get(0)+"|"+list.get(1)+"|"+list.get(2));
                }
                OutputStream outputStream=new FileOutputStream(config);
                date=new Date();
                prop.store(outputStream,format.format(date));
                Thread.sleep(1000*5);
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
