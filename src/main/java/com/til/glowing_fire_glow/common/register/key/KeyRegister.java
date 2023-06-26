package com.til.glowing_fire_glow.common.register.key;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraftforge.fml.network.NetworkEvent;

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
