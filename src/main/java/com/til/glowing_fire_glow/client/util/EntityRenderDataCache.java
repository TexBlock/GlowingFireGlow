package com.til.glowing_fire_glow.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.client.register.capability.render.AllCapabilityRenderRegister;
import com.til.glowing_fire_glow.client.register.capability.render.CapabilityRenderRegister;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class EntityRenderDataCache implements IWorldComponent {
    @VoluntarilyAssignment
    protected AllCapabilityRenderRegister allCapabilityRenderRegister;


    protected Map<Entity, Map<CapabilityRenderRegister<?, ?>, Object>> cacheData = new HashMap<>();

    public void render(Entity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        Map<CapabilityRenderRegister<?, ?>, Object> map;
        if (cacheData.containsKey(entityIn)) {
            map = cacheData.get(entityIn);
        } else {
            map = new HashMap<>();
            for (CapabilityRenderRegister<?, ?> capabilityRenderRegister : allCapabilityRenderRegister.forAll()) {
                LazyOptional<?> lazyOptional = entityIn.getCapability(capabilityRenderRegister.getCapabilityRegister().getCapability());
                lazyOptional.ifPresent(c -> map.put(capabilityRenderRegister, c));
            }
            cacheData.put(entityIn, map);
        }
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<CapabilityRenderRegister<?, ?>, Object> entry : map.entrySet()) {
            entry.getKey().render(entityIn, Util.forcedConversion(entry.getValue()), partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    @SubscribeEvent
    protected void onLivingDeath(EntityLeaveWorldEvent event) {
        cacheData.remove(event.getEntity());
    }

    @SubscribeEvent
    protected void onWorldUnload(WorldEvent.Unload event) {
        cacheData.clear();
    }

    @SubscribeEvent
    protected void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        cacheData.clear();
    }
}
