package com.til.glowing_fire_glow.common.util.math;

import net.minecraft.util.math.vector.Vector3d;

public class Quaternion {
    public final float w;
    public final float x;
    public final float y;
    public final float z;

    public Quaternion() {
        w = 0;
        x = 0;
        y = 0;
        z = 0;
    }

    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(float pitch, float yaw, float roll) {
        float cy = (float) Math.cos(yaw * 0.5f);
        float sy = (float) Math.sin(yaw * 0.5f);
        float cp = (float) Math.cos(pitch * 0.5f);
        float sp = (float) Math.sin(pitch * 0.5f);
        float cr = (float) Math.cos(roll * 0.5f);
        float sr = (float) Math.sin(roll * 0.5f);
        w = cy * cp * cr + sy * sp * sr;
        x = cy * cp * sr - sy * sp * cr;
        y = sy * cp * sr + cy * sp * cr;
        z = sy * cp * cr - cy * sp * sr;
    }

    //向量旋转
    public Vector3d vectorRotation(Vector3d vector3d) {
        Quaternion qv = new Quaternion(0, (float) vector3d.getX(), (float) vector3d.getY(), (float) vector3d.getZ());
        qv = this.multiplication(qv);
        qv = qv.multiplication(this.inverse());
        return new Vector3d(qv.x, qv.y, qv.z);
    }


    //返回欧拉角
    public EulerAngles toEulerAngles() {
        // roll (x-axis rotation)
        return new EulerAngles(this.w, this.x, this.y, this.z);
    }

    //四元数相乘
    public Quaternion multiplication(Quaternion q) {
        float w = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
        float x = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
        float y = this.w * q.y + this.y * q.w + this.z * q.x - this.x * q.z;
        float z = this.w * q.z + this.z * q.w + this.x * q.y - this.y * q.x;
        return new Quaternion(w, x, y, z);
    }

    //四元数求逆
    public Quaternion inverse() {
        return new Quaternion(w, -x, -y, -z);
    }
}
