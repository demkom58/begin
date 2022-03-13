package net.hypnosis.util.hit;

import net.hypnosis.util.math.Position;
import net.hypnosis.util.math.Vec3d;

public class PositionableHitResult <T extends Position> extends HitResult {
    private final T target;

    public PositionableHitResult(T target) {
        this(target, target.getPos());
    }

    public PositionableHitResult(T target, Vec3d pos) {
        super(pos);
        this.target = target;
    }

    public T getTarget() {
        return this.target;
    }

    @Override
    public Type getType() {
        return Type.POSITIONABLE;
    }
}
