package net.hypnosis.util.math;

public class Vec3b {
    public static final Vec3b ZERO = new Vec3b(0, 0, 0);
    private byte x;
    private byte y;
    private byte z;


    public Vec3b(byte x, byte y, byte z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3b(int x, int y, int z) {
        this((byte) x, (byte) y, (byte) z);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Vec3b Vec3b)) {
            return false;
        } else {
            if (this.getX() != Vec3b.getX()) {
                return false;
            } else if (this.getY() != Vec3b.getY()) {
                return false;
            } else {
                return this.getZ() == Vec3b.getZ();
            }
        }
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(Vec3b Vec3b) {
        if (this.getY() == Vec3b.getY()) {
            return this.getZ() == Vec3b.getZ() ? this.getX() - Vec3b.getX() : this.getZ() - Vec3b.getZ();
        } else {
            return this.getY() - Vec3b.getY();
        }
    }

    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public byte getZ() {
        return this.z;
    }

    protected Vec3b setX(byte x) {
        this.x = x;
        return this;
    }

    protected Vec3b setY(byte y) {
        this.y = y;
        return this;
    }

    protected Vec3b setZ(byte z) {
        this.z = z;
        return this;
    }

    public Vec3b add(byte x, byte y, byte z) {
        return x == 0 && y == 0 && z == 0 ? this : new Vec3b(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public Vec3b add(Vec3b vec) {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vec3b subtract(Vec3b vec) {
        return this.add((byte) -vec.getX(), (byte) -vec.getY(), (byte) -vec.getZ());
    }

    public Vec3b multiply(byte scale) {
        if (scale == 1) {
            return this;
        } else {
            return scale == 0 ? ZERO : new Vec3b(this.getX() * scale, this.getY() * scale, this.getZ() * scale);
        }
    }

    public Vec3b up() {
        return this.up((byte) 1);
    }

    public Vec3b up(byte distance) {
        return this.offset(Direction.UP, distance);
    }

    public Vec3b down() {
        return this.down((byte) 1);
    }

    public Vec3b down(byte distance) {
        return this.offset(Direction.DOWN, distance);
    }

    public Vec3b north() {
        return this.north((byte) 1);
    }

    public Vec3b north(byte distance) {
        return this.offset(Direction.NORTH, distance);
    }

    public Vec3b south() {
        return this.south((byte) 1);
    }

    public Vec3b south(byte distance) {
        return this.offset(Direction.SOUTH, distance);
    }

    public Vec3b west() {
        return this.west(1);
    }

    public Vec3b west(int distance) {
        return this.offset(Direction.WEST, distance);
    }

    public Vec3b east() {
        return this.east(1);
    }

    public Vec3b east(int distance) {
        return this.offset(Direction.EAST, distance);
    }

    public Vec3b offset(Direction direction) {
        return this.offset(direction, 1);
    }

    public Vec3b offset(Direction direction, int distance) {
        return distance == 0 ? this : new Vec3b(this.getX() + direction.getOffsetX() * distance, this.getY() + direction.getOffsetY() * distance, this.getZ() + direction.getOffsetZ() * distance);
    }

    public Vec3b offset(Direction.Axis axis, int distance) {
        if (distance == 0) {
            return this;
        } else {
            int i = axis == Direction.Axis.X ? distance : 0;
            int j = axis == Direction.Axis.Y ? distance : 0;
            int k = axis == Direction.Axis.Z ? distance : 0;
            return new Vec3b(this.getX() + i, this.getY() + j, this.getZ() + k);
        }
    }

    public Vec3b crossProduct(Vec3b vec) {
        return new Vec3b(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public boolean isWithinDistance(Vec3b vec, double distance) {
        return this.getSquaredDistance(vec) < MathHelper.square(distance);
    }

    public boolean isWithinDistance(Position pos, double distance) {
        return this.getSquaredDistance(pos) < MathHelper.square(distance);
    }

    public double getSquaredDistance(Vec3b vec) {
        return this.getSquaredDistance(vec.getX(), vec.getY(), vec.getZ());
    }

    public double getSquaredDistance(Position pos) {
        return this.getSquaredDistanceFromCenter(pos.getX(), pos.getY(), pos.getZ());
    }

    public double getSquaredDistanceFromCenter(double x, double y, double z) {
        double d = this.getX() + 0.5D - x;
        double e = this.getY() + 0.5D - y;
        double f = this.getZ() + 0.5D - z;
        return d * d + e * e + f * f;
    }

    public double getSquaredDistance(double x, double y, double z) {
        double d = this.getX() - x;
        double e = this.getY() - y;
        double f = this.getZ() - z;
        return d * d + e * e + f * f;
    }

    public int getManhattanDistance(Vec3b vec) {
        float f = Math.abs(vec.getX() - this.getX());
        float g = Math.abs(vec.getY() - this.getY());
        float h = Math.abs(vec.getZ() - this.getZ());
        return (int) (f + g + h);
    }

    public int getComponentAlongAxis(Direction.Axis axis) {
        return axis.choose(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "Vec3b{" +
               "x=" + x +
               ", y=" + y +
               ", z=" + z +
               '}';
    }

    public String toShortString() {
        return this.getX() + ", " + this.getY() + ", " + this.getZ();
    }

}

