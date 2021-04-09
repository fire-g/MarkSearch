package rpc.server;

import pool.Pool;

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

    private static boolean isRunning = false;
    private static int port;

    public ServerImpl(int port) {
        ServerImpl.port = port;
    }

    @Override
    public void stop() {
        isRunning = false;
        Pool.shutdown();
    }

    @Override
    public void start() {
        ServerSocket server;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        isRunning = true;
        System.out.println("服务启动...");
        while (true) {
            Socket socket;
            try {
                socket = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
                //如果服务停止则跳出循环
                if (server.isClosed()) {
                    isRunning = false;
                    break;
                }
                continue;
            }
            Pool.execute(new ServiceTask(socket));
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
