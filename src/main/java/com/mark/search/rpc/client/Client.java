package com.mark.search.rpc.client;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author haotian
 */
public class Client {
    public static <T> T getRemoteProxyObj(final Class<T> serviceInterface) {
        Object o = Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface}, new DynamicProxy(serviceInterface));
        return serviceInterface.cast(o);
    }

    public static <T> T getRemoteProxyObj(final Class<T> serviceInterface, String host, int port) {
        Object o = Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface}, new DynamicProxy(serviceInterface, host, port));
        return serviceInterface.cast(o);
    }

    public static <T> T getRemoteProxyObj(final Class<T> serviceInterface, InetSocketAddress address) {
        Object o = Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface}, new DynamicProxy(serviceInterface, address));
        return serviceInterface.cast(o);
    }
}
