package com.mark.search.client.controller;

import com.mark.search.annotation.Controller;
import com.mark.search.annotation.GET;
import com.mark.search.annotation.POST;

import java.util.Map;

/**
 * @author HaoTian
 */
@Controller
public class AdminController {

    @GET(path = "/")
    public String obj(){
        return "hello";
    }

    @GET(path = "/ni")
    public String jj(Map<String,Object> objectMap){
        return objectMap.toString();
    }

    @POST(path = "/api/v1/submit")
    public String j(StringBuilder builder,Map<String,Object> map){
        return map.toString()+builder.toString();
    }

}
