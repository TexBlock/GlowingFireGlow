package com.til.glowing_fire_glow.common;

import com.til.glowing_fire_glow.common.main.PlanRunComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonPlanRun extends PlanRunComponent {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        tick();
    }


}
