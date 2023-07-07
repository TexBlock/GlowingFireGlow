package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.BlockTagsMixin;
import net.minecraft.block.Block;
import net.minecraft.tags.TagRegistry;

/**
 * 标签反射添加
 *
 * @author til
 */
public class BlockTagManage extends TagManage<Block> {
    @Override
    protected TagRegistry<Block> initTagManager() {
        return BlockTagsMixin.getREGISTRY();
    }
}
