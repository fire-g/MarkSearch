package com.mark.search.client;

import com.mark.search.MarkSearchApplication;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片hash
 * @author HaoTian
 */
public class PictureHash {

    /**
     * 改变图片的尺寸
     * @param newWidth, newHeight, path
     */
    public BufferedImage changeSize(int newWidth, int newHeight, InputStream path) {
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(path);

            //字节流转图片对象
            Image bi = ImageIO.read(in);
            //构建图片流
            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            //绘制改变尺寸后的图
            tag.getGraphics().drawImage(bi, 0, 0, newWidth, newHeight, null);
            in.close();
            return tag;
        } catch (IOException ignored) {
        }
        return null;
    }

    public String pHash(InputStream in)throws IOException{
        BufferedImage image=changeSize(8,8,in);
        for(int i=0;i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){
                int p = image.getRGB(i,j);
                Color pixel= new Color(p);
                image.setRGB(i,j,new Color(getGray(pixel),getGray(pixel),getGray(pixel)).getRGB());
            }
        }
        //计算平均灰度
        int avg = gary(image);
        int[] comps = new int[64];
        for(int i=0;i<comps.length;i++){
            int j=i/8;
            int k = i%8;
            int pix = image.getRGB(j,k);
            if(pix>=avg){
                comps[i]=1;
            }else {
                comps[i]=0;
            }
        }
        StringBuffer hashCode = new StringBuffer();
        for(int i=0;i<comps.length;i+=4){
            int result = comps[i]*(int)Math.pow(2,3)+
                    comps[i+1]*(int)Math.pow(2,2)+
                    comps[i+2]*(int) Math.pow(2,1)+
                    comps[i+3];
            hashCode.append(Integer.toHexString(result));
        }
        in.close();
        return hashCode.toString();
    }

    public static int getGray(Color pixel){
        return (pixel.getRed()*30+pixel.getGreen()*60+pixel.getBlue()*10)/100;
    }

    /**
     * 计算灰度平均值
     * @return 灰度平均值
     */
    public static int gary(BufferedImage image){
        int avg=0;
        int height=image.getHeight();
        int width=image.getWidth();
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                avg = avg + image.getRGB(i,j);
            }
        }
        return avg/(height*width);
    }
}
