package com.heeexy.example.util;

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
        int days = (int) ((Today.getTime() - date1.getTime()) / (1000*3600*24));
        if (days < 1){
            time = "1天内";
        }else if(days > 1 && days < 7){
            time = days + "天前";
        }else {
            time = "7天前";
        }
        return time;
    }
}
