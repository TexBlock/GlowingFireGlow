package com.til.glowing_fire_glow.common.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.recipe.AllRecipeRegister;
import com.til.glowing_fire_glow.common.register.recipe.RecipeRegister;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = RecipeManager.class, priority = 0)
public class RecipeManagerMixin {
    @Shadow
    private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes;

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At("RETURN")
    )
    private void apply(Map<Identifier, JsonElement> objectIn, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        GlowingFireGlow.LOGGER.log(Level.INFO, "开始Mixin配方");

        Map<RecipeType<?>, Map<Identifier, Recipe<?>>> addRecipes = new HashMap<>();

        for (RecipeRegister<?, ?> recipeRegisterBasics : GlowingFireGlow.getInstance().getWorldComponent(AllRecipeRegister.class).forAll()) {
            Recipe<?> recipe = recipeRegisterBasics.mackRecipe();
            RecipeType<?> recipeType = recipe.getType();
            GlowingFireGlow.LOGGER.log(Level.INFO, "Mixin配方:" + recipeRegisterBasics.getName().toString());
            Map<Identifier, Recipe<?>> map;
            if (addRecipes.containsKey(recipeType)) {
                map = addRecipes.get(recipeType);
            } else {
                map = new HashMap<>();
                addRecipes.put(recipeType, map);
            }
            map.put(recipe.getId(), recipe);
        }

        Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipesCopy = new HashMap<>(recipes);
        for (Map.Entry<RecipeType<?>, Map<Identifier, Recipe<?>>> iRecipeTypeMapEntry : addRecipes.entrySet()) {
            Map<Identifier, Recipe<?>> map;
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
