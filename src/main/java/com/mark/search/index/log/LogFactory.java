package com.mark.search.index.log;

import com.mark.search.annotation.Component;
import com.mark.search.log.Log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志工厂，用于对日志的管理
 *
 * @author HaoTian
 */
@Component
public class LogFactory {
    /**
     * 日志目录
     */
    private File directory;
    private File write;
    private Date date=new Date();
    private DateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    public LogFactory() throws IOException {
        directory = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "l");
        if (!directory.exists()) {
            boolean b = directory.mkdirs();
            if (!b) {
                Log.log(this.getClass(),"日志目录创建失败");
            }
        }
        //获取时间

        write = new File(directory.getAbsolutePath() + File.separator + "index.redo");
        if (!write.exists()) {
            write.createNewFile();
        }
    }

    public void append(List<String> list) throws IOException {
        OutputStream stream = new FileOutputStream(write,true);
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        for (String string : list) {
            writer.write(string + "\n");
        }
        writer.close();
        stream.close();
    }

    public void redo(String log)throws IOException{
        Logger.logTime(log);
        File write=new File(directory+File.separator+"index.redo");
        OutputStream stream = new FileOutputStream(write,true);
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        writer.write(log+"\n");
        writer.close();
        stream.close();
    }

    /**
     * 读取最后n行
     * @param n n的具体数字
     * @return 日志列表
     */
    public List<String> readLastLines(int n){
        List<String> strings=new ArrayList<>();
        File file=new File(directory+File.separator+"index.redo");
        try {
            //读取最后三行
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"r");
            long len = randomAccessFile.length();
            List<String> strings1=new ArrayList<>();
            for(int i=0;i<n;i++) {
                if (len == 0L) {

                } else {
                    long pos = len - 1;
                    while (pos > 0) {
                        pos--;
                        randomAccessFile.seek(pos);
                        if (randomAccessFile.readByte() == '\n') {
                            break;
                        }
                    }
                    if (pos == 0) {
                        randomAccessFile.seek(0);
                    }
                    byte[] bytes = new byte[(int) (len - pos)];
                    randomAccessFile.read(bytes);
                    String string = new String(bytes);
                    len = pos;
                    strings1.add(string);
                }

            }
            int size=strings1.size();
            for(int j=0;j<size;j++){
                strings.add(j,strings1.get(size-j-1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    /**
     * 读取n行
     * @param time
     * @return
     */
    public List<String> readLines(long time){
        List<String> strings=new ArrayList<>();
        //格式化文件并获取
        Date date=new Date(time);
        String str = format.format(date);
        File file=new File(directory+File.separator+"index.redo");
        try {
            InputStreamReader read=new FileReader(file);
            BufferedReader reader=new BufferedReader(read);
            String string;
            while ((string=reader.readLine())!=null){
                long t = Logger.logTime(string).getTime();
                if(t>time){
                    strings.add(string);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public void redo(String[] strings){
        for(String s:strings){
            try {
                redo(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
