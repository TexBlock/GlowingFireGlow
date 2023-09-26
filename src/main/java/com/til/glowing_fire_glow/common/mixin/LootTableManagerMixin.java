package com.til.glowing_fire_glow.common.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.loot_table.AllLootTableRegister;
import com.til.glowing_fire_glow.common.register.loot_table.LootTableRegister;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LootTableManager.class)
public class LootTableManagerMixin {
    @Shadow
    private Map<ResourceLocation, LootTable> registeredLootTables;

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/profiler/IProfiler;)V",
            at = @At("RETURN")
    )
    private void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn, CallbackInfo ci) {
        ImmutableMap.Builder<ResourceLocation, LootTable> builder = ImmutableMap.builder();
        builder.putAll(registeredLootTables);
        for (LootTableRegister lootTableRegister : GlowingFireGlow.getInstance().getWorldComponent(AllLootTableRegister.class).forAll()) {
            builder.put(lootTableRegister.getName(), lootTableRegister.getLootTable());
        }
        registeredLootTables = builder.build();
    }
}
