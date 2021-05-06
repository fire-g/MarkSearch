package com.mark.search.client.server;

/**
 * 存储路由绑定数据
 *
 * @author HaoTian
 */
public class ControllerSto {
    /**
     * 访问路由
     */
    private String path;

    /**
     * HTTP访问方式:GET、POST
     */
    private String method;

    /**
     * 提供服务的类单例
     */
    private Object controller;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 是否传入参数
     */
    private boolean map;

    /**
     * 是否传入body
     */
    private boolean body;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isMap() {
        return map;
    }

    public void setMap(boolean map) {
        this.map = map;
    }

    public boolean isBody() {
        return body;
    }

    public void setBody(boolean body) {
        this.body = body;
    }
}
