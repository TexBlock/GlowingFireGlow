package com.til.glowing_fire_glow.common.register.effect;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.potion.Effect;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class EffectRegister extends RegisterBasics {

    protected Effect effect;

    @Override
    protected void init() {
        super.init();
        effect = initEffect();
        effect.setRegistryName(getName());
        ForgeRegistries.POTIONS.register(effect);
    }

    protected abstract Effect initEffect();


    public Effect getEffect() {
        return effect;
    }
}
