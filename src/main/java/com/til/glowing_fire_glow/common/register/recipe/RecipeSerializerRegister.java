package com.til.glowing_fire_glow.common.register.recipe;

import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class RecipeSerializerRegister<R extends Recipe<?>> extends RegisterBasics {

    protected RecipeSerializer<R> recipeSerializer;

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

    public RecipeSerializer<R> initRecipeSerializer() {
        return new RecipeSerializer<R>() {
            @Override
            public R read(Identifier recipeId, JsonObject json) {
                Object obj = allRecipeRegister.get(recipeId);
                if (obj == null) {
                    return null;
                }
                RecipeRegister<?, ?> recipeSerializerRegister = Util.forcedConversion(obj);
                return Util.forcedConversion(recipeSerializerRegister.mackRecipe());
            }

            @Nullable
            @Override
            public R read(Identifier recipeId, PacketByteBuf buffer) {
                Object obj = allRecipeRegister.get(recipeId);
                if (obj == null) {
                    return null;
                }
                RecipeRegister<?, ?> recipeSerializerRegister = Util.forcedConversion(obj);
                return Util.forcedConversion(recipeSerializerRegister.mackRecipe());
            }

            @Override
            public void write(PacketByteBuf buffer, R recipe) {

            }

            @Override
            public RecipeSerializer<?> setRegistryName(Identifier name) {
                return this;
            }

            @Nullable
            @Override
            public Identifier getRegistryName() {
                return getName();
            }

            @Override
            public Class<RecipeSerializer<?>> getRegistryType() {
                return Util.forcedConversion(recipeSerializerClass);
            }
        };
    }

    public RecipeSerializer<R> getRecipeSerializer() {
        return recipeSerializer;
    }
}
