package com.til.glowing_fire_glow.common.register.effect;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class EffectRegister extends RegisterBasics {

    protected StatusEffect effect;

    @Override
    protected void init() {
        super.init();
        effect = initEffect();
        effect.setRegistryName(getName());
        ForgeRegistries.POTIONS.register(effect);
    }

    protected abstract StatusEffect initEffect();


    public StatusEffect getEffect() {
        return effect;
    }
}
