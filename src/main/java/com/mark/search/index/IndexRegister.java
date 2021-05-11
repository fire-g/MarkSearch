package com.mark.search.index;

import com.mark.search.index.log.LoggerDispense;
import com.mark.search.log.Log;
import com.mark.search.pool.Pool;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.register.service.IndexCenter;
import com.mark.search.register.service.RegisterCenter;
import com.mark.search.rpc.client.Client;
import com.mark.search.util.Constant;
import com.mark.search.util.ReflexFactory;

import java.util.Arrays;


/**
 * 索引的注册服务
 * 1、测试当前服务是否正常服务
 * 2、获取所有注册中心
 * 3、随机选取一个注册中心
 * 4、发送心跳包
 *
 * @author haotian
 */
public class IndexRegister implements Runnable {
    private IndexNode indexNode;
    private final RegNode regNode;

    public IndexRegister(IndexNode indexNode, RegNode regNode) {
        this.indexNode = indexNode;
        this.regNode = regNode;
    }

    @Override
    public void run() {
        //1、测试reg服务器是否提供服务
        RegisterCenter register = Client.getRemoteProxyObj(RegisterCenter.class, regNode.getIp(), regNode.getPort());

        String hello = register.hello();
        if (!"Hello".equals(hello)) {
            return;
        }
        Log.log(this.getClass(),"注册中心:" + regNode.getIp() + ":" + regNode.getPort() + "正常...");
        //获取所有注册中心
        RegNode[] regNodes = register.list();
        //2、开始心跳发送
        final IndexCenter center = Client.getRemoteProxyObj(IndexCenter.class, regNode.getIp(), regNode.getPort());
        //注册服务
        //当配置文件里面的indexNode!=-1时
        if(IndexContent.id==0) {
            indexNode = center.register(Constant.ip, Constant.port);
            IndexContent.id = indexNode.getId();
            IndexContent.master=indexNode.isMaster();
            IndexContent.status=indexNode.getStatus();
        }else {
            center.register(indexNode);
        }
        //定时向主节点询问并获取节点信息
        if(!IndexContent.master){
            Pool.execute(ReflexFactory.getInstance(LoggerDispense.class));
        }

        //心跳发送
        while (Constant.index) {
            //发送心跳包
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            IndexNode[] indexNodes = center.heartBeat(indexNode);
            IndexContent.indexNodes.addAll(Arrays.asList(indexNodes));
        }
    }
}
