package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.IOException;

/**
 * @author til
 */
public class ForgeRegistryItemTypeAdapter<E extends IForgeRegistryEntry<E>> extends TypeAdapter<E> {

    public final IForgeRegistry<E> forgeRegistry;


    public ForgeRegistryItemTypeAdapter(IForgeRegistry<E> forgeRegistry) {
        this.forgeRegistry = forgeRegistry;
    }

    @Override
    public void write(JsonWriter out, E value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        Identifier Identifier = value.getRegistryName();
        if (Identifier == null) {
            out.nullValue();
            return;
        }
        out.value(Identifier.toString());
    }

    @Override
    public E read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        Identifier Identifier = new Identifier(in.nextString());
        return forgeRegistry.getValue(Identifier);
    }
}
