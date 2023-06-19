package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.dusk.common.register.key.AllKeyRegister;
import com.til.dusk.common.register.key.KeyRegister;
import com.til.dusk.common.register.message.MessageRegister;
import com.til.dusk.main.Dusk;
import com.til.dusk.main.world_component.ReflexManage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class KeyMessage extends MessageRegister<KeyRegister.KeyData> {
    @Override
    public void messageConsumer(KeyRegister.KeyData keyData, Supplier<NetworkEvent.Context> supplier) {
        ResourceLocation resourceLocation = new ResourceLocation(keyData.keyName);
        KeyRegister keyRegister =reflexManage.getRegisterManage(AllKeyRegister.class).get(resourceLocation);
        if (keyRegister == null) {
            Dusk.instance.logger.error("在服务端不存按键{}", keyData.keyName);
            return;
        }
        keyRegister.run(supplier);
    }
}
