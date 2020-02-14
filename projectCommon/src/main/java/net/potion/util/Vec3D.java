package net.potion.util;

import java.util.ArrayList;
import java.util.List;

public class Vec3D {
    private static final List<Vec3D> VECTOR_POOL = new ArrayList<>();
    private static int nextVector = 0;

    public double xCoord;
    public double yCoord;
    public double zCoord;

    private Vec3D(double x, double y, double z) {
        if (x == -0.0D) {
            x = 0.0D;
        }

        if (y == -0.0D) {
            y = 0.0D;
        }

        if (z == -0.0D) {
            z = 0.0D;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public static Vec3D createVectorHelper(double x, double y, double z) {
        return new Vec3D(x, y, z);
    }

    public static void resetPool() {
        VECTOR_POOL.clear();
        nextVector = 0;
    }

    public static void initialize() {
        nextVector = 0;
    }

    public static Vec3D createVector(double x, double y, double z) {
        if (nextVector >= VECTOR_POOL.size()) {
            VECTOR_POOL.add(createVectorHelper(0.0D, 0.0D, 0.0D));
        }

        return VECTOR_POOL.get(nextVector++).setComponents(x, y, z);
    }

    private Vec3D setComponents(double x, double y, double z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        return this;
    }

    public Vec3D subtract(Vec3D vec) {
        return createVector(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public Vec3D normalize() {
        double d = MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return d < 1.0E-4D ? createVector(0.0D, 0.0D, 0.0D) : createVector(this.xCoord / d, this.yCoord / d, this.zCoord / d);
    }

    public Vec3D crossProduct(Vec3D vec) {
        return createVector(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3D addVector(double x, double y, double z) {
        return createVector(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public double distanceTo(Vec3D vec) {
        double difX = vec.xCoord - this.xCoord;
        double difY = vec.yCoord - this.yCoord;
        double difZ = vec.zCoord - this.zCoord;
        return MathHelper.sqrt(difX * difX + difY * difY + difZ * difZ);
    }

    public double squareDistanceTo(Vec3D vec) {
        double difX = vec.xCoord - this.xCoord;
        double difY = vec.yCoord - this.yCoord;
        double difZ = vec.zCoord - this.zCoord;
        return difX * difX + difY * difY + difZ * difZ;
    }

    public double squareDistanceTo(double x, double y, double z) {
        double difX = x - this.xCoord;
        double difY = y - this.yCoord;
        double difZ = z - this.zCoord;
        return difX * difX + difY * difY + difZ * difZ;
    }

    public double lengthVector() {
        return MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3D getIntermediateWithXValue(Vec3D vec, double var2) {
        double difX = vec.xCoord - this.xCoord;
        double difY = vec.yCoord - this.yCoord;
        double difZ = vec.zCoord - this.zCoord;

        if (difX * difX < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - this.xCoord) / difX;
        return var10 >= 0.0D && var10 <= 1.0D
                ? createVector(this.xCoord + difX * var10, this.yCoord + difY * var10, this.zCoord + difZ * var10)
                : null;
    }

    public Vec3D getIntermediateWithYValue(Vec3D vec, double var2) {
        double difX = vec.xCoord - this.xCoord;
        double difY = vec.yCoord - this.yCoord;
        double difZ = vec.zCoord - this.zCoord;

        if (difY * difY < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - this.yCoord) / difY;
        return var10 >= 0.0D && var10 <= 1.0D ? createVector(this.xCoord + difX * var10, this.yCoord + difY * var10, this.zCoord + difZ * var10) : null;
    }

    public Vec3D getIntermediateWithZValue(Vec3D vec, double var2) {
        double difX = vec.xCoord - this.xCoord;
        double difY = vec.yCoord - this.yCoord;
        double difZ = vec.zCoord - this.zCoord;

        if (difZ * difZ < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - this.zCoord) / difZ;
        return var10 >= 0.0D && var10 <= 1.0D ? createVector(this.xCoord + difX * var10, this.yCoord + difY * var10, this.zCoord + difZ * var10) : null;
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public void rotateAroundX(float rad) {
        float cos = MathHelper.cos(rad);
        float sin = MathHelper.sin(rad);
        double x = this.xCoord;
        double y = this.yCoord * (double) cos + this.zCoord * (double) sin;
        double z = this.zCoord * (double) cos - this.yCoord * (double) sin;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public void rotateAroundY(float rad) {
        float cos = MathHelper.cos(rad);
        float sin = MathHelper.sin(rad);
        double x = this.xCoord * (double) cos + this.zCoord * (double) sin;
        double y = this.yCoord;
        double z = this.zCoord * (double) cos - this.xCoord * (double) sin;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }
}
