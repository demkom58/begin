package net.potion.world.gen;

import net.potion.block.Block;
import net.hypnosis.util.math.MathHelper;
import net.potion.world.World;

import java.util.Random;

public class MapGenCaves extends MapGenBase {
    protected void func_870_a(int var1, int var2, byte[] var3, double var4, double var6, double var8) {
        this.releaseEntitySkin(var1, var2, var3, var4, var6, var8, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void releaseEntitySkin(int var1, int var2, byte[] var3, double var4, double var6, double var8, float var10, float var11, float var12, int var13, int var14, double var15) {
        double var17 = var1 * 16 + 8;
        double var19 = var2 * 16 + 8;
        float var21 = 0.0F;
        float var22 = 0.0F;
        Random var23 = new Random(this.rand.nextLong());
        if (var14 <= 0) {
            int var24 = this.field_1306_a * 16 - 16;
            var14 = var24 - var23.nextInt(var24 / 4);
        }

        boolean var55 = false;
        if (var13 == -1) {
            var13 = var14 / 2;
            var55 = true;
        }

        int var25 = var23.nextInt(var14 / 2) + var14 / 4;

        for (boolean var26 = var23.nextInt(6) == 0; var13 < var14; ++var13) {
            double var27 = 1.5D + (double) (MathHelper.sin((float) var13 * 3.1415927F / (float) var14) * var10 * 1.0F);
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
            if (!var55 && var13 == var25 && var10 > 1.0F) {
                this.releaseEntitySkin(var1, var2, var3, var4, var6, var8, var23.nextFloat() * 0.5F + 0.5F, var11 - 1.5707964F, var12 / 3.0F, var13, var14, 1.0D);
                this.releaseEntitySkin(var1, var2, var3, var4, var6, var8, var23.nextFloat() * 0.5F + 0.5F, var11 + 1.5707964F, var12 / 3.0F, var13, var14, 1.0D);
                return;
            }

            if (var55 || var23.nextInt(4) != 0) {
                double var33 = var4 - var17;
                double var35 = var8 - var19;
                double var37 = var14 - var13;
                double var39 = var10 + 2.0F + 16.0F;
                if (var33 * var33 + var35 * var35 - var37 * var37 > var39 * var39) {
                    return;
                }

                if (var4 >= var17 - 16.0D - var27 * 2.0D && var8 >= var19 - 16.0D - var27 * 2.0D && var4 <= var17 + 16.0D + var27 * 2.0D && var8 <= var19 + 16.0D + var27 * 2.0D) {
                    int var56 = MathHelper.floor(var4 - var27) - var1 * 16 - 1;
                    int var34 = MathHelper.floor(var4 + var27) - var1 * 16 + 1;
                    int var57 = MathHelper.floor(var6 - var29) - 1;
                    int var36 = MathHelper.floor(var6 + var29) + 1;
                    int var58 = MathHelper.floor(var8 - var27) - var2 * 16 - 1;
                    int var38 = MathHelper.floor(var8 + var27) - var2 * 16 + 1;
                    if (var56 < 0) {
                        var56 = 0;
                    }

                    if (var34 > 16) {
                        var34 = 16;
                    }

                    if (var57 < 1) {
                        var57 = 1;
                    }

                    if (var36 > 120) {
                        var36 = 120;
                    }

                    if (var58 < 0) {
                        var58 = 0;
                    }

                    if (var38 > 16) {
                        var38 = 16;
                    }

                    boolean var59 = false;

                    for (int var40 = var56; !var59 && var40 < var34; ++var40) {
                        for (int var41 = var58; !var59 && var41 < var38; ++var41) {
                            for (int var42 = var36 + 1; !var59 && var42 >= var57 - 1; --var42) {
                                int var43 = (var40 * 16 + var41) * 128 + var42;
                                if (var42 >= 0 && var42 < 128) {
                                    if (var3[var43] == Block.WATER_MOVING.blockID || var3[var43] == Block.WATER_STILL.blockID) {
                                        var59 = true;
                                    }

                                    if (var42 != var57 - 1 && var40 != var56 && var40 != var34 - 1 && var41 != var58 && var41 != var38 - 1) {
                                        var42 = var57;
                                    }
                                }
                            }
                        }
                    }

                    if (!var59) {
                        for (int var60 = var56; var60 < var34; ++var60) {
                            double var61 = ((double) (var60 + var1 * 16) + 0.5D - var4) / var27;

                            for (int var62 = var58; var62 < var38; ++var62) {
                                double var44 = ((double) (var62 + var2 * 16) + 0.5D - var8) / var27;
                                int var46 = (var60 * 16 + var62) * 128 + var36;
                                boolean var47 = false;
                                if (var61 * var61 + var44 * var44 < 1.0D) {
                                    for (int var48 = var36 - 1; var48 >= var57; --var48) {
                                        double var49 = ((double) var48 + 0.5D - var6) / var29;
                                        if (var49 > -0.7D && var61 * var61 + var49 * var49 + var44 * var44 < 1.0D) {
                                            byte var51 = var3[var46];
                                            if (var51 == Block.GRASS.blockID) {
                                                var47 = true;
                                            }

                                            if (var51 == Block.STONE.blockID || var51 == Block.DIRT.blockID || var51 == Block.GRASS.blockID) {
                                                if (var48 < 10) {
                                                    var3[var46] = (byte) Block.LAVA_MOVING.blockID;
                                                } else {
                                                    var3[var46] = 0;
                                                    if (var47 && var3[var46 - 1] == Block.DIRT.blockID) {
                                                        var3[var46 - 1] = (byte) Block.GRASS.blockID;
                                                    }
                                                }
                                            }
                                        }

                                        --var46;
                                    }
                                }
                            }
                        }

                        if (var55) {
                            break;
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void func_868_a(World var1, int var2, int var3, int var4, int var5, byte[] var6) {
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
        if (this.rand.nextInt(15) != 0) {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8) {
            double var9 = var2 * 16 + this.rand.nextInt(16);
            double var11 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            double var13 = var3 * 16 + this.rand.nextInt(16);
            int var15 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.func_870_a(var4, var5, var6, var9, var11, var13);
                var15 += this.rand.nextInt(4);
            }

            for (int var16 = 0; var16 < var15; ++var16) {
                float var17 = this.rand.nextFloat() * 3.1415927F * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
                this.releaseEntitySkin(var4, var5, var6, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
            }
        }

    }
}
