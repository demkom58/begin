package net.minecraft.world.gen.struct;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTrees extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int var6 = random.nextInt(3) + 4;
        boolean var7 = true;
        if (y < 1 || y + var6 + 1 > 128) {
            return false;
        }

        for (int var8 = y; var8 <= y + 1 + var6; ++var8) {
            byte var9 = 1;
            if (var8 == y) {
                var9 = 0;
            }

            if (var8 >= y + 1 + var6 - 2) {
                var9 = 2;
            }

            for (int var10 = x - var9; var10 <= x + var9 && var7; ++var10) {
                for (int var11 = z - var9; var11 <= z + var9 && var7; ++var11) {
                    if (var8 >= 0 && var8 < 128) {
                        int var12 = world.getBlockId(var10, var8, var11);
                        if (var12 != 0 && var12 != Block.LEAVES.blockID) {
                            var7 = false;
                        }
                    } else {
                        var7 = false;
                    }
                }
            }
        }

        if (!var7) {
            return false;
        }

        int var16 = world.getBlockId(x, y - 1, z);
        if ((var16 != Block.GRASS.blockID && var16 != Block.DIRT.blockID) || y >= 128 - var6 - 1) {
            return false;
        }

        world.setBlock(x, y - 1, z, Block.DIRT.blockID);

        for (int var17 = y - 3 + var6; var17 <= y + var6; ++var17) {
            int var19 = var17 - (y + var6);
            int var21 = 1 - var19 / 2;

            for (int var22 = x - var21; var22 <= x + var21; ++var22) {
                int var13 = var22 - x;

                for (int var14 = z - var21; var14 <= z + var21; ++var14) {
                    int var15 = var14 - z;
                    if ((Math.abs(var13) != var21 || Math.abs(var15) != var21 || random.nextInt(2) != 0 && var19 != 0) && !Block.OPAQUE_CUBE_LOOKUP[world.getBlockId(var22, var17, var14)]) {
                        world.setBlock(var22, var17, var14, Block.LEAVES.blockID);
                    }
                }
            }
        }

        for (int var18 = 0; var18 < var6; ++var18) {
            int var20 = world.getBlockId(x, y + var18, z);
            if (var20 == 0 || var20 == Block.LEAVES.blockID) {
                world.setBlock(x, y + var18, z, Block.WOOD.blockID);
            }
        }

        return true;
    }
}
