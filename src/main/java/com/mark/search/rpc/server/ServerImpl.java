package com.mark.search.rpc.server;

import com.mark.search.log.Log;
import com.mark.search.pool.Pool;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author haotian
 */
public class ServerImpl implements Server {

    protected static final HashMap<String, Class<?>> SERVICE_REGISTRY = new HashMap<>();

    private boolean isRunning = false;
    private final int port;
    private ServerSocket server;

    public ServerImpl(int port) {
        this.port = port;
    }

    @Override
    public void stop() {
        if (!server.isClosed()) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isRunning = false;
    }

    @Override
    public void start() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        isRunning = true;
        Log.log(this.getClass(),"服务启动...");
        while (true) {
            Socket socket;
            try {
                socket = server.accept();
                Pool.execute(new ServiceTask(socket));
            } catch (IOException e) {
                e.printStackTrace();
                //如果服务停止则跳出循环
                if (server.isClosed()) {
                    isRunning = false;
                    break;
                }
            }
        }
    }

    @Override
    public void register(Class<?> impl) {
        Class<?>[] interfaces = impl.getInterfaces();
        for (Class<?> clazz : interfaces) {
            SERVICE_REGISTRY.put(clazz.getName(), impl);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return port;
    }
}
