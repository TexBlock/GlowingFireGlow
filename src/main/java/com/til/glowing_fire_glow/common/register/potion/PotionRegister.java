package com.til.glowing_fire_glow.common.register.potion;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class PotionRegister extends RegisterBasics {

    protected Potion potion;

    @Override
    protected void init() {
        super.init();
        potion = initPotion();
        potion.setRegistryName(getName());
        ForgeRegistries.POTIONS.register(potion);
    }

    protected abstract Potion initPotion();

    public Potion getPotion() {
        return potion;
    }
}
