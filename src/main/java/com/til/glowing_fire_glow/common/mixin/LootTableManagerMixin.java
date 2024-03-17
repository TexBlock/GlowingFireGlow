package com.til.glowing_fire_glow.common.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.loot_table.AllLootTableRegister;
import com.til.glowing_fire_glow.common.register.loot_table.LootTableRegister;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LootManager.class)
public class LootTableManagerMixin {
    @Shadow
    private Map<Identifier, LootTable> tables;

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
            at = @At("RETURN")
    )
    private void apply(Map<Identifier, JsonElement> objectIn, ResourceManager resourceManagerIn, Profiler profilerIn, CallbackInfo ci) {
        ImmutableMap.Builder<Identifier, LootTable> builder = ImmutableMap.builder();
        builder.putAll(tables);
        for (LootTableRegister lootTableRegister : GlowingFireGlow.getInstance().getWorldComponent(AllLootTableRegister.class).forAll()) {
            builder.put(lootTableRegister.getName(), lootTableRegister.getLootTable());
        }
        tables = builder.build();
    }
}
