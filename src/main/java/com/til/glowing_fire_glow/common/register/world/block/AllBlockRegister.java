package com.til.glowing_fire_glow.common.register.world.block;

import com.til.glowing_fire_glow.common.register.RegisterManage;

public class AllBlockRegister extends RegisterManage<BlockRegister> {

    @Override
    public int getExecutionOrderList() {
        return -10;
    }
}
