package net.potion.world;

import net.potion.world.chunk.ChunkCoordIntPair;
import net.potion.world.gen.BiomeGenBase;

import java.util.Arrays;

public class WorldChunkManagerHell extends WorldChunkManager {
    private BiomeGenBase biomeGenBase;
    private double field_4261_f;
    private double field_4260_g;

    public WorldChunkManagerHell(BiomeGenBase biomeGenBase, double var2, double var4) {
        this.biomeGenBase = biomeGenBase;
        this.field_4261_f = var2;
        this.field_4260_g = var4;
    }

    @Override
    public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair pair) {
        return this.biomeGenBase;
    }

    @Override
    public BiomeGenBase getBiomeGenAt(int x, int z) {
        return this.biomeGenBase;
    }

    @Override
    public BiomeGenBase[] getBiomeGensAt(int x, int z, int var3, int var4) {
        this.biomeGenBases = this.loadBlockGeneratorData(this.biomeGenBases, x, z, var3, var4);
        return this.biomeGenBases;
    }

    @Override
    public double[] getTemperatures(double[] var1, int var2, int var3, int var4, int var5) {
        if (var1 == null || var1.length < var4 * var5)
            var1 = new double[var4 * var5];

        Arrays.fill(var1, 0, var4 * var5, this.field_4261_f);
        return var1;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] bases, int x, int z, int var4, int var5) {
        if (bases == null || bases.length < var4 * var5)
            bases = new BiomeGenBase[var4 * var5];

        if (this.temperature == null || this.temperature.length < var4 * var5) {
            this.temperature = new double[var4 * var5];
            this.humidity = new double[var4 * var5];
        }

        Arrays.fill(bases, 0, var4 * var5, this.biomeGenBase);
        Arrays.fill(this.humidity, 0, var4 * var5, this.field_4260_g);
        Arrays.fill(this.temperature, 0, var4 * var5, this.field_4261_f);

        return bases;
    }
}
