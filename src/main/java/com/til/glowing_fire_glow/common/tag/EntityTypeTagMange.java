package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.EntityTypeTagsMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.RequiredTagList;

public class EntityTypeTagMange extends TagManage<EntityType<?>> {

    @Override
    protected RequiredTagList<EntityType<?>> initTagManager() {
        return EntityTypeTagsMixin.getREQUIRED_TAGS();
    }
}
