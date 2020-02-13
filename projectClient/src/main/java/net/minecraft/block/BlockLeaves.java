package net.minecraft.block;

import net.minecraft.client.render.ColorizerFoliage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.stats.StatList;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase {
    int[] adjacentTreeBlocks;
    private int baseIndexInPNG;

    protected BlockLeaves(int var1, int var2) {
        super(var1, var2, Material.LEAVES, false);
        this.baseIndexInPNG = var2;
        this.setTickOnLoad(true);
    }

    @Override
    public int getRenderColor(int var1) {
        if ((var1 & 1) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
        } else {
            return (var1 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.func_31073_c();
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z);
        if ((var5 & 1) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
        } else if ((var5 & 2) == 2) {
            return ColorizerFoliage.getFoliageColorBirch();
        } else {
            blockAccess.getWorldChunkManager().func_4069_a(x, z, 1, 1);
            double var6 = blockAccess.getWorldChunkManager().temperature[0];
            double var8 = blockAccess.getWorldChunkManager().humidity[0];
            return ColorizerFoliage.getFoliageColor(var6, var8);
        }
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        byte var5 = 1;
        int var6 = var5 + 1;
        if (var1.checkChunksExist(var2 - var6, var3 - var6, var4 - var6, var2 + var6, var3 + var6, var4 + var6)) {
            for (int var7 = -var5; var7 <= var5; ++var7) {
                for (int var8 = -var5; var8 <= var5; ++var8) {
                    for (int var9 = -var5; var9 <= var5; ++var9) {
                        int var10 = var1.getBlockId(var2 + var7, var3 + var8, var4 + var9);
                        if (var10 == Block.LEAVES.blockID) {
                            int var11 = var1.getBlockMetadata(var2 + var7, var3 + var8, var4 + var9);
                            var1.setBlockMetadata(var2 + var7, var3 + var8, var4 + var9, var11 | 8);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (!var1.multiplayerWorld) {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            if ((var6 & 8) != 0) {
                byte var7 = 4;
                int var8 = var7 + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;
                if (this.adjacentTreeBlocks == null) {
                    this.adjacentTreeBlocks = new int[var9 * var9 * var9];
                }

                if (var1.checkChunksExist(var2 - var8, var3 - var8, var4 - var8, var2 + var8, var3 + var8, var4 + var8)) {
                    for (int var12 = -var7; var12 <= var7; ++var12) {
                        for (int var13 = -var7; var13 <= var7; ++var13) {
                            for (int var14 = -var7; var14 <= var7; ++var14) {
                                int var15 = var1.getBlockId(var2 + var12, var3 + var13, var4 + var14);
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
                    var1.setBlockMetadata(var2, var3, var4, var6 & -9);
                } else {
                    this.removeLeaves(var1, var2, var3, var4);
                }
            }

        }
    }

    private void removeLeaves(World var1, int var2, int var3, int var4) {
        this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
        var1.setBlockWithNotify(var2, var3, var4, 0);
    }

    @Override
    public int quantityDropped(Random var1) {
        return var1.nextInt(20) == 0 ? 1 : 0;
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Block.SAPLING.blockID;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int blockId) {
        if (!world.multiplayerWorld && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == Item.SHEARS.shiftedIndex) {
            player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Block.LEAVES.blockID, 1, blockId & 3));
        } else {
            super.harvestBlock(world, player, x, y, z, blockId);
        }

    }

    @Override
    protected int damageDropped(int var1) {
        return var1 & 3;
    }

    @Override
    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        return (metadata & 3) == 1 ? this.blockIndexInTexture + 80 : this.blockIndexInTexture;
    }

    public void setGraphicsLevel(boolean var1) {
        this.graphicsLevel = var1;
        this.blockIndexInTexture = this.baseIndexInPNG + (var1 ? 0 : 1);
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        super.onEntityWalking(world, x, y, z, entity);
    }
}
