package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidTags.class)
public interface FluidTagsMixin {
    @Accessor
    static TagRegistry<Fluid> getREGISTRY() {
        throw new UnsupportedOperationException();
    }
}
