package com.mark.search.client;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.*;

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
            InputStream stream = new FileInputStream("C:\\Users\\18296\\Desktop\\qhca.jpg");
            PictureHash pictureHash=new PictureHash();
            System.out.println(pictureHash.pHash(stream));
            ImageIO.write(pictureHash.GrayImage(stream),"jpg",new File("C:\\Users\\18296\\Desktop\\a.jpg"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void rgb2Gray() {
    }
}
