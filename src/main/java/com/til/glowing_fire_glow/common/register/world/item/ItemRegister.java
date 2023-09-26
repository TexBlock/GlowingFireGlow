package com.til.glowing_fire_glow.common.register.world.item;

import com.til.glowing_fire_glow.client.ColorProxyManage;
import com.til.glowing_fire_glow.client.ItemPropertyOverrideManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.tag.ItemTagManage;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ItemRegister extends RegisterBasics {

    protected Item item;

    @VoluntarilyAssignment
    protected ItemTagManage itemTagManage;

    @Override
    protected void init() {
        super.init();
        //itemName = ResourceLocationUtil.fuseName(this.getName().getNamespace(), "/", new String[]{"item", this.getName().getPath()});
        item = initItem();
        item.setRegistryName(getName());
        itemTagManage.addTag(getName(), item);
        ForgeRegistries.ITEMS.register(item);
    }

    protected abstract Item initItem();

    @OnlyIn(Dist.CLIENT)
    public void dyeColor(ColorProxyManage.ItemColorPack colorPack) {
    }

    @OnlyIn(Dist.CLIENT)
    public void propertyOverride(ItemPropertyOverrideManage.ConsumerItemPropertyGetter consumerItemPropertyGetter) {
    }

    public Item getItem() {
        return item;
    }


}
