package com.til.glowing_fire_glow.client.register.key;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.key.KeyData;
import com.til.glowing_fire_glow.common.register.key.KeyRegister;
import com.til.glowing_fire_glow.common.register.message.messages.KeyMessage;
import com.til.glowing_fire_glow.util.ReflexUtil;
import com.til.glowing_fire_glow.util.Util;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@OnlyIn(Dist.CLIENT)
public abstract class KeyClientRegister<K extends KeyRegister> extends RegisterBasics {

    protected Class<K> keyRegisterClass;

    protected KeyRegister keyRegister;

    protected int inputId;
    protected KeyBinding keyMapping;
    protected boolean isKeyDown;

    @VoluntarilyAssignment
    protected KeyMessage keyMessage;

    @Override
    protected void init() {
        super.init();
        keyRegisterClass = initKeyClass();
        keyRegister = GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(keyRegisterClass);
        inputId = initInputId();
        keyMapping = new KeyBinding(getName().toString(),
                KeyConflictContext.IN_GAME,
                KeyModifier.CONTROL,
                InputMappings.Type.KEYSYM,
                inputId,
                getName().getNamespace());
    }

    protected Class<K> initKeyClass() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }

    protected abstract int initInputId();


    @Override
    protected void initBackToBack() {
        ClientRegistry.registerKeyBinding(keyMapping);
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    protected void onClientTickEvent(InputEvent.KeyInputEvent event) {
        if (keyMapping.isKeyDown()) {
            if (!isKeyDown) {
                isKeyDown = true;
                pressed();
                keyMessage.sendToServer(new KeyData(keyRegister.getName(), KeyData.KeyState.PRESSED));
            }
        } else {
            if (isKeyDown) {
                isKeyDown = false;
                release();
                keyMessage.sendToServer(new KeyData(keyRegister.getName(), KeyData.KeyState.RELEASE));
            }
        }

    }

    /***
     * 当按键按下去时
     */
    @OnlyIn(Dist.CLIENT)
    protected abstract void pressed();

    /***
     * 松开时
     */
    @OnlyIn(Dist.CLIENT)
    protected abstract void release();


}
