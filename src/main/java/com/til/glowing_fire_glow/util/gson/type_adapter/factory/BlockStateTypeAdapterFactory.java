package com.til.glowing_fire_glow.util.gson.type_adapter.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.til.glowing_fire_glow.util.Util;
import com.til.glowing_fire_glow.util.gson.type_adapter.BlockStateTypeAdapter;
import net.minecraft.block.BlockState;

public class BlockStateTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (BlockState.class.isAssignableFrom(typeToken.getRawType())) {
            return Util.forcedConversion(new BlockStateTypeAdapter());
        }
        return null;
    }
}
