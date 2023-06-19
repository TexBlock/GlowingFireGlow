package com.til.glowing_fire_glow.common.register.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public abstract class OriginalCapabilityRegister<E> extends CapabilityRegister<E> {

    @Override
    protected void init() {
        super.init();
        CapabilityManager.INSTANCE.register(cClass, new Capability.IStorage<E>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<E> capability, E instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<E> capability, E instance, Direction side, INBT nbt) {

            }
        }, () -> null);
    }
}
