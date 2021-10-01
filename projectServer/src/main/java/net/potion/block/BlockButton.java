package net.potion.block;

import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.Random;

public class BlockButton extends Block {
    protected BlockButton(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
        this.setTickOnLoad(true);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public int tickRate() {
        return 20;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        if (var5 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
            return true;
        } else if (var5 == 3 && world.isBlockNormalCube(x, y, z - 1)) {
            return true;
        } else if (var5 == 4 && world.isBlockNormalCube(x + 1, y, z)) {
            return true;
        } else {
            return var5 == 5 && world.isBlockNormalCube(x - 1, y, z);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        if (world.isBlockNormalCube(var2 - 1, var3, var4)) {
            return true;
        } else if (world.isBlockNormalCube(var2 + 1, var3, var4)) {
            return true;
        } else if (world.isBlockNormalCube(var2, var3, var4 - 1)) {
            return true;
        } else {
            return world.isBlockNormalCube(var2, var3, var4 + 1);
        }
    }

    @Override
    public void onBlockPlaced(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        int var7 = var6 & 8;
        var6 = var6 & 7;
        if (var5 == 2 && world.isBlockNormalCube(var2, var3, var4 + 1)) {
            var6 = 4;
        } else if (var5 == 3 && world.isBlockNormalCube(var2, var3, var4 - 1)) {
            var6 = 3;
        } else if (var5 == 4 && world.isBlockNormalCube(var2 + 1, var3, var4)) {
            var6 = 2;
        } else if (var5 == 5 && world.isBlockNormalCube(var2 - 1, var3, var4)) {
            var6 = 1;
        } else {
            var6 = this.getOrientation(world, var2, var3, var4);
        }

        world.setBlockMetadataWithNotify(var2, var3, var4, var6 + var7);
    }

    private int getOrientation(World var1, int var2, int var3, int var4) {
        if (var1.isBlockNormalCube(var2 - 1, var3, var4)) {
            return 1;
        } else if (var1.isBlockNormalCube(var2 + 1, var3, var4)) {
            return 2;
        } else if (var1.isBlockNormalCube(var2, var3, var4 - 1)) {
            return 3;
        } else {
            return var1.isBlockNormalCube(var2, var3, var4 + 1) ? 4 : 1;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (this.func_322_g(world, var2, var3, var4)) {
            int var6 = world.getBlockMetadata(var2, var3, var4) & 7;
            boolean var7 = false;
            if (!world.isBlockNormalCube(var2 - 1, var3, var4) && var6 == 1) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2 + 1, var3, var4) && var6 == 2) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3, var4 - 1) && var6 == 3) {
                var7 = true;
            }

            if (!world.isBlockNormalCube(var2, var3, var4 + 1) && var6 == 4) {
                var7 = true;
            }

            if (var7) {
                this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
                world.setBlockWithNotify(var2, var3, var4, 0);
            }
        }

    }

    private boolean func_322_g(World var1, int var2, int var3, int var4) {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        int var5 = blockAccess.getBlockMetadata(var2, var3, var4);
        int var6 = var5 & 7;
        boolean var7 = (var5 & 8) > 0;
        float var8 = 0.375F;
        float var9 = 0.625F;
        float var10 = 0.1875F;
        float var11 = 0.125F;
        if (var7) {
            var11 = 0.0625F;
        }

        if (var6 == 1) {
            this.setBlockBounds(0.0F, var8, 0.5F - var10, var11, var9, 0.5F + var10);
        } else if (var6 == 2) {
            this.setBlockBounds(1.0F - var11, var8, 0.5F - var10, 1.0F, var9, 0.5F + var10);
        } else if (var6 == 3) {
            this.setBlockBounds(0.5F - var10, var8, 0.0F, 0.5F + var10, var9, var11);
        } else if (var6 == 4) {
            this.setBlockBounds(0.5F - var10, var8, 1.0F - var11, 0.5F + var10, var9, 1.0F);
        }

    }

    @Override
    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.blockActivated(world, var2, var3, var4, entityPlayer);
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        int var7 = var6 & 7;
        int var8 = 8 - (var6 & 8);
        if (var8 == 0) {
            return true;
        } else {
            world.setBlockMetadataWithNotify(var2, var3, var4, var7 + var8);
            world.markBlocksDirty(var2, var3, var4, var2, var3, var4);
            world.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.5D, (double) var4 + 0.5D, "random.click", 0.3F, 0.6F);
            world.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            if (var7 == 1) {
                world.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
            } else if (var7 == 2) {
                world.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
            } else if (var7 == 3) {
                world.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
            } else if (var7 == 4) {
                world.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
            } else {
                world.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            }

            world.scheduleUpdateTick(var2, var3, var4, this.blockID, this.tickRate());
            return true;
        }
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        if ((var5 & 8) > 0) {
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            int var6 = var5 & 7;
            if (var6 == 1) {
                world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            } else if (var6 == 2) {
                world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            } else if (var6 == 3) {
                world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            } else if (var6 == 4) {
                world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
            } else {
                world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            }
        }

        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        return (blockAccess.getBlockMetadata(var2, var3, var4) & 8) > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        if ((var6 & 8) == 0) {
            return false;
        } else {
            int var7 = var6 & 7;
            if (var7 == 5 && var5 == 1) {
                return true;
            } else if (var7 == 4 && var5 == 2) {
                return true;
            } else if (var7 == 3 && var5 == 3) {
                return true;
            } else if (var7 == 2 && var5 == 4) {
                return true;
            } else {
                return var7 == 1 && var5 == 5;
            }
        }
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(x, y, z);
            if ((var6 & 8) != 0) {
                world.setBlockMetadataWithNotify(x, y, z, var6 & 7);
                world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
                int var7 = var6 & 7;
                if (var7 == 1) {
                    world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
                } else if (var7 == 2) {
                    world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
                } else if (var7 == 3) {
                    world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
                } else if (var7 == 4) {
                    world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
                } else {
                    world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
                }

                world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);
                world.markBlocksDirty(x, y, z, x, y, z);
            }
        }
    }
}
