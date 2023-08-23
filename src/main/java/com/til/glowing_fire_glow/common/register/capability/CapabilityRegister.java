package com.til.glowing_fire_glow.common.register.capability;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


public abstract class CapabilityRegister<C> extends RegisterBasics {
    protected Class<C> capabilityClass;
    protected Capability<C> capability;

    @Override
    public void beforeConfigInit() {
        super.beforeConfigInit();
        capabilityClass = initCapabilityClass();
    }

    protected Class<C> initCapabilityClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type parameterizedType = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(parameterizedType));
    }

    public void setCapability(Capability<C> capability) {
        this.capability = capability;
    }

    public Capability<C> getCapability() {
        return capability;
    }

    public Class<C> getCapabilityClass() {
        return capabilityClass;
    }

    public Supplier<C> supplierCapability(ICapabilityProvider capabilityProvider) {
        AtomicReference<C> cAtomicReference = new AtomicReference<>();
        return () -> {
            if (cAtomicReference.get() == null) {
                capabilityProvider.getCapability(getCapability()).ifPresent(cAtomicReference::set);
            }
            return cAtomicReference.get();
        };
    }

}
