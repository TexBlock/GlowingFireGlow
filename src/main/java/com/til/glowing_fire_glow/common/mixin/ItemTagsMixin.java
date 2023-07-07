package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemTags.class)
public interface ItemTagsMixin {
    @Accessor
    @Mutable
    static TagRegistry<Item> getREGISTRY() {
        throw new UnsupportedOperationException();
    }
}
