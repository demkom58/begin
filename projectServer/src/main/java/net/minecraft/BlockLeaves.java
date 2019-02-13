package net.minecraft;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase {
    int[] adjacentTreeBlocks;
    private int baseIndexInPNG;

    protected BlockLeaves(int var1, int var2) {
        super(var1, var2, Material.leaves, false);
        this.baseIndexInPNG = var2;
        this.setTickOnLoad(true);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        byte var5 = 1;
        int var6 = var5 + 1;
        if (world.checkChunksExist(x - var6, y - var6, z - var6, x + var6, y + var6, z + var6)) {
            for (int var7 = -var5; var7 <= var5; ++var7) {
                for (int var8 = -var5; var8 <= var5; ++var8) {
                    for (int var9 = -var5; var9 <= var5; ++var9) {
                        int var10 = world.getBlockId(x + var7, y + var8, z + var9);
                        if (var10 == Block.LEAVES.blockID) {
                            int var11 = world.getBlockMetadata(x + var7, y + var8, z + var9);
                            world.setBlockMetadata(x + var7, y + var8, z + var9, var11 | 8);
                        }
                    }
                }
            }
        }

    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(x, y, z);
            if ((var6 & 8) != 0) {
                byte var7 = 4;
                int var8 = var7 + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;
                if (this.adjacentTreeBlocks == null) {
                    this.adjacentTreeBlocks = new int[var9 * var9 * var9];
                }

                if (world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                    for (int var12 = -var7; var12 <= var7; ++var12) {
                        for (int var13 = -var7; var13 <= var7; ++var13) {
                            for (int var14 = -var7; var14 <= var7; ++var14) {
                                int var15 = world.getBlockId(x + var12, y + var13, z + var14);
                                if (var15 == Block.WOOD.blockID) {
                                    this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
                                } else if (var15 == Block.LEAVES.blockID) {
                                    this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
                                } else {
                                    this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
                                }
                            }
                        }
                    }

                    for (int var16 = 1; var16 <= 4; ++var16) {
                        for (int var18 = -var7; var18 <= var7; ++var18) {
                            for (int var19 = -var7; var19 <= var7; ++var19) {
                                for (int var20 = -var7; var20 <= var7; ++var20) {
                                    if (this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11] == var16 - 1) {
                                        if (this.adjacentTreeBlocks[(var18 + var11 - 1) * var10 + (var19 + var11) * var9 + var20 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var18 + var11 - 1) * var10 + (var19 + var11) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.adjacentTreeBlocks[(var18 + var11 + 1) * var10 + (var19 + var11) * var9 + var20 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var18 + var11 + 1) * var10 + (var19 + var11) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11 - 1) * var9 + var20 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11 - 1) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11 + 1) * var9 + var20 + var11] == -2) {
                                            this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11 + 1) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11) * var9 + (var20 + var11 - 1)] == -2) {
                                            this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11) * var9 + (var20 + var11 - 1)] = var16;
                                        }

                                        if (this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11 + 1] == -2) {
                                            this.adjacentTreeBlocks[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11 + 1] = var16;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int var17 = this.adjacentTreeBlocks[var11 * var10 + var11 * var9 + var11];
                if (var17 >= 0) {
                    world.setBlockMetadata(x, y, z, var6 & -9);
                } else {
                    this.removeLeaves(world, x, y, z);
                }
            }

        }
    }

    private void removeLeaves(World var1, int var2, int var3, int var4) {
        this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
        var1.setBlockWithNotify(var2, var3, var4, 0);
    }

    public int quantityDropped(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public int idDropped(int var1, Random random) {
        return Block.SAPLING.blockID;
    }

    public void harvestBlock(World world, EntityPlayer entityPlayer, int var3, int var4, int var5, int var6) {
        if (!world.singleplayerWorld && entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().itemID == Item.SHEARS.shiftedIndex) {
            entityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem_do(world, var3, var4, var5, new ItemStack(Block.LEAVES.blockID, 1, var6 & 3));
        } else {
            super.harvestBlock(world, entityPlayer, var3, var4, var5, var6);
        }

    }

    protected int damageDropped(int var1) {
        return var1 & 3;
    }

    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        return (var2 & 3) == 1 ? this.blockIndexInTexture + 80 : this.blockIndexInTexture;
    }

    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
        super.onEntityWalking(world, var2, var3, var4, entity);
    }
}
