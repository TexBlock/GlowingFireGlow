package com.til.glowing_fire_glow.common.util;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author til
 */
public class ResourceLocationUtil {

    public static ResourceLocation fuseName(String namespace, String splicing, String[] strings) {
        return new ResourceLocation(namespace, String.join(splicing, Arrays.asList(strings)));
    }

    public static ResourceLocation fuseName(String namespace, String splicing, ResourceLocation... strings) {
        List<String> stringList = new ArrayList<>(strings.length);
        for (ResourceLocation string : strings) {
            stringList.add(string.getPath());
        }
        return fuseName(namespace, splicing, stringList.toArray(new String[0]));
    }

    public static ResourceLocation fuseName(String namespace, String splicing, RegisterBasics... strings) {
        List<String> stringList = new ArrayList<>(strings.length);
        for (RegisterBasics string : strings) {
            stringList.add(string.getName().getPath());
        }
        return fuseName(namespace, splicing, stringList.toArray(new String[0]));
    }

    public static String ofPath(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        String className = clazz.getSimpleName();

        String[] cell = className.split("_");
        for (int i = 0; i < cell.length; i++) {
            String stringCell = cell[i];
            if (stringCell.isEmpty()) {
                continue;
            }
            if (i != 0) {
                stringBuilder.append("_");
            }

            char[] chars = stringCell.toCharArray();
            boolean isOldUpperCase = false;
            for (int ii = 0; ii < chars.length; ii++) {
                char c = chars[ii];
                if (Character.isUpperCase(c)) {
                    if (ii != 0 && !isOldUpperCase) {
                        stringBuilder.append('_');
                    }
                    isOldUpperCase = true;
                    stringBuilder.append(upperToLower(c));
                    continue;
                }
                isOldUpperCase = false;
                stringBuilder.append(c);
            }
        }


        /*char[] chars = className.toCharArray();
        boolean isOldUpperCase = false;
        boolean isCutApart = false;
        for (int i = 0, charsLength = chars.length; i < charsLength; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                if (i != 0 && !isOldUpperCase) {
                    stringBuilder.append('_');
                    isCutApart = true;
                }
                isOldUpperCase = true;
                stringBuilder.append(upperToLower(c));
                continue;
            }
            isOldUpperCase = false;
            switch (c) {
                case '&':
                case '$':
                case '_':
                    if (isCutApart) {
                        isCutApart = false;
                        continue;
                    }
                    stringBuilder.append('_');
                    isCutApart = true;
                    break;
                default:
                    stringBuilder.append(c);
                    isCutApart = false;
                    break;

            }
        }*/
        return stringBuilder.toString();
    }


    public static char upperToLower(char c) {
        if (c > 64 && c < 91) {
            c += 32;
        }
        return c;
    }
}
