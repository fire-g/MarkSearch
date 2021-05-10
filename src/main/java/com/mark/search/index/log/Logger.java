package com.mark.search.index.log;

import com.google.gson.Gson;
import com.mark.search.annotation.Component;
import com.mark.search.annotation.Inject;
import com.mark.search.index.IndexContent;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 恢复(重做)日志服务程序
 *
 * @author HaoTian
 */
@Component
public class Logger {
    /**
     * 日志缓存,用于计算日志hash
     */
    private static final List<String> BUFFER =new LinkedList<>();

    private final Object o=new Object();

    @Inject
    private LogFactory factory;

    public void init(){
        List<String> strings = factory.readLastLines(3);
        BUFFER.addAll(strings);
    }

    /**
     * 添加log
     * @param index 需要添加log的索引
     */
    public void add2Log(Object index){
        //生成log
        String str = index2Log(index);
        //缓存log
        synchronized (LoggerWriter.STORE) {
            LoggerWriter.STORE.add(str);
        }
        //将log加入日志缓存
        synchronized (BUFFER) {
            if (BUFFER.size() == 3) {
                BUFFER.remove(0);
            }
            BUFFER.add(str);
        }
    }

    /**
     * 索引对象转日志
     * @param index 索引对象
     * @return 日志串
     */
    public String index2Log(Object index){
        int integer=0;
        if(BUFFER.size()<3){
            integer=123456789;
        }
        StringBuilder builder=new StringBuilder();
        for(String s: BUFFER){
            builder.append(s);
        }
        integer=integer+builder.toString().hashCode();
        //如果hash位数不足则补足8位
        String hash = Integer.toHexString(integer);
        if(hash.length()<8){
            int i=8-hash.length();
            StringBuilder builder1=new StringBuilder();
            for(int j=0;j<i;j++){
                builder1.append('0');
            }
            builder1.append(hash);
            hash=builder1.toString();
        }

        long time;
        synchronized (o) {
            time = System.currentTimeMillis();
            IndexContent.time = time;
        }
        String name = index.getClass().getName();
        Gson gson=new Gson();
        String json = gson.toJson(index);
        return hash + " "+ time + " " + name+ " "+ json;
    }

    /**
     * 获取日志生成时间
     * @return 获取日志生成时间
     */
    public static Date logTime(String log){
        String dateString = log.split(" ")[1];
        return new Date(Long.parseLong(dateString));
    }

    /**
     * log转对象
     * @param str 日志串
     * @return 索引对象
     */
    public Object log2Index(String str){
        String[] strings = str.split(" ",4);
        try {
            Class<?> clazz = Class.forName(strings[2]);
            Gson gson=new Gson();
            return gson.fromJson(strings[3],clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
