package com.mark.search.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日志输出
 *
 * @author HaoTian
 */
public class Log {

    /**
     * 当前接口已经重写，打印的是提示信息。
     * 但是当前接口已经弃用，应当使用替代的<code>Log.Info(...)</code>接口
     * @param clazz 错误发生的类
     * @param args 需要打印的数据
     */
    @Deprecated
    public synchronized static void log(Class<?> clazz, Object args) {
        log(clazz,"INFO",args);
    }

    /**
     * 日志记录级别Error,当程序运行发生错误时使用
     * @param clazz 错误发生的类
     * @param args 需要打印的数据
     */
    public synchronized static void Error(Class<?> clazz, Object args) {
        log(clazz,"Error",args);
    }

    /**
     * 日志记录级别Debug,当程序运行调试时使用
     * @param clazz 错误发生的类
     * @param args 需要打印的数据
     */
    public synchronized static void Debug(Class<?> clazz, Object args) {
        log(clazz,"DEBUG",args);
    }

    /**
     * 日志记录级别Info,当程序运行发生时记录提示信息使用
     * @param clazz 错误发生的类
     * @param args 需要打印的数据
     */
    public synchronized static void Info(Class<?> clazz, Object args) {
        log(clazz,"INFO",args);
    }

    private synchronized static void log(Class<?> clazz,String type,Object args){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        Date date = new Date();
        System.out.println(
                type+" "+ " "+  df.format(date)+" " + clazz.getName()  + args.toString());
    }
}
