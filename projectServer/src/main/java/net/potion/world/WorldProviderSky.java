package net.potion.world;

import net.potion.world.chunk.ChunkProviderSky;
import net.potion.world.chunk.IChunkProvider;
import net.potion.block.Block;
import net.potion.world.gen.BiomeGenBase;

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
    public boolean canCoordinateBeSpawn(int var1, int var2) {
        int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
        return var3 != 0 && Block.BLOCKS_LIST[var3].blockMaterial.getIsSolid();
    }
}
