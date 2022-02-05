package com.mark.search.rpc.client;

import java.lang.reflect.Proxy;

/**
 * @author haotian
 */
public class Client {

    public static <T> T getRemoteProxyObj(final Class<T> serviceInterface, String host, int port) {
        Object o = Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface}, new DynamicProxy(serviceInterface, host, port));
        return serviceInterface.cast(o);
    }

}
