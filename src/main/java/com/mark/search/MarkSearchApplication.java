package com.mark.search;

import com.mark.search.annotation.Service;
import com.mark.search.client.ClientRegister;
import com.mark.search.client.server.HttpServer;
import com.mark.search.index.IndexContent;
import com.mark.search.index.IndexContentSyn;
import com.mark.search.index.IndexRegister;
import com.mark.search.index.log.Logger;
import com.mark.search.index.log.LoggerWriter;
import com.mark.search.pool.Pool;
import com.mark.search.register.CenterRegister;
import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.rpc.server.Server;
import com.mark.search.rpc.server.ServerImpl;
import com.mark.search.util.Constant;
import com.mark.search.util.ConstantSyn;
import com.mark.search.util.ReflexFactory;
import com.mark.search.util.Util;

import java.io.*;
import java.util.*;

/**
 * 启动流程
 * 1、加载配置
 * 2、启动服务
 *
 * @author HaoTian
 */
public class MarkSearchApplication {
    private final String[] args;

    public MarkSearchApplication(String[] args) {
        this.args = args;
    }

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

    public void run() throws Exception {
        //解析参数
        argsConfig();
        //获取包
        Package p = this.getClass().getPackage();
        //通过包遍历类
        try {
            searchClass(p.getName());
            ReflexFactory.init(classes.toArray(new Class[0]));
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

        //开启HTTP服务
        HttpServer httpServer = new HttpServer(Constant.http, searchController());
        Constant.server = httpServer;
        Pool.execute(httpServer);

        System.out.println("开启HTTP服务:http://" + Constant.ip + ":" + Constant.http);
        System.out.println("本地访问(HTTP):http://localhost" + ":" + Constant.http);

        //启动RPC服务
        server.start();
    }

    /**
     * 查找所有的RPC服务
     */
    public void searchService() {
        //遍历所有类
        //类剔除接口
        //
        for (Class<?> clazz : classes) {
            if (!clazz.isInterface()) {
                if (clazz.getAnnotation(com.mark.search.annotation.Service.class) != null) {
                    services.add(clazz);
                }
            }
        }
    }

    /**
     * 搜索HTTP控制服务器控制层类
     *
     * @return 类数组
     */
    public Class<?>[] searchController() {
        List<Class<?>> cla = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (!clazz.isInterface()) {
                if (clazz.getAnnotation(com.mark.search.annotation.Controller.class) != null) {
                    cla.add(clazz);
                }
            }
        }
        return cla.toArray(new Class<?>[0]);
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

    /**
     * 根据参数加载组件
     */
    private void argsConfig() throws Exception {
        //获取本机ip
        Constant.ip = Util.getAddress();
        System.out.println("本机IP:" + Constant.ip);
        //将参数存入Map
        Map<String, String> argMap = new HashMap<>();

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                argMap.put(args[i], args[i + 1]);
                i++;
            }
        }

        System.out.println("正在加载配置文件...");
        //查看参数文件是否存在
        File config = new File(Constant.configDir + File.separator + "config.properties");
        if (config.exists()) {
            //加载已有参数
            config();
        } else {
            System.out.println("配置文件未找到...\n默认模式启动...");
            //既没有配置文件,也没有输入参数,则默认启动
            //三种方案同时启动
            Constant.regNode = new RegNode(Constant.ip, Constant.port, 0);
        }

        //自定义HTTP端口
        String http = argMap.get("--http");
        if (http != null) {
            Constant.http = Integer.parseInt(http);
        }
        //自定义RPC端口
        String rpc = argMap.get("--rpc");
        if (rpc != null) {
            Constant.port = Integer.parseInt(rpc);
        }
        //设置默认注册服务器
        Constant.regNode = new RegNode(Constant.ip, Constant.port, 0);

        //设置启动模式,加入模式还是自建模式
        String status = argMap.get("--status");
        if (status != null) {
            if ("main".equals(status)) {
                Constant.regNode = new RegNode(Constant.ip, Constant.port, 0);
            } else if ("member".equals(status)) {
                String register = argMap.get("--register");
                if (register != null) {
                    setReg(register);
                } else {
                    throw new Exception("加入模式下必须要设置注册中心");
                }

            }
        }

