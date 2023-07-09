package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.til.glowing_fire_glow.common.util.NBTUtil;
import net.minecraft.nbt.INBT;

import java.io.IOException;

/**
 * @author til
 */
public class NBTTypeAdapter extends TypeAdapter<INBT> {

    @Override
    public void write(JsonWriter out, INBT value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        Streams.write(NBTUtil.toJson(value, true), out);
    }

    @Override
    public INBT read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        return NBTUtil.toTag(Streams.parse(in));
    }
}
