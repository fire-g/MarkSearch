import client.ClientRegister;
import client.service.ClientServiceImpl;
import index.IndexRegister;
import index.service.IndexServiceImpl;
import pool.Pool;
import register.CenterRegister;
import register.entity.ClientNode;
import register.entity.IndexNode;
import register.entity.RegNode;
import register.service.ClientCenter;
import register.service.ClientCenterImpl;
import register.service.IndexCenterImpl;
import register.service.RegisterCenterImpl;
import rpc.server.Server;
import rpc.server.ServerImpl;
import index.service.SearchServiceImpl;
import index.service.WriterServiceImpl;
import util.Constant;
import util.Util;

import java.io.*;
import java.util.Properties;

/**
 * 启动流程
 * 1、加载配置
 * 2、启动服务
 *
 * @author HaoTian
 */
public class MarkSearchApplication {

    public static void main(String[] args) throws IOException {
        RegNode regNode = null;
        //是否作为起始结点
        //true 表示是起始结点
        //false 表示不是起始结点，是作为边隅结点加入核心系统
        Boolean m = null;
        //处理输入参数
        //-status 将程序作为什么类型的服务
        // main:将程序作为主服务
        // member:将程序作为成员服务，需要--register参数获取已经在运行的注册中心服务
        //-as 将程序作为什么服务器启动,可同时运行多个，默认是启动一个完整的服务体系，即全部启动
        // register:将程序作为注册服务器启动
        // index:将程序作为索引服务器启动
        // client:将程序作为客户端启动
        //-port 服务器监听运行的端口,当前版本只作为客户端不需要监听
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-status":
                        String status = args[i + 1];
                        if ("main".equals(status)) {
                            m = true;
                        } else if ("member".equals(status)) {
                            m = false;
                        }
                        i++;
                        break;
                    case "-as":
                        String ss = args[i + 1];
                        String[] str = ss.split("\\|");
                        for (String s : str) {
                            if ("register".equals(s)) {
                                Constant.register = true;
                            } else if ("index".equals(s)) {
                                Constant.index = true;
                            } else if ("client".equals(s)) {
                                Constant.client = true;
                            }
                        }
                        i++;
                        break;
                    case "-port":
                        int port = Integer.parseInt(args[i + 1]);
                        Constant.port = port;
                        System.out.println(port);
                        i++;
                        break;
                    case "-register":
                        //如果未设置
                        if(m!=null&&!m) {
                            String[] o = args[i + 1].split(":");
                            String ip = o[0];
                            int po = Integer.parseInt(o[1]);
                            regNode = new RegNode(ip, po, 0);
                        }
                        break;
                    default:
                        System.out.println("无法识别的指令：" + args[i]);
                        break;
                }
            }
        }

        File config = new File("./config.properties");
        //加载配置文件
        try {
            //声明配置文件
            InputStream inStream = new FileInputStream(config);
            Properties prop = new Properties();
            prop.load(inStream);
            String key = prop.getProperty("port");
            System.out.println(key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("配置文件未找到...");
            System.out.println("正在创建配置文件...");
            boolean b = config.createNewFile();
            if (b) {
                System.out.println("配置文件创建成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Constant.ip = Util.getAddress();
        System.out.println("获取本机ip：" + Constant.ip);

        //如果配置本机未结点中心
        //则添加本机为注册中心
        if(m==null|| m) {
            //添加注册中心
            regNode = new RegNode(Constant.ip, Constant.port, 0);
        }
        //生成RPC服务对象
        Server server = new ServerImpl(Constant.port);
        //注册RPC服务
        registerService(server,regNode);
        //启动服务
        server.start();
    }

    /**
     * 注册服务
     * @param server 本地服务
     * @param regNode 远程中心服务
     */
    static void registerService(Server server,RegNode regNode){
        //注册注册中心服务
        if (Constant.register) {
            System.out.println("启动注册服务...");
            server.register(IndexCenterImpl.class);
            server.register(RegisterCenterImpl.class);
            server.register(ClientCenterImpl.class);
            //注册注册中心
            Pool.execute(new CenterRegister(new RegNode(Constant.ip,Constant.port,0),regNode));
        }
        //注册index服务
        if (Constant.index) {
            System.out.println("启动索引服务...");
            server.register(WriterServiceImpl.class);
            server.register(SearchServiceImpl.class);
            server.register(IndexServiceImpl.class);
            //添加index注册
            Pool.execute(new IndexRegister(new IndexNode(Constant.indexNode, Constant.ip, Constant.port),regNode));
        }

        //注册客户端服务
        if(Constant.client){
            server.register(ClientServiceImpl.class);
            //客户端注册
            Pool.execute(new ClientRegister(new ClientNode(Constant.ip,Constant.port),regNode));
        }
    }
}
