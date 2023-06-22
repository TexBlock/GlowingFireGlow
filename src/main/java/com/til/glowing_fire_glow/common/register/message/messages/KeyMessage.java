package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.key.AllKeyRegister;
import com.til.glowing_fire_glow.common.register.key.KeyRegister;
import com.til.glowing_fire_glow.common.register.message.MessageRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author til
 */
@VoluntarilyRegister
public class KeyMessage extends MessageRegister<KeyRegister.KeyData> {

    @VoluntarilyAssignment
    protected AllKeyRegister allKeyRegister;

    @Override
    public void messageConsumer(KeyRegister.KeyData keyData, Supplier<NetworkEvent.Context> supplier) {
        ResourceLocation resourceLocation = new ResourceLocation(keyData.keyName);
        KeyRegister keyRegister = allKeyRegister.get(resourceLocation);
        if (keyRegister == null) {
            GlowingFireGlow.LOGGER.error("在服务端不存按键{}", keyData.keyName);
            return;
        }
        keyRegister.run(supplier);
        if (keyData.isPressed) {
            keyRegister.pressedServer(supplier.get());
        } else {
            keyRegister.releaseServer(supplier.get());
        }
    }
}
