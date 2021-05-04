package com.mark.search;

import com.mark.search.annotation.Service;
import com.mark.search.client.ClientRegister;
import com.mark.search.client.server.HttpServer;
import com.mark.search.index.IndexRegister;
import com.mark.search.pool.Pool;
import com.mark.search.register.CenterRegister;
import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.rpc.server.Server;
import com.mark.search.rpc.server.ServerImpl;
import com.mark.search.util.Constant;
import com.mark.search.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 启动流程
 * 1、加载配置
 * 2、启动服务
 *
 * @author HaoTian
 */
public class MarkSearchApplication {

    /**
     * 类路径集合
     */
    private final List<String> classPaths = new ArrayList<>();

    /**
     * 类列表
     */
    private final List<Class<?>> classes = new ArrayList<>();

    /**
     * 服务列表
     */
    private final List<Class<?>> services = new ArrayList<>();

    public void run() {
        //获取包
        Package p = this.getClass().getPackage();
        //通过包遍历类
        try {
            searchClass(p.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取服务
        searchService();
        //生成RPC服务对象
        Server server = new ServerImpl(Constant.port);
        Constant.rpcServer = server;
        System.out.println("注册服务...");
        registerService();
        server.start();
    }

    public void searchService() {
        //遍历所有类
        //类剔除接口
        //
        for (Class<?> clazz : classes) {
            if (!clazz.isInterface()) {
                if (clazz.getAnnotation(com.mark.search.annotation.Service.class) != null) {
                    System.out.println(clazz.getName());
                    services.add(clazz);
                }
            }
        }
    }

    /**
     * 遍历类
     *
     * @param basePack 基础包
     * @throws ClassNotFoundException 可能出现Class无法找到的异常
     */
    public void searchClass(String basePack) throws ClassNotFoundException {
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = Objects.requireNonNull(deduceMainApplicationClass()).getResource("/").getPath();
        //然后把我们的包名basPach转换为路径名
        basePack = basePack.replace(".", File.separator);
        //然后把classpath和basePack合并
        String searchPath = classpath + basePack;
        doPath(new File(searchPath));
        //这个时候我们已经得到了指定包下所有的类的绝对路径了。我们现在利用这些绝对路径和java的反射机制得到他们的类对象
        for (String s : classPaths) {
            //把 D:\work\code\20170401\search-class\target\classes\com\baibin\search\a\A.class 这样的绝对路径转换为全类名com.baibin.search.a.A
            s = s.replace(classpath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".").replace(".class", "");
            Class<?> cls = Class.forName(s);
            classes.add(cls);
        }
    }

    /**
     * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     *
     * @param file 路径
     */
    private void doPath(File file) {
        //文件夹
        if (file.isDirectory()) {
            //文件夹我们就递归
            File[] files = file.listFiles();
            if (files != null) {
                for (File f1 : files) {
                    doPath(f1);
                }
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
    }

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
                            if ("com/mark/search/register".equals(s)) {
                                Constant.register = true;
                            } else if ("com/mark/search/index".equals(s)) {
                                Constant.index = true;
                            } else if ("com/mark/search/client".equals(s)) {
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
                        if (m != null && !m) {
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

        //开启HTTP服务
        HttpServer httpServer = new HttpServer();
        Pool.execute(httpServer);
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
        System.out.println("获取本机ip:" + Constant.ip);

        //如果配置本机未结点中心
        //则添加本机为注册中心
        if (m == null || m) {
            //添加注册中心
            regNode = new RegNode(Constant.ip, Constant.port, 0);
        }
        Constant.regNode = regNode;
        new MarkSearchApplication().run();
    }

    /**
     * 注册服务
     */
    public void registerService() {
        if (Constant.register) {
            registerService("register");
            //注册注册中心
            Pool.execute(new CenterRegister(new RegNode(Constant.ip, Constant.port, 0), Constant.regNode));
        }
        if (Constant.index) {
            registerService("index");
            //添加index注册
            Pool.execute(new IndexRegister(new IndexNode(Constant.indexNode, Constant.ip, Constant.port), Constant.regNode));
        }
        if (Constant.client) {
            registerService("client");
            //客户端注册
            Pool.execute(new ClientRegister(new ClientNode(Constant.ip, Constant.port), Constant.regNode));
        }
    }

    /**
     * 根据指定服务类型名称注册服务
     * @param name 服务类型名称
     */
    public void registerService(String name) {
        List<Class<?>> list = findServices(name);
        for (Class<?> clazz : list) {
            System.out.println("注册服务:" + clazz.toString());
            Constant.rpcServer.register(clazz);
        }
    }

    /**
     * 获取main函数所在类
     *
     * @return main函数所在类的Class对象
     */
    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 通过服务类型获取服务列表
     *
     * @param name 服务类型名
     * @return 服务列表
     */
    public List<Class<?>> findServices(String name) {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> clazz : services) {
            Service service = clazz.getAnnotation(Service.class);
            if (service.name().equals(name)) {
                classes.add(clazz);
            }
        }
        return classes;
    }
}
