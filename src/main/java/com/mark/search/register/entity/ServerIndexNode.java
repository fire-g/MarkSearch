package com.mark.search.register.entity;

/**
 * 服务器的node存储模型
 * 在注册服务的存储结构中依然以Node作为同一服务器的判断
 * 故在注册服务的存储结构中不需要重写equals方法
 *
 * @author haotian
 */
public class ServerIndexNode extends IndexNode {

    /**
     * 注册时间
     */
    private long registerTime;

    /**
     * 生存刷新时间
     */
    private long flushTime;

    public ServerIndexNode(IndexNode indexNode) {
        super(indexNode.getId(), indexNode.getIp(), indexNode.getPort());
        long t = System.currentTimeMillis();
        registerTime = t;
        flushTime = t;
    }

    public ServerIndexNode(int id, String ip, int port) {
        super(id, ip, port);
    }

    public final long getRegisterTime() {
        return registerTime;
    }

    public final void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public final long getFlushTime() {
        return flushTime;
    }

    public final void setFlushTime(long flushTime) {
        this.flushTime = flushTime;
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
        return "ServerNode{" + super.toString() +
                ",registerTime=" + registerTime +
                ", flushTime=" + flushTime +
                '}';
    }
}
