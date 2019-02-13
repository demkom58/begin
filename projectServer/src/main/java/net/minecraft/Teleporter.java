package net.minecraft;

import java.util.Random;

public class Teleporter {
    private Random field_28117_a = new Random();

    public void setExitLocation(World var1, Entity var2) {
        if (!this.findExitLocation(var1, var2)) {
            this.createExitLocation(var1, var2);
            this.findExitLocation(var1, var2);
        }
    }

    public boolean findExitLocation(World var1, Entity var2) {
        short var3 = 128;
        double var4 = -1.0D;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        int var9 = MathHelper.floor_double(var2.posX);
        int var10 = MathHelper.floor_double(var2.posZ);

        for (int var11 = var9 - var3; var11 <= var9 + var3; ++var11) {
            double var12 = (double) var11 + 0.5D - var2.posX;

            for (int var14 = var10 - var3; var14 <= var10 + var3; ++var14) {
                double var15 = (double) var14 + 0.5D - var2.posZ;

                for (int var17 = 127; var17 >= 0; --var17) {
                    if (var1.getBlockId(var11, var17, var14) == Block.portal.blockID) {
                        while (var1.getBlockId(var11, var17 - 1, var14) == Block.portal.blockID) {
                            --var17;
                        }

                        double var18 = (double) var17 + 0.5D - var2.posY;
                        double var20 = var12 * var12 + var18 * var18 + var15 * var15;
                        if (var4 < 0.0D || var20 < var4) {
                            var4 = var20;
                            var6 = var11;
                            var7 = var17;
                            var8 = var14;
                        }
                    }
                }
            }
        }

        if (var4 >= 0.0D) {
            double var22 = (double) var6 + 0.5D;
            double var16 = (double) var7 + 0.5D;
            double var23 = (double) var8 + 0.5D;
            if (var1.getBlockId(var6 - 1, var7, var8) == Block.portal.blockID) {
                var22 -= 0.5D;
            }

            if (var1.getBlockId(var6 + 1, var7, var8) == Block.portal.blockID) {
                var22 += 0.5D;
            }

            if (var1.getBlockId(var6, var7, var8 - 1) == Block.portal.blockID) {
                var23 -= 0.5D;
            }

            if (var1.getBlockId(var6, var7, var8 + 1) == Block.portal.blockID) {
                var23 += 0.5D;
            }

            var2.setLocationAndAngles(var22, var16, var23, var2.rotationYaw, 0.0F);
            var2.motionX = var2.motionY = var2.motionZ = 0.0D;
            return true;
        } else {
            return false;
        }
    }

