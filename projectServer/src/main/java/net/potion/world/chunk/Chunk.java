package net.potion.world.chunk;

import net.potion.block.Block;
import net.potion.block.BlockContainer;
import net.potion.block.EnumSkyBlock;
import net.potion.entity.Entity;
import net.potion.tileentity.TileEntity;
import net.potion.util.AxisAlignedBB;
import net.hypnosis.util.math.MathHelper;
import net.potion.util.NibbleArray;
import net.potion.world.World;

import java.util.*;

public class Chunk {
    public static boolean isLit;
    public final int xPosition;
    public final int zPosition;
    public byte[] blocks;
    public boolean isChunkLoaded;
    public World worldObj;
    public NibbleArray data;
    public NibbleArray skylightMap;
    public NibbleArray blocklightMap;
    public byte[] heightMap;
    public int lowestBlockHeight;
    public Map<ChunkPosition, TileEntity> chunkTileEntityMap;
    public List<Entity>[] entities;
    public boolean isTerrainPopulated;
    public boolean isModified;
    public boolean neverSave;
    public boolean hasEntities;
    public long lastSaveTime;

    public Chunk(World world, int x, int z) {
        this.chunkTileEntityMap = new HashMap<>();
        this.entities = new List[8];
        this.isTerrainPopulated = false;
        this.isModified = false;
        this.hasEntities = false;
        this.lastSaveTime = 0L;
        this.worldObj = world;
        this.xPosition = x;
        this.zPosition = z;
        this.heightMap = new byte[256];

        for (int i = 0; i < this.entities.length; ++i) {
            this.entities[i] = new ArrayList<>();
        }

    }

    public Chunk(World world, byte[] blocks, int x, int z) {
        this(world, x, z);
        this.blocks = blocks;
        this.data = new NibbleArray(blocks.length);
        this.skylightMap = new NibbleArray(blocks.length);
        this.blocklightMap = new NibbleArray(blocks.length);
    }

    public boolean isAtLocation(int x, int z) {
        return x == this.xPosition && z == this.zPosition;
    }

    public int getHeightValue(int x, int z) {
        return this.heightMap[z << 4 | x] & 255;
    }

    public void func_348_a() {
    }

    public void generateHeightMap() {
        int var1 = 127;

        for (int var2 = 0; var2 < 16; ++var2) {
            for (int var3 = 0; var3 < 16; ++var3) {
                int var4 = 127;

                int var5;
                for (var5 = var2 << 11 | var3 << 7; var4 > 0 && Block.LIGHT_OPACITY[this.blocks[var5 + var4 - 1] & 255] == 0; --var4) {
                }

                this.heightMap[var3 << 4 | var2] = (byte) var4;
                if (var4 < var1) {
                    var1 = var4;
                }

                if (!this.worldObj.worldProvider.hasNoSky) {
                    int var6 = 15;
                    int var7 = 127;

                    while (true) {
                        var6 -= Block.LIGHT_OPACITY[this.blocks[var5 + var7] & 255];
                        if (var6 > 0) {
                            this.skylightMap.setNibble(var2, var7, var3, var6);
                        }

                        --var7;
                        if (var7 <= 0 || var6 <= 0) {
                            break;
                        }
                    }
                }
            }
        }

        this.lowestBlockHeight = var1;

        for (int var8 = 0; var8 < 16; ++var8) {
            for (int var9 = 0; var9 < 16; ++var9) {
                this.func_333_c(var8, var9);
            }
        }

        this.isModified = true;
    }

    public void func_4053_c() {
    }

    private void func_333_c(int var1, int var2) {
        int var3 = this.getHeightValue(var1, var2);
        int var4 = this.xPosition * 16 + var1;
        int var5 = this.zPosition * 16 + var2;
        this.func_355_f(var4 - 1, var5, var3);
        this.func_355_f(var4 + 1, var5, var3);
        this.func_355_f(var4, var5 - 1, var3);
        this.func_355_f(var4, var5 + 1, var3);
    }

