package com.til.glowing_fire_glow.common.register.recipe;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class RecipeSerializerRegister<R extends IRecipe<?>> extends RegisterBasics {

    protected IRecipeSerializer<R> recipeSerializer;

    @Override
    protected void init() {
        super.init();
        recipeSerializer = initRecipeSerializer();
        recipeSerializer.setRegistryName(getName());
        ForgeRegistries.RECIPE_SERIALIZERS.register(recipeSerializer);
    }

    public abstract IRecipeSerializer<R> initRecipeSerializer();

    public IRecipeSerializer<R> getRecipeSerializer() {
        return recipeSerializer;
    }
}
