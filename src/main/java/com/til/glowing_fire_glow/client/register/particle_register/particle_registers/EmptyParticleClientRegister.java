package com.til.glowing_fire_glow.client.register.particle_register.particle_registers;

import com.til.glowing_fire_glow.client.register.particle_register.ParticleClientRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.EmptyParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class EmptyParticleClientRegister extends ParticleClientRegister<EmptyParticleRegister> {

    @Override
    public void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor[] color, double density, @Nullable Identifier Identifier) {

    }

}
