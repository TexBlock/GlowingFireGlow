package com.til.glowing_fire_glow.common.capability;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.capability.synchronous.ISynchronousManage;
import com.til.glowing_fire_glow.common.capability.synchronous.SynchronousManage;
import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.SynchronousManageCapabilityRegister;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import com.til.glowing_fire_glow.common.register.message.messages.SynchronousMessages;
import com.til.glowing_fire_glow.common.synchronous.SynchronousData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvent implements IWorldComponent {

    public static final ResourceLocation CAPABILITY = new ResourceLocation(GlowingFireGlow.MOD_ID, "capability");

    @VoluntarilyAssignment
    protected TimeRunCapabilityRegister timeRunCapabilityRegister;

    @VoluntarilyAssignment
    protected SynchronousManageCapabilityRegister synchronousManageCapabilityRegister;

    @VoluntarilyAssignment
    protected SynchronousMessages synchronousMessages;


    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_event(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof LivingEntity)) {
            return;
        }
        CapabilityProvider capabilityProvider = new CapabilityProvider();
        ITimeRun iTimeRun = new ITimeRun.TimeRun();
        ISynchronousManage iSynchronousManage = new SynchronousManage();
        if (!event.getObject().world.isRemote) {
            iTimeRun.addTimerCell(new TimerCell(() -> {
                if (!iSynchronousManage.needSynchronous()) {
                    return;
                }
                SynchronousData synchronousData = SynchronousData.of(event.getObject(), iSynchronousManage);
                for (PlayerEntity player : event.getObject().world.getPlayers()) {
                    synchronousMessages.sendToPlayerClient(synchronousData, (ServerPlayerEntity) player);
                }
                iSynchronousManage.synchronousBack();

            }, 1, true, -1000));
        }
        capabilityProvider.addCapability(timeRunCapabilityRegister.getCapability(), iTimeRun);
        capabilityProvider.addCapability(synchronousManageCapabilityRegister.getCapability(), iSynchronousManage);
        event.addCapability(CAPABILITY, capabilityProvider);
    }
}
