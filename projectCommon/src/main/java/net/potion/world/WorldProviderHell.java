package net.potion.world;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.world.chunk.ChunkProviderHell;
import net.potion.world.chunk.IChunkProvider;
import net.potion.world.gen.BiomeGenBase;
import net.hypnosis.util.math.Vec3d;

public class WorldProviderHell extends WorldProvider {
    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.HELL, 1.0D, 0.0D);
        this.isNether = true;
        this.isHellWorld = true;
        this.hasNoSky = true;
        this.worldType = -1;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public Vec3d getFogColor(float var1, float var2) {
        return new Vec3d(0.2, 0.03, 0.03);
    }

    @Override
    protected void generateLightBrightnessTable() {
        float var1 = 0.1F;

        for (int var2 = 0; var2 <= 15; ++var2) {
            float var3 = 1.0F - (float) var2 / 15.0F;
            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }

    }

    @Override
    public IChunkProvider getChunkProvider() {
        return new ChunkProviderHell(this.worldObj, this.worldObj.getRandomSeed());
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2) {
        int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
        if (var3 == Block.BEDROCK.blockID)
            return false;

        if (var3 == 0)
            return false;

        return Block.OPAQUE_CUBE_LOOKUP[var3];
    }

    @Override
    public float calculateCelestialAngle(long celestialAngle, float var3) {
        return 0.5F;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean canRespawnHere() {
        return false;
    }
}
