package net.hypnosis.util.math;

public class MathHelper {
    private static final float[] SIN_TABLE = new float[65536];
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};

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

    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    public static float sqrt(double a) {
        return (float) Math.sqrt(a);
    }

    public static int floor(float a) {
        int aInt = (int) a;
        return a < (float) aInt ? aInt - 1 : aInt;
    }

    public static int floor(double a) {
        int aInt = (int) a;
        return a < (double) aInt ? aInt - 1 : aInt;
    }


    public static long lfloor(double value) {
        long l = (long)value;
        return value < (double)l ? l - 1L : l;
    }

    public static float abs(float a) {
        return a >= 0.0F ? a : -a;
    }

    public static int abs(int a) {
        return a >= 0 ? a : -a;
    }

    public static double absMax(double a1, double a2) {
        if (a1 < 0.0D) {
            a1 = -a1;
        }

        if (a2 < 0.0D) {
            a2 = -a2;
        }

        return Math.max(a1, a2);
    }

    public static int bucketInt(int var0, int var1) {
        return var0 < 0 ? -((-var0 - 1) / var1) - 1 : var0 / var1;
    }

    public static boolean stringNullOrLengthZero(String var0) {
        return var0 == null || var0.length() == 0;
    }

    public static Vec3d getIntermediateWithXValue(Vec3d vec1, Vec3d vec2, double var2) {
        double difX = vec2.x - vec1.x;
        double difY = vec2.y - vec1.y;
        double difZ = vec2.z - vec1.z;

        if (difX * difX < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - vec1.x) / difX;
        return var10 >= 0.0D && var10 <= 1.0D
                ? new Vec3d(vec1.x + difX * var10, vec1.y + difY * var10, vec1.z + difZ * var10)
                : null;
    }

    public static Vec3d getIntermediateWithYValue(Vec3d vec1, Vec3d vec2, double var2) {
        double difX = vec2.x - vec1.x;
        double difY = vec2.y - vec1.y;
        double difZ = vec2.z - vec1.z;

        if (difY * difY < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - vec1.y) / difY;
        return var10 >= 0.0D && var10 <= 1.0D
                ? new Vec3d(vec1.x + difX * var10, vec1.y + difY * var10, vec1.z + difZ * var10)
                : null;
    }

    public static Vec3d getIntermediateWithZValue(Vec3d vec1, Vec3d vec2, double var2) {
        double difX = vec2.x - vec1.x;
        double difY = vec2.y - vec1.y;
        double difZ = vec2.z - vec1.z;

        if (difZ * difZ < 1.0000000116860974E-7D)
            return null;

        double var10 = (var2 - vec1.z) / difZ;
        return var10 >= 0.0D && var10 <= 1.0D
                ? new Vec3d(vec1.x + difX * var10, vec1.y + difY * var10, vec1.z + difZ * var10)
                : null;
    }

    public static Vec3d normalizeOrZero(Vec3d vec) {
        return normalizeOrZero(vec, 1.0E-4D);
    }

    public static Vec3d normalizeOrZero(Vec3d vec, double minValue) {
        double d = MathHelper.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
        return d < minValue ? new Vec3d(0) : vec.divide(d);
    }

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static double lerp2(double deltaX, double deltaY,
                               double x0y0, double x1y0,
                               double x0y1, double x1y1) {
        return lerp(deltaY, lerp(deltaX, x0y0, x1y0), lerp(deltaX, x0y1, x1y1));
    }

    public static double lerp3(double deltaX, double deltaY, double deltaZ,
                               double x0y0z0, double x1y0z0, double x0y1z0,
                               double x1y1z0, double x0y0z1, double x1y0z1,
                               double x0y1z1, double x1y1z1) {
        return lerp(deltaZ, lerp2(deltaX, deltaY, x0y0z0, x1y0z0, x0y1z0, x1y1z0), lerp2(deltaX, deltaY, x0y0z1, x1y0z1, x0y1z1, x1y1z1));
    }

    public static double perlinFade(double value) {
        return value * value * value * (value * (value * 6.0D - 15.0D) + 10.0D);
    }

    public static double perlinFadeDerivative(double value) {
        return 30.0D * value * value * (value - 1.0D) * (value - 1.0D);
    }

    public static int sign(double value) {
        if (value == 0.0D) {
            return 0;
        } else {
            return value > 0.0D ? 1 : -1;
        }
    }

    public static float fastInverseSqrt(float x) {
        float f = 0.5F * x;
        int i = Float.floatToIntBits(x);
        i = 1597463007 - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= 1.5F - f * x * x;
        return x;
    }

    public static double fastInverseSqrt(double x) {
        double d = 0.5D * x;
        long l = Double.doubleToRawLongBits(x);
        l = 6910469410427058090L - (l >> 1);
        x = Double.longBitsToDouble(l);
        x *= 1.5D - d * x * x;
        return x;
    }

    public static float fastInverseCbrt(float x) {
        int i = Float.floatToIntBits(x);
        i = 1419967116 - i / 3;
        float f = Float.intBitsToFloat(i);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * x);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * x);
        return f;
    }


    public static byte clamp(byte value, byte min, byte max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static double clampedLerp(double start, double end, double delta) {
        if (delta < 0.0D) {
            return start;
        } else {
            return delta > 1.0D ? end : lerp(delta, start, end);
        }
    }

    public static float clampedLerp(float start, float end, float delta) {
        if (delta < 0.0F) {
            return start;
        } else {
            return delta > 1.0F ? end : lerp(delta, start, end);
        }
    }

    public static double getLerpProgress(double value, double start, double end) {
        return (value - start) / (end - start);
    }

    public static float getLerpProgress(float value, float start, float end) {
        return (value - start) / (end - start);
    }


    public static double clampedLerpFromProgress(double lerpValue, double lerpStart, double lerpEnd, double start, double end) {
        return clampedLerp(start, end, getLerpProgress(lerpValue, lerpStart, lerpEnd));
    }

    public static float clampedLerpFromProgress(float lerpValue, float lerpStart, float lerpEnd, float start, float end) {
        return clampedLerp(start, end, getLerpProgress(lerpValue, lerpStart, lerpEnd));
    }

    public static double lerpFromProgress(double lerpValue, double lerpStart, double lerpEnd, double start, double end) {
        return lerp(getLerpProgress(lerpValue, lerpStart, lerpEnd), start, end);
    }

    public static float lerpFromProgress(float lerpValue, float lerpStart, float lerpEnd, float start, float end) {
        return lerp(getLerpProgress(lerpValue, lerpStart, lerpEnd), start, end);
    }

    public static float square(float n) {
        return n * n;
    }

    public static double square(double n) {
        return n * n;
    }

    public static int square(int n) {
        return n * n;
    }

    public static long square(long n) {
        return n * n;
    }

    public static int smallestEncompassingPowerOfTwo(int value) {
        int i = value - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }


    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int ceilLog2(int value) {
        value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)value * 125613361L >> 27) & 31];
    }

    public static int floorLog2(int value) {
        return ceilLog2(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static int packRgb(float r, float g, float b) {
        return packRgb(floor(r * 255.0F), floor(g * 255.0F), floor(b * 255.0F));
    }

    public static int packRgb(int r, int g, int b) {
        int i = (r << 8) + g;
        i = (i << 8) + b;
        return i;
    }

    public static int multiplyColors(int a, int b) {
        int i = (a & 16711680) >> 16;
        int j = (b & 16711680) >> 16;
        int k = (a & '\uff00') >> 8;
        int l = (b & '\uff00') >> 8;
        int m = (a & 255) >> 0;
        int n = (b & 255) >> 0;
        int o = (int)((float)i * (float)j / 255.0F);
        int p = (int)((float)k * (float)l / 255.0F);
        int q = (int)((float)m * (float)n / 255.0F);
        return a & -16777216 | o << 16 | p << 8 | q;
    }

    public static int multiplyColors(int color, float r, float g, float b) {
        int i = (color & 16711680) >> 16;
        int j = (color & '\uff00') >> 8;
        int k = (color & 255) >> 0;
        int l = (int)((float)i * r);
        int m = (int)((float)j * g);
        int n = (int)((float)k * b);
        return color & -16777216 | l << 16 | m << 8 | n;
    }

    public static float fractionalPart(float value) {
        return value - (float)floor(value);
    }

    public static double fractionalPart(double value) {
        return value - (double)lfloor(value);
    }
}
