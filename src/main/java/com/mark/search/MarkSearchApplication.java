package com.mark.search;

import com.mark.search.annotation.Service;
import com.mark.search.client.ClientRegister;
import com.mark.search.client.server.HttpServer;
import com.mark.search.index.IndexContent;
import com.mark.search.index.IndexContentSyn;
import com.mark.search.index.IndexRegister;
import com.mark.search.index.log.Logger;
import com.mark.search.index.log.LoggerWriter;
import com.mark.search.log.Log;
import com.mark.search.pool.Pool;
import com.mark.search.register.CenterRegister;
import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;
import com.mark.search.rpc.server.Server;
import com.mark.search.rpc.server.ServerImpl;
import com.mark.search.util.*;

import java.io.*;
import java.net.URL;
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
     * 类列表
     */
    private final List<Class<?>> classes = new ArrayList<>();

    /**
     * 服务列表
     */
    private final List<Class<?>> services = new ArrayList<>();

    public void run() throws Exception {
        //判断Protocol
        URL url = this.getClass().getResource("");
        String basePackage = this.getClass().getPackage().getName();
        Log.log(this.getClass(), "base package:" + basePackage);
        String protocol = url.getProtocol();
        Log.log(this.getClass(), "协议:" + protocol);
        if ("jar".equalsIgnoreCase(protocol)) {
            classes.addAll(PackageScan.scanJarClass(basePackage));
        }
        if ("file".equalsIgnoreCase(protocol)) {
            //获取包
            Package p = this.getClass().getPackage();
            //通过包遍历
            classes.addAll(PackageScan.scanFileClass(p.getName()));
        }
        //初始化单例工厂
        ReflexFactory.init(classes.toArray(new Class[0]));
        //初始化配置文件目录
        if (!Constant.configDir.exists()) {
            boolean b = Constant.configDir.mkdirs();
            if (b) {
                Log.log(this.getClass(), "配置文件目录创建成功...");
            } else {
                Log.log(this.getClass(), "配置文件目录创建失败...");
            }
        }

        //解析参数
        argsConfig();

        //获取服务
        findRpcServices();

        //生成RPC服务对象
        Server server = new ServerImpl(Constant.port);
        Constant.rpcServer = server;
        Log.log(this.getClass(), "注册服务...");
        registerRpcService();

        Log.log(this.getClass(), "开启HTTP服务:http://" + Constant.ip + ":" + Constant.http);
        Log.log(this.getClass(), "本地访问(HTTP):http://localhost" + ":" + Constant.http);
        //启动RPC服务
        server.start();
    }

    /**
     * 查找所有的RPC服务
     */
    public void findRpcServices() {
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
    public Class<?>[] findControllers() {
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
     * 根据参数加载组件
     */
    private void argsConfig() throws Exception {
        //获取本机ip
        Constant.ip = Util.getAddress();
        Log.log(this.getClass(), "本机IP:" + Constant.ip);
        //将参数存入Map
        Map<String, String> argMap = new HashMap<>(2);

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                argMap.put(args[i], args[i + 1]);
                i++;
            }
        }

        Log.log(this.getClass(), "正在加载配置文件...");
        //查看参数文件是否存在
        File config = new File(Constant.configDir + File.separator + "config.properties");
        if (config.exists()) {
            //加载已有参数
            config();
        } else {
            Log.log(this.getClass(), "配置文件未找到 >> 默认模式启动...");
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
        Log.log(this.getClass(), "注册中心:" + Constant.regNode.getIp() + ":" + Constant.regNode.getPort());
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
    public void registerRpcService() {
        if (Constant.register) {
            registerRpcService("register");
            //注册注册中心
            Pool.execute(new CenterRegister(new RegNode(Constant.ip, Constant.port, 0), Constant.regNode));
        }
        if (Constant.index) {
            registerRpcService("index");
            //初始化日志
            ReflexFactory.getInstance(Logger.class).init();
            //添加index注册
            Pool.execute(new IndexRegister(new IndexNode(IndexContent.id, Constant.ip, Constant.port, IndexContent.master, IndexContent.status), Constant.regNode));
            //日志缓存服务
            Pool.execute(ReflexFactory.getInstance(LoggerWriter.class));
        }
        if (Constant.client) {
            registerRpcService("client");
            //开启HTTP服务
            HttpServer httpServer = null;
            try {
                httpServer = new HttpServer(Constant.http, findControllers());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Constant.server = httpServer;
            Pool.execute(httpServer);
            //客户端注册
            Pool.execute(new ClientRegister(new ClientNode(Constant.ip, Constant.port), Constant.regNode));
        }
    }

    /**
     * 根据指定服务类型名称注册服务
     *
     * @param name 服务类型名称
     */
    public void registerRpcService(String name) {
        List<Class<?>> list = findRpcServices(name);
        for (Class<?> clazz : list) {
            Log.log(this.getClass(), "注册服务:" + clazz.toString());
            Constant.rpcServer.register(clazz);
        }
    }

    /**
     * 通过服务类型获取服务列表
     *
     * @param name 服务类型名
     * @return 服务列表
     */
    public List<Class<?>> findRpcServices(String name) {
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
