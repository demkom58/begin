package net.potion.world.gen;

import net.potion.block.Block;
import net.potion.world.World;

import java.util.Random;

public class WorldGenTaiga1 extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int var6 = random.nextInt(5) + 7;
        int var7 = var6 - random.nextInt(2) - 3;
        int var8 = var6 - var7;
        int var9 = 1 + random.nextInt(var8 + 1);
        boolean var10 = true;
        if (y < 1 || y + var6 + 1 > 128) {
            return false;
        }

        for (int var11 = y; var11 <= y + 1 + var6 && var10; ++var11) {
            int var12 = 1;
            if (var11 - y < var7) {
                var12 = 0;
            } else {
                var12 = var9;
            }

            for (int var13 = x - var12; var13 <= x + var12 && var10; ++var13) {
                for (int var14 = z - var12; var14 <= z + var12 && var10; ++var14) {
                    if (var11 >= 0 && var11 < 128) {
                        int var15 = world.getBlockId(var13, var11, var14);
                        if (var15 != 0 && var15 != Block.LEAVES.blockID) {
                            var10 = false;
                        }
                    } else {
                        var10 = false;
                    }
                }
            }
        }

        if (!var10) {
            return false;
        }

        int var18 = world.getBlockId(x, y - 1, z);
        if ((var18 != Block.GRASS.blockID && var18 != Block.DIRT.blockID) || y >= 128 - var6 - 1) {
            return false;
        }

        world.setBlock(x, y - 1, z, Block.DIRT.blockID);
        int var20 = 0;

        for (int var21 = y + var6; var21 >= y + var7; --var21) {
            for (int var23 = x - var20; var23 <= x + var20; ++var23) {
                int var25 = var23 - x;

                for (int var16 = z - var20; var16 <= z + var20; ++var16) {
                    int var17 = var16 - z;
                    if ((Math.abs(var25) != var20 || Math.abs(var17) != var20 || var20 <= 0) && !Block.OPAQUE_CUBE_LOOKUP[world.getBlockId(var23, var21, var16)]) {
                        world.setBlockAndMetadata(var23, var21, var16, Block.LEAVES.blockID, 1);
                    }
                }
            }

            if (var20 >= 1 && var21 == y + var7 + 1) {
                --var20;
            } else if (var20 < var9) {
                ++var20;
            }
        }

        for (int var22 = 0; var22 < var6 - 1; ++var22) {
            int var24 = world.getBlockId(x, y + var22, z);
            if (var24 == 0 || var24 == Block.LEAVES.blockID) {
                world.setBlockAndMetadata(x, y + var22, z, Block.WOOD.blockID, 1);
            }
        }

        return true;

    }
}
