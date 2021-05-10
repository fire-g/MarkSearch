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

    public final String getPath() {
        return path;
    }

    public final void setPath(String path) {
        this.path = path;
    }

    public final String getMethod() {
        return method;
    }

    public final void setMethod(String method) {
        this.method = method;
    }

    public final Object getController() {
        return controller;
    }

    public final void setController(Object controller) {
        this.controller = controller;
    }

    public final String getMethodName() {
        return methodName;
    }

    public final void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public final boolean isMap() {
        return map;
    }

    public final void setMap(boolean map) {
        this.map = map;
    }

    public final boolean isBody() {
        return body;
    }

    public final void setBody(boolean body) {
        this.body = body;
    }
}
