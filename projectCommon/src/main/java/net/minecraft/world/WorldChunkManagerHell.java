package net.minecraft.world;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.world.chunk.ChunkCoordIntPair;
import net.minecraft.world.gen.biome.BiomeGenBase;

import java.util.Arrays;

public class WorldChunkManagerHell extends WorldChunkManager {
    private BiomeGenBase base;
    private double defTemperature;
    private double defHumidity;

    public WorldChunkManagerHell(BiomeGenBase base, double defTemperature, double defHumidity) {
        this.base = base;
        this.defTemperature = defTemperature;
        this.defHumidity = defHumidity;
    }

    @Override
    public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair var1) {
        return this.base;
    }

    @Override
    public BiomeGenBase getBiomeGenAt(int var1, int var2) {
        return this.base;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public double getTemperature(int var1, int var2) {
        return this.defTemperature;
    }

    @Override
    public BiomeGenBase[] getBiomeGensAt(int var1, int var2, int var3, int var4) {
        this.biomeGenBases = this.loadBlockGeneratorData(this.biomeGenBases, var1, var2, var3, var4);
        return this.biomeGenBases;
    }

    @Override
    public double[] getTemperatures(double[] var1, int var2, int var3, int var4, int var5) {
        if (var1 == null || var1.length < var4 * var5) {
            var1 = new double[var4 * var5];
        }

        Arrays.fill(var1, 0, var4 * var5, this.defTemperature);
        return var1;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] bases, int var2, int var3, int var4, int var5) {
        if (bases == null || bases.length < var4 * var5) {
            bases = new BiomeGenBase[var4 * var5];
        }

        if (this.temperature == null || this.temperature.length < var4 * var5) {
            this.temperature = new double[var4 * var5];
            this.humidity = new double[var4 * var5];
        }

        Arrays.fill(bases, 0, var4 * var5, this.base);
        Arrays.fill(this.humidity, 0, var4 * var5, this.defHumidity);
        Arrays.fill(this.temperature, 0, var4 * var5, this.defTemperature);
        return bases;
    }
}
