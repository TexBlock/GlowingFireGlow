package com.til.glowing_fire_glow.common.register.message;


import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.util.ReflexUtil;
import com.til.glowing_fire_glow.util.Util;
import com.til.glowing_fire_glow.util.gson.ConfigGson;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/**
 * @author til
 */
public abstract class MessageRegister<MSG> extends RegisterBasics {


    protected static int idSequence;

    @VoluntarilyAssignment
    protected AllMessageRegister allMessageRegister;

    protected int id;
    protected Class<MSG> msgClass;


    @Override
    public void init() {
        super.init();
        id = idSequence++;
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


    @Override
    public void initBack() {
        allMessageRegister.INSTANCE.registerMessage(id, msgClass, this::encoder, this::decoder, (msg, context) -> {
            context.get().enqueueWork(() -> messageConsumer(msg, context));
            context.get().setPacketHandled(true);
        });
    }

    public void encoder(MSG msg, PacketBuffer friendlyByteBuf) {
        friendlyByteBuf.writeString(ConfigGson.getGson().toJson(msg));
    }

    public MSG decoder(PacketBuffer friendlyByteBuf) {
        return ConfigGson.getGson().fromJson(friendlyByteBuf.readString(), msgClass);
    }

    /***
     * 消息的调用
     * @param msg 消息
     * @param supplier 运行序列
     */
    public abstract void messageConsumer(MSG msg, Supplier<NetworkEvent.Context> supplier);

    public void sendToServer(MSG msg) {
        allMessageRegister.INSTANCE.sendToServer(msg);
    }

    public void sendToPlayerClient(MSG msg, ServerPlayerEntity player) {
        allMessageRegister.INSTANCE.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }


}
