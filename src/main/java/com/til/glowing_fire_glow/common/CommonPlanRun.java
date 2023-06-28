package com.til.glowing_fire_glow.common;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.PlanRunComponent;
import com.til.glowing_fire_glow.util.Extension;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

public class CommonPlanRun extends PlanRunComponent {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        tick();
    }


}
