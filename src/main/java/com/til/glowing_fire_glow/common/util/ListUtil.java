package com.til.glowing_fire_glow.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtil {

    @SafeVarargs
    public static  <E> List<E> of(E... e) {
        if (e == null || e.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.asList(e);
    }
}
