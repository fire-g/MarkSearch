package client;

import entity.Index;
import entity.MarkDoc;
import index.service.SearchService;
import index.service.WriterService;
import register.entity.IndexNode;

import java.util.*;

/**
 * @author haotian
 */
public class Client {

    /**
     * 添加索引
     * @param index 索引类
     * @return 操作成功与否
     */
    public boolean index(Index index){
        //1、获取索引服务器列表
        //随机获取结点
        int size = ClientFactory.MAP.size();
        Random random=new Random(System.currentTimeMillis());
        int n = random.nextInt(size);
        Integer[] s = ClientFactory.MAP.keySet().toArray(new Integer[0]);
        Set<IndexNode> nodeSet=ClientFactory.MAP.get(s[n]);
        IndexNode indexNode=null;

        for(IndexNode node:nodeSet){
            if(node.isMaster()){
                indexNode=node;
            }
        }
        //没有主结点
        if(indexNode==null){
            return false;
        }
        WriterService service= rpc.client.Client.getRemoteProxyObj(WriterService.class,indexNode.getIp(),indexNode.getPort());
        System.out.println(service.execute(index));
        return true;
    }

    /**
     * 搜索
     * @param word 关键字
     * @return 搜索结果
     */
    public Object search(String word){

        SearchService searchService= rpc.client.Client.getRemoteProxyObj(SearchService.class);
        MarkDoc[] markDocs= searchService.search(word);
        System.out.println(Arrays.toString(markDocs));
        List<Map<String,Object>> objs=searchService.getDocument(markDocs);
        return null;
    }
}
