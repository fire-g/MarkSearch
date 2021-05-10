package com.mark.search.register.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mark.search.register.service.RegisterCenter;

/**
 * 注册中心地址实体
 *
 * @author haotian
 */
@JsonIgnoreProperties
public class RegNode extends Node {
    /**
     * 结点状态
     * -1 死亡
     * 0 正常
     * 1 预定死亡
     */
    private int status;


    public RegNode(){

    }

    public RegNode(String ip, int port, int status) {
        super(ip, port);
        this.status = status;
    }

    public final int getStatus() {
        return status;
    }

    public final void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
