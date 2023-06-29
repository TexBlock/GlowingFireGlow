package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.EntityTypeTagsAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.TagRegistry;

public class EntityTypeTagMange extends TagManage<EntityType<?>> {

    @Override
    protected TagRegistry<EntityType<?>> initTagManager() {
        return EntityTypeTagsAccessor.getREGISTRY();
    }
}
