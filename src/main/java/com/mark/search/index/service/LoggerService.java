package com.mark.search.index.service;

/**
 * 日志线程
 * @author HaoTian
 */
public interface LoggerService {

    /**
     * 获取日志,每次最多获取100条
     * @param time 获取时间点{time}之后的日志
     * @return 返回的日志
     */
    String[] getLog(long time);
}
