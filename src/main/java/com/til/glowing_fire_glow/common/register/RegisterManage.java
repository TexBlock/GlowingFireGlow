package com.til.glowing_fire_glow.common.register;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.IdentifierUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author til
 */
public abstract class RegisterManage<R extends RegisterBasics> implements IWorldComponent {
    protected Class<R> registerClass;
    protected Identifier registerManageName;
    protected Map<Identifier, R> registerMap;

    protected RegisterManage() {
        registerClass = initType();
        registerManageName = initIdentifier();
        registerMap = new HashMap<>();
    }

    protected Class<R> initType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }

    protected final Identifier initIdentifier() {
        StringBuilder path = new StringBuilder();
        if (getBasicsRegisterManageClass() != null) {
            path.append(IdentifierUtil.ofPath(getBasicsRegisterManageClass()));
            path.append('/');
        }
        path.append(IdentifierUtil.ofPath(getClass()));
        return new Identifier(GlowingFireGlow.getInstance().getModIdOfClass(this.getClass()), path.toString());
    }

    public final void put(R register, boolean fromSon) {
        RegisterManage<?> basicsRegisterManage = GlowingFireGlow.getInstance().getReflexManage().getRegisterManage(getBasicsRegisterManageClass());
        if (basicsRegisterManage != null) {
            basicsRegisterManage.put(Util.forcedConversion(register), true);
        }
        registerMap.put(register.getName(), register);
        if (!fromSon) {
            MinecraftForge.EVENT_BUS.register(register);
        }
    }

    public R get(Identifier Identifier) {
        return registerMap.get(Identifier);
    }

    public Collection<R> forAll() {
        return registerMap.values();
    }

    @Nullable
    public Class<? extends RegisterManage<?>> getBasicsRegisterManageClass() {
        return null;
    }

    public int getVoluntarilyRegisterTime() {
        return 0;
    }


    public Class<R> getRegisterClass() {
        return registerClass;
    }

    public Identifier getRegisterManageName() {
        return registerManageName;
    }

}
