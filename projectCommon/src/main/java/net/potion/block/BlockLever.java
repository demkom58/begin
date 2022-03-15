package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

public class BlockLever extends Block {
    protected BlockLever(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return 12;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        if (var5 == 1 && world.isBlockNormalCube(x, y - 1, z)) {
            return true;
        } else if (var5 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (world.isBlockNormalCube(x - 1, y, z)) {
            return true;
        } else if (world.isBlockNormalCube(x + 1, y, z)) {
            return true;
        } else if (world.isBlockNormalCube(x, y, z - 1)) {
            return true;
        } else if (world.isBlockNormalCube(x, y, z + 1)) {
            return true;
        } else {
            return world.isBlockNormalCube(x, y - 1, z);
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        int var6 = world.getBlockMetadata(x, y, z);
        int var7 = var6 & 8;
        var6 = var6 & 7;
        var6 = -1;
        if (side == 1 && world.isBlockNormalCube(x, y - 1, z)) {
            var6 = 5 + world.rand.nextInt(2);
        }

        if (side == 2 && world.isBlockNormalCube(x, y, z + 1)) {
            var6 = 4;
        }

        if (side == 3 && world.isBlockNormalCube(x, y, z - 1)) {
            var6 = 3;
        }

        if (side == 4 && world.isBlockNormalCube(x + 1, y, z)) {
            var6 = 2;
        }

        if (side == 5 && world.isBlockNormalCube(x - 1, y, z)) {
            var6 = 1;
        }

        if (var6 == -1) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        } else {
            world.setBlockMetadataWithNotify(x, y, z, var6 + var7);
        }
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (this.checkIfAttachedToBlock(var1, var2, var3, var4)) {
            int var6 = var1.getBlockMetadata(var2, var3, var4) & 7;
            boolean var7 = false;
            if (!var1.isBlockNormalCube(var2 - 1, var3, var4) && var6 == 1) {
                var7 = true;
            }

            if (!var1.isBlockNormalCube(var2 + 1, var3, var4) && var6 == 2) {
                var7 = true;
            }

            if (!var1.isBlockNormalCube(var2, var3, var4 - 1) && var6 == 3) {
                var7 = true;
            }

            if (!var1.isBlockNormalCube(var2, var3, var4 + 1) && var6 == 4) {
                var7 = true;
            }

            if (!var1.isBlockNormalCube(var2, var3 - 1, var4) && var6 == 5) {
                var7 = true;
            }

            if (!var1.isBlockNormalCube(var2, var3 - 1, var4) && var6 == 6) {
                var7 = true;
            }

            if (var7) {
                this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }
        }

    }

    private boolean checkIfAttachedToBlock(World var1, int var2, int var3, int var4) {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z) & 7;
        float var6 = 0.1875F;
        if (var5 == 1) {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
        } else if (var5 == 2) {
            this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
        } else if (var5 == 3) {
            this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
        } else if (var5 == 4) {
            this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
        } else {
            var6 = 0.25F;
            this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
        }

    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.blockActivated(world, x, y, z, player);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.localWorld) {
            return true;
        }

        int var6 = world.getBlockMetadata(x, y, z);
        int var7 = var6 & 7;
        int var8 = 8 - (var6 & 8);
        world.setBlockMetadataWithNotify(x, y, z, var7 + var8);
        world.markBlocksDirty(x, y, z, x, y, z);
        world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, var8 > 0 ? 0.6F : 0.5F);
        world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
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

        return true;
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        if ((var5 & 8) > 0) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            int var6 = var5 & 7;
            if (var6 == 1) {
                var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
            } else if (var6 == 2) {
                var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
            } else if (var6 == 3) {
                var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
            } else if (var6 == 4) {
                var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
            } else {
                var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            }
        }

        super.onBlockRemoval(var1, var2, var3, var4);
    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        return (blockAccess.getBlockMetadata(x, y, z) & 8) > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int var5) {
        int var6 = world.getBlockMetadata(x, y, z);
        if ((var6 & 8) == 0) {
            return false;
        }

        int var7 = var6 & 7;
        if (var7 == 6 && var5 == 1) {
            return true;
        } else if (var7 == 5 && var5 == 1) {
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

    @Override
    public boolean canProvidePower() {
        return true;
    }
}
