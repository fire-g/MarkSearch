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

    public synchronized static void log(Class<?> clazz, Object args) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        Date date = new Date();
        System.out.println(
                df.format(date)+" " + clazz.getName() + "\t ::>> " + args.toString());
    }
}
