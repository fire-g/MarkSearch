package com.mark.search.index;

import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.register.service.IndexCenter;
import com.mark.search.register.service.RegisterCenter;
import com.mark.search.rpc.client.Client;
import com.mark.search.util.Constant;

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
    private final IndexNode indexNode;
    private RegNode regNode;

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
        //
        System.out.println("注册中心:" + regNode.getIp() + ":" + regNode.getPort() + "正常...");
        //获取所有注册中心
        RegNode[] regNodes = register.list();
        //2、开始心跳发送
        final IndexCenter center = Client.getRemoteProxyObj(IndexCenter.class, regNode.getIp(), regNode.getPort());
        //注册服务
        //当配置文件里面的indexNode!=-1时
        IndexNode indexNode = center.register(Constant.ip, Constant.port);
        Constant.indexNode = indexNode.getId();

        //心跳发送
        while (Constant.index) {
            //发送心跳包
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            IndexNode[] indexNodes = center.heartBeat(indexNode);
            IndexFactory.indexNodes.addAll(Arrays.asList(indexNodes));
            System.out.println(Arrays.toString(indexNodes));
        }
    }
}
