package com.mark.search.index.log;

import com.mark.search.annotation.Component;
import com.mark.search.annotation.Inject;
import com.mark.search.util.Constant;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 写日志工具,每30秒进行
 * @author HaoTian
 */
@Component
public class LoggerWriter implements Runnable {

    @Inject
    private LogFactory factory;

    /**
     * 日志存储队列
     */
    protected final static List<String> STORE =new LinkedList<>();

    @Override
    public void run() {
        while (Constant.index){
            List<String> store;
            synchronized (STORE){
                store = new LinkedList<>(STORE);
                STORE.clear();
            }
            try {
                factory.append(store);
                //存入之后暂停30s
                Thread.sleep(1000*30);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
