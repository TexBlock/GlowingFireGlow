package com.til.glowing_fire_glow;

import com.til.glowing_fire_glow.common.config.ConfigManage;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.ReflexManage;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.save.SaveManage;
import com.til.glowing_fire_glow.util.ReflexUtil;
import com.til.glowing_fire_glow.util.Util;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Mod(GlowingFireGlow.MOD_ID)
@GlowingFireGlow.Manage
@StaticVoluntarilyAssignment
public class GlowingFireGlow {

    public static final String MOD_ID = "glowing_fire_glow";
    public static final String MOD_NAME = "GlowingFireGlow";

    public static final Logger LOGGER = LogManager.getLogger();

    protected static GlowingFireGlow glowingFireGlow;

    public static GlowingFireGlow getInstance() {
        return glowingFireGlow;
    }

    public final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    protected HashSet<FMLModContainer> useMod;
    protected HashSet<Class<?>> allClass;
    protected Map<Class<?>, ModContainer> modClassMap;

    protected Map<Class<?>, IWorldComponent> worldComponentMap;
    protected List<IWorldComponent> worldComponentList;
    protected List<Class<?>> staticAssignmentList;

    @VoluntarilyAssignment
    protected ReflexManage reflexManage;
    @VoluntarilyAssignment
    protected ConfigManage configManage;

    @VoluntarilyAssignment
    protected SaveManage saveManage;

    protected Type mixinType = Type.getType(Mixin.class);

    protected Type onlyInType = Type.getType(OnlyIn.class);

