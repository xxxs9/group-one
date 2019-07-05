package com.heeexy.example.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * @author: hxy
 * @date: 2017/10/24 10:16
 */
public class StringTools {

	public static boolean isNullOrEmpty(String str) {
		return null == str || "".equals(str) || "null".equals(str);
	}

	public static boolean isNullOrEmpty(Object obj) {
		return null == obj || "".equals(obj);
	}

    public static String differentDaysByMillisecond(Date date1)
    {
        String time = "";
        Date Today=new Date();
        int maxtime = 15;
        int days = (int) ((Today.getTime() - date1.getTime()) / (1000*3600*24));
        if (days <= 1){
            time = "1天内";
        }else if(days > 1 && days <= maxtime){
            time = days + "天前";
        }else if(days > maxtime){
            time = "15天前";
        }
        return time;
    }

    public static void getPicture(String urlHttp, String path){
        String file = path + "/" + new Date().getTime() + ".jpg";
        try {
            URL url = new URL(urlHttp);
            BufferedImage img = ImageIO.read(url);
            ImageIO.write(img, "jpg", new File(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
