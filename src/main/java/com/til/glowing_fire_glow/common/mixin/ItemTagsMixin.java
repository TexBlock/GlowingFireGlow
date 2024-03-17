package com.til.glowing_fire_glow.common.mixin;

import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.RequiredTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemTags.class)
public interface ItemTagsMixin {
    @Accessor
    @Mutable
    static RequiredTagList<Item> getREQUIRED_TAGS() {
        throw new UnsupportedOperationException();
    }
}
