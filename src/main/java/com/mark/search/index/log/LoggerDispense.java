package com.mark.search.index.log;

import com.mark.search.annotation.Component;
import com.mark.search.annotation.Inject;
import com.mark.search.index.IndexContent;
import com.mark.search.index.service.LoggerService;
import com.mark.search.index.service.impl.WriterServiceImpl;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.rpc.client.Client;
import com.mark.search.util.Constant;
import com.mark.search.util.ReflexFactory;

import java.util.List;

/**
 * 日志分发工具
 * 通过RPC调用日志服务实现日志相关操作
 *
 * @author HaoTian
 */
@Component
public class LoggerDispense  implements Runnable{

    @Override
    public void run() {
        IndexNode master=null;
        for(IndexNode node:IndexContent.indexNodes){
            if(node.isMaster()){
                master=node;
            }
        }
        while (!IndexContent.master && master!=null){
            LoggerService service=Client.getRemoteProxyObj(LoggerService.class,master.getIp(),master.getPort());
            String[] list = service.getLog(IndexContent.time);
            //日志重做
            ReflexFactory.getInstance(WriterServiceImpl.class).execute(list);
        }
    }
}
