package com.til.glowing_fire_glow.common.register.message;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterManage;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AllMessageRegister extends RegisterManage<MessageRegister<?>> {


    protected final SimpleChannel INSTANCE;
    protected final String PROTOCOL_VERSION = "1";

    public AllMessageRegister() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new Identifier(GlowingFireGlow.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    }


    @Override
    public void initCommonSetup() {
        super.initCommonSetup();
        List<MessageRegister<?>> messageRegisterList = registerMap.values()
                .stream()
                .sorted(Comparator.comparing(r -> r.getName().getNamespace()))
                .sorted((a, b) -> {
                    if (a.getName().getNamespace().equals(b.getName().getNamespace())) {
                        return a.getName().getPath().compareTo(b.getName().getPath());
                    }
                    return 0;
                })
                .collect(Collectors.toList());
        for (int i = 0; i < messageRegisterList.size(); i++) {
            messageRegisterList.get(i).registerMessage(INSTANCE, i);
        }
    }
}
