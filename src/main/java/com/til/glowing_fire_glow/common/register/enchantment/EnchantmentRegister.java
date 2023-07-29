package com.til.glowing_fire_glow.common.register.enchantment;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class EnchantmentRegister extends RegisterBasics {

    protected Enchantment enchantment;

    @Override
    protected void init() {
        super.init();
        enchantment = initEnchantment();
        enchantment.setRegistryName(getName());
        ForgeRegistries.ENCHANTMENTS.register(enchantment);
    }

    protected abstract Enchantment initEnchantment();

    public Enchantment getEnchantment() {
        return enchantment;
    }

}