    public boolean createExitLocation(World var1, Entity var2) {
        byte var3 = 16;
        double var4 = -1.0D;
        int var6 = MathHelper.floor_double(var2.posX);
        int var7 = MathHelper.floor_double(var2.posY);
        int var8 = MathHelper.floor_double(var2.posZ);
        int var9 = var6;
        int var10 = var7;
        int var11 = var8;
        int var12 = 0;
        int var13 = this.field_28117_a.nextInt(4);

        for (int var14 = var6 - var3; var14 <= var6 + var3; ++var14) {
            double var15 = (double) var14 + 0.5D - var2.posX;

            for (int var17 = var8 - var3; var17 <= var8 + var3; ++var17) {
                double var18 = (double) var17 + 0.5D - var2.posZ;

                label293:
                for (int var20 = 127; var20 >= 0; --var20) {
                    if (var1.isAirBlock(var14, var20, var17)) {
                        while (var20 > 0 && var1.isAirBlock(var14, var20 - 1, var17)) {
                            --var20;
                        }

                        for (int var21 = var13; var21 < var13 + 4; ++var21) {
                            int var22 = var21 % 2;
                            int var23 = 1 - var22;
                            if (var21 % 4 >= 2) {
                                var22 = -var22;
                                var23 = -var23;
                            }

                            for (int var24 = 0; var24 < 3; ++var24) {
                                for (int var25 = 0; var25 < 4; ++var25) {
                                    for (int var26 = -1; var26 < 4; ++var26) {
                                        int var27 = var14 + (var25 - 1) * var22 + var24 * var23;
                                        int var28 = var20 + var26;
                                        int var29 = var17 + (var25 - 1) * var23 - var24 * var22;
                                        if (var26 < 0 && !var1.getBlockMaterial(var27, var28, var29).isSolid() || var26 >= 0 && !var1.isAirBlock(var27, var28, var29)) {
                                            continue label293;
                                        }
                                    }
                                }
                            }

                            double var52 = (double) var20 + 0.5D - var2.posY;
                            double var62 = var15 * var15 + var52 * var52 + var18 * var18;
                            if (var4 < 0.0D || var62 < var4) {
                                var4 = var62;
                                var9 = var14;
                                var10 = var20;
                                var11 = var17;
                                var12 = var21 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (var4 < 0.0D) {
            for (int var30 = var6 - var3; var30 <= var6 + var3; ++var30) {
                double var31 = (double) var30 + 0.5D - var2.posX;

                for (int var33 = var8 - var3; var33 <= var8 + var3; ++var33) {
                    double var35 = (double) var33 + 0.5D - var2.posZ;

                    label231:
                    for (int var37 = 127; var37 >= 0; --var37) {
                        if (var1.isAirBlock(var30, var37, var33)) {
                            while (var1.isAirBlock(var30, var37 - 1, var33)) {
                                --var37;
                            }

                            for (int var40 = var13; var40 < var13 + 2; ++var40) {
                                int var44 = var40 % 2;
                                int var48 = 1 - var44;

                                for (int var53 = 0; var53 < 4; ++var53) {
                                    for (int var58 = -1; var58 < 4; ++var58) {
                                        int var63 = var30 + (var53 - 1) * var44;
                                        int var67 = var37 + var58;
                                        int var68 = var33 + (var53 - 1) * var48;
                                        if (var58 < 0 && !var1.getBlockMaterial(var63, var67, var68).isSolid() || var58 >= 0 && !var1.isAirBlock(var63, var67, var68)) {
                                            continue label231;
                                        }
                                    }
                                }

                                double var54 = (double) var37 + 0.5D - var2.posY;
                                double var64 = var31 * var31 + var54 * var54 + var35 * var35;
                                if (var4 < 0.0D || var64 < var4) {
                                    var4 = var64;
                                    var9 = var30;
                                    var10 = var37;
                                    var11 = var33;
                                    var12 = var40 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int var32 = var9;
        int var16 = var10;
        int var34 = var11;
        int var36 = var12 % 2;
        int var19 = 1 - var36;
        if (var12 % 4 >= 2) {
            var36 = -var36;
            var19 = -var19;
        }

        if (var4 < 0.0D) {
            if (var10 < 70) {
                var10 = 70;
            }

            if (var10 > 118) {
                var10 = 118;
            }

            var16 = var10;

            for (int var38 = -1; var38 <= 1; ++var38) {
                for (int var41 = 1; var41 < 3; ++var41) {
                    for (int var45 = -1; var45 < 3; ++var45) {
                        int var49 = var32 + (var41 - 1) * var36 + var38 * var19;
                        int var55 = var16 + var45;
                        int var59 = var34 + (var41 - 1) * var19 - var38 * var36;
                        boolean var65 = var45 < 0;
                        var1.setBlockWithNotify(var49, var55, var59, var65 ? Block.obsidian.blockID : 0);
                    }
                }
            }
        }

        for (int var39 = 0; var39 < 4; ++var39) {
            var1.editingBlocks = true;

            for (int var42 = 0; var42 < 4; ++var42) {
                for (int var46 = -1; var46 < 4; ++var46) {
                    int var50 = var32 + (var42 - 1) * var36;
                    int var56 = var16 + var46;
                    int var60 = var34 + (var42 - 1) * var19;
                    boolean var66 = var42 == 0 || var42 == 3 || var46 == -1 || var46 == 3;
                    var1.setBlockWithNotify(var50, var56, var60, var66 ? Block.obsidian.blockID : Block.portal.blockID);
                }
            }

            var1.editingBlocks = false;

            for (int var43 = 0; var43 < 4; ++var43) {
                for (int var47 = -1; var47 < 4; ++var47) {
                    int var51 = var32 + (var43 - 1) * var36;
                    int var57 = var16 + var47;
                    int var61 = var34 + (var43 - 1) * var19;
                    var1.notifyBlocksOfNeighborChange(var51, var57, var61, var1.getBlockId(var51, var57, var61));
                }
            }
        }

        return true;
    }
}
