package com.til.glowing_fire_glow.client.register.particle_register;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleParsingMode;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@OnlyIn(Dist.CLIENT)
public abstract class ParticleClientRegister<P extends ParticleRegister> extends RegisterBasics {

    protected Class<P> particleRegisterClass;

    protected ParticleRegister particleRegister;

    protected ParticleParsingMode particleParsingMode = ParticleParsingMode.SINGLE;

    @Override
    protected void init() {
        super.init();
        particleRegisterClass = initParticleRegisterClass();
        particleRegister = GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(particleRegisterClass);
    }

    protected Class<P> initParticleRegisterClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }


    /***
     * 实现粒子效果
     * @param world 当前的世界
     * @param start 开始点
     * @param end 结束点
     * @param color 颜色
     * @param density 密度
     * @return 返回粒子是生命用于拼接
     */
    public abstract void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation);


    public ParticleParsingMode getParticleParsingMode() {
        return particleParsingMode;
    }

    public ParticleRegister getParticleRegister() {
        return particleRegister;
    }


}
