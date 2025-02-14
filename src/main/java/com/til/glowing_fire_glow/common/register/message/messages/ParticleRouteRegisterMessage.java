package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.client.ClientTransfer;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.message.MessageRegister;
import com.til.glowing_fire_glow.common.register.particle_register.AllParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.ParticleRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleRouteData;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.EmptyParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RoutePack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author til
 */
@VoluntarilyRegister
public class ParticleRouteRegisterMessage extends MessageRegister<ParticleRouteData> {
    @VoluntarilyAssignment
    protected AllParticleRegister allParticleRegister;

    @VoluntarilyAssignment
    protected EmptyParticleRegister emptyParticleRegister;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void messageConsumer(ParticleRouteData routeData, Supplier<NetworkEvent.Context> supplier) {
        ClientTransfer.messageConsumer(routeData);
    }

    @Override
    public void encoder(ParticleRouteData data, PacketByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeString(data.particleRegister.toString());
        friendlyByteBuf.writeInt(data.color.length);
        for (int i = 0; i < data.color.length; i++) {
            friendlyByteBuf.writeInt(data.color[i].getRGB());
        }
        friendlyByteBuf.writeInt(data.route.size());
        for (List<RoutePack.RouteCell<Double>> routeCells : data.route) {
            friendlyByteBuf.writeInt(routeCells.size());
            for (RoutePack.RouteCell<Double> routeCell : routeCells) {
                routeCell.start.write(friendlyByteBuf);
                routeCell.end.write(friendlyByteBuf);
                friendlyByteBuf.writeDouble(routeCell.data);
            }
        }
        friendlyByteBuf.writeBoolean(data.Identifier != null);
        if (data.Identifier != null) {
            friendlyByteBuf.writeString(data.Identifier.toString());
        }
    }

    @Override
    public ParticleRouteData decoder(PacketByteBuf friendlyByteBuf) {
        Identifier type = new Identifier(friendlyByteBuf.readString());
        ParticleRegister particleRegister = allParticleRegister.get(type);
        GlowingFireGlowColor[] color = new GlowingFireGlowColor[friendlyByteBuf.readInt()];
        for (int i = 0; i < color.length; i++) {
            color[i] = new GlowingFireGlowColor(friendlyByteBuf.readInt());
        }
        int l = friendlyByteBuf.readInt();
        List<List<RoutePack.RouteCell<Double>>> pack = new ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            int ll = friendlyByteBuf.readInt();
            List<RoutePack.RouteCell<Double>> data = new ArrayList<>(ll);
            for (int ii = 0; ii < ll; ii++) {
                data.add(new RoutePack.RouteCell<>(new Pos(friendlyByteBuf), new Pos(friendlyByteBuf), friendlyByteBuf.readDouble()));
            }
            pack.add(data);
        }
        Identifier Identifier = null;
        if (friendlyByteBuf.readBoolean()) {
            Identifier = new Identifier(friendlyByteBuf.readString());
        }
        if (particleRegister == null) {
            GlowingFireGlow.LOGGER.warn("错误的粒子效果{}", type);
            particleRegister = emptyParticleRegister;
        }
        return new ParticleRouteData(pack, particleRegister, color, Identifier);
    }


}
