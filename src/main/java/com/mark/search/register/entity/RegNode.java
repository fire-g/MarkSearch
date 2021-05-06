package com.mark.search.register.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    private boolean master;

    public RegNode(String ip, int port, int status) {
        super(ip, port);
        this.status = status;
        master = true;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
