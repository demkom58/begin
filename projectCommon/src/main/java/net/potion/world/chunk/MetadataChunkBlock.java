package net.potion.world.chunk;

import net.potion.block.Block;
import net.potion.block.EnumSkyBlock;
import net.potion.world.World;

public class MetadataChunkBlock {
    public final EnumSkyBlock skyBlock;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public MetadataChunkBlock(EnumSkyBlock skyBlock, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.skyBlock = skyBlock;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void method1(World world) {
        int rngX = this.maxX - this.minX + 1;
        int rngY = this.maxY - this.minY + 1;
        int rngZ = this.maxZ - this.minZ + 1;
        int volume = rngX * rngY * rngZ;

        if (volume > 32768) {
            System.out.println("Light too large, skipping!");
            return;
        }

        int var6 = 0;
        int var7 = 0;
        boolean var8 = false;
        boolean var9 = false;

        for (int x = this.minX; x <= this.maxX; ++x) {
            for (int z = this.minZ; z <= this.maxZ; ++z) {
                int var12 = x >> 4;
                int var13 = z >> 4;
                boolean var14 = false;
                if (var8 && var12 == var6 && var13 == var7) {
                    var14 = var9;
                } else {
                    var14 = world.doChunksNearChunkExist(x, 0, z, 1);
                    if (var14) {
                        Chunk var15 = world.getChunkFromChunkCoords(x >> 4, z >> 4);
                        if (var15.isEmptyChunk()) {
                            var14 = false;
                        }
                    }

                    var9 = var14;
                    var6 = var12;
                    var7 = var13;
                }

                if (!var14) {
                    continue;
                }

                if (this.minY < 0) {
                    this.minY = 0;
                }

                if (this.maxY >= 128) {
                    this.maxY = 127;
                }

                for (int var28 = this.minY; var28 <= this.maxY; ++var28) {
                    int var16 = world.getSavedLightValue(this.skyBlock, x, var28, z);
                    int var17 = 0;
                    int var18 = world.getBlockId(x, var28, z);
                    int var19 = Block.LIGHT_OPACITY[var18];
                    if (var19 == 0) {
                        var19 = 1;
                    }

                    int var20 = 0;
                    if (this.skyBlock == EnumSkyBlock.SKY) {
                        if (world.canExistingBlockSeeTheSky(x, var28, z)) {
                            var20 = 15;
                        }
                    } else if (this.skyBlock == EnumSkyBlock.BLOCK) {
                        var20 = Block.LIGHT_VALUE[var18];
                    }

                    if (var19 >= 15 && var20 == 0) {
                        var17 = 0;
                    } else {
                        int var21 = world.getSavedLightValue(this.skyBlock, x - 1, var28, z);
                        int var22 = world.getSavedLightValue(this.skyBlock, x + 1, var28, z);
                        int var23 = world.getSavedLightValue(this.skyBlock, x, var28 - 1, z);
                        int var24 = world.getSavedLightValue(this.skyBlock, x, var28 + 1, z);
                        int var25 = world.getSavedLightValue(this.skyBlock, x, var28, z - 1);
                        int var26 = world.getSavedLightValue(this.skyBlock, x, var28, z + 1);
                        var17 = var21;
                        if (var22 > var21) {
                            var17 = var22;
                        }

                        if (var23 > var17) {
                            var17 = var23;
                        }

                        if (var24 > var17) {
                            var17 = var24;
                        }

                        if (var25 > var17) {
                            var17 = var25;
                        }

                        if (var26 > var17) {
                            var17 = var26;
                        }

                        var17 = var17 - var19;
                        if (var17 < 0) {
                            var17 = 0;
                        }

                        if (var20 > var17) {
                            var17 = var20;
                        }
                    }

                    if (var16 != var17) {
                        world.setLightValue(this.skyBlock, x, var28, z, var17);
                        int var31 = var17 - 1;
                        if (var31 < 0) {
                            var31 = 0;
                        }

                        world.neighborLightPropagationChanged(this.skyBlock, x - 1, var28, z, var31);
                        world.neighborLightPropagationChanged(this.skyBlock, x, var28 - 1, z, var31);
                        world.neighborLightPropagationChanged(this.skyBlock, x, var28, z - 1, var31);
                        if (x + 1 >= this.maxX) {
                            world.neighborLightPropagationChanged(this.skyBlock, x + 1, var28, z, var31);
                        }

                        if (var28 + 1 >= this.maxY) {
                            world.neighborLightPropagationChanged(this.skyBlock, x, var28 + 1, z, var31);
                        }

                        if (z + 1 >= this.maxZ) {
                            world.neighborLightPropagationChanged(this.skyBlock, x, var28, z + 1, var31);
                        }
                    }
                }
            }
        }
    }

    public boolean method2(int var1, int var2, int var3, int var4, int var5, int var6) {
        if (var1 >= this.minX && var2 >= this.minY && var3 >= this.minZ && var4 <= this.maxX && var5 <= this.maxY && var6 <= this.maxZ) {
            return true;
        }

        byte offset = 1;
        if (var1 < this.minX - offset
                || var2 < this.minY - offset
                || var3 < this.minZ - offset
                || var4 > this.maxX + offset
                || var5 > this.maxY + offset
                || var6 > this.maxZ + offset) {
            return false;
        }

        int var8 = this.maxX - this.minX;
        int var9 = this.maxY - this.minY;
        int var10 = this.maxZ - this.minZ;
        if (var1 > this.minX) {
            var1 = this.minX;
        }

        if (var2 > this.minY) {
            var2 = this.minY;
        }

        if (var3 > this.minZ) {
            var3 = this.minZ;
        }

        if (var4 < this.maxX) {
            var4 = this.maxX;
        }

        if (var5 < this.maxY) {
            var5 = this.maxY;
        }

        if (var6 < this.maxZ) {
            var6 = this.maxZ;
        }

        int var11 = var4 - var1;
        int var12 = var5 - var2;
        int var13 = var6 - var3;
        int var14 = var8 * var9 * var10;
        int var15 = var11 * var12 * var13;
        if (var15 - var14 <= 2) {
            this.minX = var1;
            this.minY = var2;
            this.minZ = var3;
            this.maxX = var4;
            this.maxY = var5;
            this.maxZ = var6;
            return true;
        }

        return false;
    }
}
