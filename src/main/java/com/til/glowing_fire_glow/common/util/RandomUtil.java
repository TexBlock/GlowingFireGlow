package com.til.glowing_fire_glow.common.util;

import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

public class RandomUtil {

    public static Vector3d nextVector3d(Random random) {
        return new Vector3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize();
    }

    public static Vector3d nextVector3dInCircles(Random random, double maxDistance) {
        return nextVector3dOnCircles(random, random.nextDouble() * maxDistance);
    }

    public static Vector3d nextVector3dOnCircles(Random random, double distance) {
        double theta = random.nextDouble() * Math.PI;
        double phi = random.nextDouble() * 2 * Math.PI;
        double x = distance * Math.sin(theta) * Math.cos(phi);
        double y = distance * Math.sin(theta) * Math.sin(phi);
        double z = distance * Math.cos(theta);
        return new Vector3d(x, y, z);
    }

}
