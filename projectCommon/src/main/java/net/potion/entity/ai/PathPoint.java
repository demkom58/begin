package net.potion.entity.ai;

import net.hypnosis.util.math.MathHelper;

public class PathPoint {
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    private final int hash;
    public boolean isFirst = false;
    int index = -1;
    float totalPathDistance;
    float distanceToNext;
    float distanceToTarget;
    PathPoint previous;

    public PathPoint(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.hash = hash(x, y, z);
    }

    public static int hash(int x, int y, int z) {
        return y & 255 | (x & 32767) << 8 | (z & 32767) << 24 | (x < 0 ? Integer.MIN_VALUE : 0) | (z < 0 ? 32768 : 0);
    }

    public float distanceTo(PathPoint var1) {
        float var2 = (float) (var1.xCoord - this.xCoord);
        float var3 = (float) (var1.yCoord - this.yCoord);
        float var4 = (float) (var1.zCoord - this.zCoord);
        return MathHelper.sqrt(var2 * var2 + var3 * var3 + var4 * var4);
    }

    public boolean equals(Object var1) {
        if (!(var1 instanceof PathPoint)) {
            return false;
        } else {
            PathPoint var2 = (PathPoint) var1;
            return this.hash == var2.hash && this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord;
        }
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean isAssigned() {
        return this.index >= 0;
    }

    public String toString() {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }
}
