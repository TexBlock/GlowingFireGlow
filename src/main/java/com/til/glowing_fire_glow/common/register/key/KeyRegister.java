package com.til.glowing_fire_glow.common.register.key;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.message.messages.KeyMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

/**
 * @author til
 */
public abstract class KeyRegister extends RegisterBasics {

    protected int inputId;
    protected KeyBinding keyMapping;
    protected boolean isKeyDown;

    @VoluntarilyAssignment
    protected KeyMessage keyMessage;

    @Override
    public void init() {
        super.init();
        inputId = initInputId();
        keyMapping = new KeyBinding(getName().toString(),
                KeyConflictContext.IN_GAME,
                KeyModifier.CONTROL,
                InputMappings.Type.KEYSYM,
                inputId,
                getName().getNamespace());
    }


    protected abstract int initInputId();

    public void run(Supplier<NetworkEvent.Context> supplier) {
        MinecraftForge.EVENT_BUS.post(new EventKey(this, supplier));
    }

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
                keyMessage.sendToServer(new KeyData(getName(), true));
            }
        } else {
            if (isKeyDown) {
                isKeyDown = false;
                release();
                keyMessage.sendToServer(new KeyData(getName(), false));
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

    /***
     * 当按键按下去时，在服务器触发
     */
    public abstract void pressedServer(NetworkEvent.Context context);

    /***
     * 松开时，在服务器触发
     */
    public abstract void releaseServer(NetworkEvent.Context context);

    public static class KeyData {
        public String keyName = "";

        /***
         * 是按下去的，
         */
        public boolean isPressed;

        public KeyData() {
        }

        public KeyData(String keyName, boolean isPressed) {
            this.keyName = keyName;
            this.isPressed = isPressed;
        }

        public KeyData(ResourceLocation resourceLocation, boolean isPressed) {
            this(resourceLocation.toString(), isPressed);
        }
    }


}
