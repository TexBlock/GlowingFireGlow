package com.til.glowing_fire_glow.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import net.minecraftforge.client.loading.ClientModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = ClientModLoader.class, remap = false)
public class ClientModLoaderMixin {

    @Inject(method = "completeModLoading",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraftforge/eventbus/api/IEventBus;start()V",
                    opcode = 1
            ))
    private static void completeModLoading(CallbackInfoReturnable<Boolean> cir) {
        GlowingFireGlow.getInstance().getConfigManage().writeDelayed();
    }
}
