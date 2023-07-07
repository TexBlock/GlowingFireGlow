package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockTags.class)
public interface BlockTagsMixin {
    @Accessor
    static TagRegistry<Block> getREGISTRY() {
        throw new UnsupportedOperationException();
    }
}
