package com.til.glowing_fire_glow.common.register.key;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/***
 * 当按键按下去时触发
 * @author til
 */
public class EventKey extends Event {
    public final KeyRegister keyRegister;
    public final KeyData.KeyState keyState;
    public final Supplier<NetworkEvent.Context> contextSupplier;

    public EventKey(KeyRegister keyRegister, KeyData.KeyState keyState, Supplier<NetworkEvent.Context> contextSupplier) {
        this.keyRegister = keyRegister;
        this.keyState = keyState;
        this.contextSupplier = contextSupplier;
    }
}
