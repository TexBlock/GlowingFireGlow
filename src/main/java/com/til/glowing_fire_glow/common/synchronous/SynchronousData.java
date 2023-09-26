package com.til.glowing_fire_glow.common.synchronous;

import com.til.glowing_fire_glow.common.capability.synchronous.ISynchronousManage;
import com.til.glowing_fire_glow.common.register.capability.synchronous.SynchronousCapabilityRegister;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;

public class SynchronousData {

    protected int entityId;

    protected Map<SynchronousCapabilityRegister<?, ?>, CompoundNBT> data;

    public SynchronousData(int entityId, Map<SynchronousCapabilityRegister<?, ?>, CompoundNBT> data) {
        this.entityId = entityId;
        this.data = data;
    }

    public static SynchronousData of(Entity entity, ISynchronousManage iSynchronousManage) {
        Map<SynchronousCapabilityRegister<?, ?>, CompoundNBT> map = new HashMap<>();
        for (SynchronousCapabilityRegister<?, ?> synchronousCapabilityRegister : iSynchronousManage.getSynchronousCapabilitySet()) {
            LazyOptional<?> lazyOptional = entity.getCapability(synchronousCapabilityRegister.getCapabilityRegister().getCapability());
            lazyOptional.ifPresent(c -> {
                map.put(synchronousCapabilityRegister, synchronousCapabilityRegister.defaultWriteNBT(Util.forcedConversion(c)));
            });
        }
        return new SynchronousData(entity.getEntityId(), map);
    }

    public int getEntityId() {
        return entityId;
    }

    public Map<SynchronousCapabilityRegister<?, ?>, CompoundNBT> getData() {
        return data;
    }
}
