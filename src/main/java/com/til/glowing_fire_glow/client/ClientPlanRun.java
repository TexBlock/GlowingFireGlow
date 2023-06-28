package com.til.glowing_fire_glow.client;

import com.til.glowing_fire_glow.common.main.PlanRunComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientPlanRun extends PlanRunComponent {

    @SubscribeEvent(priority = EventPriority.HIGH)
    protected void onClientTickEvent(TickEvent.ClientTickEvent event) {
        tick();
    }

}
