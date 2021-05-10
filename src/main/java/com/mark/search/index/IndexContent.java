package com.mark.search.index;

import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;

import java.util.*;

/**
 * 索引节点的基本信息
 * @author haotian
 */
public class IndexContent {
    /**
     * id
     */
    public static int id=0;
    /**
     * 索引写入时间
     */
    public static Long time = 0L;

    /**
     * 是否是主节点
     */
    public static boolean master = false;

    /**
     * 所有的注册中心结点
     */
    public static Set<RegNode> regNodes = new HashSet<>();

    /**
     * 与当前结点id相同的所有结点
     * 结点格式为:[ip:port]
     * 其中port为rpc监听端口
     */
    public static Set<IndexNode> indexNodes = new HashSet<>();

    /**
     * 服务状态：0 正常服务、1 预计淘汰、2 正在复制、
     */
    public static int status = 0;

}
