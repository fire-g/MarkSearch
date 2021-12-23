package com.mark.search.index.subject;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MarkIndexTest {

    @Test
    void open() {
    }

    @Test
    void create() {
        MarkIndex index=new MarkIndex();
        MarkDocModel docModel = new MarkDocModel();
        docModel.id = 111L;
        try {
            index.create(docModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void initWriter() {
    }

    @Test
    void getDocModel() {
    }
}