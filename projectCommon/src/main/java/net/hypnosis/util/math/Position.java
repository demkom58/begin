package net.hypnosis.util.math;

public interface Position {
    double getX();

    double getY();

    double getZ();

    default Vec3d getPos() {
        return new Vec3d(getX(), getY(), getZ());
    }
}
