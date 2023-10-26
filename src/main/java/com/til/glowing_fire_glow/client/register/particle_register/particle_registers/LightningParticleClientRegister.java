package com.til.glowing_fire_glow.client.register.particle_register.particle_registers;

import com.til.glowing_fire_glow.client.particle.LightningParticle;
import com.til.glowing_fire_glow.client.register.particle_register.ParticleClientRegister;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.instance.LightningSeepOverallConfigRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.LightningParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class LightningParticleClientRegister extends ParticleClientRegister<LightningParticleRegister> {

    @VoluntarilyAssignment
    protected LightningSeepOverallConfigRegister lightningSeepOverallConfigRegister;

    @Override
    protected void init() {
        super.init();
        particleParsingMode = ParticleParsingMode.SPELL;
    }

    @Override
    public void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor[] color, double density, @Nullable ResourceLocation resourceLocation) {
        if (end == null) {
            return;
        }
        GlowingFireGlowColor inner;
        GlowingFireGlowColor outer;
        if (color.length >= 2) {
            inner = color[0];
            outer = color[1];
        } else if (color.length == 1) {
            inner = color[0];
            outer = color[0];
        } else {
            inner = GlowingFireGlowColor.DEFAULT;
            outer = GlowingFireGlowColor.DEFAULT;
        }
        particleContext.addParticle(new LightningParticle(world, start, end, lightningSeepOverallConfigRegister.getSeep()).setColorInner(inner.getRGB()).setColorOuter(outer.getRGB()));
    }

}
