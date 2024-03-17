package com.til.glowing_fire_glow.common.main;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ICanConfig;
import com.til.glowing_fire_glow.common.util.IdentifierUtil;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.IEventBus;

public interface IWorldComponent extends ICanConfig {

    default void beforeConfigInit() {
    }

    @Override
    default void defaultConfig() {
    }

    default void initNew() {
    }

    default void initCommonSetup() {

    }

    default void initDedicatedServerSetup() {

    }

    default void initClientSetup() {

    }

    default void initModProcessEvent() {

    }

    /***
     * 获取执行顺序
     */
    default int getExecutionOrderList() {
        return 0;
    }

    default void registerEvent(IEventBus eventBus) {
        eventBus.register(this);
    }

    default void registerModEvent(IEventBus eventBus) {
    }


    @Override
    default Identifier getConfigName() {
        return new Identifier(GlowingFireGlow.getInstance().getModIdOfClass(this.getClass()), IdentifierUtil.ofPath(this.getClass()));
    }

    @Override
    default Identifier getBasicsConfigName() {
        return BASICS_CONFIG_NAME;
    }

    Identifier BASICS_CONFIG_NAME = new Identifier(GlowingFireGlow.MOD_ID, "world_component");
}
