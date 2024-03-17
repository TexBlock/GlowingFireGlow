package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.til.glowing_fire_glow.common.util.NBTUtil;
import net.minecraft.nbt.NbtElement;

import java.io.IOException;

/**
 * @author til
 */
public class NBTTypeAdapter extends TypeAdapter<NbtElement> {

    @Override
    public void write(JsonWriter out, NbtElement value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        Streams.write(NBTUtil.toJson(value, true), out);
    }

    @Override
    public NbtElement read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        return NBTUtil.toTag(Streams.parse(in));
    }
}
