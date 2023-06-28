package com.til.glowing_fire_glow.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.til.glowing_fire_glow.common.mixin.BlockTagsAccessor;
import com.til.glowing_fire_glow.common.mixin.EntityTypeTagsAccessor;
import com.til.glowing_fire_glow.common.mixin.FluidTagsAccessor;
import com.til.glowing_fire_glow.common.mixin.ItemTagsAccessor;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.gson.type_adapter.*;
import com.til.glowing_fire_glow.util.gson.type_adapter.factory.*;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

/**
 * @author til
 */
public class ConfigGson {

    public static final String TYPE = "$type";
    public static final String GENERIC = "$generic";
    public static final String CONFIG = "$config";

    protected static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            init();
        }
        return gson;
    }

    protected static void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();


        gsonBuilder.registerTypeAdapter(Random.class, new RandomTypeAdapter());
        gsonBuilder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter());
        gsonBuilder.registerTypeAdapter(GlowingFireGlowColor.class, new GlowingFireGlowColorTypeAdapter());
        gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter());
        gsonBuilder.registerTypeAdapter(FluidStack.class, new FluidStackTypeAdapter());
        gsonBuilder.registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter());

        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<Item>>() {
        }.getType(), new TagTypeAdapter<>(ItemTagsAccessor.getREGISTRY()));
        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<Block>>() {
        }.getType(), new TagTypeAdapter<>(BlockTagsAccessor.getREGISTRY()));
        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<Fluid>>() {
        }.getType(), new TagTypeAdapter<>(FluidTagsAccessor.getREGISTRY()));
        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<EntityType<?>>>() {
        }.getType(), new TagTypeAdapter<>(EntityTypeTagsAccessor.getREGISTRY()));

        gsonBuilder.registerTypeAdapterFactory(new BlockStateTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new DelayedTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new AcceptTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new ForgeRegistryItemTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new RegisterBasicsAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new NBTTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new MapTypeAdapterFactory());
        gson = gsonBuilder.create();
    }


}
