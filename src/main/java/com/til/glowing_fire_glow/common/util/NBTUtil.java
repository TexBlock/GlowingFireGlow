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
                return jsonPrimitive.getAsBoolean() ? StringNBT.valueOf("true") : StringNBT.valueOf("false");
            }
            if (jsonPrimitive.isString()) {
                String s = jsonElement.getAsJsonPrimitive().getAsString();
                if (s.isEmpty()) {
                    return StringNBT.valueOf("");
                }
                char c = s.charAt(s.length() - 1);
                String ns = s.substring(0, s.length() - 1);
                if (StringUtil.checkStrIsNum(ns)) {
                    switch (c) {
                        case 'B':
                            return ByteNBT.valueOf(Byte.parseByte(ns));
                        case 'S':
                            return ShortNBT.valueOf(Short.parseShort(ns));
                        case 'I':
                            return IntNBT.valueOf(Integer.parseInt(ns));
                        case 'L':
                            return LongNBT.valueOf(Long.parseLong(ns));
                        case 'F':
                            return FloatNBT.valueOf(Float.parseFloat(ns));
                        case 'D':
                            return DoubleNBT.valueOf(Double.parseDouble(ns));
                    }
                }
                return StringNBT.valueOf(jsonElement.getAsString());
            }
            if (jsonPrimitive.isNumber()) {
                return DoubleNBT.valueOf(jsonPrimitive.getAsDouble());
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
                        return new ByteArrayNBT(list.stream().map(JsonElement::getAsByte).collect(Collectors.toList()));
                    case "I":
                        return new IntArrayNBT(list.stream().map(JsonElement::getAsInt).collect(Collectors.toList()));
                    case "L":
                        return new LongArrayNBT(list.stream().map(JsonElement::getAsLong).collect(Collectors.toList()));
                }
            }
            CompoundNBT compoundTag = new CompoundNBT();
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                compoundTag.put(entry.getKey(), toTag(entry.getValue()));
            }
            return compoundTag;
        }
        return new CompoundNBT();
    }

    public static final String LIST_TAG = "$list_tag";
    public static final String LIST = "$list";

    public static JsonElement toJson(INBT tag, boolean hasType) {
        if (tag == null) {
            return JsonNull.INSTANCE;
        }
        if (tag instanceof StringNBT) {
            StringNBT stringTag = (StringNBT) tag;
            if (stringTag.getString().equals("null")) {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(stringTag.getString());
        }
        if (tag instanceof NumberNBT) {
            if (hasType) {
                if (tag instanceof IntNBT) {
                    return new JsonPrimitive(((IntNBT) tag).getInt() + "I");
                }
                if (tag instanceof ByteNBT) {
                    return new JsonPrimitive(((ByteNBT) tag).getByte() + "B");
                }
                if (tag instanceof ShortNBT) {
                    return new JsonPrimitive(((ShortNBT) tag).getShort() + "S");
                }
                if (tag instanceof LongNBT) {
                    return new JsonPrimitive(((LongNBT) tag).getLong() + "L");
                }
                if (tag instanceof FloatNBT) {
                    return new JsonPrimitive(((FloatNBT) tag).getFloat() + "F");
                }
                if (tag instanceof DoubleNBT) {
                    return new JsonPrimitive(((DoubleNBT) tag).getDouble() + "D");
                }
            }
            return new JsonPrimitive(((NumberNBT) tag).getDouble());
        }
        if (tag instanceof CollectionNBT) {
            if (tag instanceof ListNBT) {
                ListNBT listTag = ((ListNBT) tag);
                JsonArray arrayList = new JsonArray();
                for (INBT tag1 : listTag) {
                    arrayList.add(toJson(tag1, hasType));
                }
                return arrayList;
            }
            JsonObject jsonObject = new JsonObject();
            if (tag instanceof ByteArrayNBT) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("B"));
                JsonArray jsonArray = new JsonArray();
                for (ByteNBT byteNBT : ((ByteArrayNBT) tag)) {
                    jsonArray.add(new JsonPrimitive(byteNBT.getByte()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            if (tag instanceof IntArrayNBT) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("I"));
                JsonArray jsonArray = new JsonArray();
                for (IntNBT intNBT : ((IntArrayNBT) tag)) {
                    jsonArray.add(new JsonPrimitive(intNBT.getInt()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            if (tag instanceof LongArrayNBT) {
                jsonObject.add(LIST_TAG, new JsonPrimitive("D"));
                JsonArray jsonArray = new JsonArray();
                for (LongNBT longNBT : ((LongArrayNBT) tag)) {
                    jsonArray.add(new JsonPrimitive(longNBT.getLong()));
                }
                jsonObject.add(LIST, jsonArray);
            }
            return jsonObject;
        }
        if (tag instanceof CompoundNBT) {
            CompoundNBT compoundTag = (CompoundNBT) tag;
            JsonObject jsonObject = new JsonObject();
            for (String allKey : compoundTag.keySet()) {
                jsonObject.add(allKey, toJson(compoundTag.get(allKey), hasType));
            }
            return jsonObject;
        }
        return new JsonObject();
    }
}
