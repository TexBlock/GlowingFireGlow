package com.til.glowing_fire_glow.common.main;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IWorldComponent {

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
}
