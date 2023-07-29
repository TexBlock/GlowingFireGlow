package com.til.glowing_fire_glow.common.capability.back;

import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.glowing_fire_glow.common.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IBackRunPack {

    Map<BackTypeRegister<?>, List<?>> getBackRunMap();

    void copyTo(IBackRunPack backRunPack);

    default <C> void addRunBack(BackTypeRegister<C> backTypeRegister, C c) {
        getRunBack(backTypeRegister).add(c);
    }

    default <C> List<C> getRunBack(BackTypeRegister<C> backTypeRegister) {
        Map<BackTypeRegister<?>, List<?>> map = getBackRunMap();
        List<C> backRunList;
        if (map.containsKey(backTypeRegister)) {
            backRunList = Util.forcedConversion(map.get(backTypeRegister));
        } else {
            backRunList = new ArrayList<>();
            map.put(backTypeRegister, backRunList);
        }
        return backRunList;
    }

    default <C> void runBack(BackTypeRegister<C> backTypeRegister, Consumer<C> consumer) {
        if (!getBackRunMap().containsKey(backTypeRegister)) {
            return;
        }
        for (C c : getRunBack(backTypeRegister)) {
            consumer.accept(c);
        }
    }

}
