package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.tag.TagManagerLoader;

import java.io.IOException;

/**
 * @author til
 */
public class TagTypeAdapter<E> extends TypeAdapter<TagKey<E>> {

    public final TagKey<E> tagRegistry;


    public TagTypeAdapter(TagKey<E> tagRegistry) {
        this.tagRegistry = tagRegistry;
    }

    @Override
    public void write(JsonWriter out, TagKey<E> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.id().toString());
    }

    @Override
    public TagKey<E> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        return tagRegistry.add(in.nextString());
    }
}
