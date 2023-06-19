package com.til.glowing_fire_glow.common.register.particle_register.data;

public enum ParticleParsingMode {
    /***
     * 拼接，根据坐标数组(0,1)(1,2)
     */
    SPELL,
    /***
     * 配对(0,1)(2,3)
     */
    PAIR,
    /***
     * 单个位置一次
     */
    SINGLE

}
