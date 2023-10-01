package com.til.glowing_fire_glow.common.register.recipe;

import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public abstract class RecipeSerializerRegister<R extends IRecipe<?>> extends RegisterBasics {

    protected IRecipeSerializer<R> recipeSerializer;

    protected Class<R> recipeSerializerClass;

    @VoluntarilyAssignment
    protected AllRecipeRegister allRecipeRegister;

    @Override
    protected void init() {
        super.init();
        recipeSerializerClass = initRecipeSerializerClass();
        recipeSerializer = initRecipeSerializer();
        recipeSerializer.setRegistryName(getName());
        ForgeRegistries.RECIPE_SERIALIZERS.register(recipeSerializer);
    }

    protected Class<R> initRecipeSerializerClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }

    public IRecipeSerializer<R> initRecipeSerializer() {
        return new IRecipeSerializer<R>() {
            @Override
            public R read(ResourceLocation recipeId, JsonObject json) {
                Object obj = allRecipeRegister.get(recipeId);
                if (obj == null) {
                    return null;
                }
                RecipeRegister<?, ?> recipeSerializerRegister = Util.forcedConversion(obj);
                return Util.forcedConversion(recipeSerializerRegister.mackRecipe());
            }

            @Nullable
            @Override
            public R read(ResourceLocation recipeId, PacketBuffer buffer) {
                Object obj = allRecipeRegister.get(recipeId);
                if (obj == null) {
                    return null;
                }
                RecipeRegister<?, ?> recipeSerializerRegister = Util.forcedConversion(obj);
                return Util.forcedConversion(recipeSerializerRegister.mackRecipe());
            }

            @Override
            public void write(PacketBuffer buffer, R recipe) {

            }

            @Override
            public IRecipeSerializer<?> setRegistryName(ResourceLocation name) {
                return this;
            }

            @Nullable
            @Override
            public ResourceLocation getRegistryName() {
                return getName();
            }

            @Override
            public Class<IRecipeSerializer<?>> getRegistryType() {
                return Util.forcedConversion(recipeSerializerClass);
            }
        };
    }

    public IRecipeSerializer<R> getRecipeSerializer() {
        return recipeSerializer;
    }
}
