package com.til.glowing_fire_glow.common.register;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ReflexManage implements IWorldComponent {

    /***
     * 根据RegisterManage的类型映射
     */
    protected final Map<Class<?>, RegisterManage<?>> classRegisterManageMap = new HashMap<>();

    /***
     * 根据RegisterManage的管理类型映射
     */
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
    public void initNew() {
        IWorldComponent.super.initNew();
        for (Class<?> clazz : GlowingFireGlow.getInstance().forAllClass()) {
            if (!ReflexUtil.isEffective(clazz)) {
                continue;
            }
            if (RegisterManage.class.isAssignableFrom(clazz)) {
                RegisterManage<?> registerManage = GlowingFireGlow.getInstance().getWorldComponent(Util.forcedConversion(clazz));
                classRegisterManageMap.put(clazz, registerManage);
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
                    registerManageMap.put(Util.forcedConversion(registerClass), registerManage);
                }
            }
            if (RegisterBasics.class.isAssignableFrom(clazz)) {
                if (!clazz.isAnnotationPresent(VoluntarilyRegister.class)) {
                    continue;
                }
                allVoluntarilyRegisterAssetMap.put(Util.forcedConversion(clazz), null);
            }
        }

        for (Class<?> clazz : allVoluntarilyRegisterAssetMap.keySet()) {
            RegisterBasics registerBasics;
            try {
                registerBasics = (RegisterBasics) clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (NoClassDefFoundError i) {
                GlowingFireGlow.LOGGER.error("NoClassDefFoundError of [" + clazz + "]", i);
                continue;
            }
            allVoluntarilyRegisterAssetMap.put(clazz, registerBasics);
        }
        for (IWorldComponent iWorldComponent : GlowingFireGlow.getInstance().forWorldComponent()) {
            fillRegisterBasics(iWorldComponent);
        }
        for (Class<?> forStaticAssignmentClass : GlowingFireGlow.getInstance().forStaticAssignmentClass()) {
            fillRegisterBasics(forStaticAssignmentClass);
        }
        unifyRegisterSubdivision(allVoluntarilyRegisterAssetMap
                .values()
                .stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(r -> {
                    VoluntarilyRegister voluntarilyRegister = r.getClass().getAnnotation(VoluntarilyRegister.class);
                    return voluntarilyRegister == null ? 0 : voluntarilyRegister.priority();
                }).reversed())
                .collect(Collectors.toList()));

        for (RegisterBasics registerBasics : allRegisterAssetSet) {
            registerBasics.initBackToBack();
        }
    }

    @Override
    public void initCommonSetup() {
        IWorldComponent.super.initCommonSetup();
        for (RegisterBasics registerBasics : allRegisterAssetSet) {
            registerBasics.initCommonSetup();
        }
    }

    @Override
    public void initDedicatedServerSetup() {
        IWorldComponent.super.initDedicatedServerSetup();
        for (RegisterBasics registerBasics : allRegisterAssetSet) {
            registerBasics.initDedicatedServerSetup();
        }
    }

    @Override
    public void initClientSetup() {
        IWorldComponent.super.initClientSetup();
        for (RegisterBasics registerBasics : allRegisterAssetSet) {
            registerBasics.initClientSetup();
        }
    }

    @Override
    public void initModProcessEvent() {
        IWorldComponent.super.initModProcessEvent();
        for (RegisterBasics registerBasics : allRegisterAssetSet) {
            registerBasics.initModProcessEvent();
        }
    }


    protected void unifyRegisterSubdivision(Collection<RegisterBasics> registerBasicList) {
        for (RegisterBasics registerBasics : registerBasicList) {
            GlowingFireGlow.getInstance().fillWorldComponent(registerBasics);
        }
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
        List<RegisterBasics> laterAdd = new ArrayList<>();
        for (List<RegisterBasics> value : integerListMap.values()) {
            for (RegisterBasics registerBasics : value) {
                Iterable<RegisterBasics> proactivelyAsset = registerBasics.getProactivelyAssetOnceAgain();
                if (proactivelyAsset != null) {
                    for (RegisterBasics basics : proactivelyAsset) {
                        laterAdd.add(basics);
                    }
                }
            }
        }
        if (!laterAdd.isEmpty()) {
            unifyRegisterSubdivision(laterAdd);
        }
    }

    protected void unifyRegister(Collection<RegisterBasics> registerBasicsList) {
        List<RegisterBasics> laterAdd = new ArrayList<>();
        for (RegisterBasics registerBasic : registerBasicsList) {
            registerBasic.initSetName();
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            registerBasics.getRegisterManage().put(Util.forcedConversion(registerBasics), false);
            allRegisterAssetSet.add(registerBasics);
        }
        for (RegisterBasics registerBasics : registerBasicsList) {
            fillRegisterBasics(registerBasics);
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
        for (RegisterBasics registerBasics : registerBasicsList) {
            Iterable<RegisterBasics> proactivelyAsset = registerBasics.getProactivelyAsset();
            if (proactivelyAsset != null) {
                for (RegisterBasics basics : proactivelyAsset) {
                    laterAdd.add(basics);
                }
            }
        }
        if (!laterAdd.isEmpty()) {
            unifyRegisterSubdivision(laterAdd);
        }
    }

    @Override
    public int getExecutionOrderList() {
        return 1000;
    }

    public void fillRegisterBasics(Object obj) {
        boolean isClass = obj instanceof Class<?>;
        for (Field allField : ReflexUtil.getAllFields(isClass ? ((Class<?>) obj) : obj.getClass(), isClass)) {
            if (allField.getAnnotation(VoluntarilyAssignment.class) == null) {
                continue;
            }
            if (!RegisterBasics.class.isAssignableFrom(allField.getType())) {
                continue;
            }
            try {
                allField.setAccessible(true);
                //noinspection unchecked
                allField.set(isClass ? null : obj, getVoluntarilyRegisterOfClass((Class<RegisterBasics>) allField.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
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
    public final <E extends RegisterBasics> List<E> getVoluntarilyRegisterOfClassList(Class<? extends E>... eClass) {
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

}









