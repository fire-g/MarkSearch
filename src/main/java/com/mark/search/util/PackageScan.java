package com.mark.search.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描相关工具包
 *
 * @author HaoTian
 */
public class PackageScan {

    /**
     * 搜索Jar包里面的类文件并返回类列表
     *
     * @param basePackage 包
     * @return Jar包中的所有类的列表
     * @throws IOException            IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public static List<Class<?>> scanJarClass(String basePackage) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        //通过当前线程得到类加载器并获取URL的枚举
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(
                basePackage.replace(".", "/"));
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equalsIgnoreCase(protocol)) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                if (connection != null) {
                    JarFile file = connection.getJarFile();
                    if (file != null) {
                        //得到该Jar文件下面的类实体
                        Enumeration<JarEntry> jarEntryEnumeration = file.entries();
                        while (jarEntryEnumeration.hasMoreElements()) {
                            JarEntry entry = jarEntryEnumeration.nextElement();
                            String jarEntryName = entry.getName();
                            if (jarEntryName.contains(".class") &&
                                    jarEntryName.replaceAll("/", ".").
                                            startsWith(basePackage)) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).
                                        replaceAll("/", ".");
                                Class<?> cls = Class.forName(className);
                                classes.add(cls);
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 获取main函数所在类
     *
     * @return main函数所在类的Class对象
     */
    public static Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     *
     * @param file 路径
     * @return 所有类路径
     */
    public static List<String> doPath(File file) {
        List<String> classPaths=new ArrayList<>();
        //文件夹
        if (file.isDirectory()) {
            //文件夹我们就递归
            File[] files = file.listFiles();
            if (files != null) {
                for (File f1 : files) {
                    classPaths.addAll(doPath(f1));
                }
            }
        } else {
            //标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
        return classPaths;
    }

    /**
     * 遍历类
     *
     * @param basePackage 基础包
     * @throws ClassNotFoundException 可能出现Class无法找到的异常
     */
    public static List<Class<?>> scanFileClass(String basePackage) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = Objects.requireNonNull(deduceMainApplicationClass()).getResource("/").getPath();
        //然后把我们的包名basePackage转换为路径名
        basePackage = basePackage.replace(".", File.separator);
        //然后把classpath和basePack合并
        String searchPath = classpath + basePackage;
        List<String> classPaths = doPath(new File(searchPath));
        //这个时候我们已经得到了指定包下所有的类的绝对路径了。我们现在利用这些绝对路径和java的反射机制得到他们的类对象
        for (String s : classPaths) {
            String path = classpath.replace("/", "\\").
                    replaceFirst("\\\\", "");
            s = s.replace(path
                    , "").replace(classpath, "").
                    replace("\\", ".").
                    replace("/", ".").
                    replace(".class", "");
            Class<?> cls = Class.forName(s);
            classes.add(cls);
        }
        return classes;
    }
}
