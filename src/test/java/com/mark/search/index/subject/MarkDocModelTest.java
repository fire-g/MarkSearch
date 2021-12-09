package com.mark.search.index.subject;

import com.google.gson.Gson;
import org.junit.Test;

public class MarkDocModelTest {

    @Test
    public void addModel() {
        MarkDocModel model=new MarkDocModel("gh");
        model.addModel(new MarkFieldModel("ok"));
        Gson gson=new Gson();
        String json = gson.toJson(model);
        System.out.println(json);
    }
}