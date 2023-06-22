package com.til.glowing_fire_glow.client;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.particle_register.AllParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.Pos;
import com.til.glowing_fire_glow.util.RoutePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/***
 * 客户端中转站
 * 用来处理客户端调用
 * @author til
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GlowingFireGlow.MOD_ID, value = Dist.CLIENT)
public class ClientTransfer {

    public static void messageConsumer(ParticleData data, Supplier<NetworkEvent.Context> supplier) {
        ResourceLocation name = data.type;
        ParticleRegister clientParticleRegister = GlowingFireGlow.getInstance().getReflexManage().getRegisterManage(AllParticleRegister.class).get(name);
        if (clientParticleRegister == null) {
            GlowingFireGlow.LOGGER.error("在客户端不存在粒子效果{}", data.type);
            return;
        }
        List<Extension.VariableData_2<Float, List<Particle>>> list = new ArrayList<>();
        switch (clientParticleRegister.getParticleParsingMode()) {
            case PAIR:
                Pos s = null;
                Pos e = null;
                for (Pos po : data.pos) {
                    if (s == null) {
                        s = po;
                        continue;
                    }
                    if (e == null) {
                        e = po;
                    }
                    Extension.VariableData_2<Float, List<Particle>> data_2 = clientParticleRegister.run(Minecraft.getInstance().world, s, e, data.color, data.density, data.resourceLocation);
                    if (data_2 != null) {
                        list.add(data_2);
                    }
                    s = null;
                    e = null;
                }
                break;

            case SPELL:
                if (data.pos.length >= 2) {
                    s = data.pos[0];
                    int i = 1;
                    do {
                        e = data.pos[i];
                        Extension.VariableData_2<Float, List<Particle>> data_2 = clientParticleRegister.run(Minecraft.getInstance().world, s, e, data.color, data.density, data.resourceLocation);
                        if (data_2 != null) {
                            list.add(data_2);
                        }
                        s = e;
                        i++;
                    } while (i < data.pos.length);
                }
                break;

            case SINGLE:
                for (Pos po : data.pos) {
                    Extension.VariableData_2<Float, List<Particle>> data_2 = clientParticleRegister.run(Minecraft.getInstance().world, po, data.color, data.density, data.resourceLocation);
                    if (data_2 != null) {
                        list.add(data_2);
                    }
                }
                break;

            default:
                break;
        }

        float time = 0;
        for (Extension.VariableData_2<Float, List<Particle>> data_2 : list) {
            addRun(time, () -> {
                for (Particle particle : data_2.v) {
                    Minecraft.getInstance().particles.addEffect(particle);
                }
            });
            time += data_2.k;
        }
    }

    public static void messageConsumer(ParticleRouteData data, Supplier<NetworkEvent.Context> supplier) {
        ResourceLocation name = data.type;
        ParticleRegister clientParticleRegister = GlowingFireGlow.getInstance().getReflexManage().getRegisterManage(AllParticleRegister.class).get(name);
        if (clientParticleRegister == null) {
            GlowingFireGlow.LOGGER.error("在客户端不存在粒子效果{}", data.type);
            return;
        }
        float time = 0;
        for (List<RoutePack.RouteCell<Double>> routeCells : data.route) {
            float _time = 0;
            for (RoutePack.RouteCell<Double> routeCell : routeCells) {
                Extension.VariableData_2<Float, List<Particle>> data_2 = clientParticleRegister.run(Minecraft.getInstance().world, routeCell.start, routeCell.end, data.color, routeCell.data, data.resourceLocation);
                if (data_2 != null) {
                    _time = Math.max(_time, data_2.k);
                    if (data_2.v != null) {
                        addRun(time, () -> {
                            for (Particle particle : data_2.v) {
                                Minecraft.getInstance().particles.addEffect(particle);
                            }
                        });
                    }
                }
            }
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
