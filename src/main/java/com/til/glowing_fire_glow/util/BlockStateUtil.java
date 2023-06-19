package com.til.glowing_fire_glow.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author til
 */
public class BlockStateUtil {

    public static BlockState create(@Nullable Block block, Map<String, String> v) {
        assert block != null;
        BlockState blockState = block.getDefaultState();
        if (v == null) {
            return blockState;
        }
        for (Map.Entry<Property<?>, Comparable<?>> entry : blockState.getValues().entrySet()) {
            if (!v.containsKey(entry.getKey().getName())) {
                continue;
            }
            Optional<?> vOptional = entry.getKey().parseValue(v.get(entry.getKey().getName()));
            if (!vOptional.isPresent()) {
                continue;
            }
            blockState = blockState.with(entry.getKey(), Util.forcedConversion(vOptional.get()));
        }
        return blockState;
    }

    public static BlockState create(@Nullable Block block, JsonObject v) {
        assert block != null;
        if (v == null) {
            return block.getDefaultState();
        }
        Map<String, String> map = new HashMap<>(0);
        for (Map.Entry<String, JsonElement> entry : v.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }
        return create(block, map);
    }


}
