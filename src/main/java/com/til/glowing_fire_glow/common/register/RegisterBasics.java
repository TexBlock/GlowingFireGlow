package com.til.glowing_fire_glow.common.register;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ICanConfig;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.glowing_fire_glow.common.util.Util;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author til
 */
public class RegisterBasics implements ICanConfig {
    /***
     * 注册项的名称
     */
    protected ResourceLocation name;
    protected RegisterManage<?> registerManage;

    public RegisterBasics() {
        registerManage = GlowingFireGlow.getInstance().getReflexManage().getRegisterManageOfType(Util.forcedConversion(getClass()));
    }

    protected final void initSetName() {
        name = initName();
    }

    protected ResourceLocation initName() {
        return new ResourceLocation(GlowingFireGlow.getInstance().getModIdOfClass(this.getClass()), ResourceLocationUtil.ofPath(getClass()));
    }

    /***
     * 在配置之前
     */
    protected void beforeConfigInit() {

    }

    /***
     * 初始化
     */
    protected void init() {

    }

    /***
     * 初始化回调
     */
    protected void initBack() {
    }

    /***
     * 统一的回调
     * 这个是在所有注册完成后回调
     */
    protected void initBackToBack() {
    }

    /***
     * 统一在Setup回调
     */
    protected void initCommonSetup() {

    }

    protected void initDedicatedServerSetup() {

    }

    protected void initClientSetup() {

    }

    protected void initModProcessEvent() {

    }

    /***
     * 生成默认配置
     */
    @Override
    public void defaultConfig() {
    }


    /***
     * 获取由该注册项提供的注册内容
     */
    @Nullable
    public Iterable<RegisterBasics> getProactivelyAsset() {
        return null;
    }

    /***
     * 获取由该注册项提供的注册内容
     * 在getProactivelyAsset提供的所有资源注册完成之后再次开启注册
     */
    @Nullable
    public Iterable<RegisterBasics> getProactivelyAssetOnceAgain() {
        return null;
    }

    public ResourceLocation getName() {
        return name;
    }

    public RegisterManage<?> getRegisterManage() {
        return registerManage;
    }

    @Override
    public final ResourceLocation getConfigName() {
        return getName();
    }

    @Override
    public final ResourceLocation getBasicsConfigName() {
        return getRegisterManage().getRegisterManageName();
    }

    @Override
    public String toString() {
        return name == null ? "null" : name.toString();
    }

}
