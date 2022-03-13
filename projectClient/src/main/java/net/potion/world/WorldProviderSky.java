package net.potion.world;

import net.potion.block.Block;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.chunk.ChunkProviderSky;
import net.potion.world.chunk.IChunkProvider;
import net.potion.world.gen.BiomeGenBase;
import net.hypnosis.util.math.Vec3d;

public class WorldProviderSky extends WorldProvider {
    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.SKY, 0.5D, 0.0D);
        this.worldType = 1;
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return new ChunkProviderSky(this.worldObj, this.worldObj.getRandomSeed());
    }

    @Override
    public float calculateCelestialAngle(long var1, float var3) {
        return 0.0F;
    }

    @Override
    public float[] calcSunriseSunsetColors(float var1, float var2) {
        return null;
    }

    @Override
    public Vec3d func_4096_a(float var1, float var2) {
        int var3 = 8421536;
        float var4 = MathHelper.cos(var1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
        if (var4 < 0.0F) {
            var4 = 0.0F;
        }

        if (var4 > 1.0F) {
            var4 = 1.0F;
        }

        float var5 = (float) (var3 >> 16 & 255) / 255.0F;
        float var6 = (float) (var3 >> 8 & 255) / 255.0F;
        float var7 = (float) (var3 & 255) / 255.0F;
        var5 = var5 * (var4 * 0.94F + 0.06F);
        var6 = var6 * (var4 * 0.94F + 0.06F);
        var7 = var7 * (var4 * 0.91F + 0.09F);
        return new Vec3d(var5, var6, var7);
    }

    @Override
    public boolean func_28112_c() {
        return false;
    }

    @Override
    public float getCloudHeight() {
        return 8.0F;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2) {
        int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
        return var3 != 0 && Block.BLOCKS_LIST[var3].blockMaterial.getIsSolid();
    }
}
