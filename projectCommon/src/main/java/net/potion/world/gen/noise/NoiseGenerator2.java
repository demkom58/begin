package net.potion.world.gen.noise;

import java.util.Random;

public class NoiseGenerator2 {
    private static final double field_4294_f = 0.5D * (Math.sqrt(3.0D) - 1.0D);
    private static final double field_4293_g = (3.0D - Math.sqrt(3.0D)) / 6.0D;
    private static final int[][] field_4296_d = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    public double r1;
    public double r2;
    public double r3;
    private final int[] values;

    public NoiseGenerator2() {
        this(new Random());
    }

    public NoiseGenerator2(Random random) {
        this.values = new int[512];
        this.r1 = random.nextDouble() * 256.0D;
        this.r2 = random.nextDouble() * 256.0D;
        this.r3 = random.nextDouble() * 256.0D;

        for (int i = 0; i < 256; this.values[i] = i++);

        for (int i = 0; i < 256; ++i) {
            int rnd = random.nextInt(256 - i) + i;
            int var4 = this.values[i];
            this.values[i] = this.values[rnd];
            this.values[rnd] = var4;
            this.values[i + 256] = this.values[i];
        }

    }

    private static int wrap(double var0) {
        return var0 > 0.0D ? (int) var0 : (int) var0 - 1;
    }

    private static double method1(int[] var0, double var1, double var3) {
        return (double) var0[0] * var1 + (double) var0[1] * var3;
    }

    public void method2(double[] noiseArr, double var2, double var4, int var6, int var7, double var8, double var10, double var12) {
        int var14 = 0;

        for (int var15 = 0; var15 < var6; ++var15) {
            double var16 = (var2 + (double) var15) * var8 + this.r1;

            for (int var18 = 0; var18 < var7; ++var18) {
                double var19 = (var4 + (double) var18) * var10 + this.r2;
                double var27 = (var16 + var19) * field_4294_f;
                int var29 = wrap(var16 + var27);
                int var30 = wrap(var19 + var27);
                double var31 = (double) (var29 + var30) * field_4293_g;
                double var33 = (double) var29 - var31;
                double var35 = (double) var30 - var31;
                double var37 = var16 - var33;
                double var39 = var19 - var35;
                byte var41;
                byte var42;
                if (var37 > var39) {
                    var41 = 1;
                    var42 = 0;
                } else {
                    var41 = 0;
                    var42 = 1;
                }

                double var43 = var37 - (double) var41 + field_4293_g;
                double var45 = var39 - (double) var42 + field_4293_g;
                double var47 = var37 - 1.0D + 2.0D * field_4293_g;
                double var49 = var39 - 1.0D + 2.0D * field_4293_g;
                int var51 = var29 & 255;
                int var52 = var30 & 255;
                int var53 = this.values[var51 + this.values[var52]] % 12;
                int var54 = this.values[var51 + var41 + this.values[var52 + var42]] % 12;
                int var55 = this.values[var51 + 1 + this.values[var52 + 1]] % 12;
                double var56 = 0.5D - var37 * var37 - var39 * var39;
                double var21;
                if (var56 < 0.0D) {
                    var21 = 0.0D;
                } else {
                    var56 = var56 * var56;
                    var21 = var56 * var56 * method1(field_4296_d[var53], var37, var39);
                }

                double var58 = 0.5D - var43 * var43 - var45 * var45;
                double var23;
                if (var58 < 0.0D) {
                    var23 = 0.0D;
                } else {
                    var58 = var58 * var58;
                    var23 = var58 * var58 * method1(field_4296_d[var54], var43, var45);
                }

                double var60 = 0.5D - var47 * var47 - var49 * var49;
                double var25;
                if (var60 < 0.0D) {
                    var25 = 0.0D;
                } else {
                    var60 = var60 * var60;
                    var25 = var60 * var60 * method1(field_4296_d[var55], var47, var49);
                }

                int var10001 = var14++;
                noiseArr[var10001] += 70.0D * (var21 + var23 + var25) * var12;
            }
        }

    }
}
