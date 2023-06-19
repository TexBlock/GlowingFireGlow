package com.til.glowing_fire_glow.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.util.Util;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author til
 */
public class RegisterBasicsTypeAdapter<T extends RegisterBasics> extends TypeAdapter<RegisterBasics> {

    public final Class<T> registerBasicsClass;

    public RegisterBasicsTypeAdapter(Class<T> registerBasicsClass) {
        this.registerBasicsClass = registerBasicsClass;
    }

    @Override
    public void write(JsonWriter out, RegisterBasics value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getName().toString());
    }

    @Override
    public RegisterBasics read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        ResourceLocation name = new ResourceLocation(in.nextString());
        return GlowingFireGlow.getInstance().getReflexManage().getRegisterManageOfType(Util.forcedConversion(registerBasicsClass)).get(name);
    }
}
