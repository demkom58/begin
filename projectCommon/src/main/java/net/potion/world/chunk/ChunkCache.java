package net.potion.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import net.potion.world.WorldChunkManager;

public class ChunkCache implements IBlockAccess {
    private int chunkX;
    private int chunkZ;
    private Chunk[][] chunkArray;
    private World worldObj;

    public ChunkCache(World var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.worldObj = var1;
        this.chunkX = var2 >> 4;
        this.chunkZ = var4 >> 4;
        int var8 = var5 >> 4;
        int var9 = var7 >> 4;
        this.chunkArray = new Chunk[var8 - this.chunkX + 1][var9 - this.chunkZ + 1];

        for (int iX = this.chunkX; iX <= var8; ++iX) {
            for (int iZ = this.chunkZ; iZ <= var9; ++iZ) {
                this.chunkArray[iX - this.chunkX][iZ - this.chunkZ] = var1.getChunkFromChunkCoords(iX, iZ);
            }
        }

    }

    @Override
    public int getBlockId(int x, int y, int z) {
        if (y < 0) {
            return 0;
        }

        if (y >= 128) {
            return 0;
        }

        int var4 = (x >> 4) - this.chunkX;
        int var5 = (z >> 4) - this.chunkZ;
        if (var4 >= 0 && var4 < this.chunkArray.length && var5 >= 0 && var5 < this.chunkArray[var4].length) {
            Chunk chunk = this.chunkArray[var4][var5];
            return chunk == null ? 0 : chunk.getBlockID(x & 15, y, z & 15);
        }

        return 0;
    }

    @Override
    public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
        int var4 = (var1 >> 4) - this.chunkX;
        int var5 = (var3 >> 4) - this.chunkZ;
        return this.chunkArray[var4][var5].getChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getBrightness(int var1, int var2, int var3, int var4) {
        int var5 = this.getLightValue(var1, var2, var3);
        if (var5 < var4) {
            var5 = var4;
        }

        return this.worldObj.worldProvider.lightBrightnessTable[var5];
    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getLightBrightness(int var1, int var2, int var3) {
        return this.worldObj.worldProvider.lightBrightnessTable[this.getLightValue(var1, var2, var3)];
    }

    public int getLightValue(int var1, int var2, int var3) {
        return this.getLightValueExt(var1, var2, var3, true);
    }

    public int getLightValueExt(int var1, int var2, int var3, boolean var4) {
        if (var1 < -32000000 || var3 < -32000000 || var1 >= 32000000 || var3 > 32000000) {
            return 15;
        }

        if (var4) {
            int var5 = this.getBlockId(var1, var2, var3);
            if (var5 == Block.STAIR_SINGLE.blockID || var5 == Block.FARMLAND.blockID || var5 == Block.STAIR_COMPACT_PLANKS.blockID || var5 == Block.STAIR_COMPACT_COBBLESTONE.blockID) {
                int var13 = this.getLightValueExt(var1, var2 + 1, var3, false);
                int var7 = this.getLightValueExt(var1 + 1, var2, var3, false);
                int var8 = this.getLightValueExt(var1 - 1, var2, var3, false);
                int var9 = this.getLightValueExt(var1, var2, var3 + 1, false);
                int var10 = this.getLightValueExt(var1, var2, var3 - 1, false);
                if (var7 > var13) {
                    var13 = var7;
                }

                if (var8 > var13) {
                    var13 = var8;
                }

                if (var9 > var13) {
                    var13 = var9;
                }

                if (var10 > var13) {
                    var13 = var10;
                }

                return var13;
            }
        }

        if (var2 < 0) {
            return 0;
        } else if (var2 >= 128) {
            int var12 = 15 - this.worldObj.skylightSubtracted;
            if (var12 < 0) {
                var12 = 0;
            }

            return var12;
        } else {
            int var11 = (var1 >> 4) - this.chunkX;
            int var6 = (var3 >> 4) - this.chunkZ;
            return this.chunkArray[var11][var6].getBlockLightValue(var1 & 15, var2, var3 & 15, this.worldObj.skylightSubtracted);
        }
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        if (var2 < 0) {
            return 0;
        }

        if (var2 >= 128) {
            return 0;
        }

        int var4 = (var1 >> 4) - this.chunkX;
        int var5 = (var3 >> 4) - this.chunkZ;
        return this.chunkArray[var4][var5].getBlockMetadata(var1 & 15, var2, var3 & 15);
    }

    @Override
    public Material getBlockMaterial(int var1, int var2, int var3) {
        int var4 = this.getBlockId(var1, var2, var3);
        return var4 == 0 ? Material.AIR : Block.BLOCKS_LIST[var4].blockMaterial;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean isBlockOpaqueCube(int var1, int var2, int var3) {
        Block var4 = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
        return var4 != null && var4.isOpaqueCube();
    }

    @Override
    public boolean isBlockNormalCube(int var1, int var2, int var3) {
        Block var4 = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
        if (var4 == null) {
            return false;
        } else {
            return var4.blockMaterial.getIsSolid() && var4.isNormalCube();
        }
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return this.worldObj.getWorldChunkManager();
    }

}
