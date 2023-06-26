package com.til.glowing_fire_glow.client;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.client.register.particle_register.AllParticleClientRegister;
import com.til.glowing_fire_glow.client.register.particle_register.ParticleClientRegister;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.Pos;
import com.til.glowing_fire_glow.util.RoutePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

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

        float time = 0;
        for (ParticleContext particleContext : particleContextList) {
            addRun(time, () -> {
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
        float time = 0;
        for (List<RoutePack.RouteCell<Double>> routeCells : data.route) {
            float _time = 0;
            ParticleContext particleContext = new ParticleContext();
            for (RoutePack.RouteCell<Double> routeCell : routeCells) {
                particleClientRegister.run(particleContext, Minecraft.getInstance().world, routeCell.start, routeCell.end, data.color, routeCell.data, data.resourceLocation);
            }
            addRun(time, () -> {
                for (Particle particle : particleContext.forParticle()) {
                    Minecraft.getInstance().particles.addEffect(particle);
                }
            });
            time += _time;
        }
    }

    public static final List<Extension.VariableData_2<Float, Runnable>> RUN_LIST = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void tick(TickEvent.ClientTickEvent event) {
        if (!event.phase.equals(TickEvent.Phase.END)) {
            return;
        }
        List<Extension.VariableData_2<Float, Runnable>> rList = null;
        for (Extension.VariableData_2<Float, Runnable> longRunnableData_2 : RUN_LIST) {
            longRunnableData_2.k--;
            if (longRunnableData_2.k <= 0) {
                longRunnableData_2.v.run();
                if (rList == null) {
                    rList = new ArrayList<>();
                }
                rList.add(longRunnableData_2);
            }
        }
        if (rList != null) {
            for (Extension.VariableData_2<Float, Runnable> longRunnableData_2 : rList) {
                RUN_LIST.remove(longRunnableData_2);
            }
            rList.clear();
        }
    }

    public static void addRun(float _time, Runnable runnable) {
        RUN_LIST.add(new Extension.VariableData_2<>(_time, runnable));
    }

}
