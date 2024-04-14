package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.ItemTagsMixin;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import net.minecraft.item.Item;
import net.minecraft.tag.RequiredTagList;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

/**
 * 标签反射添加
 *
 * @author til
 */
public class ItemTagManage extends TagManage<Item> {

    @VoluntarilyAssignment
    protected BlockTagManage blockTag;


    @Override
    protected TagKey<Item> initTagManager() {
        return TagKey.of(Registry.ITEM_KEY);
    }
}
