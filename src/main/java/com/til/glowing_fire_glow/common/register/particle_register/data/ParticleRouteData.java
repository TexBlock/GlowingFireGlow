package com.til.glowing_fire_glow.common.register.particle_register.data;


import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.RoutePack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ParticleRouteData {
    public List<List<RoutePack.RouteCell<Double>>> route;
    public ResourceLocation type;
    public GlowingFireGlowColor color;
    @Nullable
    public ResourceLocation resourceLocation;

    public ParticleRouteData(List<List<RoutePack.RouteCell<Double>>> route, ResourceLocation type, GlowingFireGlowColor color, @Nullable ResourceLocation resourceLocation) {
        this.route = route;
        this.type = type;
        this.color = color;
        this.resourceLocation = resourceLocation;
    }

}