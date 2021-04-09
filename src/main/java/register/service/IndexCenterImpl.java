package register.service;

import register.entity.CenterFactory;
import register.entity.IndexNode;
import register.entity.ServerIndexNode;

import java.util.Random;

/**
 * 索引库注册中心管理服务
 * @author haotian
 */
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
        CenterFactory.regNodes(indexNode);
        return true;
    }

    @Override
    public IndexNode register(String ip, int port) {
        //默认分布式扩展
        Random random=new Random();
        ServerIndexNode node=new ServerIndexNode(new IndexNode(random.nextInt(),ip,port));
        CenterFactory.regNodes(node);
        return node;
    }

    @Override
    public IndexNode[] heartBeat(IndexNode indexNode) {
        CenterFactory.liveNodeHeartBeat(indexNode);
        return CenterFactory.listNodes(indexNode.getId());
    }
}
