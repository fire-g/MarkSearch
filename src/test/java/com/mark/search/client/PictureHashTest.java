package com.mark.search.client;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PictureHashTest {

    @Test
    public void a() {
    }

    @Test
    public void changeSize() {
    }

    @Test
    public void aa(){
        try {
            InputStream stream = new FileInputStream("C:\\Users\\18296\\Desktop\\IMG_0253.PNG");
            System.out.println(new PictureHash().pHash(stream));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void rgb2Gray() {
    }
}
