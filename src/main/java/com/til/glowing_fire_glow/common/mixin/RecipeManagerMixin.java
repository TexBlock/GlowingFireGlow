package com.til.glowing_fire_glow.common.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.recipe.AllRecipeRegister;
import com.til.glowing_fire_glow.common.register.recipe.RecipeRegister;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    private Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/profiler/IProfiler;)V",
            at = @At("RETURN")
    )
    private void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn, CallbackInfo ci) {
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> addRecipes = new HashMap<>();

        for (RecipeRegister<?, ?> recipeRegisterBasics : GlowingFireGlow.getInstance().getWorldComponent(AllRecipeRegister.class).forAll()) {
            IRecipe<?> recipe = recipeRegisterBasics.mackRecipe();
            IRecipeType<?> recipeType = recipe.getType();
            Map<ResourceLocation, IRecipe<?>> map;
            if (addRecipes.containsKey(recipeType)) {
                map = addRecipes.get(recipeType);
            } else {
                map = new HashMap<>();
                addRecipes.put(recipeType, map);
            }
            map.put(recipe.getId(), recipe);
        }

        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipesCopy = new HashMap<>(recipes);
        for (Map.Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> iRecipeTypeMapEntry : addRecipes.entrySet()) {
            Map<ResourceLocation, IRecipe<?>> map;
            if (recipesCopy.containsKey(iRecipeTypeMapEntry.getKey())) {
                map = recipesCopy.get(iRecipeTypeMapEntry.getKey());
                map = new HashMap<>(map);
                recipesCopy.put(iRecipeTypeMapEntry.getKey(), map);
            } else {
                map = new HashMap<>();
                recipesCopy.put(iRecipeTypeMapEntry.getKey(), map);
            }
            map.putAll(iRecipeTypeMapEntry.getValue());
        }
        this.recipes = recipesCopy.entrySet().stream()
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream()
                                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue))));
    }
}
