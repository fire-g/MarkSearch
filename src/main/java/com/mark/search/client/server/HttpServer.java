package com.mark.search.client.server;

import com.mark.search.annotation.GET;
import com.mark.search.annotation.POST;
import com.mark.search.pool.Pool;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * 一个简易的HTTP服务提供程序
 *
 * @author HaoTian
 */
public class HttpServer implements Runnable {
    ServerSocket ss;

    public HttpServer() throws IOException {
        ss = new ServerSocket(8080);
    }

    public HttpServer(int port, Class<?>[] classes) throws IOException {
        ss = new ServerSocket(port);
        //通过注解提取处理方法
        //注册路由
        for (Class<?> clazz : classes) {
            Object o;
            try {
                o = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
            //获取方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                GET get = method.getAnnotation(GET.class);
                if (get != null) {
                    String path = get.path();
                    ControllerSto sto = new ControllerSto();
                    sto.setPath(path);
                    sto.setMethod("GET");
                    sto.setController(o);
                    sto.setMethodName(method.getName());
                    //查看参数
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> cl : parameterTypes) {
                        if (cl == StringBuilder.class) {
                            sto.setBody(true);
                        }
                        if (cl == Map.class) {
                            sto.setMap(true);
                        }
                    }
                    SocketRunnable.getList.add(sto);
                    continue;
                }
                POST post = method.getAnnotation(POST.class);
                if (post != null) {
                    String path = post.path();
                    ControllerSto sto = new ControllerSto();
                    sto.setPath(path);
                    sto.setMethod("POST");
                    sto.setController(o);
                    sto.setMethodName(method.getName());

                    //查看参数
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> cl : parameterTypes) {
                        if (cl == StringBuilder.class) {
                            sto.setBody(true);
                        }
                        if (cl == Map.class) {
                            sto.setMap(true);
                        }
                    }
                    SocketRunnable.postList.add(sto);
                }
            }
        }
    }

    @Override
    public void run() {
        //注册服务

        //如果服务监听没有停止就一直循环

        //一旦接到Socket接入立即开启线程进行处理
        try {
            while (!ss.isClosed()) {
                Socket socket = ss.accept();
                //HTTP连接处理程序
                SocketRunnable socketRunnable = new SocketRunnable(socket);
                Pool.execute(socketRunnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
