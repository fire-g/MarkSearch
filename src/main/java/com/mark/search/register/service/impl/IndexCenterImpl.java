package com.mark.search.register.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.register.CenterFactory;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.ServerIndexNode;
import com.mark.search.register.service.IndexCenter;

import java.util.Random;

/**
 * 索引库注册中心管理服务
 *
 * @author haotian
 */
@Service(name = "register")
public class IndexCenterImpl implements IndexCenter {

    @Override
    public void modify() {

    }

    @Override
    public IndexNode[] list() {
        return CenterFactory.listNodes();
    }

    @Override
    public boolean register(IndexNode indexNode) {
        ServerIndexNode node = new ServerIndexNode(indexNode);
        CenterFactory.regNodes(node);
        return true;
    }

    @Override
    public IndexNode register(String ip, int port) {
        //默认分布式扩展
        Random random = new Random();
        int i = random.nextInt();
        if(i<0){
            i= -i;
        }
        ServerIndexNode node = new ServerIndexNode(new IndexNode(i, ip, port));
        CenterFactory.regNodes(node);
        return node;
    }

    @Override
    public IndexNode[] heartBeat(IndexNode indexNode) {
        CenterFactory.liveNodeHeartBeat(indexNode);
        return CenterFactory.listNodes(indexNode.getId());
    }
}
