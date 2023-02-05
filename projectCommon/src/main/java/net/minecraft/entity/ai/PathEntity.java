package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.hypnosis.util.math.Vec3d;

public class PathEntity {
    public final int pathLength;
    private final PathPoint[] points;
    private int pathIndex;

    public PathEntity(PathPoint[] points) {
        this.points = points;
        this.pathLength = points.length;
    }

    public void incrementPathIndex() {
        ++this.pathIndex;
    }

    public boolean isFinished() {
        return this.pathIndex >= this.points.length;
    }

    public PathPoint getTargetPoint() {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    public Vec3d getPosition(Entity entity) {
        double x = (double) this.points[this.pathIndex].xCoord + (double) ((int) (entity.width + 1.0F)) * 0.5D;
        double y = this.points[this.pathIndex].yCoord;
        double z = (double) this.points[this.pathIndex].zCoord + (double) ((int) (entity.width + 1.0F)) * 0.5D;
        return new Vec3d(x, y, z);
    }
}
