package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.BlockTagsMixin;
import net.minecraft.block.Block;
import net.minecraft.tag.RequiredTagList;

/**
 * 标签反射添加
 *
 * @author til
 */
public class BlockTagManage extends TagManage<Block> {
    @Override
    protected RequiredTagList<Block> initTagManager() {
        return BlockTagsMixin.getREQUIRED_TAGS();
    }
}
