package net.minecraft;

import java.util.Random;

public class WorldGenDungeons extends WorldGenerator {
    public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
        byte var6 = 3;
        int var7 = var2.nextInt(2) + 2;
        int var8 = var2.nextInt(2) + 2;
        int var9 = 0;

        for (int var10 = var3 - var7 - 1; var10 <= var3 + var7 + 1; ++var10) {
            for (int var11 = var4 - 1; var11 <= var4 + var6 + 1; ++var11) {
                for (int var12 = var5 - var8 - 1; var12 <= var5 + var8 + 1; ++var12) {
                    Material var13 = var1.getBlockMaterial(var10, var11, var12);
                    if (var11 == var4 - 1 && !var13.isSolid()) {
                        return false;
                    }

                    if (var11 == var4 + var6 + 1 && !var13.isSolid()) {
                        return false;
                    }

                    if ((var10 == var3 - var7 - 1 || var10 == var3 + var7 + 1 || var12 == var5 - var8 - 1 || var12 == var5 + var8 + 1) && var11 == var4 && var1.isAirBlock(var10, var11, var12) && var1.isAirBlock(var10, var11 + 1, var12)) {
                        ++var9;
                    }
                }
            }
        }

        if (var9 >= 1 && var9 <= 5) {
            for (int var19 = var3 - var7 - 1; var19 <= var3 + var7 + 1; ++var19) {
                for (int var22 = var4 + var6; var22 >= var4 - 1; --var22) {
                    for (int var24 = var5 - var8 - 1; var24 <= var5 + var8 + 1; ++var24) {
                        if (var19 != var3 - var7 - 1 && var22 != var4 - 1 && var24 != var5 - var8 - 1 && var19 != var3 + var7 + 1 && var22 != var4 + var6 + 1 && var24 != var5 + var8 + 1) {
                            var1.setBlockWithNotify(var19, var22, var24, 0);
                        } else if (var22 >= 0 && !var1.getBlockMaterial(var19, var22 - 1, var24).isSolid()) {
                            var1.setBlockWithNotify(var19, var22, var24, 0);
                        } else if (var1.getBlockMaterial(var19, var22, var24).isSolid()) {
                            if (var22 == var4 - 1 && var2.nextInt(4) != 0) {
                                var1.setBlockWithNotify(var19, var22, var24, Block.COBBLESTONE_MOSSY.blockID);
                            } else {
                                var1.setBlockWithNotify(var19, var22, var24, Block.COBBLESTONE.blockID);
                            }
                        }
                    }
                }
            }

            label110:
            for (int var20 = 0; var20 < 2; ++var20) {
                for (int var23 = 0; var23 < 3; ++var23) {
                    int var25 = var3 + var2.nextInt(var7 * 2 + 1) - var7;
                    int var14 = var5 + var2.nextInt(var8 * 2 + 1) - var8;
                    if (var1.isAirBlock(var25, var4, var14)) {
                        int var15 = 0;
                        if (var1.getBlockMaterial(var25 - 1, var4, var14).isSolid()) {
                            ++var15;
                        }

                        if (var1.getBlockMaterial(var25 + 1, var4, var14).isSolid()) {
                            ++var15;
                        }

                        if (var1.getBlockMaterial(var25, var4, var14 - 1).isSolid()) {
                            ++var15;
                        }

                        if (var1.getBlockMaterial(var25, var4, var14 + 1).isSolid()) {
                            ++var15;
                        }

                        if (var15 == 1) {
                            var1.setBlockWithNotify(var25, var4, var14, Block.CHEST.blockID);
                            TileEntityChest var16 = (TileEntityChest) var1.getBlockTileEntity(var25, var4, var14);
                            int var17 = 0;

                            while (true) {
                                if (var17 >= 8) {
                                    continue label110;
                                }

                                ItemStack var18 = this.pickCheckLootItem(var2);
                                if (var18 != null) {
                                    var16.setInventorySlotContents(var2.nextInt(var16.getSizeInventory()), var18);
                                }

                                ++var17;
                            }
                        }
                    }
                }
            }

            var1.setBlockWithNotify(var3, var4, var5, Block.MOB_SPAWNER.blockID);
            TileEntityMobSpawner var21 = (TileEntityMobSpawner) var1.getBlockTileEntity(var3, var4, var5);
            var21.setMobID(this.pickMobSpawner(var2));
            return true;
        } else {
            return false;
        }
    }

    private ItemStack pickCheckLootItem(Random var1) {
        int var2 = var1.nextInt(11);
        if (var2 == 0) {
            return new ItemStack(Item.SADDLE);
        } else if (var2 == 1) {
            return new ItemStack(Item.INGOT_IRON, var1.nextInt(4) + 1);
        } else if (var2 == 2) {
            return new ItemStack(Item.BREAD);
        } else if (var2 == 3) {
            return new ItemStack(Item.WHEAT, var1.nextInt(4) + 1);
        } else if (var2 == 4) {
            return new ItemStack(Item.GUNPOWDER, var1.nextInt(4) + 1);
        } else if (var2 == 5) {
            return new ItemStack(Item.SILK, var1.nextInt(4) + 1);
        } else if (var2 == 6) {
            return new ItemStack(Item.BUCKET_EMPTY);
        } else if (var2 == 7 && var1.nextInt(100) == 0) {
            return new ItemStack(Item.APPLE_GOLD);
        } else if (var2 == 8 && var1.nextInt(2) == 0) {
            return new ItemStack(Item.REDSTONE, var1.nextInt(4) + 1);
        } else if (var2 == 9 && var1.nextInt(10) == 0) {
            return new ItemStack(Item.ITEMS_LIST[Item.RECORD_13.shiftedIndex + var1.nextInt(2)]);
        } else {
            return var2 == 10 ? new ItemStack(Item.DYE_POWDER, 1, 3) : null;
        }
    }

    private String pickMobSpawner(Random var1) {
        int var2 = var1.nextInt(4);
        if (var2 == 0) {
            return "Skeleton";
        } else if (var2 == 1) {
            return "Zombie";
        } else if (var2 == 2) {
            return "Zombie";
        } else {
            return var2 == 3 ? "Spider" : "";
        }
    }
}
