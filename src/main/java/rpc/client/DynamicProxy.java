package rpc.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author haotian
 */
public class DynamicProxy implements InvocationHandler {
    private final Object obj;
    private Socket socket;
    private final InetSocketAddress address;

    private void connect() {
        try {
            socket = new Socket();
            socket.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DynamicProxy(Object obj){
        this(obj,new InetSocketAddress("localhost",8088));
    }

    public  DynamicProxy(Object obj,InetSocketAddress address){
        this.obj=obj;
        this.address=address;
    }

    public DynamicProxy(Object obj, String host, int port){
        this(obj,new InetSocketAddress(host,port));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        connect();
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        output.writeUTF(((Class<?>) obj).getName());
        output.writeUTF(method.getName());
        if (socket.isClosed()) {
            System.out.println("socket is closed");
        }
        output.writeObject(method.getParameterTypes());
        output.writeObject(args);
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(reader, method.getReturnType());
    }
}
