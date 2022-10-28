package net.potion.world.gen.struct;

import net.hypnosis.util.math.MathConstants;
import net.potion.block.Block;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;

import java.util.Random;

public class MapGenCavesHell extends MapGenBase {
    protected void method2(int centerX, int centerZ, byte[] chunk, double var4, double var6, double var8) {
        this.method3(centerX, centerZ, chunk, var4, var6, var8, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void method3(int centerX, int centerZ, byte[] chunk, double var4, double var6, double var8, float var10, float var11, float var12, int var13, int var14, double var15) {
        double var17 = centerX * 16 + 8;
        double var19 = centerZ * 16 + 8;
        float var21 = 0.0F;
        float var22 = 0.0F;
        Random var23 = new Random(this.rand.nextLong());
        if (var14 <= 0) {
            int var24 = this.range * 16 - 16;
            var14 = var24 - var23.nextInt(var24 / 4);
        }

        boolean var54 = false;
        if (var13 == -1) {
            var13 = var14 / 2;
            var54 = true;
        }

        int var25 = var23.nextInt(var14 / 2) + var14 / 4;

        for (boolean var26 = var23.nextInt(6) == 0; var13 < var14; ++var13) {
            double var27 = 1.5D + (double) (MathHelper.sin((float) var13 * MathConstants.PI / (float) var14) * var10 * 1.0F);
            double var29 = var27 * var15;
            float var31 = MathHelper.cos(var12);
            float var32 = MathHelper.sin(var12);
            var4 += MathHelper.cos(var11) * var31;
            var6 += var32;
            var8 += MathHelper.sin(var11) * var31;
            if (var26) {
                var12 = var12 * 0.92F;
            } else {
                var12 = var12 * 0.7F;
            }

            var12 = var12 + var22 * 0.1F;
            var11 += var21 * 0.1F;
            var22 = var22 * 0.9F;
            var21 = var21 * 0.75F;
            var22 = var22 + (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 2.0F;
            var21 = var21 + (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 4.0F;
            if (!var54 && var13 == var25 && var10 > 1.0F) {
                this.method3(centerX, centerZ, chunk, var4, var6, var8, var23.nextFloat() * 0.5F + 0.5F, var11 - MathConstants.PI / 2f, var12 / 3.0F, var13, var14, 1.0D);
                this.method3(centerX, centerZ, chunk, var4, var6, var8, var23.nextFloat() * 0.5F + 0.5F, var11 + MathConstants.PI / 2f, var12 / 3.0F, var13, var14, 1.0D);
                return;
            }

            if (var54 || var23.nextInt(4) != 0) {
                double var33 = var4 - var17;
                double var35 = var8 - var19;
                double var37 = var14 - var13;
                double var39 = var10 + 2.0F + 16.0F;
                if (var33 * var33 + var35 * var35 - var37 * var37 > var39 * var39) {
                    return;
                }

                if (var4 >= var17 - 16.0D - var27 * 2.0D && var8 >= var19 - 16.0D - var27 * 2.0D && var4 <= var17 + 16.0D + var27 * 2.0D && var8 <= var19 + 16.0D + var27 * 2.0D) {
                    int var55 = MathHelper.floor(var4 - var27) - centerX * 16 - 1;
                    int var34 = MathHelper.floor(var4 + var27) - centerX * 16 + 1;
                    int var56 = MathHelper.floor(var6 - var29) - 1;
                    int var36 = MathHelper.floor(var6 + var29) + 1;
                    int var57 = MathHelper.floor(var8 - var27) - centerZ * 16 - 1;
                    int var38 = MathHelper.floor(var8 + var27) - centerZ * 16 + 1;
                    if (var55 < 0) {
                        var55 = 0;
                    }

                    if (var34 > 16) {
                        var34 = 16;
                    }

                    if (var56 < 1) {
                        var56 = 1;
                    }

                    if (var36 > 120) {
                        var36 = 120;
                    }

                    if (var57 < 0) {
                        var57 = 0;
                    }

                    if (var38 > 16) {
                        var38 = 16;
                    }

                    boolean var58 = false;

                    for (int var40 = var55; !var58 && var40 < var34; ++var40) {
                        for (int var41 = var57; !var58 && var41 < var38; ++var41) {
                            for (int var42 = var36 + 1; !var58 && var42 >= var56 - 1; --var42) {
                                int var43 = (var40 * 16 + var41) * 128 + var42;
                                if (var42 >= 0 && var42 < 128) {
                                    if (chunk[var43] == Block.LAVA_MOVING.blockID || chunk[var43] == Block.LAVA_STILL.blockID) {
                                        var58 = true;
                                    }

                                    if (var42 != var56 - 1 && var40 != var55 && var40 != var34 - 1 && var41 != var57 && var41 != var38 - 1) {
                                        var42 = var56;
                                    }
                                }
                            }
                        }
                    }

                    if (!var58) {
                        for (int var59 = var55; var59 < var34; ++var59) {
                            double var60 = ((double) (var59 + centerX * 16) + 0.5D - var4) / var27;

                            for (int var61 = var57; var61 < var38; ++var61) {
                                double var44 = ((double) (var61 + centerZ * 16) + 0.5D - var8) / var27;
                                int var46 = (var59 * 16 + var61) * 128 + var36;

                                for (int var47 = var36 - 1; var47 >= var56; --var47) {
                                    double var48 = ((double) var47 + 0.5D - var6) / var29;
                                    if (var48 > -0.7D && var60 * var60 + var48 * var48 + var44 * var44 < 1.0D) {
                                        byte var50 = chunk[var46];
                                        if (var50 == Block.BLOOD_STONE.blockID || var50 == Block.DIRT.blockID || var50 == Block.GRASS.blockID) {
                                            chunk[var46] = 0;
                                        }
                                    }

                                    --var46;
                                }
                            }
                        }

                        if (var54) {
                            break;
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void recursiveGenerate(World world, int x, int z, int centerX, int centerZ, byte[] chunk) {
        int count = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
        if (this.rand.nextInt(5) != 0) {
            count = 0;
        }

        for (int i = 0; i < count; ++i) {
            double rX = x * 16 + this.rand.nextInt(16);
            double rY = this.rand.nextInt(128);
            double rZ = z * 16 + this.rand.nextInt(16);

            int var15 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.method2(centerX, centerZ, chunk, rX, rY, rZ);
                var15 += this.rand.nextInt(4);
            }

            for (int j = 0; j < var15; ++j) {
                float var17 = this.rand.nextFloat() * MathConstants.PI * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
                this.method3(centerX, centerZ, chunk, rX, rY, rZ, var19 * 2.0F, var17, var18, 0, 0, 0.5D);
            }
        }

    }
}
