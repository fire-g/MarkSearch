package com.mark.search.register;

import com.mark.search.register.entity.RegNode;
import com.mark.search.register.service.ClientCenter;
import com.mark.search.register.service.IndexCenter;
import com.mark.search.register.service.RegisterCenter;
import com.mark.search.rpc.client.Client;

/**
 * 注册中心注册副本结点拷贝服务
 * 当当前注册中心为副本时需要向主注册中心注册获取所有信息并通过心跳定时获取变动信息
 * @author haotian
 */
public class CenterRegister implements Runnable{
    /**
     * 本地服务
     */
    private final RegNode node;

    /**
     * 远程服务
     */
    private final RegNode regNode;

    public CenterRegister(RegNode node, RegNode regNode) {
        this.node = node;
        this.regNode = regNode;
    }

    @Override
    public void run() {
        //IP和端口都相同则认为是同一台中心服务器，无需注册
        if(node.getPort()==regNode.getPort()&& node.getIp().equals(regNode.getIp())){
            return;
        }
        //获取服务
        RegisterCenter register = Client.getRemoteProxyObj(RegisterCenter.class, regNode.getIp(), regNode.getPort());
        //本机中心服务注册
        register.register(node);
        //获取所有中心服务服务
        RegNode[] regNodes=register.list();
        CenterFactory.regRegNodes(regNodes);
        IndexCenter indexCenter= Client.getRemoteProxyObj(IndexCenter.class,regNode.getIp(),regNode.getPort());
        //获取所有索引结点
        CenterFactory.regNodes(indexCenter.list());
        ClientCenter clientCenter=Client.getRemoteProxyObj(ClientCenter.class,regNode.getIp(),regNode.getPort());
        CenterFactory.regClients(clientCenter.list());
    }
}
