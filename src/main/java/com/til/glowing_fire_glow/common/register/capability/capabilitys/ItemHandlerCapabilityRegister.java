package com.til.glowing_fire_glow.common.register.capability.capabilitys;

import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class ItemHandlerCapabilityRegister extends CapabilityRegister<IItemHandler> {

    @Override
    protected Capability<IItemHandler> initCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }


}
