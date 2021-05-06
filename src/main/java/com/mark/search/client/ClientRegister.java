package com.mark.search.client;

import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.register.service.ClientCenter;
import com.mark.search.register.service.IndexCenter;
import com.mark.search.rpc.client.Client;
import com.mark.search.util.Constant;

/**
 * 客户端注册
 * @author haotian
 */
public class ClientRegister implements Runnable{
    private final ClientNode clientNode;
    private final RegNode regNode;

    public ClientRegister(ClientNode clientNode, RegNode regNode) {
        this.clientNode = clientNode;
        this.regNode = regNode;
    }

    @Override
    public void run() {
        while (Constant.client){
            //1、注册客户端
            ClientCenter clientCenter= Client.getRemoteProxyObj(ClientCenter.class,regNode.getIp(),regNode.getPort());
            clientCenter.register(clientNode);
            //2、获取所有索引服务
            IndexCenter center = Client.getRemoteProxyObj(IndexCenter.class, regNode.getIp(), regNode.getPort());
            IndexNode[] nodes=center.list();
            ClientFactory.addNodes(nodes);
            //发送心跳包
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
