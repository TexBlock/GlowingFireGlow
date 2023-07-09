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
        CapabilityManager.INSTANCE.register(cClass, new Capability.IStorage<E>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<E> capability, E instance, Direction side) {
                CompoundNBT compoundNBT = new CompoundNBT();
                saveManage.getSavePack(instance.getClass()).write(Util.forcedConversion(instance), compoundNBT);
                return compoundNBT;
            }

            @Override
            public void readNBT(Capability<E> capability, E instance, Direction side, INBT nbt) {
                if (!(nbt instanceof CompoundNBT)) {
                    return;
                }
                saveManage.getSavePack(instance.getClass()).read(Util.forcedConversion(instance), (CompoundNBT) nbt);
            }
        }, () -> null);

    }
}
