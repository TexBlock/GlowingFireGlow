package com.til.glowing_fire_glow.common.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagRegistry;

import java.io.IOException;

/**
 * @author til
 */
public class TagTypeAdapter<E> extends TypeAdapter<ITag.INamedTag<E>> {

    public final TagRegistry<E> tagRegistry;


    public TagTypeAdapter(TagRegistry<E> tagRegistry) {
        this.tagRegistry = tagRegistry;
    }

    @Override
    public void write(JsonWriter out, ITag.INamedTag<E> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getName().toString());
    }

    @Override
    public ITag.INamedTag<E> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        return tagRegistry.createTag(in.nextString());
    }
}
