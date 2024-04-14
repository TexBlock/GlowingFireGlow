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
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void messageConsumer(ParticleData data, Supplier<NetworkEvent.Context> supplier) {
        ClientTransfer.messageConsumer(data);
    }

    @Override
    public void encoder(ParticleData data, PacketByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeString(data.particleRegister.toString());
        friendlyByteBuf.writeInt(data.color.length);
        for (int i = 0; i < data.color.length; i++) {
            friendlyByteBuf.writeInt(data.color[i].getRGB());
        }
        friendlyByteBuf.writeDouble(data.density);
        friendlyByteBuf.writeInt(data.pos.length);
        for (Pos po : data.pos) {
            po.write(friendlyByteBuf);
        }
        friendlyByteBuf.writeBoolean(data.Identifier != null);
        if (data.Identifier != null) {
            friendlyByteBuf.writeString(data.Identifier.toString());
        }
    }

    @Override
    public ParticleData decoder(PacketByteBuf friendlyByteBuf) {
        Identifier type = new Identifier(friendlyByteBuf.readString());
        ParticleRegister particleRegister = allParticleRegister.get(type);
        GlowingFireGlowColor[] color = new GlowingFireGlowColor[friendlyByteBuf.readInt()];
        for (int i = 0; i < color.length; i++) {
            color[i] = new GlowingFireGlowColor(friendlyByteBuf.readInt());
        }
        double density = friendlyByteBuf.readDouble();
        int l = friendlyByteBuf.readInt();
        Pos[] pos = new Pos[l];
        for (int i = 0; i < l; i++) {
            pos[i] = new Pos(friendlyByteBuf);
        }
        Identifier Identifier = null;
        if (friendlyByteBuf.readBoolean()) {
            Identifier = new Identifier(friendlyByteBuf.readString());
        }
        if (particleRegister == null) {
            GlowingFireGlow.LOGGER.warn("错误的粒子效果{}", type);
            particleRegister = emptyParticleRegister;
        }
        return new ParticleData(particleRegister, color, density, Identifier, pos);
    }
}
