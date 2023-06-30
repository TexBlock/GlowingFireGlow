package com.til.glowing_fire_glow.common.capability;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvent implements IWorldComponent {

    public static final ResourceLocation CAPABILITY = new ResourceLocation(GlowingFireGlow.MOD_ID, "capability");

    @VoluntarilyAssignment
    protected TimeRunCapabilityRegister timeRunCapabilityRegister;


    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_itemStack(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof LivingEntity)) {
            return;
        }
        CapabilityProvider capabilityProvider = new CapabilityProvider();
        capabilityProvider.addCapability(timeRunCapabilityRegister.getCapability(), new ITimeRun.TimeRun());
        event.addCapability(CAPABILITY, capabilityProvider);
    }

}
