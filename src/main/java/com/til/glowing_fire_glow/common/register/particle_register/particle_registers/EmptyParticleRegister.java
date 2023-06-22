package com.til.glowing_fire_glow.common.register.particle_register.particle_registers;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

@VoluntarilyRegister
public class EmptyParticleRegister extends ParticleRegister {

    @Override
    public void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation) {

    }
}
