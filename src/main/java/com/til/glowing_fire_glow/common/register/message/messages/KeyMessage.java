package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.key.AllKeyRegister;
import com.til.glowing_fire_glow.common.register.key.EventKey;
import com.til.glowing_fire_glow.common.register.key.KeyRegister;
import com.til.glowing_fire_glow.common.register.key.KeyData;
import com.til.glowing_fire_glow.common.register.message.MessageRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author til
 */
@VoluntarilyRegister
public class KeyMessage extends MessageRegister<KeyData> {

    @VoluntarilyAssignment
    protected AllKeyRegister allKeyRegister;

    @Override
    public void messageConsumer(KeyData keyData, Supplier<NetworkEvent.Context> supplier) {
        ResourceLocation resourceLocation = new ResourceLocation(keyData.keyName);
        KeyRegister keyRegister = allKeyRegister.get(resourceLocation);
        if (keyRegister == null) {
            GlowingFireGlow.LOGGER.error("在服务端不存按键{}", keyData.keyName);
            return;
        }
        MinecraftForge.EVENT_BUS.post(new EventKey(keyRegister, keyData.keyState, supplier));
        switch (keyData.keyState) {
            case PRESSED:
                keyRegister.pressedServer(supplier.get());
                break;
            case RELEASE:
                keyRegister.releaseServer(supplier.get());
                break;
        }
    }
}
