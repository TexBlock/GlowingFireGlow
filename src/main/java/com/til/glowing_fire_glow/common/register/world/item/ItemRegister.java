package com.til.glowing_fire_glow.common.register.world.item;

import com.til.glowing_fire_glow.client.ColorProxy;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.tag.ItemTagManage;
import com.til.glowing_fire_glow.util.ResourceLocationUtil;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ItemRegister extends RegisterBasics {
    protected ResourceLocation itemName;

    protected Item item;

    @VoluntarilyAssignment
    protected ItemTagManage itemTagManage;

    @Override
    protected void init() {
        super.init();
        itemName = ResourceLocationUtil.fuseName(this.getName().getNamespace(), "/", new String[]{"block", this.getName().getPath()});
        item = initItem();
        item.setRegistryName(itemName);
        itemTagManage.addTag(itemName, item);
        ForgeRegistries.ITEMS.register(item);
    }

    protected abstract Item initItem();

    @OnlyIn(Dist.CLIENT)
    public void dyeColor(ColorProxy.ItemColorPack colorPack) {
    }


    public ResourceLocation getItemName() {
        return itemName;
    }

    public Item getItem() {
        return item;
    }


}
