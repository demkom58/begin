package net.minecraft.util;

public class MathHelper {
    private static final float[] SIN_TABLE = new float[65536];

    static {
        for (int var0 = 0; var0 < 65536; ++var0) {
            SIN_TABLE[var0] = (float) Math.sin((double) var0 * 3.141592653589793D * 2.0D / 65536.0D);
        }
    }

    public static float sin(float radians) {
        return SIN_TABLE[(int) (radians * 10430.378F) & '\uffff'];
    }

    public static float cos(float radians) {
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
}
