package com.til.glowing_fire_glow.common.save;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.util.Util;

import java.util.HashMap;
import java.util.Map;

public class SaveManage implements GlowingFireGlow.IWorldComponent {

    protected final Map<Class<?>, SavePack<?>> savePackMap = new HashMap<>();

    public <E> SavePack<E> getSavePack(Class<E> clazz) {
        if (savePackMap.containsKey(clazz)) {
            return Util.forcedConversion(savePackMap.get(clazz));
        }
        SavePack<E> savePack = new SavePack<>(clazz);
        savePackMap.put(clazz, savePack);
        return savePack;
    }

}
