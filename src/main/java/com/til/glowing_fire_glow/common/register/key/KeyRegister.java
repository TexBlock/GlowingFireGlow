package com.til.glowing_fire_glow.common.register.key;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.message.messages.KeyMessage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

/**
 * @author til
 */
public abstract class KeyRegister extends RegisterBasics {

    /***
     * 当按键按下去时，在服务器触发
     */
    public abstract void pressedServer(NetworkEvent.Context context);

    /***
     * 松开时，在服务器触发
     */
    public abstract void releaseServer(NetworkEvent.Context context);


}
