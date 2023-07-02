package com.til.glowing_fire_glow.util;

import net.minecraft.util.ResourceLocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

    protected static final Pattern NUMBER_PATTERN = Pattern.compile("-?/d+(\\./d+)?");

    /***
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum(String str) {
        if (str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String formatLang(ResourceLocation resourceLocation) {
        return formatLang(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static String formatLang(String... namespace) {
        if (namespace.length == 0) {
            return "";
        }
        return String.join(".", namespace);
    }

}
