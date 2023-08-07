package com.til.glowing_fire_glow.common.register.capability.synchronous;

import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import com.til.glowing_fire_glow.common.synchronous.SynchronousManage;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.nbt.CompoundNBT;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class SynchronousCapabilityRegister<C  , CR extends CapabilityRegister<C>> extends RegisterBasics {

    @VoluntarilyAssignment
    protected ReflexManage reflexManage;

    @VoluntarilyAssignment
    protected SynchronousManage synchronousManage;

    protected Class<C> capabilityClass;
    protected Class<CR> capabilityRegisterClass;
    protected CR capabilityRegister;

    @Override
    protected void init() {
        super.init();
        initClass();
        capabilityRegister = reflexManage.getVoluntarilyRegisterOfClass(capabilityRegisterClass);
    }

    protected void initClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type[] types = parameterized.getActualTypeArguments();
        capabilityClass = Util.forcedConversion(ReflexUtil.asClass(types[0]));
        capabilityRegisterClass = Util.forcedConversion(ReflexUtil.asClass(types[1]));
    }

    public Class<C> getCapabilityClass() {
        return capabilityClass;
    }

    public Class<CR> getCapabilityRegisterClass() {
        return capabilityRegisterClass;
    }

    public CR getCapabilityRegister() {
        return capabilityRegister;
    }

    public CompoundNBT defaultWriteNBT(C instance) {
        CompoundNBT compoundNBT = new CompoundNBT();
        synchronousManage.getPack(instance.getClass()).write(Util.forcedConversion(instance), compoundNBT);
        return compoundNBT;
    }

    public void defaultReadNBT(C instance, CompoundNBT nbt) {
        synchronousManage.getPack(instance.getClass()).read(Util.forcedConversion(instance), nbt);
    }
}
