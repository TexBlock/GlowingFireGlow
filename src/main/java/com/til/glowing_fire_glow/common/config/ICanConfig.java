package com.til.glowing_fire_glow.common.config;

import net.minecraft.util.ResourceLocation;

public interface ICanConfig {

    /***
     * 补全默认配置
     */
    void defaultConfig();

    ResourceLocation getConfigName();

    ResourceLocation getBasicsConfigName();
}
