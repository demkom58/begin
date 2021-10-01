package net.potion.util;

import org.joml.Vector3d;

public class MathHelper {
    private static final float[] SIN_TABLE = new float[65536];

    static {
        double total = 65536;
        for (int i = 0; i < total; ++i)
            SIN_TABLE[i] = (float) Math.sin((double) i * Math.PI * 2.0D / total);
    }

    public static float sin(float radians) {
        return SIN_TABLE[(int) (radians * 10430.378F) & '\uffff'];
    }

    public static float sin(double radians) {
        return SIN_TABLE[(int) (radians * 10430.378F) & '\uffff'];
    }

    public static float cos(float radians) {
        return SIN_TABLE[(int) (radians * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float cos(double radians) {
        return SIN_TABLE[(int) (radians * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float sqrt(float var0) {
        return (float) Math.sqrt(var0);
    }

    public static float sqrt(double var0) {
        return (float) Math.sqrt(var0);
    }

    public static int floor(float var0) {
        int var1 = (int) var0;
        return var0 < (float) var1 ? var1 - 1 : var1;
    }

    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < (double) var2 ? var2 - 1 : var2;
    }

    public static float abs(float var0) {
        return var0 >= 0.0F ? var0 : -var0;
    }

    public static double absMax(double var0, double var2) {
        if (var0 < 0.0D) {
            var0 = -var0;
        }

        if (var2 < 0.0D) {
            var2 = -var2;
        }

        return var0 > var2 ? var0 : var2;
    }

    public static int bucketInt(int var0, int var1) {
        return var0 < 0 ? -((-var0 - 1) / var1) - 1 : var0 / var1;
    }

    public static boolean stringNullOrLengthZero(String var0) {
        return var0 == null || var0.length() == 0;
    }

    public static Vector3d getIntermediateWithXValue(Vector3d vec1, Vector3d vec2, double var2) {
        double difX = vec2.x - vec1.x;
        double difY = vec2.y - vec1.y;
        double difZ = vec2.z - vec1.z;

        if (difX * difX < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - vec1.x) / difX;
        return var10 >= 0.0D && var10 <= 1.0D
                ? new Vector3d(vec1.x + difX * var10, vec1.y + difY * var10, vec1.z + difZ * var10)
                : null;
    }

    public static Vector3d getIntermediateWithYValue(Vector3d vec1, Vector3d vec2, double var2) {
        double difX = vec2.x - vec1.x;
        double difY = vec2.y - vec1.y;
        double difZ = vec2.z - vec1.z;

        if (difY * difY < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - vec1.y) / difY;
        return var10 >= 0.0D && var10 <= 1.0D
                ? new Vector3d(vec1.x + difX * var10, vec1.y + difY * var10, vec1.z + difZ * var10)
                : null;
    }

    public static Vector3d getIntermediateWithZValue(Vector3d vec1, Vector3d vec2, double var2) {
        double difX = vec2.x - vec1.x;
        double difY = vec2.y - vec1.y;
        double difZ = vec2.z - vec1.z;

        if (difZ * difZ < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - vec1.z) / difZ;
        return var10 >= 0.0D && var10 <= 1.0D
                ? new Vector3d(vec1.x + difX * var10, vec1.y + difY * var10, vec1.z + difZ * var10)
                : null;
    }

}
