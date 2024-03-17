package com.til.glowing_fire_glow.client.register.entity_render;

import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@OnlyIn(Dist.CLIENT)
public abstract class IndependenceEntityRenderRegister<E extends Entity, ET extends EntityTypeRegister<E>, ER extends EntityRenderer<? extends E>> extends EntityRenderRegister<E, ET> {

    protected Class<ER> entityRenderClass;

    @Override
    protected void initClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type[] types = parameterized.getActualTypeArguments();
        entityClass = Util.forcedConversion(ReflexUtil.asClass(types[0]));
        entityTypeRegisterClass = Util.forcedConversion(ReflexUtil.asClass(types[1]));
        entityRenderClass = Util.forcedConversion(ReflexUtil.asClass(types[2]));
    }

    @Override
    protected EntityRenderer<? super E> createRenderFor(EntityRenderDispatcher manager) {
        ER entityRender;
        try {
            entityRender = entityRenderClass.getConstructor(EntityRenderDispatcher.class).newInstance(manager);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return Util.forcedConversion(entityRender);
    }
}
