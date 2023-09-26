package com.til.glowing_fire_glow.client.register.capability.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.CapabilityRegister;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@OnlyIn(Dist.CLIENT)
public abstract class CapabilityRenderRegister<C, CE extends CapabilityRegister<C>> extends RegisterBasics {

    @VoluntarilyAssignment
    protected ReflexManage reflexManage;

    protected Class<C> capabilityClass;
    protected Class<CE> capabilityRegisterClass;

    protected CE capabilityRegister;


    @Override
    protected void init() {
        super.init();
        initClass();
        capabilityRegister = reflexManage.getVoluntarilyRegisterOfClass(capabilityRegisterClass);
    }

    protected void initClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type[] types = parameterized.getActualTypeArguments();
        capabilityClass = Util.forcedConversion(ReflexUtil.asClass(types[0]));
        capabilityRegisterClass = Util.forcedConversion(ReflexUtil.asClass(types[1]));
    }

    public abstract void render(Entity entity, C c, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn);

    public Class<C> getCapabilityClass() {
        return capabilityClass;
    }

    public Class<CE> getCapabilityRegisterClass() {
        return capabilityRegisterClass;
    }

    public CE getCapabilityRegister() {
        return capabilityRegister;
    }

}
