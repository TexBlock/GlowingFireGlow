package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.RequiredTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidTags.class)
public interface FluidTagsMixin {
    @Accessor
    static RequiredTagList<Fluid> getREQUIRED_TAGS() {
        throw new UnsupportedOperationException();
    }
}