        //设置运行组件
        String as = argMap.get("--as");
        if (as != null) {
            setAs(as);
        }
        Pool.execute(ReflexFactory.getInstance(ConstantSyn.class));
        Pool.execute(ReflexFactory.getInstance(IndexContentSyn.class));
        System.out.println("注册中心:" + Constant.regNode.getIp() + ":" + Constant.regNode.getPort());
    }

    /**
     * 设置组件
     *
     * @param as 组件
     */
    private void setAs(String as) {
        String[] ass = as.split("\\|");
        //先全部置未不运行
        Constant.client = false;
        Constant.index = false;
        Constant.register = false;
        //然后根据条件指定运行
        for (String str : ass) {
            if ("index".equals(str)) {
                Constant.index = true;
            } else if ("register".equals(str)) {
                Constant.register = true;
            } else if ("client".equals(str)) {
                Constant.client = true;
            }
        }
        System.out.println("index:" + Constant.index);
    }

    private void setReg(String reg) {
        String[] ss = reg.split(":");
        Constant.regNode = new RegNode(ss[0], Integer.parseInt(ss[1]), 0);
    }

    /**
     * 加载配置文件
     */
    private void config() {
        File config = new File(Constant.configDir + File.separator + "config.properties");
        //加载配置文件
        try {
            //声明配置文件
            InputStream inStream = new FileInputStream(config);
            Properties prop = new Properties();
            prop.load(inStream);
            //获取rpc端口
            String key = prop.getProperty("port");
            if (key != null) {
                Constant.port = Integer.parseInt(key);
            }
            //获取http端口
            String http = prop.getProperty("http.port");
            if (http != null) {
                Constant.http = Integer.parseInt(http);
            }

            //获取组件
            String as = prop.getProperty("as");
            if (as != null) {
                setAs(as);
            }

            //设置注册中心
            String reg = prop.getProperty("register");
            if (reg != null) {
                setReg(reg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexConfig();
    }

    private void indexConfig() {
        File config = new File(Constant.configDir + File.separator + "index.properties");
        if (!config.exists()) {
            return;
        }
        //加载配置文件
        try {
            //声明配置文件
            InputStream inStream = new FileInputStream(config);
            Properties prop = new Properties();
            prop.load(inStream);
            inStream.close();
            //获取节点id
            String id = prop.getProperty("id");
            if (id != null) {
                IndexContent.id = Integer.parseInt(id);
            }
            //获取节点状态
            String master = prop.getProperty("master");
            if (master != null) {
                IndexContent.master = Boolean.parseBoolean(master);
            }

            //获取状态
            String as = prop.getProperty("status");
            if (as != null) {
                IndexContent.status = Integer.parseInt(as);
            }

            //获取时间
            String reg = prop.getProperty("time");
            if (reg != null) {
                IndexContent.time = Long.valueOf(reg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        //是否作为起始结点
        //true 表示是起始结点
        //false 表示不是起始结点，是作为边隅结点加入核心系统
        //处理输入参数
        //-status 将程序作为什么类型的服务
        // main:将程序作为主服务
        // member:将程序作为成员服务，需要--register参数获取已经在运行的注册中心服务
        //-as 将程序作为什么服务器启动,可同时运行多个，默认是启动一个完整的服务体系，即全部启动
        // register:将程序作为注册服务器启动
        // index:将程序作为索引服务器启动
        // client:将程序作为客户端启动
        //-port 服务器监听运行的端口,当前版本只作为客户端不需要监听
        new MarkSearchApplication(args).run();
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
            //初始化日志
            ReflexFactory.getInstance(Logger.class).init();
            //添加index注册
            Pool.execute(new IndexRegister(new IndexNode(IndexContent.id, Constant.ip, Constant.port, IndexContent.master, IndexContent.status), Constant.regNode));
            //日志缓存服务
            Pool.execute(ReflexFactory.getInstance(LoggerWriter.class));
        }
        if (Constant.client) {
            registerService("client");
            //客户端注册
            Pool.execute(new ClientRegister(new ClientNode(Constant.ip, Constant.port), Constant.regNode));
        }
    }

    /**
     * 根据指定服务类型名称注册服务
     *
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
