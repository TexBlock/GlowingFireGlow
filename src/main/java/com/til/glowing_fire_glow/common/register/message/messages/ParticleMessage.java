package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.dusk.client.ClientTransfer;
import com.til.dusk.common.register.message.MessageRegister;
import com.til.dusk.common.register.particle_register.data.ParticleData;
import com.til.dusk.main.world_component.ReflexManage;
import com.til.dusk.util.DuskColor;
import com.til.dusk.util.Pos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class ParticleMessage extends MessageRegister<ParticleData> {
    @Override
    public void messageConsumer(ParticleData data, Supplier<NetworkEvent.Context> supplier) {
        ClientTransfer.messageConsumer(data, supplier);
    }

    @Override
    public void encoder(ParticleData data, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(data.type().toString());
        friendlyByteBuf.writeInt(data.color().getRGB());
        friendlyByteBuf.writeDouble(data.density());
        friendlyByteBuf.writeInt(data.pos().length);
        for (Pos po : data.pos()) {
            po.write(friendlyByteBuf);
        }
        friendlyByteBuf.writeBoolean(data.resourceLocation() != null);
        if (data.resourceLocation() != null) {
            friendlyByteBuf.writeUtf(data.resourceLocation().toString());
        }
    }

    @Override
    public ParticleData decoder(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation type = new ResourceLocation(friendlyByteBuf.readUtf());
        DuskColor color = new DuskColor(friendlyByteBuf.readInt());
        double density = friendlyByteBuf.readDouble();
        int l = friendlyByteBuf.readInt();
        Pos[] pos = new Pos[l];
        for (int i = 0; i < l; i++) {
            pos[i] = new Pos(friendlyByteBuf);
        }
        ResourceLocation resourceLocation = null;
        if (friendlyByteBuf.readBoolean()) {
            resourceLocation = new ResourceLocation(friendlyByteBuf.readUtf());
        }
        return new ParticleData(type, color, density, resourceLocation, pos);
    }
}
