package com.til.glowing_fire_glow.common.config;

import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.IOUtil;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author til
 */
public class ConfigManage implements IWorldComponent {

    @VoluntarilyAssignment
    protected GsonManage gsonManage;

    public static final String CONFIG_NAME = GlowingFireGlow.MOD_ID + ".config";

    protected Map<ICanConfig, File> needWrite = new HashMap<>();
    protected Map<ICanConfig, File> delayedWrite = new HashMap<>();

    public File mackFile(ICanConfig registerBasics) {
        File basicsFile = FMLPaths.CONFIGDIR.get().toFile();
        ResourceLocation name = registerBasics.getConfigName();
        ResourceLocation basicsName = registerBasics.getBasicsConfigName();
        String version = GlowingFireGlow.getInstance().getModVersion(registerBasics.getClass());
        return new File(basicsFile, String.format("%s/%s/%s/%s/%s.json", CONFIG_NAME, basicsName.getPath(), name.getNamespace(), version, name.getPath()));
    }

    public void initRegister(ICanConfig registerBasics) {
        File file = mackFile(registerBasics);
        if (!file.exists()) {
            needWrite.put(registerBasics, file);
            registerBasics.defaultConfig();
            return;
        }
        String s = IOUtil.readFileByLines(file);
        if (s.isEmpty()) {
            return;
        }
        JsonObject jsonObject = gsonManage.getGson().fromJson(s, JsonObject.class);
        try {
            writeRegister(registerBasics, jsonObject);
        } catch (Exception e) {
            GlowingFireGlow.LOGGER.error(MessageFormat.format("赋值[{0}]配置时出错", registerBasics.toString()), e);
        }
    }

    public void addDelayedWrite(ICanConfig registerBasics) {
        if (!needWrite.containsKey(registerBasics)) {
            throw new RuntimeException("错误的延迟写入配置");
        }
        File file = needWrite.get(registerBasics);
        needWrite.remove(registerBasics);
        delayedWrite.put(registerBasics, file);
    }

    public void writeRegister(ICanConfig registerBasics, JsonObject jsonObject) throws IllegalAccessException {
        for (Field field : ReflexUtil.getAllFields(registerBasics.getClass(), false)) {
            if (!field.isAnnotationPresent(ConfigField.class)) {
                continue;
            }
            if (!jsonObject.has(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            field.set(registerBasics, gsonManage.getGson().fromJson(jsonObject.get(field.getName()), field.getGenericType()));
        }
    }

    public JsonObject readRegister(ICanConfig registerBasics) throws IllegalAccessException {
        JsonObject jsonObject = new JsonObject();
        for (Field field : ReflexUtil.getAllFields(registerBasics.getClass(), false)) {
            if (!field.isAnnotationPresent(ConfigField.class)) {
                continue;
            }
            field.setAccessible(true);
            Object v = field.get(registerBasics);
            if (v == null) {
                continue;
            }
            jsonObject.add(field.getName(), gsonManage.getGson().toJsonTree(v));
        }
        return jsonObject;
    }

    @Override
    public void initCommonSetup() {
        IWorldComponent.super.initCommonSetup();
        write(needWrite);
        needWrite.clear();
        needWrite = null;
    }

    public void writeDelayed() {
        write(delayedWrite);
        delayedWrite.clear();
        delayedWrite = null;
    }

    protected void write(Map<ICanConfig, File> registerBasicsFileMap) {
        for (Map.Entry<ICanConfig, File> entry : registerBasicsFileMap.entrySet()) {
            JsonObject jsonObject;
            try {
                jsonObject = readRegister(entry.getKey());
            } catch (Exception e) {
                GlowingFireGlow.LOGGER.error(MessageFormat.format("写入[{0}]配置时出错", entry.getKey().toString()), e);
                return;
            }
            IOUtil.writer(entry.getValue(), gsonManage.getGson().toJson(jsonObject));
        }
    }

    @Override
    public int getExecutionOrderList() {
        return -500;
    }

}

