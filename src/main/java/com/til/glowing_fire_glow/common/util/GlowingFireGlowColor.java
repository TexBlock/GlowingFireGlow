package com.til.glowing_fire_glow.common.util;

/***
 * 一个颜色类，他代替{@linkplain java.awt.Color}
 * @author til
 */
public class GlowingFireGlowColor {

    public static final GlowingFireGlowColor DEFAULT = new GlowingFireGlowColor(255, 255, 255, 255);

    public final int value;

    public GlowingFireGlowColor(int rgb) {
        value = 0xff000000 | rgb;
    }

    public GlowingFireGlowColor(int rgba, boolean hasAlpha) {
        if (hasAlpha) {
            value = rgba;
        } else {
            value = 0xff000000 | rgba;
        }
    }

    public GlowingFireGlowColor(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF));
    }

    public GlowingFireGlowColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public GlowingFireGlowColor(float r, float g, float b) {
        this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5));
    }

    public GlowingFireGlowColor(float r, float g, float b, float a) {
        this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
    }

    public GlowingFireGlowColor() {
        this(0, 0, 0);
    }


    public int getRed() {
        return (getRGB() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getRGB() >> 8) & 0xFF;
    }


    public int getBlue() {
        return (getRGB()) & 0xFF;
    }


    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    public int getRGB() {
        return value;
    }

    public GlowingFireGlowColor blend(GlowingFireGlowColor baseColor) {
        return new GlowingFireGlowColor(getRed() / 255.0f * baseColor.getRed() / 255.0f,
                getGreen() / 255.0f * baseColor.getGreen() / 255.0f,
                getBlue() / 255.0f * baseColor.getBlue() / 255.0f,
                getAlpha() / 255.0f);
    }

    public static GlowingFireGlowColor lerp(GlowingFireGlowColor min, GlowingFireGlowColor max, float t) {
        return new GlowingFireGlowColor(
                (int) MathUtil.lerp(t, max.getRed(), min.getRed()),
                (int) MathUtil.lerp(t, max.getGreen(), min.getGreen()),
                (int) MathUtil.lerp(t, max.getBlue(), min.getBlue()),
                (int) MathUtil.lerp(t, max.getAlpha(), min.getAlpha())
        );
    }
}
