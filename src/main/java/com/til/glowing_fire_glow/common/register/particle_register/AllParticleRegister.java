package com.til.glowing_fire_glow.common.register.particle_register;

import com.til.dusk.common.register.RegisterManage;
import com.til.dusk.common.register.message.messages.ParticleMessage;
import com.til.dusk.common.register.message.messages.ParticleRouteRegisterMessage;
import com.til.dusk.common.register.particle_register.data.ParticleData;
import com.til.dusk.common.register.particle_register.data.ParticleRouteData;
import com.til.dusk.main.Dusk;
import com.til.glowing_fire_glow.common.register.RegisterManage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author til
 */
public class AllParticleRegister extends RegisterManage<ParticleRegister> {
    public static final ResourceLocation DEFAULT = new ResourceLocation(Dusk.MOD_ID, "textures/particle/modparticle.png");

    protected final Map<ServerLevel, List<ParticleData>> MAP = new HashMap<>();
    protected final Map<ServerLevel, List<ParticleRouteData>> ROUTE_DATA = new HashMap<>();
    protected final Map<ServerPlayer, List<ParticleData>> PLAYER_MAP = new HashMap<>();
    protected final Map<ServerPlayer, List<ParticleRouteData>> PLAYER_ROUTE_DATA = new HashMap<>();


    @SubscribeEvent
    protected void onEvent(TickEvent.ServerTickEvent event) {
        for (Map.Entry<ServerLevel, List<ParticleData>> entry : MAP.entrySet()) {
            ServerLevel key = entry.getKey();
            for (ParticleData d : entry.getValue()) {
                for (ServerPlayer player : key.getPlayers(p -> true)) {
                    Dusk.instance.getReflexManage().getRegisterBasicsOfClass(ParticleMessage.class).sendToPlayerClient(d, player);
                }
            }
        }
        for (Map.Entry<ServerLevel, List<ParticleRouteData>> entry : ROUTE_DATA.entrySet()) {
            ServerLevel key = entry.getKey();
            for (ParticleRouteData d : entry.getValue()) {
                for (ServerPlayer player : key.getPlayers(p -> true)) {
                    Dusk.instance.getReflexManage().getRegisterBasicsOfClass(ParticleRouteRegisterMessage.class).sendToPlayerClient(d, player);
                }
            }
        }
        for (Map.Entry<ServerPlayer, List<ParticleData>> entry : PLAYER_MAP.entrySet()) {
            ServerPlayer key = entry.getKey();
            for (ParticleData d : entry.getValue()) {
                Dusk.instance.getReflexManage().getRegisterBasicsOfClass(ParticleMessage.class).sendToPlayerClient(d, key);
            }
        }
        for (Map.Entry<ServerPlayer, List<ParticleRouteData>> entry : PLAYER_ROUTE_DATA.entrySet()) {
            ServerPlayer k = entry.getKey();
            for (ParticleRouteData d : entry.getValue()) {
                Dusk.instance.getReflexManage().getRegisterBasicsOfClass(ParticleRouteRegisterMessage.class).sendToPlayerClient(d, k);
            }
        }
        MAP.clear();
        ROUTE_DATA.clear();
        PLAYER_MAP.clear();
    }

}
