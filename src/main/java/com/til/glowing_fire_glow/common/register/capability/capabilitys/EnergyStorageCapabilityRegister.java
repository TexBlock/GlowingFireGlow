package com.til.glowing_fire_glow.common.register.capability.capabilitys;


import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class EnergyStorageCapabilityRegister extends CapabilityRegister<IEnergyStorage> {

    @Override
    protected Capability<IEnergyStorage> initCapability() {
        return CapabilityEnergy.ENERGY;
    }



}
