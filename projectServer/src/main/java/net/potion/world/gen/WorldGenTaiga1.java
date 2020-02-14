package net.potion.world.gen;

import net.potion.world.World;
import net.potion.block.Block;

import java.util.Random;

public class WorldGenTaiga1 extends WorldGenerator {
    @Override
    public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
        int var6 = var2.nextInt(5) + 7;
        int var7 = var6 - var2.nextInt(2) - 3;
        int var8 = var6 - var7;
        int var9 = 1 + var2.nextInt(var8 + 1);
        boolean var10 = true;
        if (var4 >= 1 && var4 + var6 + 1 <= 128) {
            for (int var11 = var4; var11 <= var4 + 1 + var6 && var10; ++var11) {
                int var12 = 1;
                if (var11 - var4 < var7) {
                    var12 = 0;
                } else {
                    var12 = var9;
                }

                for (int var13 = var3 - var12; var13 <= var3 + var12 && var10; ++var13) {
                    for (int var14 = var5 - var12; var14 <= var5 + var12 && var10; ++var14) {
                        if (var11 >= 0 && var11 < 128) {
                            int var15 = var1.getBlockId(var13, var11, var14);
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
            } else {
                int var18 = var1.getBlockId(var3, var4 - 1, var5);
                if ((var18 == Block.GRASS.blockID || var18 == Block.DIRT.blockID) && var4 < 128 - var6 - 1) {
                    var1.setBlock(var3, var4 - 1, var5, Block.DIRT.blockID);
                    int var20 = 0;

                    for (int var21 = var4 + var6; var21 >= var4 + var7; --var21) {
                        for (int var23 = var3 - var20; var23 <= var3 + var20; ++var23) {
                            int var25 = var23 - var3;

                            for (int var16 = var5 - var20; var16 <= var5 + var20; ++var16) {
                                int var17 = var16 - var5;
                                if ((Math.abs(var25) != var20 || Math.abs(var17) != var20 || var20 <= 0) && !Block.OPAQUE_CUBE_LOOKUP[var1.getBlockId(var23, var21, var16)]) {
                                    var1.setBlockAndMetadata(var23, var21, var16, Block.LEAVES.blockID, 1);
                                }
                            }
                        }

                        if (var20 >= 1 && var21 == var4 + var7 + 1) {
                            --var20;
                        } else if (var20 < var9) {
                            ++var20;
                        }
                    }

                    for (int var22 = 0; var22 < var6 - 1; ++var22) {
                        int var24 = var1.getBlockId(var3, var4 + var22, var5);
                        if (var24 == 0 || var24 == Block.LEAVES.blockID) {
                            var1.setBlockAndMetadata(var3, var4 + var22, var5, Block.WOOD.blockID, 1);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
