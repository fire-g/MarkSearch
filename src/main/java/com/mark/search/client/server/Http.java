package com.mark.search.client.server;

import com.mark.search.log.Log;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HaoTian
 */
public class Http {
    private String version;
    private String method;
    private String path;
    private String userAgent;
    private Map<String,Object> map=new HashMap<>();
    private ControllerSto sto;
    private StringBuilder builder=new StringBuilder();

    public Http() {
    }

    public Http(InputStream stream) throws IOException {
        BufferedReader bd = new BufferedReader(new InputStreamReader(stream));
        //接受HTTP请求
        String requestHeader;
        int contentLength = 0;
        while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
            System.out.println(requestHeader);
            //获得GET参数
            if (requestHeader.startsWith("GET")) {
                String[] string = requestHeader.split(" ");
                method = string[0];
                version = string[2];
                String condition = string[1];

                //处理路由
                String[] conditions = getStrings(map, condition);
                path = conditions[0];
                sto=findGetByPath(conditions[0]);
                Log.log(this.getClass(),"GET "+condition + (sto==null?" 404 NotFound":""));
            }else if(requestHeader.startsWith("POST")){
                String[] string = requestHeader.split(" ");
                method = string[0];
                version = string[2];
                String condition = string[1];
                Log.log(this.getClass(),"POST参数:"+condition);
                //处理路由
                String[] conditions = getStrings(map, condition);
                path = conditions[0];
                sto= findPostByPath(conditions[0]);
            }
            //获得POST参数
            //1.获取请求内容长度
            if (requestHeader.startsWith("content-length")) {
                int begin = requestHeader.indexOf("content-length:") + "content-length:".length();
                String postParamterLength = requestHeader.substring(begin).trim();
                contentLength = Integer.parseInt(postParamterLength);
            }
        }
        if (contentLength > 0) {
            for (int i = 0; i < contentLength; i++) {
                int a = bd.read();
                builder.append((char) a);
                if(builder.toString().getBytes().length>=contentLength){
                    break;
                }
            }
            Log.log(this.getClass(),"POST参数是：" + builder.toString());
        }
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 提取参数
     * @param obj obj
     * @param condition con
     * @return e
     * @throws UnsupportedEncodingException s
     */
    private String[] getStrings(Map<String, Object> obj, String condition) throws UnsupportedEncodingException {
        String[] conditions = condition.split("\\?");
        if (conditions.length == 2) {
            String[] m = conditions[1].split("&");
            for (String s : m) {
                String[] ss = s.split("=");
                String ab = URLDecoder.decode(ss[1], "utf-8");
                obj.put(ss[0], ab);
            }
        }
        return conditions;
    }

    public ControllerSto findGetByPath(String path){
        for(ControllerSto sto:SocketRunnable.getList){
            if(sto.getPath().equals(path)){
                return sto;
            }
        }
        return null;
    }

    public ControllerSto findPostByPath(String path){
        for(ControllerSto sto:SocketRunnable.postList){
            if(sto.getPath().equals(path)){
                return sto;
            }
        }
        return null;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public ControllerSto getSto() {
        return sto;
    }

    public void setSto(ControllerSto sto) {
        this.sto = sto;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