    private void func_355_f(int var1, int var2, int var3) {
        int var4 = this.worldObj.getHeightValue(var1, var2);
        if (var4 > var3) {
            this.worldObj.scheduleLightingUpdate(EnumSkyBlock.SKY, var1, var3, var2, var1, var4, var2);
            this.isModified = true;
        } else if (var4 < var3) {
            this.worldObj.scheduleLightingUpdate(EnumSkyBlock.SKY, var1, var4, var2, var1, var3, var2);
            this.isModified = true;
        }

    }

    private void func_339_g(int var1, int var2, int var3) {
        int var4 = this.heightMap[var3 << 4 | var1] & 255;
        int var5 = var4;
        if (var2 > var4) {
            var5 = var2;
        }

        for (int var6 = var1 << 11 | var3 << 7; var5 > 0
                && Block.LIGHT_OPACITY[this.blocks[var6 + var5 - 1] & 255] == 0; --var5) {
        }

        if (var5 != var4) {
            this.worldObj.markBlocksDirtyVertical(var1, var3, var5, var4);
            this.heightMap[var3 << 4 | var1] = (byte) var5;
            if (var5 < this.lowestBlockHeight) {
                this.lowestBlockHeight = var5;
            } else {
                int var7 = 127;

                for (int var8 = 0; var8 < 16; ++var8) {
                    for (int var9 = 0; var9 < 16; ++var9) {
                        if ((this.heightMap[var9 << 4 | var8] & 255) < var7) {
                            var7 = this.heightMap[var9 << 4 | var8] & 255;
                        }
                    }
                }

                this.lowestBlockHeight = var7;
            }

            int var12 = this.xPosition * 16 + var1;
            int var13 = this.zPosition * 16 + var3;
            if (var5 < var4) {
                for (int var14 = var5; var14 < var4; ++var14) {
                    this.skylightMap.setNibble(var1, var14, var3, 15);
                }
            } else {
                this.worldObj.scheduleLightingUpdate(EnumSkyBlock.SKY, var12, var4, var13, var12, var5, var13);

                for (int var15 = var4; var15 < var5; ++var15) {
                    this.skylightMap.setNibble(var1, var15, var3, 0);
                }
            }

            int var16 = 15;

            int var10;
            for (var10 = var5; var5 > 0 && var16 > 0; this.skylightMap.setNibble(var1, var5, var3, var16)) {
                --var5;
                int var11 = Block.LIGHT_OPACITY[this.getBlockID(var1, var5, var3)];
                if (var11 == 0) {
                    var11 = 1;
                }

                var16 -= var11;
                if (var16 < 0) {
                    var16 = 0;
                }
            }

            while (var5 > 0 && Block.LIGHT_OPACITY[this.getBlockID(var1, var5 - 1, var3)] == 0) {
                --var5;
            }

            if (var5 != var10) {
                this.worldObj.scheduleLightingUpdate(EnumSkyBlock.SKY, var12 - 1, var5, var13 - 1, var12 + 1, var10, var13 + 1);
            }

            this.isModified = true;
        }
    }

    public int getBlockID(int var1, int var2, int var3) {
        return this.blocks[var1 << 11 | var3 << 7 | var2] & 255;
    }

    public boolean setBlockIDWithMetadata(int var1, int var2, int var3, int var4, int var5) {
        byte var6 = (byte) var4;
        int var7 = this.heightMap[var3 << 4 | var1] & 255;
        int var8 = this.blocks[var1 << 11 | var3 << 7 | var2] & 255;
        if (var8 == var4 && this.data.getNibble(var1, var2, var3) == var5) {
            return false;
        } else {
            int var9 = this.xPosition * 16 + var1;
            int var10 = this.zPosition * 16 + var3;
            this.blocks[var1 << 11 | var3 << 7 | var2] = (byte) (var6 & 255);
            if (var8 != 0 && !this.worldObj.singleplayerWorld) {
                Block.BLOCKS_LIST[var8].onBlockRemoval(this.worldObj, var9, var2, var10);
            }

            this.data.setNibble(var1, var2, var3, var5);
            if (!this.worldObj.worldProvider.hasNoSky) {
                if (Block.LIGHT_OPACITY[var6 & 255] != 0) {
                    if (var2 >= var7) {
                        this.func_339_g(var1, var2 + 1, var3);
                    }
                } else if (var2 == var7 - 1) {
                    this.func_339_g(var1, var2, var3);
                }

                this.worldObj.scheduleLightingUpdate(EnumSkyBlock.SKY, var9, var2, var10, var9, var2, var10);
            }

            this.worldObj.scheduleLightingUpdate(EnumSkyBlock.BLOCK, var9, var2, var10, var9, var2, var10);
            this.func_333_c(var1, var3);
            this.data.setNibble(var1, var2, var3, var5);
            if (var4 != 0) {
                Block.BLOCKS_LIST[var4].onBlockAdded(this.worldObj, var9, var2, var10);
            }

            this.isModified = true;
            return true;
        }
    }

