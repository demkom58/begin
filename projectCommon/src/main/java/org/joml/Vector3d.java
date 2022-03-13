package org.joml;

import net.potion.util.MathHelper;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public static void initialize() {

    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3d set(double i) {
        this.x = y = z = i;
        return this;
    }

    public Vector3d div(double d) {
        this.x /= d;
        this.y /= d;
        this.z /= d;
        return this;
    }

    public Vector3d add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public double distance(Vector3d to) {
        return Math.sqrt(distanceSquared(to));
    }

    public double distanceSquared(Vector3d to) {
        return distanceSquared(to.x, to.y, to.z);
    }


    public double distanceSquared(double x, double y, double z) {
        return (x * x - this.x * this.x)
                + (y * y - this.y * this.y)
                + (z * z - this.z * this.z);
    }

    public Vector3d sub(Vector3d vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public void rotateX(float angle) {
        float cos = MathHelper.cos(angle);
        float sin = MathHelper.sin(angle);

        double y = this.y * cos + this.z * sin;
        double z = this.z * cos - this.y * sin;

        this.y = y;
        this.z = z;
    }

    public void rotateY(float angle) {
        float cos = MathHelper.cos(angle);
        float sin = MathHelper.sin(angle);

        double x = this.x * cos + this.z * sin;
        double z = this.z * cos - this.x * sin;

        this.x = x;
        this.z = z;
    }

    public void rotateZ(float angle) {
        float cos = MathHelper.cos(angle);
        float sin = MathHelper.sin(angle);

        double x = this.x * cos + this.y * sin;
        double y = this.y * cos - this.x * sin;

        this.x = x;
        this.y = y;
    }

    public Vector3d cross(Vector3d vec) {
        double x = this.x;
        double y = this.y;
        double z = this.z;

        this.x = y * vec.z - z * vec.y;
        this.y = z * vec.x - x * vec.z;
        this.z = x * vec.y - y * vec.x;

        return this;
    }
}
