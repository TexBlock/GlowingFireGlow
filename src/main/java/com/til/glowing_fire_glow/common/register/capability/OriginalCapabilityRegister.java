package com.til.glowing_fire_glow.common.register.capability;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.save.SaveManage;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
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
            public NbtElement writeNBT(Capability<E> capability, E instance, Direction side) {
                return defaultWriteNBT(capability, instance, side);
            }

            @Override
            public void readNBT(Capability<E> capability, E instance, Direction side, NbtElement nbt) {
                defaultReadNBT(capability, instance, side, nbt);
            }
        }, this::defaultFactory);

    }

    @Nullable
    protected NbtElement defaultWriteNBT(Capability<E> capability, E instance, Direction side) {
        NbtCompound NbtCompound = new NbtCompound();
        saveManage.getSavePack(instance.getClass()).write(Util.forcedConversion(instance), NbtCompound);
        return NbtCompound;
    }

    protected void defaultReadNBT(Capability<E> capability, E instance, Direction side, NbtElement nbt) {
        if (!(nbt instanceof NbtCompound)) {
            return;
        }
        saveManage.getSavePack(instance.getClass()).read(Util.forcedConversion(instance), (NbtCompound) nbt);
    }

    protected E defaultFactory() {
        return null;
    }
}
