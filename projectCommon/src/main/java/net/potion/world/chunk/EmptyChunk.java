package net.potion.world.chunk;

import net.potion.block.EnumSkyBlock;
import net.potion.entity.Entity;
import net.potion.tileentity.TileEntity;
import net.potion.util.AxisAlignedBB;
import net.potion.world.World;

import java.util.List;
import java.util.Random;

public class EmptyChunk extends Chunk {
    public EmptyChunk(World world, int x, int z) {
        super(world, x, z);
        this.neverSave = true;
    }

    public EmptyChunk(World world, byte[] data, int x, int z) {
        super(world, data, x, z);
        this.neverSave = true;
    }

    @Override
    public boolean isAtLocation(int x, int z) {
        return x == this.xPosition && z == this.zPosition;
    }

    @Override
    public int getHeightValue(int x, int z) {
        return 0;
    }

    @Override
    public void method2() {
    }

    @Override
    public void generateHeightMap() {
    }

    @Override
    public void generateHeightAndSkyLightMap() {
    }

    @Override
    public void method3() {
    }

    @Override
    public int getBlockID(int x, int y, int z) {
        return 0;
    }

    @Override
    public boolean setBlockIDWithMetadata(int x, int y, int z, int blockId, int metadata) {
        return true;
    }

    @Override
    public boolean setBlockID(int x, int y, int z, int blockId) {
        return true;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        return 0;
    }

    @Override
    public void setBlockMetadata(int x, int y, int z, int metadata) {
    }

    @Override
    public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
        return 0;
    }

    @Override
    public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int light) {
    }

    @Override
    public int getBlockLightValue(int x, int y, int z, int skylightSubtracted) {
        return 0;
    }

    @Override
    public void addEntity(Entity entity) {
    }

    @Override
    public void removeEntity(Entity entity) {
    }

    @Override
    public void removeEntityAtIndex(Entity entity, int listIdx) {
    }

    @Override
    public boolean canBlockSeeTheSky(int x, int y, int z) {
        return false;
    }

    @Override
    public TileEntity getChunkBlockTileEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public void addTileEntity(TileEntity tileEntity) {
    }

    @Override
    public void setChunkBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
    }

    @Override
    public void removeChunkBlockTileEntity(int x, int y, int z) {
    }

    @Override
    public void onChunkLoad() {
    }

    @Override
    public void onChunkUnload() {
    }

    @Override
    public void setChunkModified() {
    }

    @Override
    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB bb, List<Entity> result) {
    }

    @Override
    public void getEntitiesOfTypeWithinAABB(Class<? extends Entity> type, AxisAlignedBB bb, List<Entity> entities) {
    }

    @Override
    public boolean needsSaving(boolean var1) {
        return false;
    }

    @Override
    public int setChunkData(byte[] src, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int offset) {
        int rngX = maxX - minX;
        int rngY = maxY - minY;
        int rngZ = maxZ - minZ;
        int volume = rngX * rngY * rngZ;
        return volume + volume / 2 * 3;
    }

    @Override
    public Random createSpecialRandom(long var1) {
        return new Random(this.world.getRandomSeed()
                + (this.xPosition * this.xPosition * 4987142L)
                + (this.xPosition * 5947611L)
                + ((long) this.zPosition * this.zPosition) * 4392871L
                + (this.zPosition * 389711L) ^ var1);
    }

    @Override
    public boolean method1() {
        return true;
    }
}
