package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.RequiredTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTypeTags.class)
public interface EntityTypeTagsMixin {
    @Accessor
    static RequiredTagList<EntityType<?>> getREQUIRED_TAGS() {
        throw new UnsupportedOperationException();
    }
}
