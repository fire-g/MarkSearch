package com.mark.search.util;

import com.mark.search.annotation.Inject;
import com.mark.search.annotation.Single;
import com.mark.search.log.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单例工厂,所有的单例都在这里可以查找到并提供调用
 *
 * 本次实现未考虑循环调用问题
 * @author HaoTian
 */
public class ReflexFactory {
    /**
     * 存储单例
     */
    private final static Map<String,Object> MAP = new HashMap<>();

    /**
     * 单例工厂初始化,生成所有单例
     * 流程:
     *
     * @param list 传入列表
     */
    public static void init(Class<?>[] list){
        //获取被Single注解标注的注解
        List< Class<? extends Annotation>> interfaces=new ArrayList<>(2);
        for(Class<?> clz:list){
            if(clz.isInterface()){
                Single single=clz.getAnnotation(Single.class);
                if(single!=null){
                    if(clz.isAnnotation()){
                        interfaces.add(clz.asSubclass(Annotation.class));
                    }
                }
            }
        }
        //获取单例
        for(Class<?> clz:list){
            if(!clz.isInterface()){
                for(Class<? extends  Annotation> annotation:interfaces){
                    Annotation obj =clz.getAnnotation(annotation);
                    if(obj!=null){
                        //获取单例
                        //当map中不包含实体类,则实例化
                        if(!MAP.containsKey(clz.getName())){
                            //实例化对象
                            MAP.put(clz.getName(),newInstance(clz));
                        }
                    }
                }
            }
        }
    }

    /**
     * 反射获取对象
     * @param clz class
     * @return 对象
     */
    public static <T> T newInstance(Class<T> clz){
        Log.log(ReflexFactory.class,"实例化单例对象:"+clz.getName());
        T o=null;
        try {
            o = clz.newInstance();
            Field[] fields = clz.getDeclaredFields();
            for(Field field:fields){
                //获取注入注解
                Inject inject=field.getAnnotation(Inject.class);
                //当存在注入注解
                if(inject!=null){
                    Class<?> clazz = field.getType();
                    //此处需添加单例判定
                    if (!MAP.containsKey(clazz.getName())) {
                        MAP.put(clazz.getName(),newInstance(clazz));
                    }
                    field.setAccessible(true);
                    field.set(o, MAP.get(clazz.getName()));
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    /**
     * 生成一个对象,同时注入属性
     * 如果属性没有生成实例则生成实例,且需要注入属性的属性类必须是被Single修饰的
     * @param clz clz
     * @param <T> T
     * @return t
     */
    public static <T> T  getInstance(Class<T> clz) {
        Object o = MAP.get(clz.getName());
        if(o != null){
            return clz.cast(o);
        }
        return newInstance(clz);
    }

}
