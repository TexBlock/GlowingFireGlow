package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.RequiredTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockTags.class)
public interface BlockTagsMixin {
    @Accessor
    static RequiredTagList<Block> getREQUIRED_TAGS() {
        throw new UnsupportedOperationException();
    }
}
