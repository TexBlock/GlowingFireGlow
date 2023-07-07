package com.til.glowing_fire_glow.util.gson.type_adapter;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.crafting.Ingredient;

import java.io.IOException;

public class IngredientTypeAdapter extends TypeAdapter<Ingredient> {


    @Override
    public void write(JsonWriter out, Ingredient value) throws IOException {
        JsonElement jsonElement = value.serialize();
        Streams.write(jsonElement, out);
    }

    @Override
    public Ingredient read(JsonReader in) throws IOException {
        return Ingredient.deserialize(Streams.parse(in));
    }
}
