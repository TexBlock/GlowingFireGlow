package com.til.glowing_fire_glow.util.math;

public class NumberPack {

    protected double basics;
    protected double multiply;

    public NumberPack(double basics, double multiply) {
        this.basics = basics;
        this.multiply = multiply;
    }

    public double of(int level) {
        return basics + level * multiply;
    }
}
