package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.til.glowing_fire_glow.common.util.NBTUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;

public class FluidStackTypeAdapter extends TypeAdapter<FluidStack> {

    @Override
    public void write(JsonWriter out, FluidStack value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        NbtCompound NbtCompound = value.writeToNBT(new NbtCompound());
        JsonElement jsonElement = NBTUtil.toJson(NbtCompound, true);
        Streams.write(jsonElement, out);
    }


    @Override
    public FluidStack read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        JsonElement jsonElement = Streams.parse(in);
        return FluidStack.loadFluidStackFromNBT((NbtCompound) NBTUtil.toTag(jsonElement));
    }
}
