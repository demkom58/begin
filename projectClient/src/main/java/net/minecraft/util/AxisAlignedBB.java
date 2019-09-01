package net.minecraft.util;

import java.util.ArrayList;
import java.util.List;

public class AxisAlignedBB {
    private static List<AxisAlignedBB> boundingBoxes = new ArrayList<>();
    private static int numBoundingBoxesInUse = 0;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    private AxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public static AxisAlignedBB getBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static void func_28196_a() {
        boundingBoxes.clear();
        numBoundingBoxesInUse = 0;
    }

    public static void clearBoundingBoxPool() {
        numBoundingBoxesInUse = 0;
    }

    public static AxisAlignedBB getBoundingBoxFromPool(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (numBoundingBoxesInUse >= boundingBoxes.size()) {
            boundingBoxes.add(getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D));
        }

        return boundingBoxes.get(numBoundingBoxesInUse++).setBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public AxisAlignedBB addCoord(double x, double y, double z) {
        double minX = this.minX;
        double minY = this.minY;
        double minZ = this.minZ;

        double maxX = this.maxX;
        double maxY = this.maxY;
        double maxZ = this.maxZ;

        if (x < 0.0D) {
            minX += x;
        }

        if (x > 0.0D) {
            maxX += x;
        }

        if (y < 0.0D) {
            minY += y;
        }

        if (y > 0.0D) {
            maxY += y;
        }

        if (z < 0.0D) {
            minZ += z;
        }

        if (z > 0.0D) {
            maxZ += z;
        }

        return getBoundingBoxFromPool(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB expand(double x, double y, double z) {
        double minX = this.minX - x;
        double minY = this.minY - y;
        double minZ = this.minZ - z;

        double maxX = this.maxX + x;
        double maxY = this.maxY + y;
        double maxZ = this.maxZ + z;

        return getBoundingBoxFromPool(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB getOffsetBoundingBox(double x, double y, double z) {
        return getBoundingBoxFromPool(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public double calculateXOffset(AxisAlignedBB axis, double off) {
        if (axis.maxY > this.minY && axis.minY < this.maxY) {
            if (axis.maxZ > this.minZ && axis.minZ < this.maxZ) {
                if (off > 0.0D && axis.maxX <= this.minX) {
                    double var4 = this.minX - axis.maxX;
                    if (var4 < off) {
                        off = var4;
                    }
                }

                if (off < 0.0D && axis.minX >= this.maxX) {
                    double var6 = this.maxX - axis.minX;
                    if (var6 > off) {
                        off = var6;
                    }
                }

                return off;
            }

            return off;
        }

        return off;
    }

    public double calculateYOffset(AxisAlignedBB axis, double off) {
        if (axis.maxX > this.minX && axis.minX < this.maxX) {
            if (axis.maxZ > this.minZ && axis.minZ < this.maxZ) {
                if (off > 0.0D && axis.maxY <= this.minY) {
                    double var4 = this.minY - axis.maxY;
                    if (var4 < off) {
                        off = var4;
                    }
                }

                if (off < 0.0D && axis.minY >= this.maxY) {
                    double var6 = this.maxY - axis.minY;
                    if (var6 > off) {
                        off = var6;
                    }
                }

                return off;
            }

            return off;
        }

        return off;
    }

    public double calculateZOffset(AxisAlignedBB axis, double off) {
        if (axis.maxX > this.minX && axis.minX < this.maxX) {
            if (axis.maxY > this.minY && axis.minY < this.maxY) {
                if (off > 0.0D && axis.maxZ <= this.minZ) {
                    double var4 = this.minZ - axis.maxZ;
                    if (var4 < off) {
                        off = var4;
                    }
                }

                if (off < 0.0D && axis.minZ >= this.maxZ) {
                    double var6 = this.maxZ - axis.minZ;
                    if (var6 > off) {
                        off = var6;
                    }
                }

                return off;
            }

            return off;
        }

        return off;
    }

    public boolean intersectsWith(AxisAlignedBB axis) {
        if (axis.maxX > this.minX && axis.minX < this.maxX) {
            if (axis.maxY > this.minY && axis.minY < this.maxY) {
                return axis.maxZ > this.minZ && axis.minZ < this.maxZ;
            }

            return false;
        }

        return false;
    }

    public AxisAlignedBB offset(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public boolean isVecInXYZ(Vec3D vec) {
        if (vec.xCoord > this.minX && vec.xCoord < this.maxX) {
            if (vec.yCoord > this.minY && vec.yCoord < this.maxY) {
                return vec.zCoord > this.minZ && vec.zCoord < this.maxZ;
            }

            return false;
        }

        return false;
    }

    public double getAverageEdgeLength() {
        double var1 = this.maxX - this.minX;
        double var3 = this.maxY - this.minY;
        double var5 = this.maxZ - this.minZ;
        return (var1 + var3 + var5) / 3.0D;
    }

    public AxisAlignedBB getInsetBoundingBox(double x, double y, double z) {
        double minX = this.minX + x;
        double minY = this.minY + y;
        double minZ = this.minZ + z;

        double maxX = this.maxX - x;
        double maxY = this.maxY - y;
        double maxZ = this.maxZ - z;

        return getBoundingBoxFromPool(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AxisAlignedBB copy() {
        return getBoundingBoxFromPool(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public MovingObjectPosition func_706_a(Vec3D vec1, Vec3D vec2) {
        Vec3D var3 = vec1.getIntermediateWithXValue(vec2, this.minX);
        Vec3D var4 = vec1.getIntermediateWithXValue(vec2, this.maxX);
        Vec3D var5 = vec1.getIntermediateWithYValue(vec2, this.minY);
        Vec3D var6 = vec1.getIntermediateWithYValue(vec2, this.maxY);
        Vec3D var7 = vec1.getIntermediateWithZValue(vec2, this.minZ);
        Vec3D var8 = vec1.getIntermediateWithZValue(vec2, this.maxZ);

        if (!this.isVecInYZ(var3)) {
            var3 = null;
        }

        if (!this.isVecInYZ(var4)) {
            var4 = null;
        }

        if (!this.isVecInXZ(var5)) {
            var5 = null;
        }

        if (!this.isVecInXZ(var6)) {
            var6 = null;
        }

        if (!this.isVecInXY(var7)) {
            var7 = null;
        }

        if (!this.isVecInXY(var8)) {
            var8 = null;
        }

        Vec3D var9 = null;
        if (var3 != null && (var9 == null || vec1.squareDistanceTo(var3) < vec1.squareDistanceTo(var9))) {
            var9 = var3;
        }

        if (var4 != null && (var9 == null || vec1.squareDistanceTo(var4) < vec1.squareDistanceTo(var9))) {
            var9 = var4;
        }

        if (var5 != null && (var9 == null || vec1.squareDistanceTo(var5) < vec1.squareDistanceTo(var9))) {
            var9 = var5;
        }

        if (var6 != null && (var9 == null || vec1.squareDistanceTo(var6) < vec1.squareDistanceTo(var9))) {
            var9 = var6;
        }

        if (var7 != null && (var9 == null || vec1.squareDistanceTo(var7) < vec1.squareDistanceTo(var9))) {
            var9 = var7;
        }

        if (var8 != null && (var9 == null || vec1.squareDistanceTo(var8) < vec1.squareDistanceTo(var9))) {
            var9 = var8;
        }

        if (var9 == null) {
            return null;
        } else {
            byte var10 = -1;
            if (var9 == var3) {
                var10 = 4;
            }

            if (var9 == var4) {
                var10 = 5;
            }

            if (var9 == var5) {
                var10 = 0;
            }

            if (var9 == var6) {
                var10 = 1;
            }

            if (var9 == var7) {
                var10 = 2;
            }

            if (var9 == var8) {
                var10 = 3;
            }

            return new MovingObjectPosition(0, 0, 0, var10, var9);
        }
    }

    private boolean isVecInYZ(Vec3D vec) {
        if (vec == null)
            return false;

        return vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
    }

    private boolean isVecInXZ(Vec3D vec) {
        if (vec == null)
            return false;

        return vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
    }

    private boolean isVecInXY(Vec3D vec) {
        if (vec == null)
            return false;

        return vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
    }

    public void setBB(AxisAlignedBB axis) {
        this.minX = axis.minX;
        this.minY = axis.minY;
        this.minZ = axis.minZ;
        this.maxX = axis.maxX;
        this.maxY = axis.maxY;
        this.maxZ = axis.maxZ;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}
