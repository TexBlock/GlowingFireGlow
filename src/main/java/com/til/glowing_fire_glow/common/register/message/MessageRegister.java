package com.til.glowing_fire_glow.common.register.message;


import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ReflexUtil;
import com.til.glowing_fire_glow.common.util.Util;
import com.til.glowing_fire_glow.common.util.gson.GsonManage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * @author til
 */
public abstract class MessageRegister<MSG> extends RegisterBasics {


    @VoluntarilyAssignment
    protected AllMessageRegister allMessageRegister;

    @VoluntarilyAssignment
    protected GsonManage gsonManage;

    protected int id;
    protected Class<MSG> msgClass;


    @Override
    public void init() {
        super.init();
        msgClass = initMsgType();
    }

    protected Class<MSG> initMsgType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualTypeArguments = parameterized.getActualTypeArguments()[0];
        return Util.forcedConversion(ReflexUtil.asClass(actualTypeArguments));
    }

    public void encoder(MSG msg, PacketBuffer friendlyByteBuf) {
        friendlyByteBuf.writeString(gsonManage.getGson().toJson(msg));
    }

    public MSG decoder(PacketBuffer friendlyByteBuf) {
        return gsonManage.getGson().fromJson(friendlyByteBuf.readString(), msgClass);
    }

    /***
     * 消息的调用
     * @param msg 消息
     * @param supplier 运行序列
     */
    protected abstract void messageConsumer(MSG msg, Supplier<NetworkEvent.Context> supplier);

    public void sendToServer(MSG msg) {
        allMessageRegister.INSTANCE.sendToServer(msg);
    }

    public void sendToPlayerClient(MSG msg, ServerPlayerEntity player) {
        allMessageRegister.INSTANCE.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public void registerMessage(SimpleChannel simpleChannel, int id) {
        this.id = id;
        simpleChannel.registerMessage(id, msgClass, this::encoder, this::decoder, (msg, context) -> {
            context.get().enqueueWork(() -> messageConsumer(msg, context));
            context.get().setPacketHandled(true);
        });
    }
}
