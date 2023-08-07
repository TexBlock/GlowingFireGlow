package com.til.glowing_fire_glow.common.synchronous;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.util.Util;

import java.util.HashMap;
import java.util.Map;

public class SynchronousManage implements IWorldComponent {

    protected final Map<Class<?>, SynchronousPack<?>> savePackMap = new HashMap<>();

    public <E> SynchronousPack<E> getPack(Class<E> clazz) {
        if (savePackMap.containsKey(clazz)) {
            return Util.forcedConversion(savePackMap.get(clazz));
        }
        SynchronousPack<E> savePack = new SynchronousPack<>(clazz);
        savePackMap.put(clazz, savePack);
        return savePack;
    }

}
