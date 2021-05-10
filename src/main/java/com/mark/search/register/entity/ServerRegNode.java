package com.mark.search.register.entity;

/**
 * @author haotian
 */
public class ServerRegNode extends RegNode {
    /**
     * 注册时间
     */
    private long registerTime;

    /**
     * 生存刷新时间
     */
    private long flushTime;

    public ServerRegNode(RegNode node) {
        super(node.getIp(), node.getPort(), node.getStatus());
        long l = System.currentTimeMillis();
        registerTime = l;
        flushTime = l;
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
}
