package com.til.glowing_fire_glow.client;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.world.item.AllItemRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
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
        Map<String, ModelPredicateProvider> map = new HashMap<>();
        ConsumerItemPropertyGetter consumerItemPropertyGetter = map::put;
        for (ItemRegister itemRegister : allItemRegister.forAll()) {
            itemRegister.propertyOverride(consumerItemPropertyGetter);
            if (!map.isEmpty()) {
                for (Map.Entry<String, ModelPredicateProvider> stringModelPredicateProviderEntry : map.entrySet()) {
                    ModelPredicateProviderRegistry.register(
                            itemRegister.getItem(),
                            new Identifier(itemRegister.getName().getNamespace(), stringModelPredicateProviderEntry.getKey()),
                            stringModelPredicateProviderEntry.getValue());
                }
            }
            map.clear();
        }
    }


    @OnlyIn(Dist.CLIENT)
    public interface ConsumerItemPropertyGetter {
        void accept(String name, ModelPredicateProvider iItemPropertyGetter);
    }


}
