package com.til.glowing_fire_glow.common.register.attribute;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AttributeRegister extends RegisterBasics {

    protected EntityAttribute attribute;

    @Override
    protected void init() {
        super.init();
        attribute = initAttribute();
        attribute.setRegistryName(getName());
        ForgeRegistries.ATTRIBUTES.register(attribute);
    }

    protected abstract EntityAttribute initAttribute();

    public EntityAttribute getAttribute() {
        return attribute;
    }
}
