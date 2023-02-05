package net.minecraft.world.gen.struct;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTaiga2 extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int var6 = random.nextInt(4) + 6;
        int var7 = 1 + random.nextInt(2);
        int var8 = var6 - var7;
        int var9 = 2 + random.nextInt(2);
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

        int var21 = world.getBlockId(x, y - 1, z);
        if ((var21 != Block.GRASS.blockID && var21 != Block.DIRT.blockID) || y >= 128 - var6 - 1) {
            return false;
        }

        world.setBlock(x, y - 1, z, Block.DIRT.blockID);
        int var23 = random.nextInt(2);
        int var24 = 1;
        byte var25 = 0;

        for (int var26 = 0; var26 <= var8; ++var26) {
            int var16 = y + var6 - var26;

            for (int var17 = x - var23; var17 <= x + var23; ++var17) {
                int var18 = var17 - x;

                for (int var19 = z - var23; var19 <= z + var23; ++var19) {
                    int var20 = var19 - z;
                    if ((Math.abs(var18) != var23 || Math.abs(var20) != var23 || var23 <= 0) && !Block.OPAQUE_CUBE_LOOKUP[world.getBlockId(var17, var16, var19)]) {
                        world.setBlockAndMetadata(var17, var16, var19, Block.LEAVES.blockID, 1);
                    }
                }
            }

            if (var23 >= var24) {
                var23 = var25;
                var25 = 1;
                ++var24;
                if (var24 > var9) {
                    var24 = var9;
                }
            } else {
                ++var23;
            }
        }

        int var27 = random.nextInt(3);

        for (int var28 = 0; var28 < var6 - var27; ++var28) {
            int var29 = world.getBlockId(x, y + var28, z);
            if (var29 == 0 || var29 == Block.LEAVES.blockID) {
                world.setBlockAndMetadata(x, y + var28, z, Block.WOOD.blockID, 1);
            }
        }

        return true;

    }
}
