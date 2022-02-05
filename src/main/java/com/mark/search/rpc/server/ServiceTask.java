package com.mark.search.rpc.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.search.util.ReflexFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 *
 * @author haotian
 */
public class ServiceTask implements Runnable {
    Socket socket;

    public ServiceTask(Socket client) {
        this.socket = client;
    }

    @Override
    public void run() {
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(socket.getInputStream());
            //需要获取
            //1、服务名
            String service = input.readUTF();
            //2、方法名
            String method = input.readUTF();
            //3、参数类型
            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
            //4、参数
            Object[] arguments = (Object[]) input.readObject();
            Class<?> serviceClass = ServerImpl.SERVICE_REGISTRY.get(service);
            if (serviceClass == null) {
                throw new ClassNotFoundException(service + " not found");
            }
            Method method1 = serviceClass.getMethod(method, parameterTypes);
            //获取执行结果
            Object o = ReflexFactory.getInstance(serviceClass);
            Object result = method1.invoke(o, arguments);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(str);
            writer.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
