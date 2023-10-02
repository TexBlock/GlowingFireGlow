package com.til.glowing_fire_glow.common.register.particle_register;


import com.til.glowing_fire_glow.client.ClientTransfer;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RoutePack;
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

    public void add(World world, GlowingFireGlowColor[] color, double density, ResourceLocation resourceLocation, Pos... pos) {
        if (world.isRemote) {
            ClientTransfer.messageConsumer(new ParticleData(this, color, density, resourceLocation, pos));
        } else {
            ServerWorld serverLevel = (ServerWorld) world;
            List<ParticleData> list;
            if (allParticleRegister.MAP.containsKey(serverLevel)) {
                list = allParticleRegister.MAP.get(serverLevel);
            } else {
                list = new ArrayList<>();
                allParticleRegister.MAP.put(serverLevel, list);
            }
            list.add(new ParticleData(this, color, density, resourceLocation, pos));

        }
    }

    public void add(World world, List<List<RoutePack.RouteCell<Double>>> route, GlowingFireGlowColor color, @Nullable ResourceLocation resourceLocation) {
        if (world.isRemote) {
            ClientTransfer.messageConsumer(new ParticleRouteData(route, this, color, resourceLocation));
        } else {
            ServerWorld serverLevel = (ServerWorld) world;
            List<ParticleRouteData> list;
            if (allParticleRegister.ROUTE_DATA.containsKey(serverLevel)) {
                list = allParticleRegister.ROUTE_DATA.get(serverLevel);
            } else {
                list = new ArrayList<>();
                allParticleRegister.ROUTE_DATA.put(serverLevel, list);
            }
            list.add(new ParticleRouteData(route, this, color, resourceLocation));
        }
    }

    public void add(PlayerEntity player, GlowingFireGlowColor[] color, double density, @Nullable ResourceLocation resourceLocation, Pos... pos) {
        if (player.world.isRemote) {
            ClientTransfer.messageConsumer(new ParticleData(this, color, density, resourceLocation, pos));
        } else {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            List<ParticleData> list;
            if (allParticleRegister.PLAYER_MAP.containsKey(serverPlayer)) {
                list = allParticleRegister.PLAYER_MAP.get(serverPlayer);
            } else {
                list = new ArrayList<>();
                allParticleRegister.PLAYER_MAP.put(serverPlayer, list);
            }
            list.add(new ParticleData(this, color, density, resourceLocation, pos));
        }
    }

    public void add(PlayerEntity player, List<List<RoutePack.RouteCell<Double>>> route, GlowingFireGlowColor color, @Nullable ResourceLocation resourceLocation) {
        if (player.world.isRemote) {
            ClientTransfer.messageConsumer(new ParticleRouteData(route, this, color, resourceLocation));

        } else {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            List<ParticleRouteData> list;
            if (allParticleRegister.PLAYER_ROUTE_DATA.containsKey(serverPlayer)) {
                list = allParticleRegister.PLAYER_ROUTE_DATA.get(serverPlayer);
            } else {
                list = new ArrayList<>();
                allParticleRegister.PLAYER_ROUTE_DATA.put(serverPlayer, list);
            }
            list.add(new ParticleRouteData(route, this, color, resourceLocation));
        }
    }


}
