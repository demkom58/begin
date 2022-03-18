package net.potion.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.world.IBlockAccess;
import net.potion.world.World;
import net.potion.world.WorldChunkManager;

public class OldChunkCache implements IBlockAccess {
    private final int chunkX;
    private final int chunkZ;
    private final OldChunk[][] chunkArray;
    private final World worldObj;

    public OldChunkCache(World world, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        this.worldObj = world;
        this.chunkX = startX >> 4;
        this.chunkZ = startZ >> 4;
        int endCX = endX >> 4;
        int endCZ = endZ >> 4;
        this.chunkArray = new OldChunk[endCX - this.chunkX + 1][endCZ - this.chunkZ + 1];

        for (int iX = this.chunkX; iX <= endCX; ++iX) {
            for (int iZ = this.chunkZ; iZ <= endCZ; ++iZ) {
                this.chunkArray[iX - this.chunkX][iZ - this.chunkZ] = world.getChunkFromChunkCoords(iX, iZ);
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

        int cX = (x >> 4) - this.chunkX;
        int cZ = (z >> 4) - this.chunkZ;
        if (cX >= 0 && cX < this.chunkArray.length && cZ >= 0 && cZ < this.chunkArray[cX].length) {
            OldChunk chunk = this.chunkArray[cX][cZ];
            return chunk == null ? 0 : chunk.getBlockID(x & 15, y, z & 15);
        }

        return 0;
    }

    @Override
    public TileEntity getBlockTileEntity(int x, int y, int z) {
        int cX = (x >> 4) - this.chunkX;
        int cZ = (z >> 4) - this.chunkZ;
        return this.chunkArray[cX][cZ].getChunkBlockTileEntity(x & 15, y, z & 15);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getBrightness(int x, int y, int z, int minBrightness) {
        int lightValue = this.getLightValue(x, y, z);
        if (lightValue < minBrightness) {
            lightValue = minBrightness;
        }

        return this.worldObj.worldProvider.lightBrightnessTable[lightValue];
    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getLightBrightness(int x, int y, int z) {
        return this.worldObj.worldProvider.lightBrightnessTable[this.getLightValue(x, y, z)];
    }

    public int getLightValue(int x, int y, int z) {
        return this.getLightValueExt(x, y, z, true);
    }

    public int getLightValueExt(int x, int y, int z, boolean lookNearMax) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return 15;
        }

        if (lookNearMax) {
            int blockId = this.getBlockId(x, y, z);
            if (blockId == Block.STAIR_SINGLE.blockID
                    || blockId == Block.FARMLAND.blockID
                    || blockId == Block.STAIR_COMPACT_PLANKS.blockID
                    || blockId == Block.STAIR_COMPACT_COBBLESTONE.blockID) {
                int lightUp = this.getLightValueExt(x, y + 1, z, false);
                int lightXp = this.getLightValueExt(x + 1, y, z, false);
                int lightXm = this.getLightValueExt(x - 1, y, z, false);
                int lightZp = this.getLightValueExt(x, y, z + 1, false);
                int lightZm = this.getLightValueExt(x, y, z - 1, false);
                if (lightXp > lightUp) {
                    lightUp = lightXp;
                }

                if (lightXm > lightUp) {
                    lightUp = lightXm;
                }

                if (lightZp > lightUp) {
                    lightUp = lightZp;
                }

                if (lightZm > lightUp) {
                    lightUp = lightZm;
                }

                return lightUp;
            }
        }

        if (y < 0) {
            return 0;
        }

        if (y >= 128) {
            int var12 = 15 - this.worldObj.skylightSubtracted;
            if (var12 < 0) {
                var12 = 0;
            }

            return var12;
        }

        int cX = (x >> 4) - this.chunkX;
        int cY = (z >> 4) - this.chunkZ;
        return this.chunkArray[cX][cY].getBlockLightValue(x & 15, y, z & 15, this.worldObj.skylightSubtracted);
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        if (y < 0) {
            return 0;
        }

        if (y >= 128) {
            return 0;
        }

        int cX = (x >> 4) - this.chunkX;
        int xY = (z >> 4) - this.chunkZ;
        return this.chunkArray[cX][xY].getBlockMetadata(x & 15, y, z & 15);
    }

    @Override
    public Material getBlockMaterial(int x, int y, int z) {
        int blockId = this.getBlockId(x, y, z);
        return blockId == 0 ? Material.AIR : Block.BLOCKS_LIST[blockId].blockMaterial;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean isBlockOpaqueCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        return block != null && block.isOpaqueCube();
    }

    @Override
    public boolean isBlockNormalCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        if (block == null) {
            return false;
        }

        return block.blockMaterial.getIsSolid() && block.isNormalCube();
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return this.worldObj.getWorldChunkManager();
    }

}
