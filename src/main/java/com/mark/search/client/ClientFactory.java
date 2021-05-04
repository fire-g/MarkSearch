package com.mark.search.client;

import com.mark.search.register.entity.IndexNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author haotian
 */
public class ClientFactory {
    protected final static HashMap<Integer, Set<IndexNode>> MAP =new HashMap<>();

    /**
     * 标识哪个结点增加了
     */
    public static void addNode(IndexNode indexNode){
        if(MAP.containsKey(indexNode.getId())){
            Set<IndexNode> indexNodes = MAP.get(indexNode.getId());
            indexNodes.add(indexNode);
        }else {
            Set<IndexNode> indexNodes =new HashSet<>();
            indexNodes.add(indexNode);
            MAP.put(indexNode.getId(), indexNodes);
        }
    }

    public static void addNodes(IndexNode[] indexNodes){
        for(IndexNode indexNode : indexNodes){
            addNode(indexNode);
        }
    }

    /**
     * 标识哪个结点死亡了
     */
    public static void deadNode(IndexNode indexNode){
        if(MAP.containsKey(indexNode.getId())){
            Set<IndexNode> indexNodes = MAP.get(indexNode.getId());
            indexNodes.remove(indexNode);
        }
    }

}
