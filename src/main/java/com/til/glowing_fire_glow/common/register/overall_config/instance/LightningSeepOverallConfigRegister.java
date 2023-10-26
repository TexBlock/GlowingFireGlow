package com.til.glowing_fire_glow.common.register.overall_config.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class LightningSeepOverallConfigRegister extends OverallConfigRegister {
    @ConfigField
    protected float seep;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        seep = 2;
    }

    public float getSeep() {
        return seep;
    }
}
