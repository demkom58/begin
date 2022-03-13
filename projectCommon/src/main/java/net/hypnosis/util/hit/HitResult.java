package net.hypnosis.util.hit;


import net.hypnosis.util.math.Position;
import net.hypnosis.util.math.Vec3d;

public abstract class HitResult {
    protected final Vec3d pos;

    protected HitResult(Vec3d pos) {
        this.pos = pos;
    }

    public double squaredDistanceTo(Position entity) {
        double d = this.pos.x - entity.getX();
        double e = this.pos.y - entity.getY();
        double f = this.pos.z - entity.getZ();
        return d * d + e * e + f * f;
    }

    public abstract HitResult.Type getType();

    public Vec3d getPos() {
        return this.pos;
    }

    public enum Type {
        MISS,
        BLOCK,
        POSITIONABLE;

        Type() {
        }
    }
}
