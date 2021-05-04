package com.mark.search.client.server;

import com.mark.search.pool.Pool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个简易的HTTP服务提供程序
 * @author HaoTian
 */
public class HttpServer implements Runnable {
    ServerSocket ss;

    public HttpServer() throws IOException {
        ss = new ServerSocket(8080);
    }

    public HttpServer(int port) throws IOException{
        ss = new ServerSocket(port);
    }

    @Override
    public void run() {
        //如果服务监听没有停止就一直循环
        //一旦接到Socket接入立即开启线程进行处理
        try {
            while (!ss.isClosed()) {
                Socket socket = ss.accept();
                //HTTP连接处理程序
                SocketRunnable socketRunnable=new SocketRunnable(socket);
                Pool.execute(socketRunnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