    public boolean setBlockID(int var1, int var2, int var3, int var4) {
        byte var5 = (byte) var4;
        int var6 = this.heightMap[var3 << 4 | var1] & 255;
        int var7 = this.blocks[var1 << 11 | var3 << 7 | var2] & 255;
        if (var7 == var4) {
            return false;
        } else {
            int var8 = this.xPosition * 16 + var1;
            int var9 = this.zPosition * 16 + var3;
            this.blocks[var1 << 11 | var3 << 7 | var2] = (byte) (var5 & 255);
            if (var7 != 0) {
                Block.BLOCKS_LIST[var7].onBlockRemoval(this.worldObj, var8, var2, var9);
            }

            this.data.setNibble(var1, var2, var3, 0);
            if (Block.LIGHT_OPACITY[var5 & 255] != 0) {
                if (var2 >= var6) {
                    this.func_339_g(var1, var2 + 1, var3);
                }
            } else if (var2 == var6 - 1) {
                this.func_339_g(var1, var2, var3);
            }

            this.worldObj.scheduleLightingUpdate(EnumSkyBlock.SKY, var8, var2, var9, var8, var2, var9);
            this.worldObj.scheduleLightingUpdate(EnumSkyBlock.BLOCK, var8, var2, var9, var8, var2, var9);
            this.func_333_c(var1, var3);
            if (var4 != 0 && !this.worldObj.singleplayerWorld) {
                Block.BLOCKS_LIST[var4].onBlockAdded(this.worldObj, var8, var2, var9);
            }

            this.isModified = true;
            return true;
        }
    }

    public int getBlockMetadata(int var1, int var2, int var3) {
        return this.data.getNibble(var1, var2, var3);
    }

    public void setBlockMetadata(int var1, int var2, int var3, int var4) {
        this.isModified = true;
        this.data.setNibble(var1, var2, var3, var4);
    }

