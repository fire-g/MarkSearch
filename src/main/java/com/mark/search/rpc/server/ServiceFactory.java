package com.mark.search.rpc.server;


import java.util.HashMap;
import java.util.Map;

/**
 * 服务工厂
 * @author haotian
 */
public class ServiceFactory {
    private static final Map<String,Object> map=new HashMap<>();

    public static <T> T newInstance(Class<T> clazz){
        String name = clazz.getName();
        if(map.containsKey(name)){
            Object o=map.get(name);
            T obj=null;
            if(o != null){
                obj=(T)o;
            }
            return obj;
        }
        try {
            T obj = clazz.newInstance();
            map.put(name,obj);
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
