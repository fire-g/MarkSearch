package com.mark.search.rpc;

/**
 * RPC请求逻辑包结构
 */
public class Package {
    /**
     * 协议版本
     */
    private byte[] version;

    /**
     * 魔法值,用于鉴定是否是合法的协议
     */
    private byte magic;

    /**
     * 物理包hash
     */
    private byte[] hash;

    /**
     * 包长度
     */
    private Long len;

    public byte[] getVersion() {
        return version;
    }

    public void setVersion(byte[] version) {
        this.version = version;
    }

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
