package net.hypnosis.util.math;

import net.hypnosis.util.math.Direction.Axis;

import java.util.EnumSet;

public class Vec3d {
    public static final Vec3d ZERO = new Vec3d(0.0D, 0.0D, 0.0D);
    public final double x;
    public final double y;
    public final double z;

    public static Vec3d unpackRgb(int rgb) {
        double r = (double)(rgb >> 16 & 255) / 255.0D;
        double g = (double)(rgb >> 8 & 255) / 255.0D;
        double b = (double)(rgb & 255) / 255.0D;
        return new Vec3d(r, g, b);
    }

    public static Vec3d ofCenter(Vec3i vec) {
        return new Vec3d(vec.getX() + 0.5D, vec.getY() + 0.5D, vec.getZ() + 0.5D);
    }

    public static Vec3d of(Vec3i vec) {
        return new Vec3d(vec.getX(), vec.getY(), vec.getZ());
    }

    public static Vec3d ofBottomCenter(Vec3i vec) {
        return new Vec3d(vec.getX() + 0.5D, vec.getY(), vec.getZ() + 0.5D);
    }

    public static Vec3d ofCenter(Vec3i vec, double deltaY) {
        return new Vec3d(vec.getX() + 0.5D, vec.getY() + deltaY, vec.getZ() + 0.5D);
    }

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(double value) {
        this(value, value, value);
    }

    public Vec3d(Vec3f vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vec3d(Vec3d vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vec3d relativize(Vec3d vec) {
        return new Vec3d(vec.x - this.x, vec.y - this.y, vec.z - this.z);
    }

    public Vec3d normalize() {
        double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return d < 1.0E-4D ? ZERO : new Vec3d(this.x / d, this.y / d, this.z / d);
    }

    public double dotProduct(Vec3d vec) {
        return this.x * vec.x + this.y * vec.y + this.z * vec.z;
    }

    public Vec3d crossProduct(Vec3d vec) {
        return new Vec3d(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
    }

    public Vec3d subtract(Vec3d vec) {
        return this.subtract(vec.x, vec.y, vec.z);
    }

    public Vec3d subtract(double value) {
        return this.subtract(value, value, value);
    }

    public Vec3d subtract(double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    public Vec3d add(Vec3d vec) {
        return this.add(vec.x, vec.y, vec.z);
    }

    public Vec3d add(double value) {
        return add(value, value, value);
    }

    public Vec3d add(double x, double y, double z) {
        return new Vec3d(this.x + x, this.y + y, this.z + z);
    }

    public Vec3d divide(Vec3d vec) {
        return divide(vec.x, vec.y, vec.z);
    }

    public Vec3d divide(double value) {
        return divide(value, value, value);
    }

    public Vec3d divide(double x, double y, double z) {
        return new Vec3d(this.x / x, this.y / y, this.z / z);
    }

    public boolean isInRange(Position pos, double radius) {
        return this.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) < radius * radius;
    }

    public double distanceTo(Vec3d vec) {
        double d = vec.x - this.x;
        double e = vec.y - this.y;
        double f = vec.z - this.z;
        return Math.sqrt(d * d + e * e + f * f);
    }

    public double squaredDistanceTo(Vec3d vec) {
        double d = vec.x - this.x;
        double e = vec.y - this.y;
        double f = vec.z - this.z;
        return d * d + e * e + f * f;
    }

    public double squaredDistanceTo(double x, double y, double z) {
        double d = x - this.x;
        double e = y - this.y;
        double f = z - this.z;
        return d * d + e * e + f * f;
    }

    public Vec3d multiply(double value) {
        return this.multiply(value, value, value);
    }

    public Vec3d negate() {
        return this.multiply(-1.0D);
    }

    public Vec3d multiply(Vec3d vec) {
        return this.multiply(vec.x, vec.y, vec.z);
    }

    public Vec3d multiply(double x, double y, double z) {
        return new Vec3d(this.x * x, this.y * y, this.z * z);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double horizontalLength() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public double horizontalLengthSquared() {
        return this.x * this.x + this.z * this.z;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Vec3d vec3d)) {
            return false;
        } else {
            if (Double.compare(vec3d.x, this.x) != 0) {
                return false;
            } else if (Double.compare(vec3d.y, this.y) != 0) {
                return false;
            } else {
                return Double.compare(vec3d.z, this.z) == 0;
            }
        }
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this.x);
        int i = (int)(l ^ l >>> 32);
        l = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(l ^ l >>> 32);
        l = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(l ^ l >>> 32);
        return i;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public Vec3d lerp(Vec3d to, double delta) {
        return new Vec3d(MathHelper.lerp(delta, this.x, to.x), MathHelper.lerp(delta, this.y, to.y), MathHelper.lerp(delta, this.z, to.z));
    }

    public Vec3d rotateX(float angle) {
        float cos = MathHelper.cos(angle);
        float sin = MathHelper.sin(angle);

        double x = this.x;
        double y = this.y * cos + this.z * sin;
        double z = this.z * cos - this.y * sin;

        return new Vec3d(x, y, z);
    }

    public Vec3d rotateY(float angle) {
        float cos = MathHelper.cos(angle);
        float sin = MathHelper.sin(angle);

        double x = this.x * cos + this.z * sin;
        double y = this.y;
        double z = this.z * cos - this.x * sin;

        return new Vec3d(x, y, z);
    }

    public Vec3d rotateZ(float angle) {
        float cos = MathHelper.cos(angle);
        float sin = MathHelper.sin(angle);

        double x = this.x * (double)cos + this.y * (double)sin;
        double y = this.y * (double)cos - this.x * (double)sin;
        double z = this.z;

        return new Vec3d(x, y, z);
    }

    public static Vec3d fromPolar(Vec2f polar) {
        return fromPolar(polar.x, polar.y);
    }

    public static Vec3d fromPolar(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
        float g = MathHelper.sin(-yaw * MathConstants.RADIANS_PER_DEGREE - MathConstants.PI);
        float h = -MathHelper.cos(-pitch * MathConstants.RADIANS_PER_DEGREE);
        float i = MathHelper.sin(-pitch * MathConstants.RADIANS_PER_DEGREE);
        return new Vec3d(g * h, i, f * h);
    }

    public Vec3d floorAlongAxes(EnumSet<Axis> axes) {
        double d = axes.contains(Axis.X) ? (double)MathHelper.floor(this.x) : this.x;
        double e = axes.contains(Axis.Y) ? (double)MathHelper.floor(this.y) : this.y;
        double f = axes.contains(Axis.Z) ? (double)MathHelper.floor(this.z) : this.z;
        return new Vec3d(d, e, f);
    }

    public double getComponentAlongAxis(Axis axis) {
        return axis.choose(this.x, this.y, this.z);
    }

    public Vec3d withAxis(Axis axis, double value) {
        double d = axis == Axis.X ? value : this.x;
        double e = axis == Axis.Y ? value : this.y;
        double f = axis == Axis.Z ? value : this.z;
        return new Vec3d(d, e, f);
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }
}
