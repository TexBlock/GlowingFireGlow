package com.til.glowing_fire_glow.common.main;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IWorldComponent {

    default void init(InitType initType) {
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

    enum InitType {
        NEW,
        FML_COMMON_SETUP,
        FML_DEDICATED_SERVER_SETUP,
        FML_CLIENT_SETUP,
        INTER_MOD_PROCESS_EVENT,
    }
}
