package com.til.glowing_fire_glow.common.capability.synchronous;

import com.til.glowing_fire_glow.common.register.capability.synchronous.SynchronousCapabilityRegister;

import java.util.HashSet;
import java.util.Set;

public class SynchronousManage implements ISynchronousManage {

    protected Set<SynchronousCapabilityRegister<?, ?>> set = new HashSet<>();

    @Override
    public Set<SynchronousCapabilityRegister<?, ?>> getSynchronousCapabilitySet() {
        return set;
    }
}

