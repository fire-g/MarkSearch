package com.mark.search.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mark.search.annotation.Controller;
import com.mark.search.annotation.GET;
import com.mark.search.annotation.Inject;
import com.mark.search.annotation.POST;
import com.mark.search.client.Client;
import com.mark.search.index.subject.*;

import java.util.Map;

/**
 * @author HaoTian
 */
@Controller
public class AdminController {

    @Inject
    private Client client;

    @GET(path = "/")
    public String obj() {
        return "hello";
    }

    @GET(path = "/ni")
    public String jj(Map<String, Object> objectMap) {
        return objectMap.toString();
    }

    @POST(path = "/api/v1/submit")
    public String j(StringBuilder builder, Map<String, Object> map) {
        String type = (String) map.get("type");
        Gson gson = new Gson();
        Index index = null;
        if ("Normal".equals(type)) {
            index = gson.fromJson(builder.toString(), Normal.class);
        } else if ("Blog".equals(type)) {
            index = gson.fromJson(builder.toString(), Blog.class);
        } else if ("Picture".equals(type)) {
            index = gson.fromJson(builder.toString(), Picture.class);
        } else if ("Question".equals(type)) {
            index = gson.fromJson(builder.toString(), Question.class);
        }
        if (index != null) {
            new Client().index(index);
        }
        return type;
    }

    @GET(path = "/search")
    public String search(Map<String, Object> map) {
        String word = (String) map.get("word");
        System.out.println("Word:" + word);
        Object o = new Client().search(word);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET(path = "/index-nodes")
    public String list(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object o = new Client().list();
        if(o==null){
            return null;
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET(path = "/register-nodes")
    public String reg(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object o = new Client().regNodes();
        if(o==null){
            return null;
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GET(path = "/client-nodes")
    public String clients(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object o = new Client().clients();
        if(o==null){
            return null;
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
