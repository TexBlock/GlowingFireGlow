package com.til.glowing_fire_glow.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.capability.AllCapabilityRegister;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.Logging.CAPABILITIES;

@Mixin(value = CapabilityManager.class, remap = false)
public class CapabilityManagerMixin {

    @Shadow
    private volatile IdentityHashMap<String, List<Function<Capability<?>, Object>>> callbacks;

    @Inject(method = "injectCapabilities", at = @At("RETURN"))
    private void injectCapabilities(List<ModFileScanData> data, CallbackInfo ci) {
        for (CapabilityRegister<?> capabilityRegister : GlowingFireGlow.getInstance().getWorldComponent(AllCapabilityRegister.class).forAll()) {
            Type type = Type.getType(capabilityRegister.getCapabilityClass());
            final String capabilityName = type.getInternalName().replace('/', '.').intern();
            List<Function<Capability<?>, Object>> functionList;
            if (callbacks.containsKey(capabilityName)) {
                functionList = callbacks.get(capabilityName);
            } else {
                functionList = new ArrayList<>();
                callbacks.put(capabilityName, functionList);
            }
            functionList.add(capability -> {
                capabilityRegister.setCapability(Util.forcedConversion(capability));
                return null;
            });
        }
    }
}

