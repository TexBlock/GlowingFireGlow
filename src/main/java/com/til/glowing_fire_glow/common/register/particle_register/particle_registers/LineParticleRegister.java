package com.til.glowing_fire_glow.common.register.particle_register.particle_registers;

import com.til.dusk.client.particle.DefaultParticle;
import com.til.dusk.common.config.ConfigField;
import com.til.dusk.common.register.particle_register.AllParticleRegister;
import com.til.dusk.common.register.particle_register.ParticleRegister;
import com.til.dusk.common.register.particle_register.data.ParticleParsingMode;
import com.til.dusk.main.world_component.ReflexManage;
import com.til.dusk.util.DuskColor;
import com.til.dusk.util.Extension;
import com.til.dusk.util.Pos;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.util.Extension;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class LineParticleRegister extends ParticleRegister {


    @Override
    public void init() {
        super.init();
        particleParsingMode = ParticleParsingMode.PAIR;
    }

    @Override
    public Extension.VariableData_2<Float, List<Particle>> run(ClientLevel world, Pos start, Pos end, DuskColor color, double density, @Nullable ResourceLocation resourceLocation) {
        List<Particle> list = new ArrayList<>();
        Pos _start = new Pos(start);
        density = density * interval;
        int dis = (int) (start.distance(end) * density);
        Pos movePos = Pos.movePos(start, end, (start.distance(end) * density));
        for (int i = 0; i < dis; i++) {
            list.add(new DefaultParticle(world, _start, AllParticleRegister.DEFAULT, color, new Pos(), size, (int) life));
            _start = _start.move(movePos);
        }
        return new Extension.VariableData_2<>(life, list);
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
