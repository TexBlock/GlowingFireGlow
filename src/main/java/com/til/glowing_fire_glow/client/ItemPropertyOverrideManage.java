package com.til.glowing_fire_glow.client;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.world.item.AllItemRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ItemPropertyOverrideManage implements IWorldComponent {

    @VoluntarilyAssignment
    protected AllItemRegister allItemRegister;

    @Override
    public void initClientSetup() {
        IWorldComponent.super.initClientSetup();
        Map<String, IItemPropertyGetter> map = new HashMap<>();
        ConsumerItemPropertyGetter consumerItemPropertyGetter = map::put;
        for (ItemRegister itemRegister : allItemRegister.forAll()) {
            itemRegister.propertyOverride(consumerItemPropertyGetter);
            if (!map.isEmpty()) {
                for (Map.Entry<String, IItemPropertyGetter> stringIItemPropertyGetterEntry : map.entrySet()) {
                    ItemModelsProperties.registerProperty(
                            itemRegister.getItem(),
                            new ResourceLocation(itemRegister.getName().getNamespace(), stringIItemPropertyGetterEntry.getKey()),
                            stringIItemPropertyGetterEntry.getValue());
                }
            }
            map.clear();
        }
    }


    @OnlyIn(Dist.CLIENT)
    public interface ConsumerItemPropertyGetter {
        void accept(String name, IItemPropertyGetter iItemPropertyGetter);
    }


}
