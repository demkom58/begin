package net.potion.world.chunk;

import net.potion.entity.Entity;
import net.potion.block.EnumSkyBlock;
import net.potion.tileentity.TileEntity;
import net.potion.util.AxisAlignedBB;
import net.potion.world.World;

import java.util.List;
import java.util.Random;

public class EmptyChunk extends Chunk {
    public EmptyChunk(World var1, int var2, int var3) {
        super(var1, var2, var3);
        this.neverSave = true;
    }

    public EmptyChunk(World var1, byte[] var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.neverSave = true;
    }

    @Override
    public boolean isAtLocation(int var1, int var2) {
        return var1 == this.xPosition && var2 == this.zPosition;
    }

    @Override
    public int getHeightValue(int var1, int var2) {
        return 0;
    }

    @Override
    public void func_1014_a() {
    }

    @Override
    public void generateHeightMap() {
    }

    @Override
    public void func_1024_c() {
    }

    @Override
    public void func_4143_d() {
    }

    @Override
    public int getBlockID(int var1, int var2, int var3) {
        return 0;
    }

    @Override
    public boolean setBlockIDWithMetadata(int var1, int var2, int var3, int var4, int var5) {
        return true;
    }

    @Override
    public boolean setBlockID(int var1, int var2, int var3, int var4) {
        return true;
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        return 0;
    }

    @Override
    public void setBlockMetadata(int var1, int var2, int var3, int var4) {
    }

    @Override
    public int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
        return 0;
    }

    @Override
    public void setLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
    }

    @Override
    public int getBlockLightValue(int var1, int var2, int var3, int var4) {
        return 0;
    }

    @Override
    public void addEntity(Entity var1) {
    }

    @Override
    public void removeEntity(Entity var1) {
    }

    @Override
    public void removeEntityAtIndex(Entity var1, int var2) {
    }

    @Override
    public boolean canBlockSeeTheSky(int var1, int var2, int var3) {
        return false;
    }

    @Override
    public TileEntity getChunkBlockTileEntity(int var1, int var2, int var3) {
        return null;
    }

    @Override
    public void addTileEntity(TileEntity var1) {
    }

    @Override
    public void setChunkBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
    }

    @Override
    public void removeChunkBlockTileEntity(int var1, int var2, int var3) {
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
    public void getEntitiesWithinAABBForEntity(Entity var1, AxisAlignedBB var2, List var3) {
    }

    @Override
    public void getEntitiesOfTypeWithinAAAB(Class type, AxisAlignedBB bb, List entities) {
    }

    @Override
    public boolean needsSaving(boolean var1) {
        return false;
    }

    @Override
    public int setChunkData(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        int var9 = var5 - var2;
        int var10 = var6 - var3;
        int var11 = var7 - var4;
        int var12 = var9 * var10 * var11;
        return var12 + var12 / 2 * 3;
    }

    @Override
    public Random func_997_a(long var1) {
        return new Random(this.worldObj.getRandomSeed() + (long) (this.xPosition * this.xPosition * 4987142) + (long) (this.xPosition * 5947611) + (long) (this.zPosition * this.zPosition) * 4392871L + (long) (this.zPosition * 389711) ^ var1);
    }

    @Override
    public boolean func_21167_h() {
        return true;
    }
}
