package com.til.glowing_fire_glow.common.register.capability;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.util.Util;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;


public abstract class CapabilityRegister<C> extends RegisterBasics {
    protected Class<C> cClass;
    protected Capability<C> capability;

    @Override
    public void beforeConfigInit() {
        super.beforeConfigInit();
        cClass = initClass();
    }

    protected Class<C> initClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(actualTypeArguments);
    }

    protected abstract Capability<C> initCapability();

    public Capability<C> getCapability() {
        if (capability == null) {
            capability = initCapability();
        }
        return capability;
    }

}
