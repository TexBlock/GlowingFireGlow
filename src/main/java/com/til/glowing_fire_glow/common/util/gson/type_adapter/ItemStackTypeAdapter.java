package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.til.glowing_fire_glow.common.util.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.io.IOException;

public class ItemStackTypeAdapter extends TypeAdapter<ItemStack> {

    @Override
    public void write(JsonWriter out, ItemStack value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        NbtCompound NbtCompound = value.serializeNBT();
        JsonElement jsonElement = NBTUtil.toJson(NbtCompound, true);
        Streams.write(jsonElement, out);
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        JsonElement jsonElement = Streams.parse(in);
        return ItemStack.fromNbt((NbtCompound) NBTUtil.toTag(jsonElement));
    }
}
