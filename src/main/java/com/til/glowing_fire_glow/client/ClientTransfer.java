package com.til.glowing_fire_glow.client;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.client.register.particle_register.AllParticleClientRegister;
import com.til.glowing_fire_glow.client.register.particle_register.ParticleClientRegister;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.synchronous.SynchronousCapabilityRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.common.synchronous.SynchronousData;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RoutePack;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 客户端中转站
 * 用来处理客户端调用
 * @author til
 */
@OnlyIn(Dist.CLIENT)
@StaticVoluntarilyAssignment
@Mod.EventBusSubscriber(modid = GlowingFireGlow.MOD_ID, value = Dist.CLIENT)
public class ClientTransfer {

    @VoluntarilyAssignment
    protected static AllParticleClientRegister allParticleClientRegister;

    @VoluntarilyAssignment
    protected static ClientPlanRun clientPlanRun;

    public static void messageConsumer(ParticleData data) {
        ParticleClientRegister<?> particleClientRegister = allParticleClientRegister.relationship(data.particleRegister);
        if (particleClientRegister == null) {
            GlowingFireGlow.LOGGER.error("客户端不存在粒子效果的映射{}", data.particleRegister.toString());
            return;
        }
        List<ParticleContext> particleContextList = new ArrayList<>();
        switch (particleClientRegister.getParticleParsingMode()) {
            case PAIR:
                Pos s = null;
                Pos e;
                for (Pos po : data.pos) {
                    if (s == null) {
                        s = po;
                        continue;
                    }
                    e = po;
                    ParticleContext particleContext = new ParticleContext();
                    particleClientRegister.run(particleContext, Minecraft.getInstance().world, s, e, data.color, data.density, data.resourceLocation);
                    particleContextList.add(particleContext);
                    s = null;
                }
                break;

            case SPELL:
                if (data.pos.length >= 2) {
                    s = data.pos[0];
                    int i = 1;
                    do {
                        e = data.pos[i];
                        ParticleContext particleContext = new ParticleContext();
                        particleClientRegister.run(particleContext, Minecraft.getInstance().world, s, e, data.color, data.density, data.resourceLocation);
                        particleContextList.add(particleContext);
                        s = e;
                        i++;
                    } while (i < data.pos.length);
                }
                break;

            case SINGLE:
                for (Pos po : data.pos) {
                    ParticleContext particleContext = new ParticleContext();
                    particleClientRegister.run(particleContext, Minecraft.getInstance().world, po, null, data.color, data.density, data.resourceLocation);
                    particleContextList.add(particleContext);
                }
                break;

            default:
                break;
        }

        int time = 0;
        for (ParticleContext particleContext : particleContextList) {
            clientPlanRun.add(time, () -> {
                for (Particle particle : particleContext.forParticle()) {
                    Minecraft.getInstance().particles.addEffect(particle);
                }
            });
            time += particleContext.getParticleTime();
        }
    }

    public static void messageConsumer(ParticleRouteData data) {
        ParticleClientRegister<?> particleClientRegister = allParticleClientRegister.relationship(data.particleRegister);
        if (particleClientRegister == null) {
            GlowingFireGlow.LOGGER.error("客户端不存在粒子效果的映射{}", data.particleRegister.toString());
            return;
        }
        int time = 0;
        for (List<RoutePack.RouteCell<Double>> routeCells : data.route) {
            int _time = 0;
            ParticleContext particleContext = new ParticleContext();
            for (RoutePack.RouteCell<Double> routeCell : routeCells) {
                particleClientRegister.run(particleContext, Minecraft.getInstance().world, routeCell.start, routeCell.end, data.color, routeCell.data, data.resourceLocation);
            }
            clientPlanRun.add(time, () -> {
                for (Particle particle : particleContext.forParticle()) {
                    Minecraft.getInstance().particles.addEffect(particle);
                }
            });
            time += _time;
        }
    }

    public static void messageConsumer(SynchronousData synchronousData) {
        World world = Minecraft.getInstance().world;
        if (world == null) {
            return;
        }
        Entity entity = world.getEntityByID(synchronousData.getEntityId());
        if (entity == null) {
            return;
        }
        for (Map.Entry<SynchronousCapabilityRegister<?, ?>, CompoundNBT> entry : synchronousData.getData().entrySet()) {
            LazyOptional<?> lazyOptional = entity.getCapability(entry.getKey().getCapabilityRegister().getCapability());
            lazyOptional.ifPresent(c -> {
                entry.getKey().defaultReadNBT(Util.forcedConversion(c), entry.getValue());
            });
        }
    }

}
