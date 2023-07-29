package com.til.glowing_fire_glow.common.register.recipe;

import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.item.crafting.IRecipe;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class RecipeRegister<R extends IRecipe<?>, RS extends RecipeSerializerRegister<R>> extends RegisterBasics {

    @VoluntarilyAssignment
    protected ReflexManage reflexManage;


    protected Class<R> recipeClass;
    protected Class<RS> recipeSerializerClass;

    protected RS recipeSerializer;

    @Override
    protected void init() {
        super.init();
        recipeClass = initRecipeClass();
        recipeSerializerClass = initRecipeSerializerClass();
        recipeSerializer = reflexManage.getVoluntarilyRegisterOfClass(recipeSerializerClass);
    }

    protected Class<R> initRecipeClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }

    protected Class<RS> initRecipeSerializerClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[1];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }

    public Class<R> getRecipeClass() {
        return recipeClass;
    }

    public Class<RS> getRecipeSerializerClass() {
        return recipeSerializerClass;
    }

    public RS getRecipeSerializer() {
        return recipeSerializer;
    }

    public abstract R mackRecipe();

}
