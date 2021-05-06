package com.mark.search.index.subject;


import com.mark.search.index.annotation.Search;

/**
 * @author HaoTian
 */
public class Picture extends Index {
    @Search(value = "a", index = true)
    private Short a;

    @Search(value = "b", index = true)
    private Short b;

    @Search(value = "c", index = true)
    private Short c;

    @Search(value = "d", index = true)
    private Short d;

    @Search(value = "e", index = true)
    private Short e;

    @Search(value = "f", index = true)
    private Short f;

    @Search(value = "g", index = true)
    private Short g;

    @Search(value = "h", index = true)
    private Short h;

    @Search(value = "url", index = true, store = true)
    private String url;

    /**
     * SimHash
     */
    @Search(value = "sim_hash", store = true, index = true)
    private String simHash;

    /**
     * 文件摘要
     */
    @Search(value = "hash", store = true, index = true)
    private String sha;

    /**
     * 图片存储地址
     */
    @Search(value = "address", index = true, store = true)
    private String address;

    public final String getSimHash() {
        return simHash;
    }

    public final void setSimHash(String simHash) {
        this.simHash = simHash;
    }

    public final Short getA() {
        return a;
    }

    public final void setA(Short a) {
        this.a = a;
    }

    public final Short getB() {
        return b;
    }

    public final void setB(Short b) {
        this.b = b;
    }

    public final Short getC() {
        return c;
    }

    public final void setC(Short c) {
        this.c = c;
    }

    public final Short getD() {
        return d;
    }

    public final void setD(Short d) {
        this.d = d;
    }

    public final Short getE() {
        return e;
    }

    public final void setE(Short e) {
        this.e = e;
    }

    public final Short getF() {
        return f;
    }

    public final void setF(Short f) {
        this.f = f;
    }

    public final Short getG() {
        return g;
    }

    public final void setG(Short g) {
        this.g = g;
    }

    public final Short getH() {
        return h;
    }

    public final void setH(Short h) {
        this.h = h;
    }

    public String getUrl() {
        return url;
    }

    public final void setUrl(String url) {
        this.url = url;
    }

    public final String getAddress() {
        return address;
    }

    public final void setAddress(String address) {
        this.address = address;
    }
}
