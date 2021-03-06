package com.mark.search.register.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author haotian
 */
@JsonIgnoreProperties
public class IndexNode extends Node {
    /**
     * 索引结点id
     */
    private int id;


    /**
     * 结点状态
     * -1 死亡
     * 0 正常
     * 1 预定死亡
     */
    private int status;

    /**
     * 是否是主结点
     */
    private boolean master;

    public IndexNode() {

    }

    public IndexNode(int id, String ip, int port) {
        super(ip, port);
        this.id = id;
        master = true;
    }

    public IndexNode(int id,String ip,int port,boolean master,int status){
        super(ip,port);
        this.id=id;
        this.master=master;
        this.status=status;
    }

    public final boolean isMaster() {
        return master;
    }

    public final void setMaster(boolean master) {
        this.master = master;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", " + super.toString() +
                ", status=" + status +
                ", master=" + master +
                '}';
    }
}
