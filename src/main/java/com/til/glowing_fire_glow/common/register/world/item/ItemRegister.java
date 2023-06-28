package com.til.glowing_fire_glow.common.register.world.item;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegister extends RegisterBasics {

    protected Item item;
    protected Tag<Item> itemTag;

    @Override
    public void initBack() {
        super.initBack();
        ForgeRegistries.ITEMS.register(item);
        //Dusk.instance.getTagManage().getItemTag().addTag(itemTag, item);
    }

    public Item getItem() {
        return item;
    }

    public Tag<Item> getItemTag() {
        return itemTag;
    }

}
