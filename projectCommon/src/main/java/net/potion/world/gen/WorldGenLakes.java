package net.potion.world.gen;

import net.potion.block.Block;
import net.potion.block.EnumSkyBlock;
import net.potion.material.Material;
import net.potion.world.World;

import java.util.Random;

public class WorldGenLakes extends WorldGenerator {
    private int blockId;

    public WorldGenLakes(int var1) {
        this.blockId = var1;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        x = x - 8;

        z = z - 8;
        while (y > 0 && world.isAirBlock(x, y, z)) {
            --y;
        }

        y = y - 4;
        boolean[] var6 = new boolean[2048];
        int var7 = random.nextInt(4) + 4;

        for (int var8 = 0; var8 < var7; ++var8) {
            double var9 = random.nextDouble() * 6.0D + 3.0D;
            double var11 = random.nextDouble() * 4.0D + 2.0D;
            double var13 = random.nextDouble() * 6.0D + 3.0D;
            double var15 = random.nextDouble() * (16.0D - var9 - 2.0D) + 1.0D + var9 / 2.0D;
            double var17 = random.nextDouble() * (8.0D - var11 - 4.0D) + 2.0D + var11 / 2.0D;
            double var19 = random.nextDouble() * (16.0D - var13 - 2.0D) + 1.0D + var13 / 2.0D;

            for (int var21 = 1; var21 < 15; ++var21) {
                for (int var22 = 1; var22 < 15; ++var22) {
                    for (int var23 = 1; var23 < 7; ++var23) {
                        double var24 = ((double) var21 - var15) / (var9 / 2.0D);
                        double var26 = ((double) var23 - var17) / (var11 / 2.0D);
                        double var28 = ((double) var22 - var19) / (var13 / 2.0D);
                        double var30 = var24 * var24 + var26 * var26 + var28 * var28;
                        if (var30 < 1.0D) {
                            var6[(var21 * 16 + var22) * 8 + var23] = true;
                        }
                    }
                }
            }
        }

        for (int var35 = 0; var35 < 16; ++var35) {
            for (int var39 = 0; var39 < 16; ++var39) {
                for (int var10 = 0; var10 < 8; ++var10) {
                    boolean var46 = !var6[(var35 * 16 + var39) * 8 + var10] && (var35 < 15 && var6[((var35 + 1) * 16 + var39) * 8 + var10] || var35 > 0 && var6[((var35 - 1) * 16 + var39) * 8 + var10] || var39 < 15 && var6[(var35 * 16 + var39 + 1) * 8 + var10] || var39 > 0 && var6[(var35 * 16 + (var39 - 1)) * 8 + var10] || var10 < 7 && var6[(var35 * 16 + var39) * 8 + var10 + 1] || var10 > 0 && var6[(var35 * 16 + var39) * 8 + (var10 - 1)]);
                    if (var46) {
                        Material var12 = world.getBlockMaterial(x + var35, y + var10, z + var39);
                        if (var10 >= 4 && var12.isLiquid()) {
                            return false;
                        }

                        if (var10 < 4 && !var12.isSolid() && world.getBlockId(x + var35, y + var10, z + var39) != this.blockId) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int var36 = 0; var36 < 16; ++var36) {
            for (int var40 = 0; var40 < 16; ++var40) {
                for (int var43 = 0; var43 < 8; ++var43) {
                    if (var6[(var36 * 16 + var40) * 8 + var43]) {
                        world.setBlock(x + var36, y + var43, z + var40, var43 >= 4 ? 0 : this.blockId);
                    }
                }
            }
        }

        for (int var37 = 0; var37 < 16; ++var37) {
            for (int var41 = 0; var41 < 16; ++var41) {
                for (int var44 = 4; var44 < 8; ++var44) {
                    if (var6[(var37 * 16 + var41) * 8 + var44] && world.getBlockId(x + var37, y + var44 - 1, z + var41) == Block.DIRT.blockID && world.getSavedLightValue(EnumSkyBlock.SKY, x + var37, y + var44, z + var41) > 0) {
                        world.setBlock(x + var37, y + var44 - 1, z + var41, Block.GRASS.blockID);
                    }
                }
            }
        }

        if (Block.BLOCKS_LIST[this.blockId].blockMaterial == Material.LAVA) {
            for (int var38 = 0; var38 < 16; ++var38) {
                for (int var42 = 0; var42 < 16; ++var42) {
                    for (int var45 = 0; var45 < 8; ++var45) {
                        boolean var47 = !var6[(var38 * 16 + var42) * 8 + var45] && (var38 < 15 && var6[((var38 + 1) * 16 + var42) * 8 + var45] || var38 > 0 && var6[((var38 - 1) * 16 + var42) * 8 + var45] || var42 < 15 && var6[(var38 * 16 + var42 + 1) * 8 + var45] || var42 > 0 && var6[(var38 * 16 + (var42 - 1)) * 8 + var45] || var45 < 7 && var6[(var38 * 16 + var42) * 8 + var45 + 1] || var45 > 0 && var6[(var38 * 16 + var42) * 8 + (var45 - 1)]);
                        if (var47 && (var45 < 4 || random.nextInt(2) != 0) && world.getBlockMaterial(x + var38, y + var45, z + var42).isSolid()) {
                            world.setBlock(x + var38, y + var45, z + var42, Block.STONE.blockID);
                        }
                    }
                }
            }
        }

        return true;
    }
}
