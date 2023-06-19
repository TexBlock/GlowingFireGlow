package com.til.glowing_fire_glow.common.register;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.util.ReflexUtil;
import com.til.glowing_fire_glow.util.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ReflexManage implements GlowingFireGlow.IWorldComponent {
    protected final Map<Class<?>, RegisterManage<?>> classRegisterManageMap = new HashMap<>();
    protected final Map<Class<?>, RegisterManage<?>> registerManageMap = new HashMap<>();

    /***
     * 自动注册的包
     */
    protected final Map<Class<?>, RegisterBasics> allVoluntarilyRegisterAssetMap = new HashMap<>();

    /***
     * 所有注册过的注册项
     */
    protected final Set<RegisterBasics> allRegisterAssetSet = new HashSet<>();


    @Override
    public void init(InitType initType) {

        switch (initType) {

            case NEW:
            case FML_CLIENT_SETUP:
                break;
            case FML_COMMON_SETUP:
                GlowingFireGlow.IWorldComponent.super.init(initType);
                for (Class<?> clazz : GlowingFireGlow.getInstance().forAllClass()) {
                    if (Modifier.isAbstract(clazz.getModifiers())) {
                        continue;
                    }
                    if (clazz.getAnnotation(Deprecated.class) != null) {
                        continue;
                    }
                    if (RegisterManage.class.isAssignableFrom(clazz)) {
                        RegisterManage<?> registerManage = GlowingFireGlow.getInstance().getWorldComponent(Util.forcedConversion(clazz));
                        Class<?> registerClass = registerManage.getRegisterClass();
                        if (!registerManageMap.containsKey(registerClass)) {
                            registerManageMap.put(registerClass, registerManage);
                            continue;
                        }
                        RegisterManage<?> oldRegisterManage = registerManageMap.get(registerClass);
                        if (oldRegisterManage.getBasicsRegisterManageClass() == null) {
                            if (oldRegisterManage.getBasicsRegisterManageClass() == null) {
                                throw new RuntimeException("注册管理者冲突，注册类型[" + registerClass + "]，冲突管理者[" + oldRegisterManage + "," + registerManage + "]");
                            }
                            continue;
                        }
                        if (registerManage.getBasicsRegisterManageClass() == null) {
                            registerManageMap.put(Util.forcedConversion(oldRegisterManage), registerManage);
                        }
                    }
                    if (RegisterBasics.class.isAssignableFrom(clazz)) {
                        if (!clazz.isAnnotationPresent(VoluntarilyRegister.class)) {
                            continue;
                        }
                        allVoluntarilyRegisterAssetMap.put(Util.forcedConversion(clazz), null);
                    }
                }
                break;
            case FML_DEDICATED_SERVER_SETUP:
                for (Class<?> clazz : allVoluntarilyRegisterAssetMap.keySet()) {
                    RegisterBasics registerBasics;
                    try {
                        registerBasics = (RegisterBasics) clazz.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                    allVoluntarilyRegisterAssetMap.put(clazz, registerBasics);
                }
                unifyRegisterSubdivision(allVoluntarilyRegisterAssetMap.values());

                for (RegisterBasics registerBasics : allRegisterAssetSet) {
                    registerBasics.initBackToBack();
                }
                break;
        }


    }


    protected void unifyRegisterSubdivision(Collection<RegisterBasics> registerBasicList) {
        Map<Integer, List<RegisterBasics>> integerListMap = new HashMap<>(8);
        for (RegisterBasics registerBasics : registerBasicList) {
            List<RegisterBasics> delayedRegisterList;
            if (integerListMap.containsKey(registerBasics.getRegisterManage().getVoluntarilyRegisterTime())) {
                delayedRegisterList = integerListMap.get(registerBasics.getRegisterManage().getVoluntarilyRegisterTime());
            } else {
                delayedRegisterList = new ArrayList<>(32);
                integerListMap.put(registerBasics.getRegisterManage().getVoluntarilyRegisterTime(), delayedRegisterList);
            }
            delayedRegisterList.add(registerBasics);
        }
        List<Integer> integerList = integerListMap.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (Integer integer : integerList) {
            unifyRegister(integerListMap.get(integer));
        }
    }

    protected void unifyRegister(Collection<RegisterBasics> registerBasicsList) {
        for (RegisterBasics registerBasic : registerBasicsList) {
            registerBasic.initSetName();
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            registerBasics.getRegisterManage().put(Util.forcedConversion(registerBasics), false);
            allRegisterAssetSet.add(registerBasics);
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            for (Field allField : ReflexUtil.getAllFields(registerBasics.getClass())) {
                if (allField.getAnnotation(ReflexManage.VoluntarilyAssignment.class) == null) {
                    continue;
                }
                try {
                    allField.setAccessible(true);
                    //noinspection unchecked
                    allField.set(this, getVoluntarilyRegisterOfClass((Class<RegisterBasics>) allField.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            registerBasics.beforeConfigInit();
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            GlowingFireGlow.getInstance().getConfigManage().initRegister(registerBasics);
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            registerBasics.init();
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            registerBasics.initBack();
        }
    }

    @Override
    public int getExecutionOrderList() {
        return 1000;
    }


    public <E extends RegisterManage<?>> E getRegisterManage(Class<E> registerManageClass) {
        return Util.forcedConversion(classRegisterManageMap.get(registerManageClass));
    }

    public <E extends RegisterBasics> RegisterManage<E> getRegisterManageOfType(Class<E> eClass) {
        Class<E> basType = eClass;
        while (basType != null) {
            if (registerManageMap.containsKey(basType)) {
                return Util.forcedConversion(registerManageMap.get(basType));
            }
            basType = Util.forcedConversion(basType.getSuperclass());
        }
        throw new RuntimeException("未找到目标类型为[" + eClass + "]注册管理器");
    }

    public <E extends RegisterBasics> E getVoluntarilyRegisterOfClass(Class<E> eClass) {
        return Util.forcedConversion(allVoluntarilyRegisterAssetMap.get(eClass));
    }

    @SafeVarargs
    public final <E extends RegisterBasics> List<E> getVoluntarilyRegisterOfClass(Class<? extends E>... eClass) {
        ArrayList<E> arrayList = new ArrayList<>(eClass.length);
        for (Class<? extends E> aClass : eClass) {
            arrayList.add(getVoluntarilyRegisterOfClass(aClass));
        }
        return arrayList;
    }

    public Iterable<RegisterManage<?>> forAllRegisterManage() {
        return Util.forcedConversion(classRegisterManageMap.values());
    }

    public Iterable<Class<? extends RegisterManage<?>>> forAllRegisterManageClass() {
        return Util.forcedConversion(classRegisterManageMap.keySet());
    }

    @Target({ElementType.TYPE, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface VoluntarilyRegister {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface VoluntarilyAssignment {
    }
}
