package net.potion.world;

import net.potion.world.chunk.ChunkCoordIntPair;
import net.potion.world.gen.BiomeGenBase;
import net.potion.world.gen.NoiseGeneratorOctaves2;

import java.util.Random;

public class WorldChunkManager {
    public double[] temperature;
    public double[] humidity;
    public double[] field_4257_c;
    public BiomeGenBase[] biomeGenBases;
    private NoiseGeneratorOctaves2 genOc1;
    private NoiseGeneratorOctaves2 genOc2;
    private NoiseGeneratorOctaves2 genOc3;

    protected WorldChunkManager() {
    }

    public WorldChunkManager(World world) {
        this.genOc1 = new NoiseGeneratorOctaves2(new Random(world.getRandomSeed() * 9871L), 4);
        this.genOc2 = new NoiseGeneratorOctaves2(new Random(world.getRandomSeed() * 39811L), 4);
        this.genOc3 = new NoiseGeneratorOctaves2(new Random(world.getRandomSeed() * 543321L), 2);
    }

    public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair pair) {
        return this.getBiomeGenAt(pair.chunkXPos << 4, pair.chunkZPos << 4);
    }

    public BiomeGenBase getBiomeGenAt(int x, int z) {
        return this.getBiomeGensAt(x, z, 1, 1)[0];
    }

    public BiomeGenBase[] getBiomeGensAt(int x, int z, int var3, int var4) {
        this.biomeGenBases = this.loadBlockGeneratorData(this.biomeGenBases, x, z, var3, var4);
        return this.biomeGenBases;
    }

    public double[] getTemperatures(double[] var1, int var2, int var3, int var4, int var5) {
        if (var1 == null || var1.length < var4 * var5) {
            var1 = new double[var4 * var5];
        }

        var1 = this.genOc1.func_4101_a(var1, var2, var3, var4, var5, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        this.field_4257_c = this.genOc3.func_4101_a(this.field_4257_c, var2, var3, var4, var5, 0.25D, 0.25D, 0.5882352941176471D);
        int var6 = 0;

        for (int var7 = 0; var7 < var4; ++var7) {
            for (int var8 = 0; var8 < var5; ++var8) {
                double var9 = this.field_4257_c[var6] * 1.1D + 0.5D;
                double var11 = 0.01D;
                double var13 = 1.0D - var11;
                double var15 = (var1[var6] * 0.15D + 0.7D) * var13 + var9 * var11;
                var15 = 1.0D - (1.0D - var15) * (1.0D - var15);
                if (var15 < 0.0D) {
                    var15 = 0.0D;
                }

                if (var15 > 1.0D) {
                    var15 = 1.0D;
                }

                var1[var6] = var15;
                ++var6;
            }
        }

        return var1;
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] bases, int x, int z, int var4, int var5) {
        if (bases == null || bases.length < var4 * var5)
            bases = new BiomeGenBase[var4 * var5];

        this.temperature = this.genOc1.func_4101_a(this.temperature, x, z, var4, var4, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        this.humidity = this.genOc2.func_4101_a(this.humidity, x, z, var4, var4, 0.05000000074505806D, 0.05000000074505806D, 0.3333333333333333D);
        this.field_4257_c = this.genOc3.func_4101_a(this.field_4257_c, x, z, var4, var4, 0.25D, 0.25D, 0.5882352941176471D);
        int var6 = 0;

        for (int var7 = 0; var7 < var4; ++var7) {
            for (int var8 = 0; var8 < var5; ++var8) {
                double var9 = this.field_4257_c[var6] * 1.1D + 0.5D;
                double var11 = 0.01D;
                double var13 = 1.0D - var11;
                double var15 = (this.temperature[var6] * 0.15D + 0.7D) * var13 + var9 * var11;
                var11 = 0.002D;
                var13 = 1.0D - var11;
                double var17 = (this.humidity[var6] * 0.15D + 0.5D) * var13 + var9 * var11;
                var15 = 1.0D - (1.0D - var15) * (1.0D - var15);
                if (var15 < 0.0D) {
                    var15 = 0.0D;
                }

                if (var17 < 0.0D) {
                    var17 = 0.0D;
                }

                if (var15 > 1.0D) {
                    var15 = 1.0D;
                }

                if (var17 > 1.0D) {
                    var17 = 1.0D;
                }

                this.temperature[var6] = var15;
                this.humidity[var6] = var17;
                bases[var6++] = BiomeGenBase.getBiomeFromLookup(var15, var17);
            }
        }

        return bases;
    }
}
