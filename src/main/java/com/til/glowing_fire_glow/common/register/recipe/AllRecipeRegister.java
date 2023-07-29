package com.til.glowing_fire_glow.common.register.recipe;

import com.til.glowing_fire_glow.common.register.RegisterManage;

public class AllRecipeRegister extends RegisterManage<RecipeRegister<?, ?>> {
    @Override
    public int getVoluntarilyRegisterTime() {
        return -100;
    }
}
