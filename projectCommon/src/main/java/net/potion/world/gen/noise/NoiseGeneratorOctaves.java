package net.potion.world.gen.noise;

import java.util.Arrays;
import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator {
    private final NoiseGeneratorPerlin[] generators;
    private final int octaves;

    public NoiseGeneratorOctaves(Random random, int octaves) {
        this.octaves = octaves;
        this.generators = new NoiseGeneratorPerlin[octaves];

        for (int i = 0; i < octaves; ++i) {
            this.generators[i] = new NoiseGeneratorPerlin(random);
        }

    }

    public double noise(double x, double y) {
        double vals = 0.0D;
        double mul = 1.0D;

        for (int i = 0; i < this.octaves; ++i) {
            vals += this.generators[i].method2(x * mul, y * mul) / mul;
            mul /= 2.0D;
        }

        return vals;
    }

    public double[] generateNoiseOctaves(double[] values, double x, double y, double z, int width, int height, int length, double var11, double var13, double var15) {
        if (values == null) {
            values = new double[width * height * length];
        } else {
            Arrays.fill(values, 0.0D);
        }

        double m = 1.0D;
        for (int i = 0; i < this.octaves; ++i) {
            this.generators[i].method3(values, x, y, z, width, height, length, var11 * m, var13 * m, var15 * m, m);
            m /= 2.0D;
        }

        return values;
    }

    public double[] generateNoiseOctaves(double[] values, int x, int z, int width, int length, double var6, double var8, double var10) {
        return this.generateNoiseOctaves(values, x, 10.0D, z, width, 1, length, var6, 1.0D, var8);
    }
}
