package com.mark.search.register.service;

import java.util.Map;

/**
 * 管理服务接口
 * @author HaoTian
 */
public interface AdminService {

    /**
     * 列出所有节点
     * @return 返回节点信息
     */
    Map<String,Object> listAll();
}
