package com.til.glowing_fire_glow.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetUtil {

    @SafeVarargs
    public static <E> Set<E> of(E... e) {
        if (e == null || e.length == 0) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(e));
    }

}
