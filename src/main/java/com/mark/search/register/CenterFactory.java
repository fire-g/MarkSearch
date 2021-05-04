package com.mark.search.register;

import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.register.entity.ServerIndexNode;

import java.util.*;

/**
 * 存储注册列表
 * 所有节点数据都
 * @author haotian
 */
public class CenterFactory {

    /**
     * 在本机注册的所有索引服务器
     */
    private static final Set<ServerIndexNode> SERVER_NODES = new HashSet<>();

    /**
     * 所有客户端
     */
    private static final Set<ClientNode> CLIENT_NODES = new HashSet<>();

    /**
     * 所有服务器相同的注册结点集合
     */
    protected static final Set<RegNode> REGISTER_NODES = new HashSet<>();

    /**
     * 所有服务器索引结点集合
     */
    private static final Set<IndexNode> INDEX_NODES = new HashSet<>();

    /**
     * 注册客户端
     *
     * @param clientNode 客户端结点
     */
    public static void regClient(ClientNode clientNode) {
        CLIENT_NODES.add(clientNode);
    }

    public static ClientNode[] listClient() {
        return CLIENT_NODES.toArray(new ClientNode[0]);
    }

    /**
     * 批量复制客户端地址
     *
     * @param clientNodes 客户端地址列表
     */
    public static void regClients(ClientNode[] clientNodes) {
        CLIENT_NODES.addAll(Arrays.asList(clientNodes));
    }

    /**
     * 注册注册中心
     *
     * @param node 注册中心信息
     */
    public static void regRegNode(RegNode node) {
        REGISTER_NODES.remove(node);
        REGISTER_NODES.add(node);
    }

    /**
     * 批量注册注册中心
     *
     * @param nodes 所有中心结点
     */
    public static void regRegNodes(RegNode[] nodes) {
        REGISTER_NODES.addAll(Arrays.asList(nodes));
    }

    /**
     * 上报死亡的注册结点
     *
     * @param node 注册中心信息
     */
    public static void deadRegNode(RegNode node) {
        REGISTER_NODES.remove(node);
        //将指定注册结点状态设为死亡
        node.setStatus(-1);
        REGISTER_NODES.add(node);
    }

    /**
     * 注册索引结点
     * 1、向所有结点中添加
     * 2、向本地结点中添加
     */
    public synchronized static void regNodes(IndexNode indexNode) {
        INDEX_NODES.remove(indexNode);
        INDEX_NODES.add(indexNode);
        SERVER_NODES.add(new ServerIndexNode(indexNode));
    }

    /**
     * 复制所有索引结点
     *
     * @param indexNodes 结点
     */
    public synchronized static void regNodes(IndexNode[] indexNodes) {
        INDEX_NODES.addAll(Arrays.asList(indexNodes));
    }

    /**
     * 指定node心跳
     *
     * @param indexNode node
     */
    public synchronized static void liveNodeHeartBeat(IndexNode indexNode) {
        for (ServerIndexNode node1 : SERVER_NODES) {
            //在IP、监听端口、索引id都相同的情况下才能认为两个服务器地址代表的服务器相同
            //确定为同一台服务器则刷新生存时间
            if (node1.getIp().equals(indexNode.getIp()) && node1.getPort() == indexNode.getPort() && node1.getId() == indexNode.getId()) {
                node1.setFlushTime(System.currentTimeMillis());
                break;
            }
        }
    }

    /**
     * 获取所有注册服务器
     *
     * @return 注册服务器列表
     */
    public synchronized static RegNode[] listRegNode() {
        return REGISTER_NODES.toArray(new RegNode[0]);
    }

    /**
     * 获取所有索引结点
     */
    public synchronized static IndexNode[] listNodes() {
        return INDEX_NODES.toArray(new IndexNode[0]);
    }

    /**
     * 获取所有在5秒钟内状态修改的索引结点
     */
    public synchronized static IndexNode[] modifyListNodes() {
        return new IndexNode[0];
    }

    /**
     * 当前架构下，该方法无效
     * 获取结点id为指定结点id的状态修改或新增的结点
     *
     * @param node 结点id
     * @return Node[]
     */
    public synchronized static IndexNode[] modifyListNodes(int node) {
        List<IndexNode> indexNodeList = new ArrayList<>();
        for (ServerIndexNode node1 : SERVER_NODES) {
            if (node1.getId() == node) {
                //5000ms没有刷新生存时间则说明该服务已经死亡
                if ((System.currentTimeMillis() - node1.getFlushTime()) > 5000) {
                    node1.setStatus(-1);
                    indexNodeList.add(node1);
                }
                //如果该服务的注册时间在5000ms以内，则认为这是一个新服务并且未告知其他客户端
                if ((System.currentTimeMillis() - node1.getRegisterTime()) < 5000) {
                    indexNodeList.add(node1);
                }
            }
        }
        return indexNodeList.toArray(new IndexNode[0]);
    }

    /**
     * 获取结点id为指定id的所有结点
     *
     * @param node 结点id
     * @return Node[]
     */
    public synchronized static IndexNode[] listNodes(int node) {
        List<IndexNode> indexNodeList = new ArrayList<>();
        for (IndexNode indexNode1 : INDEX_NODES) {
            if (indexNode1.getId() == node) {
                indexNodeList.add(indexNode1);
            }
        }
        return indexNodeList.toArray(new IndexNode[0]);
    }
}
