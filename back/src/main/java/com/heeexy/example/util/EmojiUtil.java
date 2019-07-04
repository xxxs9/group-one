package com.heeexy.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: liminhao
 * @date: 2019-07-04 09:14
 * @description:
 * @version:
 */
public class EmojiUtil {

    public static String filterEmoji(String nick_name) {
        //nick_name 所获取的用户昵称
        if (nick_name == null) {
            return nick_name;
        }
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(nick_name);
        if (emojiMatcher.find()) {
            //将所获取的表情转换为*
            nick_name = emojiMatcher.replaceAll("*");
            return nick_name;
        }
        return nick_name;
    }

    /**
     * 将str转变为Unicode未解码字符串
     * @param source
     * @return
     */
    public static String unicode(String source){
        StringBuffer sb = new StringBuffer();
        char [] source_char = source.toCharArray();
        String unicode = null;
        for (int i=0;i<source_char.length;i++) {
            unicode = Integer.toHexString(source_char[i]);
            if (unicode.length() <= 2) {
                unicode = "00" + unicode;
            }
            sb.append("\\u" + unicode);
        }

        return sb.toString();

    }

    /**
     * 从数据库中获取需要将数据库中的Unicode未解码字符串转变为解码后的字符
     * @param unicode
     * @return
     */
    public static String decodeUnicode(String unicode) {
        StringBuffer sb = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            sb.append((char) data);
        }
        return sb.toString();

    }
}

