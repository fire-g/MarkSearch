package com.mark.search.index.log;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogFactoryTest {

    @Test
    public void append() {
        try {
            List<String> str=new ArrayList<>();
            str.add("胜多负少廊坊市地方就死定了发动机流量费");
            new LogFactory().append(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void readLastLines(){
        try {
            List<String> strings = new LogFactory().readLastLines(3);
            for (String s:strings){
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
