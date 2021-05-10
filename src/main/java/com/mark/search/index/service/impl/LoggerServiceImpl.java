package com.mark.search.index.service.impl;

import com.mark.search.annotation.Inject;
import com.mark.search.annotation.Service;
import com.mark.search.index.log.LogFactory;
import com.mark.search.index.service.LoggerService;

/**
 * 日志处理服务实例
 * @author HaoTian
 */
@Service
public class LoggerServiceImpl implements LoggerService {
    @Inject
    private LogFactory factory;

    @Override
    public String[] getLog(long time) {
        return factory.readLines(time).toArray(new String[0]);
    }
}
