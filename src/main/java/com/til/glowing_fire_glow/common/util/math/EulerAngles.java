package com.til.glowing_fire_glow.common.util.math;

public class EulerAngles {
    public final float pitch;
    public final float yaw;
    public final float roll;

    public EulerAngles(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public EulerAngles(float w, float x, float y, float z) {
        // roll (x-axis rotation)
        float sinr_cosp = 2 * (w * x + y * z);
        float cosr_cosp = 1 - 2 * (x * x + y * y);
        this.roll = (float) Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        float sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1) {
            this.pitch = Math.copySign(1.57075f, sinp); // use 90 degrees if out of range
        } else {
            this.pitch = (float) Math.asin(sinp);
        }

        // yaw (z-axis rotation)
        float siny_cosp = 2 * (w * z + x * y);
        float cosy_cosp = 1 - 2 * (y * y + z * z);
        this.yaw = (float) Math.atan2(siny_cosp, cosy_cosp);
    }
}
