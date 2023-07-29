package com.til.glowing_fire_glow.common.capability.back;

import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackRunPack implements IBackRunPack {

    protected Map<BackTypeRegister<?>, List<?>> map = new HashMap<>();

    @Override
    public Map<BackTypeRegister<?>, List<?>> getBackRunMap() {
        return map;
    }

    @Override
    public void copyTo(IBackRunPack backRunPack) {
        map = new HashMap<>(backRunPack.getBackRunMap());
    }
}
