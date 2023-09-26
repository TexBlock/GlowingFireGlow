package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.glowing_fire_glow.client.ClientTransfer;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.message.MessageRegister;
import com.til.glowing_fire_glow.common.synchronous.SynchronousData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@VoluntarilyRegister
public class SynchronousMessages extends MessageRegister<SynchronousData> {

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void messageConsumer(SynchronousData synchronousMessages, Supplier<NetworkEvent.Context> supplier) {
        ClientTransfer.messageConsumer(synchronousMessages);
    }

}
