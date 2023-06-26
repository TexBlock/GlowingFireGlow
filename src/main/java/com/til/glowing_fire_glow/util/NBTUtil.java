package com.til.glowing_fire_glow.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import java.util.Map;
import java.util.Set;

/**
 * @author til
 */
public class NBTUtil {
    public static void clear(CompoundNBT compoundTag) {
        Set<String> strings = compoundTag.keySet();
        if (strings.isEmpty()) {
            return;
        }
        for (String string : strings) {
            compoundTag.remove(string);
        }
    }

    public static void clear(JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            jsonObject.remove(entry.getKey());
        }
    }

    public static void copy(CompoundNBT old, CompoundNBT compoundTag) {
        clear(old);
        Set<String> strings = compoundTag.keySet();
        if (strings.isEmpty()) {
            return;
        }
        for (String string : strings) {
            INBT tag = compoundTag.get(string);
            if (tag == null) {
                continue;
            }
            old.put(string, tag);
        }
    }

    public static INBT toTag(JsonElement jsonElement) {
        if (jsonElement.isJsonNull()) {
            return StringNBT.valueOf("null");
        }
        if (jsonElement.isJsonPrimitive()) {
            String s = jsonElement.getAsJsonPrimitive().getAsString();
            if (s.isEmpty()) {
                return DoubleNBT.valueOf(0);
            }
            String ns = s;
            char c = s.charAt(s.length() - 1);
            if (!Character.isDigit(c)) {
                ns = s.substring(0, s.length() - 1);
            }
            try {
                return DoubleNBT.valueOf(Double.parseDouble(ns));
            } catch (Exception e) {
                return StringNBT.valueOf(jsonElement.getAsJsonPrimitive().getAsString());
            }
        }
        if (jsonElement.isJsonArray()) {
            ListNBT listTag = new ListNBT();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                listTag.add(toTag(element));
            }
            return listTag;
        }
        if (jsonElement.isJsonObject()) {
            CompoundNBT compoundTag = new CompoundNBT();
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                compoundTag.put(entry.getKey(), toTag(entry.getValue()));
            }
            return compoundTag;
        }
        return new CompoundNBT();
    }

    public static JsonElement toJson(INBT tag) {
        if (tag instanceof StringNBT) {
            StringNBT stringTag = (StringNBT) tag;
            return new JsonPrimitive(stringTag.getString());
        }
        if (tag instanceof NumberNBT) {
            NumberNBT numberTag = (NumberNBT) tag;
            return new JsonPrimitive(numberTag.getDouble());
        }
        if (tag instanceof ListNBT) {
            ListNBT listTag = ((ListNBT) tag);
            JsonArray arrayList = new JsonArray();
            for (INBT tag1 : listTag) {
                arrayList.add(toJson(tag1));
            }
            return arrayList;
        }
        if (tag instanceof CompoundNBT) {
            CompoundNBT compoundTag = (CompoundNBT) tag;
            JsonObject jsonObject = new JsonObject();
            for (String allKey : compoundTag.keySet()) {
                jsonObject.add(allKey, toJson(compoundTag.get(allKey)));
            }
            return jsonObject;
        }
        return new JsonObject();
    }
}
