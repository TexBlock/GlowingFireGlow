package com.til.glowing_fire_glow.common.register.capability;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.save.SaveManage;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public abstract class OriginalCapabilityRegister<E> extends CapabilityRegister<E> {

    @VoluntarilyAssignment
    protected SaveManage saveManage;

    @Override
    protected void initCommonSetup() {
        super.initCommonSetup();
        CapabilityManager.INSTANCE.register(getCapabilityClass(), new Capability.IStorage<E>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<E> capability, E instance, Direction side) {
                return defaultWriteNBT(capability, instance, side);
            }

            @Override
            public void readNBT(Capability<E> capability, E instance, Direction side, INBT nbt) {
                defaultReadNBT(capability, instance, side, nbt);
            }
        }, this::defaultFactory);

    }

    @Nullable
    protected INBT defaultWriteNBT(Capability<E> capability, E instance, Direction side) {
        CompoundNBT compoundNBT = new CompoundNBT();
        saveManage.getSavePack(instance.getClass()).write(Util.forcedConversion(instance), compoundNBT);
        return compoundNBT;
    }

    protected void defaultReadNBT(Capability<E> capability, E instance, Direction side, INBT nbt) {
        if (!(nbt instanceof CompoundNBT)) {
            return;
        }
        saveManage.getSavePack(instance.getClass()).read(Util.forcedConversion(instance), (CompoundNBT) nbt);
    }

    protected E defaultFactory() {
        return null;
    }
}
