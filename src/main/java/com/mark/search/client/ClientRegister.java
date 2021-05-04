package com.mark.search.client;

import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.register.service.ClientCenter;
import com.mark.search.register.service.IndexCenter;
import com.mark.search.rpc.client.Client;

/**
 * 客户端注册
 * @author haotian
 */
public class ClientRegister implements Runnable{
    private ClientNode clientNode;
    private RegNode regNode;

    public ClientRegister(ClientNode clientNode, RegNode regNode) {
        this.clientNode = clientNode;
        this.regNode = regNode;
    }

    @Override
    public void run() {
        //1、注册客户端
        ClientCenter clientCenter= Client.getRemoteProxyObj(ClientCenter.class,regNode.getIp(),regNode.getPort());
        clientCenter.register(clientNode);
        //2、获取所有索引服务
        IndexCenter center = Client.getRemoteProxyObj(IndexCenter.class, regNode.getIp(), regNode.getPort());
        IndexNode[] nodes=center.list();
        ClientFactory.addNodes(nodes);
    }
}
