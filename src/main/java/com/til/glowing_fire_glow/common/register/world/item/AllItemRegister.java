package com.til.glowing_fire_glow.common.register.world.item;

import com.til.glowing_fire_glow.common.register.RegisterManage;

public class AllItemRegister extends RegisterManage<ItemRegister> {


    @Override
    public int getExecutionOrderList() {
        return -10;
    }
}
