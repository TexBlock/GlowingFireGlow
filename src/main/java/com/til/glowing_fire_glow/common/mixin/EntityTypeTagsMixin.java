package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTypeTags.class)
public interface EntityTypeTagsMixin {
    @Accessor
    static TagRegistry<EntityType<?>> getREGISTRY() {
        throw new UnsupportedOperationException();
    }
}
