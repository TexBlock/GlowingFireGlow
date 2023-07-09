package com.til.glowing_fire_glow.common.util.gson.type_adapter.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.util.Util;
import com.til.glowing_fire_glow.common.util.gson.type_adapter.RegisterBasicsTypeAdapter;

/**
 * @author til
 */
public class RegisterBasicsAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (RegisterBasics.class.isAssignableFrom(type.getRawType())) {
            return Util.forcedConversion(new RegisterBasicsTypeAdapter<>(Util.forcedConversion(type.getRawType())));
        }
        return null;
    }
}
