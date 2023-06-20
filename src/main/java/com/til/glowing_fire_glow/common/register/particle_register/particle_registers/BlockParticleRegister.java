package com.til.glowing_fire_glow.common.register.particle_register.particle_registers;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
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
public class BlockParticleRegister extends ParticleRegister {

    @VoluntarilyAssignment
    protected LineParticleRegister lineParticleRegister;

    @Override
    public void init() {
        super.init();
        particleParsingMode = ParticleParsingMode.SINGLE;
    }

    @Override
    public Extension.VariableData_2<Float, List<Particle>> run(ClientWorld world, Pos pos, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation) {
        List<Particle> list = new ArrayList<>();
        Pos p1 = pos.move(-0.5, -0.5, -0.5);
        Pos p2 = p1.addX(1);
        Pos p3 = p1.addZ(1);
        Pos p4 = p1.move(1, 0, 1);
        Pos p5 = p1.addY(1);
        Pos p6 = p5.addX(1);
        Pos p7 = p5.addZ(1);
        Pos p8 = p5.move(1, 0, 1);
        Extension.VariableData_2[] l = new Extension.VariableData_2[]{
                new Extension.VariableData_2<>(p1, p2),
                new Extension.VariableData_2<>(p1, p3),
                new Extension.VariableData_2<>(p4, p2),
                new Extension.VariableData_2<>(p4, p3),
                new Extension.VariableData_2<>(p5, p6),
                new Extension.VariableData_2<>(p5, p7),
                new Extension.VariableData_2<>(p8, p6),
                new Extension.VariableData_2<>(p8, p7),
                new Extension.VariableData_2<>(p1, p5),
                new Extension.VariableData_2<>(p2, p6),
                new Extension.VariableData_2<>(p3, p7),
                new Extension.VariableData_2<>(p4, p8),
        };
        for (Extension.VariableData_2 posPosData_2 : l) {
            Extension.VariableData_2<Float, List<Particle>> data_2 = lineParticleRegister.run(world, (Pos) posPosData_2.k, (Pos) posPosData_2.v, color, density, resourceLocation);
            if (data_2 != null) {
                list.addAll(data_2.v);
            }
        }
        return new Extension.VariableData_2<>(0f, list);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
    }
}
