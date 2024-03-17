package com.til.glowing_fire_glow.common.register.key;

import net.minecraft.util.Identifier;

public class KeyData {
    public String keyName = "";

    /***
     * 是按下去的，
     */
    public KeyState keyState;

    public KeyData() {
    }

    public KeyData(String keyName, KeyState keyState) {
        this.keyName = keyName;
        this.keyState = keyState;
    }

    public KeyData(Identifier Identifier, KeyState keyState) {
        this(Identifier.toString(), keyState);
    }

    public enum KeyState {

        /***
         * 被按下时
         */
        PRESSED,

        /***
         * 松开时
         */
        RELEASE;
    }
}
