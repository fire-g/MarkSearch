package com.mark.search.rpc.server;

/**
 * RPC服务接口
 *
 * @author haotian
 */
public interface Server {
    /**
     * 停止RPC服务
     */
    void stop();

    /**
     * 开启RPC服务
     */
    void start();

    /**
     * 注册RPC服务
     *
     * @param impl 服务实例
     */
    void register(Class<?> impl);

    /**
     * 查看RPC服务是否在线
     *
     * @return true表示在线, false表示不在线
     */
    boolean isRunning();

    /**
     * 获取RPC服务监听端口
     *
     * @return 监听端口
     */
    int getPort();
}
