package com.til.glowing_fire_glow.common.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

/**
 * @author til
 */
public class GlowingFireGlowCommonMixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        Mixins.addConfiguration("glowing_fire_glow.client.mixins.json");
        Mixins.addConfiguration("glowing_fire_glow.common.mixins.json");
    }
}
