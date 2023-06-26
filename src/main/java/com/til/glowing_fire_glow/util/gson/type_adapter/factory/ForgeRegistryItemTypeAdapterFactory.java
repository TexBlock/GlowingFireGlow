package com.til.glowing_fire_glow.util.gson.type_adapter.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.til.glowing_fire_glow.util.gson.type_adapter.ForgeRegistryItemTypeAdapter;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

/**
 * @author til
 */
public class ForgeRegistryItemTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<?> clazz = type.getRawType();
        if (IForgeRegistryEntry.class.isAssignableFrom(clazz)) {
            Class<?> basics = clazz;
            while (basics != null) {
                IForgeRegistry<?> forgeRegistry = RegistryManager.ACTIVE.getRegistry((Class<? super IForgeRegistryEntry<?>>) basics);
                if (forgeRegistry == null) {
                    basics = basics.getSuperclass();
                    continue;
                }
                return new ForgeRegistryItemTypeAdapter(forgeRegistry);
            }
        }
        return null;
    }
}
