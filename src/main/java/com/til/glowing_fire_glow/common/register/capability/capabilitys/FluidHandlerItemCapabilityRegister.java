package com.til.glowing_fire_glow.common.register.capability.capabilitys;

import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class FluidHandlerItemCapabilityRegister extends CapabilityRegister<IFluidHandlerItem> {

    @Override
    protected Capability<IFluidHandlerItem> initCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }


}
