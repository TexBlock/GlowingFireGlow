package com.til.glowing_fire_glow.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import net.minecraftforge.fml.server.ServerModLoader;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerModLoader.class, remap = false)
public class ServerModLoaderMixin {

    @Inject(method = "load",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraftforge/eventbus/api/IEventBus;start()V",
                    opcode = 1
            ))
    private static void load(CallbackInfo ci) {
        GlowingFireGlow.getInstance().getConfigManage().writeDelayed();
    }
}
