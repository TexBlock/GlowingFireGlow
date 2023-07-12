package com.til.glowing_fire_glow.common.event;

import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TimeRunEvent implements IWorldComponent {

    @VoluntarilyAssignment
    protected TimeRunCapabilityRegister timeRunCapabilityRegister;

    @SubscribeEvent
    protected void onEntityTick(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        livingUpdateEvent.getEntityLiving().getCapability(timeRunCapabilityRegister.getCapability())
                .ifPresent(ITimeRun::tick);
    }

}
