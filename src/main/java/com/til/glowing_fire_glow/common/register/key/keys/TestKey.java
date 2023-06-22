package com.til.glowing_fire_glow.common.register.key.keys;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.key.EventKey;
import com.til.glowing_fire_glow.common.register.key.KeyRegister;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.BlockParticleRegister;
import com.til.glowing_fire_glow.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.util.Pos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

@VoluntarilyRegister
public class TestKey extends KeyRegister {


    @VoluntarilyAssignment
    protected BlockParticleRegister blockParticleRegister;

    @Override
    protected int initInputId() {
        return GLFW.GLFW_NO_API;
    }

    @Override
    protected void pressed() {

    }

    @Override
    protected void release() {

    }

    @Override
    public void pressedServer(NetworkEvent.Context context) {
        ServerPlayerEntity serverPlayerEntity =context.getSender();
        if (serverPlayerEntity == null) {
            return;
        }
        Pos lockPos = new Pos(serverPlayerEntity);
        blockParticleRegister.add(serverPlayerEntity, new GlowingFireGlowColor(255, 255, 255, 255), 10, null, lockPos);
    }

    @Override
    public void releaseServer(NetworkEvent.Context context) {

    }
}
