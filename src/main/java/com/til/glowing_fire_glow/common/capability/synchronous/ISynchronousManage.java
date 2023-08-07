package com.til.glowing_fire_glow.common.capability.synchronous;

import com.til.glowing_fire_glow.common.register.capability.synchronous.SynchronousCapabilityRegister;

import java.util.Set;

public interface ISynchronousManage {

    Set<SynchronousCapabilityRegister<?, ?>> getSynchronousCapabilitySet();

    default void addSynchronousCapability(SynchronousCapabilityRegister<?, ?> synchronousCapabilityRegister) {
        getSynchronousCapabilitySet().add(synchronousCapabilityRegister);
    }

    default void synchronousBack() {
        getSynchronousCapabilitySet().clear();
    }

    default boolean needSynchronous() {
        return !getSynchronousCapabilitySet().isEmpty();
    }

}
