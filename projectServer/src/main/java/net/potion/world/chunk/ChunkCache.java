package net.potion.world.chunk;

import net.potion.world.IBlockAccess;
import net.potion.material.Material;
import net.potion.block.Block;
import net.potion.tileentity.TileEntity;
import net.potion.world.World;

public class ChunkCache implements IBlockAccess {
    private int chunkX;
    private int chunkZ;
    private Chunk[][] chunkArray;
    private World worldObj;

    public ChunkCache(World world, int chunkX, int var3, int chunkZ, int var5, int var6, int var7) {
        this.worldObj = world;
        this.chunkX = chunkX >> 4;
        this.chunkZ = chunkZ >> 4;
        int var8 = var5 >> 4;
        int var9 = var7 >> 4;
        this.chunkArray = new Chunk[var8 - this.chunkX + 1][var9 - this.chunkZ + 1];

        for (int iX = this.chunkX; iX <= var8; ++iX) {
            for (int iZ = this.chunkZ; iZ <= var9; ++iZ) {
                this.chunkArray[iX - this.chunkX][iZ - this.chunkZ] = world.getChunkFromChunkCoords(iX, iZ);
            }
        }

    }

    @Override
    public int getBlockId(int var1, int var2, int var3) {
        if (var2 < 0) {
            return 0;
        } else if (var2 >= 128) {
            return 0;
        } else {
            int var4 = (var1 >> 4) - this.chunkX;
            int var5 = (var3 >> 4) - this.chunkZ;
            if (var4 >= 0 && var4 < this.chunkArray.length && var5 >= 0 && var5 < this.chunkArray[var4].length) {
                Chunk var6 = this.chunkArray[var4][var5];
                return var6 == null ? 0 : var6.getBlockID(var1 & 15, var2, var3 & 15);
            } else {
                return 0;
            }
        }
    }

    @Override
    public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
        int var4 = (var1 >> 4) - this.chunkX;
        int var5 = (var3 >> 4) - this.chunkZ;
        return this.chunkArray[var4][var5].getChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        if (var2 < 0) {
            return 0;
        } else if (var2 >= 128) {
            return 0;
        } else {
            int var4 = (var1 >> 4) - this.chunkX;
            int var5 = (var3 >> 4) - this.chunkZ;
            return this.chunkArray[var4][var5].getBlockMetadata(var1 & 15, var2, var3 & 15);
        }
    }

    @Override
    public Material getBlockMaterial(int var1, int var2, int var3) {
        int var4 = this.getBlockId(var1, var2, var3);
        return var4 == 0 ? Material.AIR : Block.BLOCKS_LIST[var4].blockMaterial;
    }

    @Override
    public boolean isBlockNormalCube(int var1, int var2, int var3) {
        Block var4 = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
        if (var4 == null) {
            return false;
        } else {
            return var4.blockMaterial.getIsSolid() && var4.isACube();
        }
    }
}
