package com.til.glowing_fire_glow.common.register.entity_type;

import com.sun.javafx.geom.Vec2d;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.util.ReflexUtil;
import com.til.glowing_fire_glow.util.Util;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EntityTypeRegister<E extends Entity> extends RegisterBasics {
    protected Class<?> entityClass;
    protected EntityType<E> entityType;

    @ConfigField
    protected EntityClassification entityClassification;

    @ConfigField
    protected float width;

    @ConfigField
    protected float height;

    /***
     * 追踪范围
     */
    @ConfigField
    protected int trackingRange;

    /***
     * 更新时间间隔
     */
    @ConfigField
    protected int updateInterval;

    @ConfigField
    protected boolean shouldReceiveVelocityUpdates;

    @Override
    public void init() {
        super.init();
        entityClass = initClass();
        entityType = initEntityType();
    }

    protected EntityType<E> initEntityType() {
        return EntityType.Builder
                .create(this::create, entityClassification)
                .size(width, height)
                .setTrackingRange(trackingRange)
                .setUpdateInterval(updateInterval)
                .setShouldReceiveVelocityUpdates(shouldReceiveVelocityUpdates)
                .setCustomClientFactory(this::createInClient)
                .build(getName().toString());
    }

    protected Class<E> initClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return Util.forcedConversion(ReflexUtil.asClass(parameterized.getActualTypeArguments()[0]));
    }


    protected abstract E create(EntityType<E> eEntityType, World world);

    protected E createInClient(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        return create(getEntityType(), world);
    }

    public EntityType<E> getEntityType() {
        return entityType;
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        entityClassification = EntityClassification.MISC;
        width = 0.5f;
        height = 0.5f;
        trackingRange = 10;
        updateInterval = 20;
        shouldReceiveVelocityUpdates = false;
    }
}
