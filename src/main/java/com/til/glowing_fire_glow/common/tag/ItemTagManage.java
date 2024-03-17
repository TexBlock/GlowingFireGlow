package com.til.glowing_fire_glow.common.tag;

import com.til.glowing_fire_glow.common.mixin.ItemTagsMixin;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import net.minecraft.item.Item;
import net.minecraft.tag.RequiredTagList;

/**
 * 标签反射添加
 *
 * @author til
 */
public class ItemTagManage extends TagManage<Item> {

    @VoluntarilyAssignment
    protected BlockTagManage blockTag;


    @Override
    protected RequiredTagList<Item> initTagManager() {
        return ItemTagsMixin.getREQUIRED_TAGS();
    }
}
