package com.til.glowing_fire_glow.common.register.particle_register.data;

import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;


public class ParticleData {
    public ResourceLocation type;
    public GlowingFireGlowColor color;
    public double density;
    @Nullable
    public ResourceLocation resourceLocation;
    public Pos[] pos;

    public ParticleData(ResourceLocation type, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation, Pos[] pos) {
        this.type = type;
        this.color = color;
        this.density = density;
        this.resourceLocation = resourceLocation;
        this.pos = pos;
    }
}