package com.til.glowing_fire_glow.client.register.entity_render;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@OnlyIn(Dist.CLIENT)
public abstract class EntityRenderRegister<E extends Entity, ET extends EntityTypeRegister<E>> extends RegisterBasics {
    protected Class<E> entityClass;
    protected Class<E> entityTypeRegisterClass;
    protected EntityTypeRegister<E> entityTypeRegister;

    @Override
    protected void init() {
        super.init();
        initClass();
        entityTypeRegister = GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(Util.forcedConversion(entityTypeRegisterClass));
    }

    @Override
    protected void initCommonSetup() {
        super.initCommonSetup();
        RenderingRegistry.registerEntityRenderingHandler(entityTypeRegister.getEntityType(), this::createRenderFor);
    }

    protected void initClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type[] types = parameterized.getActualTypeArguments();
        entityClass = Util.forcedConversion(ReflexUtil.asClass(types[0]));
        entityTypeRegisterClass = Util.forcedConversion(ReflexUtil.asClass(types[1]));
    }

    protected abstract EntityRenderer<? super E> createRenderFor(EntityRenderDispatcher manager);
}
