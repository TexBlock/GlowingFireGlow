package com.til.glowing_fire_glow.common.register.message;

import com.google.gson.Gson;
import com.til.dusk.common.register.RegisterManage;
import com.til.dusk.main.Dusk;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterManage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class AllMessageRegister extends RegisterManage<MessageRegister<?>> {


    protected final SimpleChannel INSTANCE;
    protected final String PROTOCOL_VERSION = "1";

    public AllMessageRegister() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(GlowingFireGlow.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    }


}
