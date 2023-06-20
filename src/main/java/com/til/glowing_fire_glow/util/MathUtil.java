package com.til.glowing_fire_glow.util;

public class MathUtil {
    public static int clamp(int a, int max, int min) {
        return a > max ? max : a < min ? max : a;
    }

    public static float clamp(float a, float max, float min) {
        return a > max ? max : a < min ? max : a;
    }

    public static int lerp(float t, int max, int min) {
        return (int) (min + (max - min) * clamp(t, 1, 0));
    }

    public static float lerp(float t, float max, float min) {
        return min + (max - min) * clamp(t, 1, 0);
    }

    public static double lerp(double t, double max, double min) {
        return min + (max - min) * clamp((float) t, 1, 0);
    }
}
