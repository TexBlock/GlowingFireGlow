package com.til.glowing_fire_glow.common.register.back_type;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BackTypeRegister<C> extends RegisterBasics {

    protected Type backType;
    protected Class<C> backClass;

    @Override
    protected void init() {
        super.init();
        backType = initBackType();
        backClass = Util.forcedConversion(ReflexUtil.asClass(backType));
    }

    protected Type initBackType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    public Type getBackType() {
        return backType;
    }

    public Class<C> getBackClass() {
        return backClass;
    }
}
