package com.til.glowing_fire_glow.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;

import java.util.Random;

public class Pos {

    public final double x;
    public final double y;
    public final double z;

    public Pos() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Pos(Vector3d vec3i) {
        this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public Pos(Vector3f vec3i) {
        this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public Pos(Vector3i vec3i) {
        this(vec3i.getX() + 0.5, vec3i.getY() + 0.5, vec3i.getZ() + 0.5);
    }

    public Pos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Pos(Entity entity) {
        this.x = entity.getPosX();
        this.y = entity.getPosY() + entity.getEyeHeight();
        this.z = entity.getPosZ();
    }

    public Pos(Pos pos) {
        this(pos.x, pos.y, pos.z);
    }

    public Pos(TileEntity tileEntity) {
        this(tileEntity.getPos());
    }

    public Pos(double rotationYaw, double rotationPitch) {
        double fYawDtoR = (rotationYaw / 180d) * Math.PI;
        double fPitDtoR = (rotationPitch / 180d) * Math.PI;
        x = -Math.sin(fYawDtoR) * Math.cos(fPitDtoR);
        y = -Math.sin(fPitDtoR);
        z = Math.cos(fYawDtoR) * Math.cos(fPitDtoR);
    }

    public Pos(CompoundNBT nbtTagCompound) {
        x = nbtTagCompound.getDouble("x");
        y = nbtTagCompound.getDouble("y");
        z = nbtTagCompound.getDouble("z");
    }

    public Pos(ByteBuf friendlyByteBuf) {
        x = friendlyByteBuf.readDouble();
        y = friendlyByteBuf.readDouble();
        z = friendlyByteBuf.readDouble();
    }

    public static AxisAlignedBB asAABB(Pos posMax, Pos posMin) {
        return new AxisAlignedBB(posMax.vector3d(), posMin.vector3d());
    }

    public void write(ByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeDouble(x);
        friendlyByteBuf.writeDouble(y);
        friendlyByteBuf.writeDouble(z);
    }

    public CompoundNBT getNBT() {
        return writeNBT(new CompoundNBT());
    }

    public CompoundNBT writeNBT(CompoundNBT nbtTagCompound) {
        nbtTagCompound.putDouble("x", x);
        nbtTagCompound.putDouble("y", y);
        nbtTagCompound.putDouble("z", z);
        return nbtTagCompound;
    }

    public double getAngle(Pos vec) {
        return Math.acos(normalize().dotProduct(vec.normalize()));
    }

    public double dotProduct(Pos vec) {
        double d = vec.x * x + vec.y * y + vec.z * z;

        if (d > 1 && d < 1.00001) {
            d = 1;
        } else if (d < -1 && d > -1.00001) {
            d = -1;
        }
        return d;
    }


    public Pos addX(double x) {
        return new Pos(this.x + x, y, z);
    }

    public Pos addY(double y) {
        return new Pos(x, this.y + y, z);
    }

    public Pos addZ(double z) {
        return new Pos(x, y, this.z + z);
    }

    public Pos move(Pos pos) {
        return new Pos(x + pos.x, y + pos.y, z + pos.z);
    }

    public Pos move(double x, double y, double z) {
        return new Pos(this.x + x, this.y + y, this.z + z);
    }

    public AxisAlignedBB axisAlignedBB(double dAmbit) {
        return new AxisAlignedBB(x - dAmbit, y - dAmbit, z - dAmbit, x + dAmbit,
                y + dAmbit, z + dAmbit);
    }

    public float distance(Pos pos) {
        double f = this.x - pos.x;
        double f1 = this.y - pos.y;
        double f2 = this.z - pos.z;
        return (float) Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public Pos perpendicular() {
        if (z == 0) {
            return zCrossProduct();
        }
        return xCrossProduct();
    }

    public Pos xCrossProduct() {
        double d1 = -y;
        return new Pos(0, z, d1);
    }

    public Pos zCrossProduct() {
        double d1 = -x;
        return new Pos(y, d1, 0);
    }

    public Pos normalize() {
        double d = mag();
        if (d != 0) {
            return multiply(1 / d);
        }

        return this;
    }

    public Pos multiply(double d) {
        return multiply(d, d, d);
    }

    public Pos multiply(double fx, double fy, double fz) {
        return new Pos(x * fx, y * fy, z * fz);
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public float distance(Entity entity) {
        double f = this.x - entity.getPosX();
        double f1 = this.y - entity.getPosY() + entity.getEyeHeight();
        double f2 = this.z - entity.getPosZ();
        return (float) Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public BlockPos blockPos() {
        return new BlockPos(x, y, z);
    }

    public Vector3f vector3f() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public Vector3d vector3d() {
        return new Vector3d(x, y, z);
    }

    public Vector3i vector3i() {
        return new Vector3i(x, y, z);
    }

    public static final Random rand = new Random();
    public static final Pos POS0 = new Pos(0, 0, 0);

    public static Pos randomPos() {
        return new Pos(rand.nextBoolean() ? rand.nextDouble() : -rand.nextDouble(),
                rand.nextBoolean() ? rand.nextDouble() : -rand.nextDouble(),
                rand.nextBoolean() ? rand.nextDouble() : -rand.nextDouble());
    }

    public static Pos randomPos(double x, double y, double z) {
        return new Pos(rand.nextBoolean() ? rand.nextDouble() * x : -(rand.nextDouble() * x),
                rand.nextBoolean() ? rand.nextDouble() * y : -(rand.nextDouble() * y),
                rand.nextBoolean() ? rand.nextDouble() * z : -(rand.nextDouble() * z));
    }

    public static Pos randomPos(double x, double y, double z, double xMin, double yMin, double zMin) {
        return new Pos((rand.nextBoolean() ? rand.nextDouble() * x : -(rand.nextDouble() * x)) + xMin,
                (rand.nextBoolean() ? rand.nextDouble() * y : -(rand.nextDouble() * y)) + yMin,
                (rand.nextBoolean() ? rand.nextDouble() * z : -(rand.nextDouble() * z)) + zMin);
    }

    public static Pos coordinateDifference(Pos pos1, Pos pos2) {
        return new Pos(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
    }

    public static Pos movePos(Pos pos1, Pos pos2, double tick) {
        if (tick <= 0) {
            return POS0;
        }
        Pos pos = coordinateDifference(pos1, pos2);
        return new Pos(pos.x / tick, pos.y / tick, pos.z / tick);
    }

    public Pos coordinateDifference(Pos pos) {
        return new Pos(pos.x - this.x, pos.y - this.y, pos.z - this.z);
    }

    public Pos movePos(Pos pos, double tick) {
        if (tick <= 0) {
            return POS0;
        }
        Pos p = coordinateDifference(pos);
        return new Pos(p.x / tick, p.y / tick, p.z / tick);
    }

    public Pos limtX(double d) {
        d = Math.abs(d);
        double xx = x;
        if (Math.abs(x) > d) {
            if (x > 0) {
                xx = d;
            } else {
                xx = -d;
            }
        }
        return new Pos(xx, y, z);
    }

    public Pos limtY(double d) {
        d = Math.abs(d);
        double yy = y;
        if (Math.abs(y) > d) {
            if (y > 0) {
                yy = d;
            } else {
                yy = -d;
            }
        }
        return new Pos(x, yy, z);
    }

    public Pos limtZ(double d) {
        d = Math.abs(d);
        double zz = z;
        if (Math.abs(z) > d) {
            if (z > 0) {
                zz = d;
            } else {
                zz = -d;
            }
        }
        return new Pos(x, y, zz);
    }

    public Pos toLimit(double d) {
        d = Math.abs(d);
        double xx = x;
        double yy = y;
        double zz = z;
        if (Math.abs(x) > d) {
            if (x > 0) {
                xx = d;
            } else {
                xx = -d;
            }
        }

        if (Math.abs(y) > d) {
            if (y > 0) {
                yy = d;
            } else {
                yy = -d;
            }
        }

        if (Math.abs(z) > d) {
            if (z > 0) {
                zz = d;
            } else {
                zz = -d;
            }
        }

        return new Pos(xx, yy, zz);
    }

    public Pos crossProduct(Pos vec) {
        double d = y * vec.z - z * vec.y;
        double d1 = z * vec.x - x * vec.z;
        double d2 = x * vec.y - y * vec.x;
        return new Pos(d, d1, d2);
    }

    public Pos toNegative() {
        return new Pos(-x, -y, -z);
    }

    public Pos negativeX() {
        return new Pos(-x, y, z);
    }

    public Pos negativeY() {
        return new Pos(x, -y, z);
    }

    public Pos toNegativeZ() {
        return new Pos(x, y, -z);
    }


    /***
     * 返回相量
     */
    public Pos phasor() {
        return new Pos(Math.atan2(this.x, this.y) * 180 / Math.PI, (Math.atan2(this.y, getSqrt()) * 180.0D / Math.PI), 0);
    }

    public double getSqrt() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public Pos lerp(Pos old, float l) {
        return new Pos(x + (old.x - x) / l,
                y + (old.y - y) / l,
                z + (old.z - z) / l);
    }

    /***
     * 返回向量
     */
    public static Pos getVector(double rotationYaw, double rotationPitch) {
        double fYawDtoR = (rotationYaw / 180d) * Math.PI;
        double fPitDtoR = (rotationPitch / 180d) * Math.PI;
        return new Pos(-Math.sin(fYawDtoR) * Math.cos(fPitDtoR), -Math.sin(fPitDtoR), Math.cos(fYawDtoR) * Math.cos(fPitDtoR));
    }

    /***
     * 返回向量
     */
    public Pos getVector() {
        return getVector(x, y);
    }

    public Pos toVector() {
        return getVector(x, y);
    }

    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("x", new JsonPrimitive(x));
        jsonObject.add("y", new JsonPrimitive(y));
        jsonObject.add("z", new JsonPrimitive(z));
        return jsonObject;
    }

    public Pos(JsonObject jsonObject) {
        if (jsonObject.has("x")) {
            x = jsonObject.get("x").getAsDouble();
        } else {
            x = 0;
        }
        if (jsonObject.has("y")) {
            y = jsonObject.get("y").getAsDouble();
        } else {
            y = 0;
        }
        if (jsonObject.has("z")) {
            z = jsonObject.get("z").getAsDouble();
        } else {
            z = 0;
        }
    }

    public boolean isIn(AxisAlignedBB aabb) {
        return aabb.maxX > x && aabb.minX < x &&
               aabb.maxY > y && aabb.minY < y &&
               aabb.maxZ > z && aabb.minZ < z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Pos) {
            Pos pos = (Pos) obj;
            return pos.x == x && pos.y == y && pos.z == z;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }

}
