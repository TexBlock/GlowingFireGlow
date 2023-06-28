package com.til.glowing_fire_glow.common.config;

import com.google.gson.JsonObject;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.util.Extension;
import com.til.glowing_fire_glow.util.IOUtil;
import com.til.glowing_fire_glow.util.ReflexUtil;
import com.til.glowing_fire_glow.util.gson.ConfigGson;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author til
 */
public class ConfigManage implements IWorldComponent {

    public static final String CONFIG_NAME = GlowingFireGlow.MOD_ID + ".config";

    protected List<Map.Entry<File, RegisterBasics>> needWrite = new ArrayList<>();

    public File mackFile(RegisterBasics registerBasics) {
        File basicsFile = FMLPaths.CONFIGDIR.get().toFile();
        ResourceLocation name = registerBasics.getName();
        ResourceLocation basicsName = registerBasics.getRegisterManage().getRegisterManageName();
        String version = GlowingFireGlow.getInstance().getModVersion(registerBasics.getClass());
        return new File(basicsFile, String.format("%s/%s/%s/%s/%s.json", CONFIG_NAME, basicsName.getPath(), name.getNamespace(), version, name.getPath()));
    }

    public void write() {
        for (Map.Entry<File, RegisterBasics> entry : needWrite) {
            JsonObject jsonObject;
            try {
                jsonObject = readRegister(entry.getValue());
            } catch (IllegalAccessException e) {
                GlowingFireGlow.LOGGER.error("写入配置时出现问题", e);
                continue;
            }
            IOUtil.writer(entry.getKey(), ConfigGson.getGson().toJson(jsonObject));
        }
        needWrite.clear();
        needWrite = null;
    }

    public void initRegister(RegisterBasics registerBasics) {
        File file = mackFile(registerBasics);
        if (!file.exists()) {
            needWrite.add(new Extension.VariableData_2<>(file, registerBasics));
            registerBasics.defaultConfig();
            return;
        }
        String s = IOUtil.readFileByLines(file);
        if (s.isEmpty()) {
            return;
        }
        JsonObject jsonObject = ConfigGson.getGson().fromJson(s, JsonObject.class);
        try {
            writeRegister(registerBasics, jsonObject);
        } catch (IllegalAccessException e) {
            GlowingFireGlow.LOGGER.error("赋值配置时出现问题", e);
        }
    }

    public void writeRegister(RegisterBasics registerBasics, JsonObject jsonObject) throws IllegalAccessException {
        for (Field field : ReflexUtil.getAllFields(registerBasics.getClass(), false)) {
            if (!field.isAnnotationPresent(ConfigField.class)) {
                continue;
            }
            if (!jsonObject.has(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            field.set(registerBasics, ConfigGson.getGson().fromJson(jsonObject.get(field.getName()), field.getGenericType()));
        }
    }

    public JsonObject readRegister(RegisterBasics registerBasics) throws IllegalAccessException {
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
            jsonObject.add(field.getName(), ConfigGson.getGson().toJsonTree(v));
        }
        return jsonObject;
    }


    @Override
    public void init(InitType initType) {
        switch (initType) {
            case NEW:
                break;
            case FML_DEDICATED_SERVER_SETUP:
                break;
            case FML_CLIENT_SETUP:
                break;
            case FML_COMMON_SETUP:
                for (Map.Entry<File, RegisterBasics> entry : needWrite) {
                    JsonObject jsonObject;
                    try {
                        jsonObject = readRegister(entry.getValue());
                    } catch (IllegalAccessException e) {
                        GlowingFireGlow.LOGGER.error("写人配置时出错", e);
                        return;
                    }
                    IOUtil.writer(entry.getKey(), ConfigGson.getGson().toJson(jsonObject));
                }
                needWrite.clear();
                needWrite = null;
                break;
        }
    }

    @Override
    public int getExecutionOrderList() {
        return -500;
    }
}
