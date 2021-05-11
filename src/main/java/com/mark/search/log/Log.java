package com.mark.search.log;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日志输出
 *
 * @author HaoTian
 */
public class Log {

    public synchronized static void log(Class<?> clazz, Object args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(
                calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                        + " " +
                        calendar.get(Calendar.HOUR_OF_DAY)+":" + calendar.get(Calendar.MINUTE)+":" + calendar.get(Calendar.SECOND)
                        +" " +
                        calendar.get(Calendar.MILLISECOND) + " " + clazz.getName() + "\t ::>> " + args.toString());
    }
}
