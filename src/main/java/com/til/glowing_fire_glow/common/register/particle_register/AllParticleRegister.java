package com.til.glowing_fire_glow.common.register.particle_register;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterManage;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.message.messages.ParticleMessage;
import com.til.glowing_fire_glow.common.register.message.messages.ParticleRouteRegisterMessage;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author til
 */
public class AllParticleRegister extends RegisterManage<ParticleRegister> {
    public static final ResourceLocation DEFAULT = new ResourceLocation(GlowingFireGlow.MOD_ID, "textures/particle/modparticle.png");

    protected final Map<ServerWorld, List<ParticleData>> MAP = new HashMap<>();
    protected final Map<ServerWorld, List<ParticleRouteData>> ROUTE_DATA = new HashMap<>();
    protected final Map<ServerPlayerEntity, List<ParticleData>> PLAYER_MAP = new HashMap<>();
    protected final Map<ServerPlayerEntity, List<ParticleRouteData>> PLAYER_ROUTE_DATA = new HashMap<>();

    @VoluntarilyAssignment
    protected ParticleMessage particleMessage;

    @VoluntarilyAssignment
    protected ParticleRouteRegisterMessage particleRouteRegisterMessage;

    @SubscribeEvent
    protected void onEvent(TickEvent.ServerTickEvent event) {
        for (Map.Entry<ServerWorld, List<ParticleData>> entry : MAP.entrySet()) {
            ServerWorld key = entry.getKey();
            for (ParticleData d : entry.getValue()) {
                for (ServerPlayerEntity player : key.getPlayers(p -> true)) {
                    particleMessage.sendToPlayerClient(d, player);
                }
            }
        }
        for (Map.Entry<ServerWorld, List<ParticleRouteData>> entry : ROUTE_DATA.entrySet()) {
            ServerWorld key = entry.getKey();
            for (ParticleRouteData d : entry.getValue()) {
                for (ServerPlayerEntity player : key.getPlayers(p -> true)) {
                    particleRouteRegisterMessage.sendToPlayerClient(d, player);
                }
            }
        }
        for (Map.Entry<ServerPlayerEntity, List<ParticleData>> entry : PLAYER_MAP.entrySet()) {
            ServerPlayerEntity key = entry.getKey();
            for (ParticleData d : entry.getValue()) {
                particleMessage.sendToPlayerClient(d, key);
            }
        }
        for (Map.Entry<ServerPlayerEntity, List<ParticleRouteData>> entry : PLAYER_ROUTE_DATA.entrySet()) {
            ServerPlayerEntity k = entry.getKey();
            for (ParticleRouteData d : entry.getValue()) {
                particleRouteRegisterMessage.sendToPlayerClient(d, k);
            }
        }
        MAP.clear();
        ROUTE_DATA.clear();
        PLAYER_MAP.clear();
    }

}
