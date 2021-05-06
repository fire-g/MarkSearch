package com.mark.search.client.server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HaoTian
 */
public class SocketRunnable implements Runnable {
    private int status=200;
    protected static List<ControllerSto> postList = new ArrayList<>();
    protected static List<ControllerSto> getList = new ArrayList<>();

    private final Socket socket;

    public SocketRunnable(Socket socket) {
        this.socket = socket;
    }

    public ControllerSto findGetByPath(String path){
        for(ControllerSto sto:getList){
            if(sto.getPath().equals(path)){
                return sto;
            }
        }
        return null;
    }

    public ControllerSto findPostByPath(String path){
        for(ControllerSto sto:postList){
            if(sto.getPath().equals(path)){
                return sto;
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            BufferedReader bd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //接受HTTP请求
            String requestHeader;
            int contentLength = 0;
            ControllerSto sto=null;
            Map<String,Object> obj=new HashMap<>();
            while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
                System.out.println(requestHeader);
                //获得GET参数
                if (requestHeader.startsWith("GET")) {
                    int begin = requestHeader.indexOf("GET") + 4;
                    int end = requestHeader.indexOf("HTTP/") - 1;
                    //获取路由
                    String condition = requestHeader.substring(begin, end);
                    //处理路由
                    String[] conditions = condition.split("\\?");
                    if(conditions.length==2) {
                        String[] m = conditions[1].split("&");
                        for (String s : m) {
                            String[] ss = s.split("=");
                            obj.put(ss[0],ss[1]);
                        }
                    }
                    sto=findGetByPath(conditions[0]);
                }else if(requestHeader.startsWith("POST")){
                    int begin = requestHeader.indexOf("GET") + 6;
                    int end = requestHeader.indexOf("HTTP/") - 1;
                    //获取路由
                    String condition = requestHeader.substring(begin, end);
                    System.out.println("POST参数:"+condition);
                    //处理路由
                    String[] conditions = condition.split("\\?");
                    if(conditions.length==2) {
                        String[] m = conditions[1].split("&");
                        for (String s : m) {
                            String[] ss = s.split("=");
                            obj.put(ss[0],ss[1]);
                        }
                    }
                    sto= findPostByPath(conditions[0]);
                }
                //获得POST参数
                //1.获取请求内容长度
                if (requestHeader.startsWith("Content-Length")) {
                    int begin = requestHeader.indexOf("Content-Length:") + "Content-Length:".length();
                    String postParamterLength = requestHeader.substring(begin).trim();
                    contentLength = Integer.parseInt(postParamterLength);
                    System.out.println("POST参数长度是：" + Integer.parseInt(postParamterLength));
                }
            }
            StringBuilder sb = new StringBuilder();
            if (contentLength > 0) {
                for (int i = 0; i < contentLength; i++) {
                    int a = bd.read();
                    sb.append((char) a);
                    if(sb.toString().getBytes().length>=contentLength){
                        break;
                    }
                }
                System.out.println("POST参数是：" + sb.toString());
            }
            //发送回执
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            if(sto==null){
                status=404;
                pw.println("HTTP/1.1 "+status+" NotFound");
                pw.println("Content-type:application/json;charset=utf-8");
                pw.println();
            }else {
                pw.println("HTTP/1.1 "+status+" OK");
                pw.println("Content-type:application/json;charset=utf-8");
                pw.println();
                Object o = sto.getController();
                List<Class<?>> pa=new ArrayList<>();
                if(sto.isBody()){
                    pa.add(StringBuilder.class);
                }
                if(sto.isMap()){
                    pa.add(Map.class);
                }
                Method method = o.getClass().getMethod(sto.getMethodName(),pa.toArray(new Class<?>[0]));
                List<Object> objects=new ArrayList<>();
                for(Class<?> cla:pa){
                    if(cla==Map.class){
                        objects.add(obj);
                    }
                    if(cla==StringBuilder.class){
                        objects.add(sb);
                    }
                }
                Object object = method.invoke(o,objects.toArray());
                pw.println(object.toString());
            }
            pw.flush();
            socket.close();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
