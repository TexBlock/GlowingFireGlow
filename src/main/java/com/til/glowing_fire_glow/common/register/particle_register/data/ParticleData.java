package com.til.glowing_fire_glow.common.register.particle_register.data;

import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;


public class ParticleData {
    public ParticleRegister particleRegister;
    public GlowingFireGlowColor color[];
    public double density;
    @Nullable
    public ResourceLocation resourceLocation;
    public Pos[] pos;

    public ParticleData(ParticleRegister particleRegister, GlowingFireGlowColor color[], double density, @Nullable ResourceLocation resourceLocation, Pos[] pos) {
        this.particleRegister = particleRegister;
        this.color = color;
        this.density = density;
        this.resourceLocation = resourceLocation;
        this.pos = pos;
    }
}