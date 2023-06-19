package com.til.glowing_fire_glow.common.register.particle_register;

import com.til.dusk.common.event.RegisterLangEvent;
import com.til.dusk.common.register.RegisterBasics;
import com.til.dusk.common.register.particle_register.data.ParticleData;
import com.til.dusk.common.register.particle_register.data.ParticleParsingMode;
import com.til.dusk.common.register.particle_register.data.ParticleRouteData;
import com.til.dusk.util.DuskColor;
import com.til.dusk.util.Extension;
import com.til.dusk.util.Pos;
import com.til.dusk.util.RoutePack;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author til
 */

public abstract class ParticleRegister extends RegisterBasics {


    protected ParticleParsingMode particleParsingMode = ParticleParsingMode.SPELL;
    protected AllParticleRegister allParticleRegister;

    @Override
    public void init() {
        super.init();
        allParticleRegister = GlowingFireGlow.getInstance().getReflexManage().getRegisterManage(AllParticleRegister.class);
    }

    public void add(Level world, DuskColor color, double density, ResourceLocation resourceLocation, Pos... pos) {
        if (world instanceof ServerLevel serverLevel) {
            List<ParticleData> list;
            if (allParticleRegister.MAP.containsKey(serverLevel)) {
                list = allParticleRegister.MAP.get(serverLevel);
            } else {
                list = new ArrayList<>();
                allParticleRegister.MAP.put(serverLevel, list);
            }
            list.add(new ParticleData(name, color, density, resourceLocation, pos));
            return;
        }
        throw new RuntimeException("在服务端了粒子创建中出现了非服务端的世界");
    }

    public void add(Level world, List<List<RoutePack.RouteCell<Double>>> route, DuskColor color, @Nullable ResourceLocation resourceLocation) {
        if (world instanceof ServerLevel serverLevel) {
            List<ParticleRouteData> list;
            if (allParticleRegister.ROUTE_DATA.containsKey(serverLevel)) {
                list = allParticleRegister.ROUTE_DATA.get(serverLevel);
            } else {
                list = new ArrayList<>();
                allParticleRegister.ROUTE_DATA.put(serverLevel, list);
            }
            list.add(new ParticleRouteData(route, name, color, resourceLocation));
            return;
        }
        throw new RuntimeException("在服务端了粒子创建中出现了非服务端的世界");
    }

    public void add(Player player, DuskColor color, double density, @Nullable ResourceLocation resourceLocation, Pos... pos) {
        if (player instanceof ServerPlayer serverPlayer) {
            List<ParticleData> list;
            if (allParticleRegister.PLAYER_MAP.containsKey(serverPlayer)) {
                list = allParticleRegister.PLAYER_MAP.get(serverPlayer);
            } else {
                list = new ArrayList<>();
                allParticleRegister.PLAYER_MAP.put(serverPlayer, list);
            }
            list.add(new ParticleData(name, color, density, resourceLocation, pos));
            return;
        }
        throw new RuntimeException("在服务端了粒子创建中出现了非服务端的玩家");
    }

    public void add(Player player, List<List<RoutePack.RouteCell<Double>>> route, DuskColor color, @Nullable ResourceLocation resourceLocation) {
        if (player instanceof ServerPlayer serverPlayer) {
            List<ParticleRouteData> list;
            if (allParticleRegister.PLAYER_ROUTE_DATA.containsKey(serverPlayer)) {
                list = allParticleRegister.PLAYER_ROUTE_DATA.get(serverPlayer);
            } else {
                list = new ArrayList<>();
                allParticleRegister.PLAYER_ROUTE_DATA.put(serverPlayer, list);
            }
            list.add(new ParticleRouteData(route, name, color, resourceLocation));
            return;
        }
        throw new RuntimeException("在服务端了粒子创建中出现了非服务端的玩家");
    }

    /***
     * 实现粒子效果
     * @param world 当前的世界
     * @param start 开始点
     * @param end 结束点
     * @param color 颜色
     * @param density 密度
     * @return 返回粒子是生命用于拼接
     */
    @Nullable
    public Extension.VariableData_2<Float, List<Particle>> run(ClientLevel world, Pos start, Pos end, GlowingFireGlowColor color, double density, @javax.annotation.Nullable ResourceLocation resourceLocation) {
        return null;
    }

    @Nullable
    public Extension.VariableData_2<Float, List<Particle>> run(ClientLevel world, Pos pos, GlowingFireGlowColor color, double density, @javax.annotation.Nullable ResourceLocation resourceLocation) {
        return null;
    }


    public ParticleParsingMode getParticleParsingMode() {
        return particleParsingMode;
    }

}
