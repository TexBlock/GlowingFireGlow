package com.til.glowing_fire_glow.common.register.particle_register;


import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import com.til.glowing_fire_glow.util.RoutePack;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author til
 */

public abstract class ParticleRegister extends RegisterBasics {


    protected ParticleParsingMode particleParsingMode = ParticleParsingMode.SPELL;

    @VoluntarilyAssignment
    protected AllParticleRegister allParticleRegister;

    @Override
    public void init() {
        super.init();
    }

    public void add(World world, GlowingFireGlowColor color, double density, ResourceLocation resourceLocation, Pos... pos) {
        if (world instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) world;
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

    public void add(World world, List<List<RoutePack.RouteCell<Double>>> route, GlowingFireGlowColor color, @Nullable ResourceLocation resourceLocation) {
        if (world instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) world;
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

    public void add(PlayerEntity player, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation, Pos... pos) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
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

    public void add(PlayerEntity player, List<List<RoutePack.RouteCell<Double>>> route, GlowingFireGlowColor color, @Nullable ResourceLocation resourceLocation) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
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
    public Extension.VariableData_2<Float, List<Particle>> run(ClientWorld world, Pos start, Pos end, GlowingFireGlowColor color, double density, @javax.annotation.Nullable ResourceLocation resourceLocation) {
        return null;
    }

    @Nullable
    public Extension.VariableData_2<Float, List<Particle>> run(ClientWorld world, Pos pos, GlowingFireGlowColor color, double density, @javax.annotation.Nullable ResourceLocation resourceLocation) {
        return null;
    }


    public ParticleParsingMode getParticleParsingMode() {
        return particleParsingMode;
    }

}
