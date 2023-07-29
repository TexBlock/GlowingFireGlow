package com.til.glowing_fire_glow.common.register.attribute;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AttributeRegister extends RegisterBasics {

    protected Attribute attribute;

    @Override
    protected void init() {
        super.init();
        attribute = initAttribute();
        attribute.setRegistryName(getName());
        ForgeRegistries.ATTRIBUTES.register(attribute);
    }

    protected abstract Attribute initAttribute();

    public Attribute getAttribute() {
        return attribute;
    }
}
