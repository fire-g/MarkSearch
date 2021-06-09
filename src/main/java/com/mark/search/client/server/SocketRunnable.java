package com.mark.search.client.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HaoTian
 */
public class SocketRunnable implements Runnable {
    private int status = 200;
    protected static List<ControllerSto> postList = new ArrayList<>();
    protected static List<ControllerSto> getList = new ArrayList<>();

    private final Socket socket;

    public SocketRunnable(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            //解析HTTP请求
            Http http = new Http(socket.getInputStream());
            //发送回执
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            ControllerSto sto = http.getSto();
            if (sto == null) {
                status = 404;
                pw.println(http.getVersion() + " " + status + " NotFound");
            } else {
                pw.println(http.getVersion() + " " + status + " OK");
            }
            pw.println("Content-type:application/json;charset=utf-8");
            pw.println();
            if (sto != null) {
                Object o = http.getSto().getController();
                List<Class<?>> pa = new ArrayList<>();
                if (http.getSto().isBody()) {
                    pa.add(StringBuilder.class);
                }
                if (http.getSto().isMap()) {
                    pa.add(Map.class);
                }
                Method method = o.getClass().getMethod(http.getSto().getMethodName(), pa.toArray(new Class<?>[0]));
                List<Object> objects = new ArrayList<>();
                for (Class<?> cla : pa) {
                    if (cla == Map.class) {
                        objects.add(http.getMap());
                    }
                    if (cla == StringBuilder.class) {
                        objects.add(http.getBuilder());
                    }
                }
                Object object = method.invoke(o, objects.toArray());
                pw.println(object.toString());
            }
            pw.flush();
            socket.close();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
