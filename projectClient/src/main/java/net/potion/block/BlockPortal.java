package net.potion.block;

import net.potion.entity.Entity;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.Random;

public class BlockPortal extends BlockBreakable {
    public BlockPortal(int var1, int var2) {
        super(var1, var2, Material.PORTAL, false);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        if (blockAccess.getBlockId(x - 1, y, z) != this.blockID && blockAccess.getBlockId(x + 1, y, z) != this.blockID) {
            float var7 = 0.125F;
            float var8 = 0.5F;
            this.setBlockBounds(0.5F - var7, 0.0F, 0.5F - var8, 0.5F + var7, 1.0F, 0.5F + var8);
        } else {
            float var5 = 0.5F;
            float var6 = 0.125F;
            this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
        }

    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean tryToCreatePortal(World var1, int var2, int var3, int var4) {
        byte var5 = 0;
        byte var6 = 0;
        if (var1.getBlockId(var2 - 1, var3, var4) == Block.OBSIDIAN.blockID || var1.getBlockId(var2 + 1, var3, var4) == Block.OBSIDIAN.blockID) {
            var5 = 1;
        }

        if (var1.getBlockId(var2, var3, var4 - 1) == Block.OBSIDIAN.blockID || var1.getBlockId(var2, var3, var4 + 1) == Block.OBSIDIAN.blockID) {
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
                            if (var10 != Block.OBSIDIAN.blockID) {
                                return false;
                            }
                        } else if (var10 != 0 && var10 != Block.FIRE.blockID) {
                            return false;
                        }
                    }
                }
            }

            var1.editingBlocks = true;

            for (int var11 = 0; var11 < 2; ++var11) {
                for (int var12 = 0; var12 < 3; ++var12) {
                    var1.setBlockWithNotify(var2 + var5 * var11, var3 + var12, var4 + var6 * var11, Block.PORTAL.blockID);
                }
            }

            var1.editingBlocks = false;
            return true;
        }
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        byte var6 = 0;
        byte var7 = 1;
        if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
            var6 = 1;
            var7 = 0;
        }

        int var8;
        for (var8 = var3; var1.getBlockId(var2, var8 - 1, var4) == this.blockID; --var8) {
        }

        if (var1.getBlockId(var2, var8 - 1, var4) != Block.OBSIDIAN.blockID) {
            var1.setBlockWithNotify(var2, var3, var4, 0);
        } else {
            int var9;
            for (var9 = 1; var9 < 4 && var1.getBlockId(var2, var8 + var9, var4) == this.blockID; ++var9) {
            }

            if (var9 == 3 && var1.getBlockId(var2, var8 + var9, var4) == Block.OBSIDIAN.blockID) {
                boolean var10 = var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID;
                boolean var11 = var1.getBlockId(var2, var3, var4 - 1) == this.blockID || var1.getBlockId(var2, var3, var4 + 1) == this.blockID;
                if (var10 && var11) {
                    var1.setBlockWithNotify(var2, var3, var4, 0);
                } else if ((var1.getBlockId(var2 + var6, var3, var4 + var7) != Block.OBSIDIAN.blockID || var1.getBlockId(var2 - var6, var3, var4 - var7) != this.blockID) && (var1.getBlockId(var2 - var6, var3, var4 - var7) != Block.OBSIDIAN.blockID || var1.getBlockId(var2 + var6, var3, var4 + var7) != this.blockID)) {
                    var1.setBlockWithNotify(var2, var3, var4, 0);
                }
            } else {
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (blockAccess.getBlockId(x, y, z) == this.blockID) {
            return false;
        } else {
            boolean var6 = blockAccess.getBlockId(x - 1, y, z) == this.blockID && blockAccess.getBlockId(x - 2, y, z) != this.blockID;
            boolean var7 = blockAccess.getBlockId(x + 1, y, z) == this.blockID && blockAccess.getBlockId(x + 2, y, z) != this.blockID;
            boolean var8 = blockAccess.getBlockId(x, y, z - 1) == this.blockID && blockAccess.getBlockId(x, y, z - 2) != this.blockID;
            boolean var9 = blockAccess.getBlockId(x, y, z + 1) == this.blockID && blockAccess.getBlockId(x, y, z + 2) != this.blockID;
            boolean var10 = var6 || var7;
            boolean var11 = var8 || var9;
            if (var10 && side == 4) {
                return true;
            } else if (var10 && side == 5) {
                return true;
            } else if (var11 && side == 2) {
                return true;
            } else {
                return var11 && side == 3;
            }
        }
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null) {
            entity.setInPortal();
        }

    }

    @Override
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var5.nextInt(100) == 0) {
            var1.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.5D, (double) var4 + 0.5D, "portal.portal", 1.0F, var5.nextFloat() * 0.4F + 0.8F);
        }

        for (int var6 = 0; var6 < 4; ++var6) {
            double var7 = (float) var2 + var5.nextFloat();
            double var9 = (float) var3 + var5.nextFloat();
            double var11 = (float) var4 + var5.nextFloat();
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            int var19 = var5.nextInt(2) * 2 - 1;
            var13 = ((double) var5.nextFloat() - 0.5D) * 0.5D;
            var15 = ((double) var5.nextFloat() - 0.5D) * 0.5D;
            var17 = ((double) var5.nextFloat() - 0.5D) * 0.5D;
            if (var1.getBlockId(var2 - 1, var3, var4) != this.blockID && var1.getBlockId(var2 + 1, var3, var4) != this.blockID) {
                var7 = (double) var2 + 0.5D + 0.25D * (double) var19;
                var13 = var5.nextFloat() * 2.0F * (float) var19;
            } else {
                var11 = (double) var4 + 0.5D + 0.25D * (double) var19;
                var17 = var5.nextFloat() * 2.0F * (float) var19;
            }

            var1.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
        }

    }
}
