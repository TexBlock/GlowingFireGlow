package com.til.glowing_fire_glow.common.register.particle_register.particle_registers;

import com.til.glowing_fire_glow.client.particle.DefaultParticle;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.particle_register.AllParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author til
 */
@VoluntarilyRegister
public class LineParticleRegister extends ParticleRegister {


    @Override
    public void init() {
        super.init();
        particleParsingMode = ParticleParsingMode.PAIR;
    }

    @Override
    public void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation) {
        if (end == null) {
            return;
        }
        Pos _start = new Pos(start);
        density = density * interval;
        int dis = (int) (start.distance(end) * density);
        Pos movePos = Pos.movePos(start, end, (start.distance(end) * density));
        for (int i = 0; i < dis; i++) {
            particleContext.addParticle(new DefaultParticle(world)
                    .setPos(_start.x, _start.y, _start.z)
                    .setColor(color)
                    .setSize(size)
                    .setLifeTime((int) life)
                    .setSizeChange()
                    .setTextureName(AllParticleRegister.DEFAULT));
            _start = _start.move(movePos);
        }
        particleContext.setParticleTime((int) life);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        life = 40;
        interval = 1f;
        size = 0.1f;
    }

    @ConfigField
    public float life;

    @ConfigField
    public float interval;

    @ConfigField
    public float size;
}
