package com.til.glowing_fire_glow.common.register.message.messages;


import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.client.ClientTransfer;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.message.MessageRegister;
import com.til.glowing_fire_glow.common.register.particle_register.AllParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleData;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.EmptyParticleRegister;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author til
 */
@VoluntarilyRegister
public class ParticleMessage extends MessageRegister<ParticleData> {

    @VoluntarilyAssignment
    protected AllParticleRegister allParticleRegister;

    @VoluntarilyAssignment
    protected EmptyParticleRegister emptyParticleRegister;

    @Override
    public void messageConsumer(ParticleData data, Supplier<NetworkEvent.Context> supplier) {
        ClientTransfer.messageConsumer(data);
    }

    @Override
    public void encoder(ParticleData data, PacketBuffer friendlyByteBuf) {
        friendlyByteBuf.writeString(data.particleRegister.toString());
        friendlyByteBuf.writeInt(data.color.getRGB());
        friendlyByteBuf.writeDouble(data.density);
        friendlyByteBuf.writeInt(data.pos.length);
        for (Pos po : data.pos) {
            po.write(friendlyByteBuf);
        }
        friendlyByteBuf.writeBoolean(data.resourceLocation != null);
        if (data.resourceLocation != null) {
            friendlyByteBuf.writeString(data.resourceLocation.toString());
        }
    }

    @Override
    public ParticleData decoder(PacketBuffer friendlyByteBuf) {
        ResourceLocation type = new ResourceLocation(friendlyByteBuf.readString());
        ParticleRegister particleRegister = allParticleRegister.get(type);
        GlowingFireGlowColor color = new GlowingFireGlowColor(friendlyByteBuf.readInt());
        double density = friendlyByteBuf.readDouble();
        int l = friendlyByteBuf.readInt();
        Pos[] pos = new Pos[l];
        for (int i = 0; i < l; i++) {
            pos[i] = new Pos(friendlyByteBuf);
        }
        ResourceLocation resourceLocation = null;
        if (friendlyByteBuf.readBoolean()) {
            resourceLocation = new ResourceLocation(friendlyByteBuf.readString());
        }
        if (particleRegister == null) {
            GlowingFireGlow.LOGGER.warn("错误的粒子效果{}", type);
            particleRegister = emptyParticleRegister;
        }
        return new ParticleData(particleRegister, color, density, resourceLocation, pos);
    }
}
