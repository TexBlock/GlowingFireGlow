package com.til.glowing_fire_glow.common.util;

import com.google.gson.*;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author til
 */
public class NBTUtil {
    public static void clear(NbtCompound compoundTag) {
        Set<String> strings = compoundTag.getKeys();
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

    public static void copy(NbtCompound old, NbtCompound compoundTag) {
        clear(old);
        Set<String> strings = compoundTag.getKeys();
        if (strings.isEmpty()) {
            return;
        }
        for (String string : strings) {
            NbtElement tag = compoundTag.get(string);
            if (tag == null) {
                continue;
            }
            old.put(string, tag);
        }
    }

    public static NbtElement toTag(JsonElement jsonElement) {
        if (jsonElement.isJsonNull()) {
            return NbtString.of("null");
        }
        /*if (jsonElement.isJsonPrimitive()) {
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
        }*/
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean()) {
                return jsonPrimitive.getAsBoolean() ? NbtString.of("true") : NbtString.of("false");
            }
            if (jsonPrimitive.isString()) {
                String s = jsonElement.getAsJsonPrimitive().getAsString();
                if (s.isEmpty()) {
                    return NbtString.of("");
                }
                char c = s.charAt(s.length() - 1);
                String ns = s.substring(0, s.length() - 1);
                if (StringUtil.checkStrIsNum(ns)) {
                    switch (c) {
                        case 'B':
                            return NbtByte.of(Byte.parseByte(ns));
                        case 'S':
                            return NbtShort.of(Short.parseShort(ns));
                        case 'I':
                            return NbtInt.of(Integer.parseInt(ns));
                        case 'L':
                            return NbtLong.of(Long.parseLong(ns));
                        case 'F':
                            return NbtFloat.of(Float.parseFloat(ns));
                        case 'D':
                            return NbtDouble.of(Double.parseDouble(ns));
                    }
                }
                return NbtString.of(jsonElement.getAsString());
            }
            if (jsonPrimitive.isNumber()) {
                return NbtDouble.of(jsonPrimitive.getAsDouble());
            }
        }
        if (jsonElement.isJsonArray()) {
            NbtList listTag = new NbtList();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                listTag.add(toTag(element));
            }
            return listTag;
        }
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.size() == 2 && jsonObject.has(LIST_TAG) && jsonObject.has(LIST)) {
                String tag = jsonObject.get(LIST_TAG).getAsString();
                JsonArray arrayList = jsonObject.getAsJsonArray(LIST);
                List<JsonElement> list = new ArrayList<>(arrayList.size());
                for (JsonElement element : arrayList) {
                    list.add(element);
                }
                switch (tag) {
                    case "B":
                        return new NbtByteArray(list.stream().map(JsonElement::getAsByte).collect(Collectors.toList()));
                    case "I":
                        return new NbtIntArray(list.stream().map(JsonElement::getAsInt).collect(Collectors.toList()));
                    case "L":
                        return new NbtLongArray(list.stream().map(JsonElement::getAsLong).collect(Collectors.toList()));
                }
            }
            NbtCompound compoundTag = new NbtCompound();
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                compoundTag.put(entry.getKey(), toTag(entry.getValue()));
            }
            return compoundTag;
        }
        return new NbtCompound();
    }

    public static final String LIST_TAG = "$list_tag";
    public static final String LIST = "$list";

    public static JsonElement toJson(NbtElement tag, boolean hasType) {
        if (tag == null) {
            return JsonNull.INSTANCE;
        }
        if (tag instanceof NbtString) {
            NbtString stringTag = (NbtString) tag;
            if (stringTag.asString().equals("null")) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(stringTag.asString());
        }
        if (tag instanceof AbstractNbtNumber) {
            if (hasType) {
                if (tag instanceof NbtInt) {
                    return new JsonPrimitive(((NbtInt) tag).intValue() + "I");
                }
                if (tag instanceof NbtByte) {
                    return new JsonPrimitive(((NbtByte) tag).byteValue() + "B");
                }
                if (tag instanceof NbtShort) {
                    return new JsonPrimitive(((NbtShort) tag).shortValue() + "S");
                }
                if (tag instanceof NbtLong) {
                    return new JsonPrimitive(((NbtLong) tag).longValue() + "L");
                }
                if (tag instanceof NbtFloat) {
                    return new JsonPrimitive(((NbtFloat) tag).floatValue() + "F");
                }
                if (tag instanceof NbtDouble) {
                    return new JsonPrimitive(((NbtDouble) tag).doubleValue() + "D");
                }
            }
            return new JsonPrimitive(((AbstractNbtNumber) tag).doubleValue());
        }
        if (tag instanceof AbstractNbtList) {
            if (tag instanceof NbtList) {
                NbtList listTag = ((NbtList) tag);
                JsonArray arrayList = new JsonArray();
                for (NbtElement tag1 : listTag) {
                    arrayList.add(toJson(tag1, hasType));
                }
                return arrayList;
            }
            JsonObject jsonObject = new JsonObject();
            if (tag instanceof NbtByteArray) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("B"));
                JsonArray jsonArray = new JsonArray();
                for (NbtByte byteNBT : ((NbtByteArray) tag)) {
                    jsonArray.add(new JsonPrimitive(byteNBT.byteValue()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            if (tag instanceof NbtIntArray) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("I"));
                JsonArray jsonArray = new JsonArray();
                for (NbtInt intNBT : ((NbtIntArray) tag)) {
                    jsonArray.add(new JsonPrimitive(intNBT.intValue()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            if (tag instanceof NbtLongArray) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("D"));
                JsonArray jsonArray = new JsonArray();
                for (NbtLong longNBT : ((NbtLongArray) tag)) {
                    jsonArray.add(new JsonPrimitive(longNBT.longValue()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            return jsonObject;
        }
        if (tag instanceof NbtCompound) {
            NbtCompound compoundTag = (NbtCompound) tag;
            JsonObject jsonObject = new JsonObject();
            for (String allKey : compoundTag.getKeys()) {
                jsonObject.add(allKey, toJson(compoundTag.get(allKey), hasType));
            }
            return jsonObject;
        }
        return new JsonObject();
    }
}
