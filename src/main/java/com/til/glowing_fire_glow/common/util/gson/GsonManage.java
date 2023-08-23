package com.til.glowing_fire_glow.common.util.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.mixin.BlockTagsMixin;
import com.til.glowing_fire_glow.common.mixin.EntityTypeTagsMixin;
import com.til.glowing_fire_glow.common.mixin.FluidTagsMixin;
import com.til.glowing_fire_glow.common.mixin.ItemTagsMixin;
import com.til.glowing_fire_glow.common.register.loot_table.LootTableRegister;
import com.til.glowing_fire_glow.common.util.gson.type_adapter.*;
import com.til.glowing_fire_glow.common.util.gson.type_adapter.factory.*;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootTable;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * @author til
 */
public class GsonManage implements IWorldComponent {

    public static final String TYPE = "$type";
    public static final String GENERIC = "$generic";
    public static final String CONFIG = "$config";

    protected Gson gson;

    @Override
    public void initNew() {
        IWorldComponent.super.initNew();
        Field gsonBuilder_excluder;
        Field gson_factories;
        try {
            gsonBuilder_excluder = GsonBuilder.class.getDeclaredField("excluder");
            gsonBuilder_excluder.setAccessible(true);
            gson_factories = Gson.class.getDeclaredField("factories");
            gson_factories.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(Random.class, new RandomTypeAdapter());
        gsonBuilder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter());
        gsonBuilder.registerTypeAdapter(GlowingFireGlowColor.class, new GlowingFireGlowColorTypeAdapter());
        gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter());
        gsonBuilder.registerTypeAdapter(FluidStack.class, new FluidStackTypeAdapter());
        gsonBuilder.registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter());
        gsonBuilder.registerTypeAdapter(Ingredient.class, new IngredientTypeAdapter());
        gsonBuilder.registerTypeAdapter(LootTable.class, new LootTable.Serializer());

        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<Item>>() {
        }.getType(), new TagTypeAdapter<>(ItemTagsMixin.getREGISTRY()));
        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<Block>>() {
        }.getType(), new TagTypeAdapter<>(BlockTagsMixin.getREGISTRY()));
        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<Fluid>>() {
        }.getType(), new TagTypeAdapter<>(FluidTagsMixin.getREGISTRY()));
        gsonBuilder.registerTypeAdapter(new TypeToken<Tag<EntityType<?>>>() {
        }.getType(), new TagTypeAdapter<>(EntityTypeTagsMixin.getREGISTRY()));

        gsonBuilder.registerTypeAdapterFactory(new BlockStateTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new DelayedTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new AcceptTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new ForgeRegistryItemTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new RegisterBasicsAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new NBTTypeAdapterFactory());
        gsonBuilder.registerTypeAdapterFactory(new MapTypeAdapterFactory());

        gson = gsonBuilder.create();
    }

    @Override
    public int getExecutionOrderList() {
        return 2000;
    }

    public Gson getGson() {
        return gson;
    }
}
