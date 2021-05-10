package com.mark.search.register.entity;

import java.io.Serializable;

/**
 * @author haotian
 */
public class Node implements Serializable {
    /**
     * 索引结点ip
     */
    private String ip;

    /**
     * 索引结点端口
     */
    private int port;

    public final String getIp() {
        return ip;
    }

    public final void setIp(String ip) {
        this.ip = ip;
    }

    public final int getPort() {
        return port;
    }

    public final void setPort(int port) {
        this.port = port;
    }

    public Node() {
    }

    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        if (port != node.port) {
            return false;
        }
        return ip != null ? ip.equals(node.ip) : node.ip == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
