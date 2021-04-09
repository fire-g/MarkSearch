package util;

import java.util.LinkedList;
import java.util.List;

/**
 * 程序公共参数
 * @author haotian
 */
public class Constant {
    /**
     * 将当前程序启动成注册服务器
     */
    public static boolean register = false;

    /**
     * 将当前程序启动成索引服务器
     */
    public static boolean index = false;

    /**
     * 将当前服务器启动成客户端
     */
    public static boolean client = false;

    /**
     * rpc服务监听接口
     */
    public static int port = 8088;

    /**
     *Client监听的HTTP端口
     */
    public static int http = 8080;

    /**
     * 本机ip
     */
    public static String ip = "127.0.0.1";

    /**
     * 索引服务的id
     */
    public static int indexNode = 0;

    /**
     * 是否是索引服务id为@{indexNode}的主结点
     */
    public static boolean indexMaster=false;

}
