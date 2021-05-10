package com.mark.search.register.entity;


/**
 * @author haotian
 */
public class ClientNode extends Node {

    public ClientNode(){

    }

    public ClientNode(String ip, int port) {
        super(ip, port);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
