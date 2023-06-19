package com.til.glowing_fire_glow.common.register.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.til.dusk.common.event.RegisterLangEvent;
import com.til.dusk.common.register.RegisterBasics;
import com.til.dusk.common.register.message.messages.KeyMessage;
import com.til.dusk.main.Dusk;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author til
 */
public abstract class KeyRegister extends RegisterBasics {

    protected int inputId;
    protected KeyMapping keyMapping;
    protected boolean lock = true;

    @Override
    public void init() {
        super.init();
        inputId = initInputId();
        keyMapping = new KeyMapping(name.toString(), KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputConstants.Type.KEYSYM, inputId, getName() + ".key");
    }


    protected abstract int initInputId();

    public void run(Supplier<NetworkEvent.Context> supplier) {
        MinecraftForge.EVENT_BUS.post(new EventKey(this, supplier));
    }

    @SubscribeEvent
    protected void registerKeyMapping(RegisterKeyMappingsEvent registerKeyMappingsEvent) {
        registerKeyMappingsEvent.register(keyMapping);
    }

    @SubscribeEvent
    protected void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (keyMapping.isDown()) {
            if (lock) {
                lock = false;
                Dusk.instance.getReflexManage().getRegisterBasicsOfClass(KeyMessage.class).sendToServer(new KeyData(name.toString()));
            }
        } else {
            lock = true;
        }
    }


    public static class KeyData {
        public String keyName = "";

        public KeyData() {
        }

        public KeyData(String keyName) {
            this.keyName = keyName;
        }

        public KeyData(ResourceLocation resourceLocation) {
            this(resourceLocation.toString());
        }
    }


}
