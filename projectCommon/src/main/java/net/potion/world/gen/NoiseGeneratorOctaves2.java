package net.potion.world.gen;

import java.util.Arrays;
import java.util.Random;

public class NoiseGeneratorOctaves2 extends NoiseGenerator {
    private final NoiseGenerator2[] gens;
    private final int length;

    public NoiseGeneratorOctaves2(Random random, int length) {
        this.length = length;
        this.gens = new NoiseGenerator2[length];

        for (int i = 0; i < length; ++i) {
            this.gens[i] = new NoiseGenerator2(random);
        }

    }

    public double[] noise(double[] noiseArr, double var2, double var4, int var6, int var7, double var8, double var10, double var12) {
        return this.noise(noiseArr, var2, var4, var6, var7, var8, var10, var12, 0.5D);
    }

    public double[] noise(double[] noiseArr, double var2, double var4, int var6, int var7, double var8, double var10, double var12, double var14) {
        var8 = var8 / 1.5D;
        var10 = var10 / 1.5D;
        if (noiseArr != null && noiseArr.length >= var6 * var7) {
            Arrays.fill(noiseArr, 0.0D);
        } else {
            noiseArr = new double[var6 * var7];
        }

        double var23 = 1.0D;
        double var18 = 1.0D;

        for (int i = 0; i < this.length; ++i) {
            this.gens[i].method2(noiseArr, var2, var4, var6, var7, var8 * var18, var10 * var18, 0.55D / var23);
            var18 *= var12;
            var23 *= var14;
        }

        return noiseArr;
    }
}
