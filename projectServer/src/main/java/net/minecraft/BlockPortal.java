package net.minecraft;

import java.util.Random;

public class BlockPortal extends BlockBreakable {
    public BlockPortal(int var1, int var2) {
        super(var1, var2, Material.portal, false);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        if (blockAccess.getBlockId(var2 - 1, var3, var4) != this.blockID && blockAccess.getBlockId(var2 + 1, var3, var4) != this.blockID) {
            float var7 = 0.125F;
            float var8 = 0.5F;
            this.setBlockBounds(0.5F - var7, 0.0F, 0.5F - var8, 0.5F + var7, 1.0F, 0.5F + var8);
        } else {
            float var5 = 0.5F;
            float var6 = 0.125F;
            this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
        }

    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }

    public boolean tryToCreatePortal(World var1, int var2, int var3, int var4) {
        byte var5 = 0;
        byte var6 = 0;
        if (var1.getBlockId(var2 - 1, var3, var4) == Block.obsidian.blockID || var1.getBlockId(var2 + 1, var3, var4) == Block.obsidian.blockID) {
            var5 = 1;
        }

        if (var1.getBlockId(var2, var3, var4 - 1) == Block.obsidian.blockID || var1.getBlockId(var2, var3, var4 + 1) == Block.obsidian.blockID) {
            var6 = 1;
        }

        if (var5 == var6) {
            return false;
        } else {
            if (var1.getBlockId(var2 - var5, var3, var4 - var6) == 0) {
                var2 -= var5;
                var4 -= var6;
            }

            for (int var7 = -1; var7 <= 2; ++var7) {
                for (int var8 = -1; var8 <= 3; ++var8) {
                    boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 3;
                    if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 3) {
                        int var10 = var1.getBlockId(var2 + var5 * var7, var3 + var8, var4 + var6 * var7);
                        if (var9) {
                            if (var10 != Block.obsidian.blockID) {
                                return false;
                            }
                        } else if (var10 != 0 && var10 != Block.fire.blockID) {
                            return false;
                        }
                    }
                }
            }

            var1.editingBlocks = true;

            for (int var11 = 0; var11 < 2; ++var11) {
                for (int var12 = 0; var12 < 3; ++var12) {
                    var1.setBlockWithNotify(var2 + var5 * var11, var3 + var12, var4 + var6 * var11, Block.portal.blockID);
                }
            }

            var1.editingBlocks = false;
            return true;
        }
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        byte var6 = 0;
        byte var7 = 1;
        if (world.getBlockId(var2 - 1, var3, var4) == this.blockID || world.getBlockId(var2 + 1, var3, var4) == this.blockID) {
            var6 = 1;
            var7 = 0;
        }

        int var8;
        for (var8 = var3; world.getBlockId(var2, var8 - 1, var4) == this.blockID; --var8) {
        }

        if (world.getBlockId(var2, var8 - 1, var4) != Block.obsidian.blockID) {
            world.setBlockWithNotify(var2, var3, var4, 0);
        } else {
            int var9;
            for (var9 = 1; var9 < 4 && world.getBlockId(var2, var8 + var9, var4) == this.blockID; ++var9) {
            }

            if (var9 == 3 && world.getBlockId(var2, var8 + var9, var4) == Block.obsidian.blockID) {
                boolean var10 = world.getBlockId(var2 - 1, var3, var4) == this.blockID || world.getBlockId(var2 + 1, var3, var4) == this.blockID;
                boolean var11 = world.getBlockId(var2, var3, var4 - 1) == this.blockID || world.getBlockId(var2, var3, var4 + 1) == this.blockID;
                if (var10 && var11) {
                    world.setBlockWithNotify(var2, var3, var4, 0);
                } else if ((world.getBlockId(var2 + var6, var3, var4 + var7) != Block.obsidian.blockID || world.getBlockId(var2 - var6, var3, var4 - var7) != this.blockID) && (world.getBlockId(var2 - var6, var3, var4 - var7) != Block.obsidian.blockID || world.getBlockId(var2 + var6, var3, var4 + var7) != this.blockID)) {
                    world.setBlockWithNotify(var2, var3, var4, 0);
                }
            } else {
                world.setBlockWithNotify(var2, var3, var4, 0);
            }
        }
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null) {
            entity.setInPortal();
        }

    }
}
