package com.til.glowing_fire_glow.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    protected static final Pattern NUMBER_PATTERN = Pattern.compile("-?/d+(\\./d+)?");

    /***
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
