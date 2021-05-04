package com.mark.search.util;

import javax.naming.Context;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 扫描类
 *
 * @author haotian
 */
public class Util {
    public static final String LOCAL_IP = "127.0.0.1";


    /**
     * 获取本机ip
     *
     * @return ip
     */
    public static String getAddress() {
        String lip = LOCAL_IP;
        Enumeration<NetworkInterface> allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress ip;
        while (allNetInterfaces != null && allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (ip instanceof Inet4Address) {
                    if (!ip.getHostAddress().equals(LOCAL_IP)) {
                        lip = ip.getHostAddress();
                    }
                }
            }
        }
        return lip;
    }

    /**
     * 两个int拼接成一个long
     * @param low long的低32位int
     * @param high long的高32位int
     * @return 拼接好的long
     */
    public static long combineInt2Long(int low, int high) {
        return ((long) low & 0x00000000FFFFFFFFL) | (((long) high << 32) & 0xFFFFFFFF00000000L);
    }

    public static void scanAllClasses() {
        String url = null;
        try {
            url = getClassPath();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<String> classes = getClassesList(url);
        // 遍历classes，如果发现@Component就注入到容器中
//        scanComponent2Container(classes);
        System.out.println(classes);
    }

    private static String getClassPath() throws UnsupportedEncodingException {
        String url = URLDecoder.decode(Context.class.getResource("/").getPath(), String.valueOf(Charset.defaultCharset()));
        if (url.startsWith("/")) {
            url = url.replaceFirst("/", "");
        }
        url = url.replaceAll("/", "\\\\");
        return url;
    }

    private static List<String> getClassesList(String url) {
        File file = new File(url);
        List<String> classes = getAllClass(file);
        for (int i = 0; i < classes.size(); i++) {
            classes.set(i, classes.get(i).replace(url, "").replace(".class", "").replace("\\", "."));
        }
        return classes;
    }

    private static List<String> getAllClass(File file) {
        List<String> ret = new ArrayList<>();
        System.out.println("name:" + file.getName());
        System.out.println(file.isDirectory());
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            System.out.println(list != null);
            if (list != null) {
                System.out.println(list.length);
                for (File i : list) {
                    List<String> j = getAllClass(i);
                    System.out.println("s" + j);
                    ret.addAll(j);
                }
            }
        } else {
            ret.add(file.getAbsolutePath());
        }
        return ret;
    }


}
