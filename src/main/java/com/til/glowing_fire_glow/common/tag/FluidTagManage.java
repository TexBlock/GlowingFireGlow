package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.FluidTagsMixin;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.TagRegistry;

/**
 * @author til
 */
public class FluidTagManage extends TagManage<Fluid> {


    @Override
    protected TagRegistry<Fluid> initTagManager() {
        return FluidTagsMixin.getREGISTRY();
    }
}
