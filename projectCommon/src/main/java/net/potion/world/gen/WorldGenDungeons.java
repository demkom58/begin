package net.potion.world.gen;

import net.potion.block.Block;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.tileentity.TileEntityChest;
import net.potion.tileentity.TileEntityMobSpawner;
import net.potion.world.World;

import java.util.Random;

public class WorldGenDungeons extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        byte var6 = 3;
        int var7 = random.nextInt(2) + 2;
        int var8 = random.nextInt(2) + 2;
        int var9 = 0;

        for (int var10 = x - var7 - 1; var10 <= x + var7 + 1; ++var10) {
            for (int var11 = y - 1; var11 <= y + var6 + 1; ++var11) {
                for (int var12 = z - var8 - 1; var12 <= z + var8 + 1; ++var12) {
                    Material var13 = world.getBlockMaterial(var10, var11, var12);
                    if (var11 == y - 1 && !var13.isSolid()) {
                        return false;
                    }

                    if (var11 == y + var6 + 1 && !var13.isSolid()) {
                        return false;
                    }

                    if ((var10 == x - var7 - 1 || var10 == x + var7 + 1 || var12 == z - var8 - 1 || var12 == z + var8 + 1) && var11 == y && world.isAirBlock(var10, var11, var12) && world.isAirBlock(var10, var11 + 1, var12)) {
                        ++var9;
                    }
                }
            }
        }

        if (var9 >= 1 && var9 <= 5) {
            for (int var19 = x - var7 - 1; var19 <= x + var7 + 1; ++var19) {
                for (int var22 = y + var6; var22 >= y - 1; --var22) {
                    for (int var24 = z - var8 - 1; var24 <= z + var8 + 1; ++var24) {
                        if (var19 != x - var7 - 1 && var22 != y - 1 && var24 != z - var8 - 1 && var19 != x + var7 + 1 && var22 != y + var6 + 1 && var24 != z + var8 + 1) {
                            world.setBlockWithNotify(var19, var22, var24, 0);
                        } else if (var22 >= 0 && !world.getBlockMaterial(var19, var22 - 1, var24).isSolid()) {
                            world.setBlockWithNotify(var19, var22, var24, 0);
                        } else if (world.getBlockMaterial(var19, var22, var24).isSolid()) {
                            if (var22 == y - 1 && random.nextInt(4) != 0) {
                                world.setBlockWithNotify(var19, var22, var24, Block.COBBLESTONE_MOSSY.blockID);
                            } else {
                                world.setBlockWithNotify(var19, var22, var24, Block.COBBLESTONE.blockID);
                            }
                        }
                    }
                }
            }

            label110:
            for (int var20 = 0; var20 < 2; ++var20) {
                for (int var23 = 0; var23 < 3; ++var23) {
                    int var25 = x + random.nextInt(var7 * 2 + 1) - var7;
                    int var14 = z + random.nextInt(var8 * 2 + 1) - var8;
                    if (world.isAirBlock(var25, y, var14)) {
                        int var15 = 0;
                        if (world.getBlockMaterial(var25 - 1, y, var14).isSolid()) {
                            ++var15;
                        }

                        if (world.getBlockMaterial(var25 + 1, y, var14).isSolid()) {
                            ++var15;
                        }

                        if (world.getBlockMaterial(var25, y, var14 - 1).isSolid()) {
                            ++var15;
                        }

                        if (world.getBlockMaterial(var25, y, var14 + 1).isSolid()) {
                            ++var15;
                        }

                        if (var15 == 1) {
                            world.setBlockWithNotify(var25, y, var14, Block.CHEST.blockID);
                            TileEntityChest var16 = (TileEntityChest) world.getBlockTileEntity(var25, y, var14);
                            int var17 = 0;

                            while (true) {
                                if (var17 >= 8) {
                                    continue label110;
                                }

                                ItemStack var18 = this.pickCheckLootItem(random);
                                if (var18 != null) {
                                    var16.setInventorySlotContents(random.nextInt(var16.getSizeInventory()), var18);
                                }

                                ++var17;
                            }
                        }
                    }
                }
            }

            world.setBlockWithNotify(x, y, z, Block.MOB_SPAWNER.blockID);
            TileEntityMobSpawner var21 = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
            var21.setMobID(this.pickMobSpawner(random));
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
