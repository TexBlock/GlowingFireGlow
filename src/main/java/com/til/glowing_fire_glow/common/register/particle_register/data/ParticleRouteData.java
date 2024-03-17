package com.til.glowing_fire_glow.common.register.particle_register.data;


import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.RoutePack;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.List;

public class ParticleRouteData {
    public List<List<RoutePack.RouteCell<Double>>> route;
    public ParticleRegister particleRegister;
    public GlowingFireGlowColor[] color;
    @Nullable
    public Identifier Identifier;

    public ParticleRouteData(List<List<RoutePack.RouteCell<Double>>> route, ParticleRegister particleRegister, GlowingFireGlowColor[] color, @Nullable Identifier Identifier) {
        this.route = route;
        this.particleRegister = particleRegister;
        this.color = color;
        this.Identifier = Identifier;
    }

}