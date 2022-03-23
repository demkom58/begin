package net.potion.world.gen.struct;

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
        byte rangeY = 3;
        int rangeX = random.nextInt(2) + 2;
        int rangeZ = random.nextInt(2) + 2;
        int var9 = 0;

        for (int iX = x - rangeX - 1; iX <= x + rangeX + 1; ++iX) {
            for (int iY = y - 1; iY <= y + rangeY + 1; ++iY) {
                for (int iZ = z - rangeZ - 1; iZ <= z + rangeZ + 1; ++iZ) {
                    Material material = world.getBlockMaterial(iX, iY, iZ);
                    if (iY == y - 1 && !material.isSolid()) {
                        return false;
                    }

                    if (iY == y + rangeY + 1 && !material.isSolid()) {
                        return false;
                    }

                    if ((iX == x - rangeX - 1 || iX == x + rangeX + 1 || iZ == z - rangeZ - 1 || iZ == z + rangeZ + 1)
                            && iY == y
                            && world.isAirBlock(iX, iY, iZ)
                            && world.isAirBlock(iX, iY + 1, iZ)) {
                        ++var9;
                    }
                }
            }
        }

        if (var9 < 1 || var9 > 5) {
            return false;
        }

        for (int iX = x - rangeX - 1; iX <= x + rangeX + 1; ++iX) {
            for (int iY = y + rangeY; iY >= y - 1; --iY) {
                for (int iZ = z - rangeZ - 1; iZ <= z + rangeZ + 1; ++iZ) {
                    if (iX != x - rangeX - 1 && iY != y - 1 && iZ != z - rangeZ - 1 && iX != x + rangeX + 1 && iY != y + rangeY + 1 && iZ != z + rangeZ + 1) {
                        world.setBlockWithNotify(iX, iY, iZ, 0);
                    } else if (iY >= 0 && !world.getBlockMaterial(iX, iY - 1, iZ).isSolid()) {
                        world.setBlockWithNotify(iX, iY, iZ, 0);
                    } else if (world.getBlockMaterial(iX, iY, iZ).isSolid()) {
                        if (iY == y - 1 && random.nextInt(4) != 0) {
                            world.setBlockWithNotify(iX, iY, iZ, Block.COBBLESTONE_MOSSY.blockID);
                        } else {
                            world.setBlockWithNotify(iX, iY, iZ, Block.COBBLESTONE.blockID);
                        }
                    }
                }
            }
        }

        label110:
        for (int var20 = 0; var20 < 2; ++var20) {
            for (int var23 = 0; var23 < 3; ++var23) {
                int randX = x + random.nextInt(rangeX * 2 + 1) - rangeX;
                int randZ = z + random.nextInt(rangeZ * 2 + 1) - rangeZ;
                if (!world.isAirBlock(randX, y, randZ)) {
                    continue;
                }

                int nearBlocks = 0;
                if (world.getBlockMaterial(randX - 1, y, randZ).isSolid()) {
                    ++nearBlocks;
                }

                if (world.getBlockMaterial(randX + 1, y, randZ).isSolid()) {
                    ++nearBlocks;
                }

                if (world.getBlockMaterial(randX, y, randZ - 1).isSolid()) {
                    ++nearBlocks;
                }

                if (world.getBlockMaterial(randX, y, randZ + 1).isSolid()) {
                    ++nearBlocks;
                }

                if (nearBlocks == 1) {
                    world.setBlockWithNotify(randX, y, randZ, Block.CHEST.blockID);
                    TileEntityChest chest = (TileEntityChest) world.getBlockTileEntity(randX, y, randZ);
                    int var17 = 0;

                    while (true) {
                        if (var17 >= 8) {
                            continue label110;
                        }

                        ItemStack var18 = this.pickCheckLootItem(random);
                        if (var18 != null) {
                            chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), var18);
                        }

                        ++var17;
                    }
                }
            }
        }

        world.setBlockWithNotify(x, y, z, Block.MOB_SPAWNER.blockID);
        TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);
        spawner.setMobID(this.pickMobSpawner(random));
        return true;
    }

    private ItemStack pickCheckLootItem(Random rand) {
        int id = rand.nextInt(11);
        if (id == 0) {
            return new ItemStack(Item.SADDLE);
        } else if (id == 1) {
            return new ItemStack(Item.INGOT_IRON, rand.nextInt(4) + 1);
        } else if (id == 2) {
            return new ItemStack(Item.BREAD);
        } else if (id == 3) {
            return new ItemStack(Item.WHEAT, rand.nextInt(4) + 1);
        } else if (id == 4) {
            return new ItemStack(Item.GUNPOWDER, rand.nextInt(4) + 1);
        } else if (id == 5) {
            return new ItemStack(Item.SILK, rand.nextInt(4) + 1);
        } else if (id == 6) {
            return new ItemStack(Item.BUCKET_EMPTY);
        } else if (id == 7 && rand.nextInt(100) == 0) {
            return new ItemStack(Item.APPLE_GOLD);
        } else if (id == 8 && rand.nextInt(2) == 0) {
            return new ItemStack(Item.REDSTONE, rand.nextInt(4) + 1);
        } else if (id == 9 && rand.nextInt(10) == 0) {
            return new ItemStack(Item.ITEMS_LIST[Item.RECORD_13.shiftedIndex + rand.nextInt(2)]);
        } else {
            return id == 10 ? new ItemStack(Item.DYE_POWDER, 1, 3) : null;
        }
    }

    private String pickMobSpawner(Random rand) {
        int value = rand.nextInt(4);
        if (value == 0) {
            return "Skeleton";
        } else if (value == 1) {
            return "Zombie";
        } else if (value == 2) {
            return "Zombie";
        } else if (value == 3) {
            return "Spider";
        } else {
            return "";
        }
    }
}
