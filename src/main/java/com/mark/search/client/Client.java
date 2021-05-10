package com.mark.search.client;

import com.mark.search.annotation.Component;
import com.mark.search.index.service.SearchService;
import com.mark.search.index.service.WriterService;
import com.mark.search.index.subject.Index;
import com.mark.search.index.subject.MarkDoc;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.service.ClientCenter;
import com.mark.search.register.service.IndexCenter;
import com.mark.search.register.service.RegisterCenter;
import com.mark.search.util.Constant;

import java.util.*;

/**
 * @author haotian
 */
@Component
public class Client {

    /**
     * 添加索引
     *
     * @param index 索引类
     * @return 操作成功与否
     */
    public boolean index(Index index) {
        //1、获取索引服务器列表
        //随机获取结点
        int size = ClientFactory.MAP.size();
        System.out.println("Map:" + size);
        Random random = new Random(System.currentTimeMillis());
        int n = random.nextInt(size);
        Integer[] s = ClientFactory.MAP.keySet().toArray(new Integer[0]);
        Set<IndexNode> nodeSet = ClientFactory.MAP.get(s[n]);
        IndexNode indexNode = null;

        for (IndexNode node : nodeSet) {
            if (node.isMaster()) {
                indexNode = node;
            }
        }
        //没有主结点
        if (indexNode == null) {
            return false;
        }
        WriterService service = com.mark.search.rpc.client.Client.getRemoteProxyObj(
                WriterService.class, indexNode.getIp(), indexNode.getPort());
        System.out.println(service.execute(index));
        return true;
    }

    /**
     * 搜索
     *
     * @param word 关键字
     * @return 搜索结果
     */
    public Object search(String word) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (Set<IndexNode> indexNodeSet : ClientFactory.MAP.values()) {
            IndexNode node = indexNodeSet.iterator().next();
            SearchService searchService = com.mark.search.rpc.client.Client.getRemoteProxyObj(
                    SearchService.class, node.getIp(), node.getPort());
            MarkDoc[] markDocs = searchService.search(word);
            System.out.println("Search:" + markDocs.length);
            results.addAll(searchService.getDocument(markDocs));
        }
        return results;
    }

    public Object list(){
        IndexCenter center = com.mark.search.rpc.client.Client.getRemoteProxyObj(
                IndexCenter.class, Constant.regNode.getIp(),Constant.regNode.getPort());
        return center.list();
    }

    public Object regNodes(){
        RegisterCenter center= com.mark.search.rpc.client.Client.getRemoteProxyObj(
                RegisterCenter.class,Constant.regNode.getIp(),Constant.regNode.getPort());
        return center.list();
    }

    public Object clients(){
        ClientCenter center= com.mark.search.rpc.client.Client.getRemoteProxyObj(
          ClientCenter.class,Constant.regNode.getIp(),Constant.regNode.getPort()
        );
        return center.list();
    }

}