    public int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
        if (var1 == EnumSkyBlock.SKY) {
            return this.skylightMap.getNibble(var2, var3, var4);
        } else {
            return var1 == EnumSkyBlock.BLOCK ? this.blocklightMap.getNibble(var2, var3, var4) : 0;
        }
    }

    public void setLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
        this.isModified = true;
        if (var1 == EnumSkyBlock.SKY) {
            this.skylightMap.setNibble(var2, var3, var4, var5);
        } else {
            if (var1 != EnumSkyBlock.BLOCK) {
                return;
            }

            this.blocklightMap.setNibble(var2, var3, var4, var5);
        }

    }

    public int getBlockLightValue(int var1, int var2, int var3, int var4) {
        int var5 = this.skylightMap.getNibble(var1, var2, var3);
        if (var5 > 0) {
            isLit = true;
        }

        var5 = var5 - var4;
        int var6 = this.blocklightMap.getNibble(var1, var2, var3);
        if (var6 > var5) {
            var5 = var6;
        }

        return var5;
    }

    public void addEntity(Entity var1) {
        this.hasEntities = true;
        int var2 = MathHelper.floor(var1.posX / 16.0D);
        int var3 = MathHelper.floor(var1.posZ / 16.0D);
        if (var2 != this.xPosition || var3 != this.zPosition) {
            System.out.println("Wrong location! " + var1);
            Thread.dumpStack();
        }

        int var4 = MathHelper.floor(var1.posY / 16.0D);
        if (var4 < 0) {
            var4 = 0;
        }

        if (var4 >= this.entities.length) {
            var4 = this.entities.length - 1;
        }

        var1.addedToChunk = true;
        var1.chunkCoordX = this.xPosition;
        var1.chunkCoordY = var4;
        var1.chunkCoordZ = this.zPosition;
        this.entities[var4].add(var1);
    }

    public void removeEntity(Entity var1) {
        this.removeEntityAtIndex(var1, var1.chunkCoordY);
    }

    public void removeEntityAtIndex(Entity var1, int var2) {
        if (var2 < 0) {
            var2 = 0;
        }

        if (var2 >= this.entities.length) {
            var2 = this.entities.length - 1;
        }

        this.entities[var2].remove(var1);
    }

    public boolean canBlockSeeTheSky(int var1, int var2, int var3) {
        return var2 >= (this.heightMap[var3 << 4 | var1] & 255);
    }

    public TileEntity getChunkBlockTileEntity(int var1, int var2, int var3) {
        ChunkPosition var4 = new ChunkPosition(var1, var2, var3);
        TileEntity var5 = this.chunkTileEntityMap.get(var4);
        if (var5 == null) {
            int var6 = this.getBlockID(var1, var2, var3);
            if (!Block.IS_BLOCK_CONTAINER[var6]) {
                return null;
            }

            BlockContainer var7 = (BlockContainer) Block.BLOCKS_LIST[var6];
            var7.onBlockAdded(this.worldObj, this.xPosition * 16 + var1, var2, this.zPosition * 16 + var3);
            var5 = this.chunkTileEntityMap.get(var4);
        }

        if (var5 != null && var5.isInvalid()) {
            this.chunkTileEntityMap.remove(var4);
            return null;
        }

        return var5;
    }

    public void addTileEntity(TileEntity var1) {
        int var2 = var1.xCoord - this.xPosition * 16;
        int var3 = var1.yCoord;
        int var4 = var1.zCoord - this.zPosition * 16;
        this.setChunkBlockTileEntity(var2, var3, var4, var1);
        if (this.isChunkLoaded) {
            this.worldObj.loadedTileEntityList.add(var1);
        }

    }

    public void setChunkBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
        ChunkPosition var5 = new ChunkPosition(var1, var2, var3);
        var4.worldObj = this.worldObj;
        var4.xCoord = this.xPosition * 16 + var1;
        var4.yCoord = var2;
        var4.zCoord = this.zPosition * 16 + var3;
        if (this.getBlockID(var1, var2, var3) != 0 && Block.BLOCKS_LIST[this.getBlockID(var1, var2, var3)] instanceof BlockContainer) {
            var4.validate();
            this.chunkTileEntityMap.put(var5, var4);
        } else {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
        }
    }

    public void removeChunkBlockTileEntity(int var1, int var2, int var3) {
        ChunkPosition var4 = new ChunkPosition(var1, var2, var3);
        if (this.isChunkLoaded) {
            TileEntity var5 = this.chunkTileEntityMap.remove(var4);
            if (var5 != null) {
                var5.invalidate();
            }
        }

    }

    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.func_31047_a(this.chunkTileEntityMap.values());

        for (int i = 0; i < this.entities.length; ++i) {
            this.worldObj.addLoadedEntities(this.entities[i]);
        }

    }

    public void onChunkUnload() {
        this.isChunkLoaded = false;

        for (TileEntity tileEntity : this.chunkTileEntityMap.values()) {
            tileEntity.invalidate();
        }

        for (int i = 0; i < this.entities.length; ++i) {
            this.worldObj.addUnloadedEntities(this.entities[i]);
        }

    }

    public void setChunkModified() {
        this.isModified = true;
    }

    public void getEntitiesWithinAABBForEntity(Entity var1, AxisAlignedBB var2, List<Entity> var3) {
        int var4 = MathHelper.floor((var2.minY - 2.0D) / 16.0D);
        int var5 = MathHelper.floor((var2.maxY + 2.0D) / 16.0D);
        if (var4 < 0) {
            var4 = 0;
        }

        if (var5 >= this.entities.length) {
            var5 = this.entities.length - 1;
        }

        for (int var6 = var4; var6 <= var5; ++var6) {
            List<Entity> entities = this.entities[var6];

            for (int var8 = 0; var8 < entities.size(); ++var8) {
                Entity var9 = entities.get(var8);
                if (var9 != var1 && var9.boundingBox.intersectsWith(var2)) {
                    var3.add(var9);
                }
            }
        }

    }

    public void getEntitiesOfTypeWithinAAAB(Class var1, AxisAlignedBB var2, List<Entity> var3) {
        int var4 = MathHelper.floor((var2.minY - 2.0D) / 16.0D);
        int var5 = MathHelper.floor((var2.maxY + 2.0D) / 16.0D);
        if (var4 < 0) {
            var4 = 0;
        }

        if (var5 >= this.entities.length) {
            var5 = this.entities.length - 1;
        }

        for (int i = var4; i <= var5; ++i) {
            List<Entity> var7 = this.entities[i];

            for (int var8 = 0; var8 < var7.size(); ++var8) {
                Entity var9 = var7.get(var8);
                if (var1.isAssignableFrom(var9.getClass()) && var9.boundingBox.intersectsWith(var2)) {
                    var3.add(var9);
                }
            }
        }

    }

    public boolean needsSaving(boolean var1) {
        if (this.neverSave) {
            return false;
        }

        if (var1) {
            if (this.hasEntities && this.worldObj.getWorldTime() != this.lastSaveTime) {
                return true;
            }
        } else if (this.hasEntities && this.worldObj.getWorldTime() >= this.lastSaveTime + 600L) {
            return true;
        }

        return this.isModified;
    }

    public int getChunkData(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        int var9 = var5 - var2;
        int var10 = var6 - var3;
        int var11 = var7 - var4;
        if (var9 * var10 * var11 == this.blocks.length) {
            System.arraycopy(this.blocks, 0, var1, var8, this.blocks.length);
            var8 = var8 + this.blocks.length;
            System.arraycopy(this.data.data, 0, var1, var8, this.data.data.length);
            var8 = var8 + this.data.data.length;
            System.arraycopy(this.blocklightMap.data, 0, var1, var8, this.blocklightMap.data.length);
            var8 = var8 + this.blocklightMap.data.length;
            System.arraycopy(this.skylightMap.data, 0, var1, var8, this.skylightMap.data.length);
            var8 = var8 + this.skylightMap.data.length;
            return var8;
        } else {
            for (int var12 = var2; var12 < var5; ++var12) {
                for (int var13 = var4; var13 < var7; ++var13) {
                    int var14 = var12 << 11 | var13 << 7 | var3;
                    int var15 = var6 - var3;
                    System.arraycopy(this.blocks, var14, var1, var8, var15);
                    var8 += var15;
                }
            }

            for (int var20 = var2; var20 < var5; ++var20) {
                for (int var23 = var4; var23 < var7; ++var23) {
                    int var26 = (var20 << 11 | var23 << 7 | var3) >> 1;
                    int var29 = (var6 - var3) / 2;
                    System.arraycopy(this.data.data, var26, var1, var8, var29);
                    var8 += var29;
                }
            }

            for (int var21 = var2; var21 < var5; ++var21) {
                for (int var24 = var4; var24 < var7; ++var24) {
                    int var27 = (var21 << 11 | var24 << 7 | var3) >> 1;
                    int var30 = (var6 - var3) / 2;
                    System.arraycopy(this.blocklightMap.data, var27, var1, var8, var30);
                    var8 += var30;
                }
            }

            for (int var22 = var2; var22 < var5; ++var22) {
                for (int var25 = var4; var25 < var7; ++var25) {
                    int var28 = (var22 << 11 | var25 << 7 | var3) >> 1;
                    int var31 = (var6 - var3) / 2;
                    System.arraycopy(this.skylightMap.data, var28, var1, var8, var31);
                    var8 += var31;
                }
            }

            return var8;
        }
    }

    public Random func_334_a(long var1) {
        return new Random(this.worldObj.getRandomSeed() + (long) (this.xPosition * this.xPosition * 4987142) + (long) (this.xPosition * 5947611) + (long) (this.zPosition * this.zPosition) * 4392871L + (long) (this.zPosition * 389711) ^ var1);
    }

    public boolean func_21101_g() {
        return false;
    }

    public void checkBlocks() {
        ChunkBlockMap.fix(this.blocks);
    }
}