    public GlowingFireGlow() {
        glowingFireGlow = this;
        modEventBus.register(this);
        Field fmlModContainer_scanResults;
        Field fmlModContainer_modClass;
        Field classData_clazz;

        try {
            fmlModContainer_scanResults = FMLModContainer.class.getDeclaredField("scanResults");
            fmlModContainer_scanResults.setAccessible(true);
            fmlModContainer_modClass = FMLModContainer.class.getDeclaredField("modClass");
            fmlModContainer_modClass.setAccessible(true);
            classData_clazz = ModFileScanData.ClassData.class.getDeclaredField("clazz");
            classData_clazz.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        useMod = new HashSet<>();
        allClass = new HashSet<>();
        modClassMap = new HashMap<>();
        worldComponentMap = new HashMap<>();
        worldComponentList = new ArrayList<>();
        staticAssignmentList = new ArrayList<>();

        ModList.get().forEachModContainer((modId, modContainer) -> {
            if (!(modContainer instanceof FMLModContainer)) {
                return;
            }
            FMLModContainer fmlModContainer = ((FMLModContainer) modContainer);

            Class<?> modClass;
            try {
                modClass = (Class<?>) fmlModContainer_modClass.get(fmlModContainer);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (modClass.getAnnotation(Manage.class) == null) {
                return;
            }
            useMod.add(fmlModContainer);
        });

        for (FMLModContainer fmlModContainer : useMod) {
            ModFileScanData modFileScanData;
            try {
                modFileScanData = (ModFileScanData) fmlModContainer_scanResults.get(fmlModContainer);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            HashSet<Type> excludeTypeList = new HashSet<>();

            for (ModFileScanData.AnnotationData annotation : modFileScanData.getAnnotations()) {
                if (annotation.getTargetType() != ElementType.TYPE) {
                    continue;
                }
                if (annotation.getAnnotationType().equals(mixinType)) {
                    excludeTypeList.add(annotation.getClassType());
                }
                if (annotation.getAnnotationType().equals(onlyInType)) {
                    Object v = annotation.getAnnotationData().get("value");
                    if (!(v instanceof ModAnnotation.EnumHolder)) {
                        continue;
                    }
                    ModAnnotation.EnumHolder enumHolder = ((ModAnnotation.EnumHolder) v);
                    if (!enumHolder.getValue().equals(FMLLoader.getDist().toString())) {
                        excludeTypeList.add(annotation.getClassType());
                    }
                }
            }


            for (ModFileScanData.ClassData aClass : modFileScanData.getClasses()) {
                Type type;
                try {
                    type = (Type) classData_clazz.get(aClass);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (excludeTypeList.contains(type)) {
                    continue;
                }
                String className = type.getClassName();
                if (className.indexOf('$') != -1) {
                    continue;
                }
                Class<?> clazz;
                try {
                    clazz = Class.forName(type.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                allClass.add(clazz);
                modClassMap.put(clazz, fmlModContainer);
            }
        }


        for (Class<?> aClass : allClass) {
            if (!ReflexUtil.isEffective(aClass)) {
                continue;
            }
            if (aClass.getAnnotation(StaticVoluntarilyAssignment.class) != null) {
                staticAssignmentList.add(aClass);
            }
            if (IWorldComponent.class.isAssignableFrom(aClass)) {
                try {
                    IWorldComponent worldComponent = (IWorldComponent) aClass.getDeclaredConstructor().newInstance();
                    worldComponentMap.put(aClass, worldComponent);
                    worldComponent.registerEvent(MinecraftForge.EVENT_BUS);
                    worldComponent.registerModEvent(modEventBus);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        worldComponentList = worldComponentMap.values().stream().sorted(Comparator.comparing(w -> -w.getExecutionOrderList())).collect(Collectors.toList());
        fillWorldComponent(this);
        for (Class<?> aClass : staticAssignmentList) {
            fillWorldComponent(aClass);
        }
        for (IWorldComponent iWorldComponent : worldComponentList) {
            fillWorldComponent(iWorldComponent);
        }
        for (IWorldComponent iWorldComponent : worldComponentList) {
            iWorldComponent.init(IWorldComponent.InitType.NEW);
        }
    }

    @SubscribeEvent
    protected void setup(final FMLCommonSetupEvent event) {
        for (IWorldComponent iWorldComponent : worldComponentList) {
            iWorldComponent.init(IWorldComponent.InitType.FML_COMMON_SETUP);
        }
    }

    @SubscribeEvent
    protected void doServerStuff(final FMLDedicatedServerSetupEvent event) {
        for (IWorldComponent iWorldComponent : worldComponentList) {
            iWorldComponent.init(IWorldComponent.InitType.FML_DEDICATED_SERVER_SETUP);
        }
    }

    @SubscribeEvent
    protected void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        for (IWorldComponent iWorldComponent : worldComponentList) {
            iWorldComponent.init(IWorldComponent.InitType.FML_CLIENT_SETUP);
        }
    }

    @SubscribeEvent
    protected void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        /*InterModComms.sendTo("GlowingFireGlow", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });*/

    }

    @SubscribeEvent
    protected void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
/*    @SubscribeEvent
    protected void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }*/

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
/*    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }*/


    /***
     * 通过类获取类所在的mod
     */
    @Nullable
    public ModContainer getModContainerOfClass(Class<?> clazz) {
        return modClassMap.get(clazz);
    }

    /***
     * 获取类所在的modId
     */
    public String getModIdOfClass(Class<?> clazz) {
        ModContainer modContainer = getModContainerOfClass(clazz);
        if (modContainer == null) {
            return "minecraft";
        }
        return modContainer.getModId();
    }

    public String getModVersion(Class<?> clazz) {
        ModContainer modContainer = getModContainerOfClass(clazz);
        if (modContainer == null) {
            return "0.0.0";
        }
        return modContainer.getModInfo().getVersion().toString();
    }

    public Iterable<Class<?>> forAllClass() {
        return allClass;
    }

    public Iterable<Class<?>> forStaticAssignmentClass() {
        return staticAssignmentList;
    }

    public Iterable<IWorldComponent> forWorldComponent() {
        return worldComponentList;
    }

    public <W extends IWorldComponent> W getWorldComponent(Class<W> wClass) {
        return Util.forcedConversion(worldComponentMap.get(wClass));
    }

    public void fillWorldComponent(Object obj) {
        boolean isClass = obj instanceof Class<?>;
        for (Field allField : ReflexUtil.getAllFields(isClass ? ((Class<?>) obj) :obj.getClass(), isClass)) {
            if (allField.getAnnotation(VoluntarilyAssignment.class) == null) {
                continue;
            }
            if (!IWorldComponent.class.isAssignableFrom(allField.getType())) {
                continue;
            }
            try {
                allField.setAccessible(true);
                allField.set(isClass ? null : obj, getWorldComponent(Util.forcedConversion(allField.getType())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public ReflexManage getReflexManage() {
        return reflexManage;
    }

    public ConfigManage getConfigManage() {
        return configManage;
    }

    public SaveManage getSaveManage() {
        return saveManage;
    }

    /***
     * 注解表示接受GlowingFireGlow对mod的管理
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Manage {

    }


}
