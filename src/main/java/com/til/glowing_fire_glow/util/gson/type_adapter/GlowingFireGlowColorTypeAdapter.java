package com.til.glowing_fire_glow.util.gson.type_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;

import java.io.IOException;

/**
 * @author til
 */
public class GlowingFireGlowColorTypeAdapter extends TypeAdapter<GlowingFireGlowColor> {

    public static final String R = "r";
    public static final String G = "g";
    public static final String B = "b";
    public static final String A = "a";


    @Override
    public void write(JsonWriter out, GlowingFireGlowColor value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name(R).value(value.getRed());
        out.name(G).value(value.getGreen());
        out.name(B).value(value.getBlue());
        out.name(A).value(value.getAlpha());
        out.endObject();
    }

    @Override
    public GlowingFireGlowColor read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            return null;
        }
        int r = 0;
        int g = 0;
        int b = 0;
        int a = 0;
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case R:
                    r = in.nextInt();
                    break;
                case B:
                    b = in.nextInt();
                    break;
                case G:
                    g = in.nextInt();
                    break;
                case A:
                    a = in.nextInt();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return new GlowingFireGlowColor(r, g, b, a);
    }

}
