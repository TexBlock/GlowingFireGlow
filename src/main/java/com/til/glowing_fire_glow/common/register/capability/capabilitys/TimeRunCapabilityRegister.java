package com.til.glowing_fire_glow.common.register.capability.capabilitys;


import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author til
 */
@VoluntarilyRegister
public class TimeRunCapabilityRegister extends OriginalCapabilityRegister<ITimeRun> {


    @CapabilityInject(ITimeRun.class)
    public static Capability<ITimeRun> timeRunCapability;

    @Override
    protected Capability<ITimeRun> initCapability() {
        return timeRunCapability;
    }
}
