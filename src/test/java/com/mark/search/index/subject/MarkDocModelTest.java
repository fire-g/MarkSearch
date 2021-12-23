package com.mark.search.index.subject;

import com.google.gson.Gson;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MarkDocModelTest {


    @Test
    public void addModel() {
        MarkDocModel model=new MarkDocModel("gh");
        model.addModel(new MarkFieldModel("ok"));
        Gson gson=new Gson();
        String json = gson.toJson(model);
        File file = new File("./data/config/mark.json");
        System.out.println(file.exists());
        try {
            OutputStream stream=new FileOutputStream(file);
            stream.write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}