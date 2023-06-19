package com.til.glowing_fire_glow.common.register.message.messages;

import com.til.dusk.client.ClientTransfer;
import com.til.dusk.common.register.message.MessageRegister;
import com.til.dusk.common.register.particle_register.data.ParticleRouteData;
import com.til.dusk.main.world_component.ReflexManage;
import com.til.dusk.util.DuskColor;
import com.til.dusk.util.Pos;
import com.til.dusk.util.RoutePack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author til
 */
@ReflexManage.VoluntarilyRegister
public class ParticleRouteRegisterMessage extends MessageRegister<ParticleRouteData> {
    @Override
    public void encoder(ParticleRouteData data, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(data.type().toString());
        friendlyByteBuf.writeInt(data.color().getRGB());
        friendlyByteBuf.writeInt(data.route().size());
        for (List<RoutePack.RouteCell<Double>> routeCells : data.route()) {
            friendlyByteBuf.writeInt(routeCells.size());
            for (RoutePack.RouteCell<Double> routeCell : routeCells) {
                routeCell.start().write(friendlyByteBuf);
                routeCell.end().write(friendlyByteBuf);
                friendlyByteBuf.writeDouble(routeCell.data());
            }
        }
        friendlyByteBuf.writeBoolean(data.resourceLocation() != null);
        if (data.resourceLocation() != null) {
            friendlyByteBuf.writeUtf(data.resourceLocation().toString());
        }
    }

    @Override
    public ParticleRouteData decoder(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation type = new ResourceLocation(friendlyByteBuf.readUtf());
        DuskColor color = new DuskColor(friendlyByteBuf.readInt());
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
        ResourceLocation resourceLocation = null;
        if (friendlyByteBuf.readBoolean()) {
            resourceLocation = new ResourceLocation(friendlyByteBuf.readUtf());
        }
        return new ParticleRouteData(pack, type, color, resourceLocation);
    }

    @Override
    public void messageConsumer(ParticleRouteData routeData, Supplier<NetworkEvent.Context> supplier) {
        ClientTransfer.messageConsumer(routeData, supplier);
    }
}
