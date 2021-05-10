package com.mark.search.util;

import com.mark.search.client.server.HttpServer;
import com.mark.search.register.entity.RegNode;
import com.mark.search.rpc.server.Server;

import java.io.File;

/**
 * 程序公共参数
 *
 * @author haotian
 */
public class Constant {
    /**
     * 配置信息存储地址
     */
    public static File configDir = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "c");
    /**
     * 将当前程序启动成注册服务器
     */
    public static boolean register = true;

    /**
     * 将当前程序启动成索引服务器
     */
    public static boolean index = true;

    /**
     * 将当前服务器启动成客户端
     */
    public static boolean client = true;

    /**
     * rpc服务监听接口
     */
    public static int port = 8088;

    /**
     * Client监听的HTTP端口
     */
    public static int http = 8080;

    /**
     * 本机ip
     */
    public static String ip = "127.0.0.1";

    public static RegNode regNode = null;

    /**
     * HTTP服务线程
     */
    public static HttpServer server = null;

    /**
     * 提供RPC服务
     */
    public static Server rpcServer = null;
}
