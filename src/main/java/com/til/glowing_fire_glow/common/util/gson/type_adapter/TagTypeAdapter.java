package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.tag.Tag;
import net.minecraft.tag.RequiredTagList;

import java.io.IOException;

/**
 * @author til
 */
public class TagTypeAdapter<E> extends TypeAdapter<Tag.Identified<E>> {

    public final RequiredTagList<E> tagRegistry;


    public TagTypeAdapter(RequiredTagList<E> tagRegistry) {
        this.tagRegistry = tagRegistry;
    }

    @Override
    public void write(JsonWriter out, Tag.Identified<E> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getId().toString());
    }

    @Override
    public Tag.Identified<E> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        return tagRegistry.add(in.nextString());
    }
}
