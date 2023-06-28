package com.til.glowing_fire_glow.common.register.world.fluid;

import com.til.glowing_fire_glow.common.register.RegisterManage;

public class AllFluidRegister extends RegisterManage<FluidRegister> {
    @Override
    public int getExecutionOrderList() {
        return -10;
    }
}
