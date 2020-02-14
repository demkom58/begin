package net.potion.block;

import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.util.MovingObjectPosition;
import net.potion.world.World;
import net.potion.util.Vec3D;

import java.util.Random;

public class BlockTorch extends Block {
    protected BlockTorch(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
        this.setTickOnLoad(true);
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
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    private boolean func_31032_h(World var1, int var2, int var3, int var4) {
        return var1.isBlockNormalCube(var2, var3, var4) || var1.getBlockId(var2, var3, var4) == Block.FENCE.blockID;
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
            return this.func_31032_h(world, x, y - 1, z);
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        int var6 = world.getBlockMetadata(x, y, z);
        if (side == 1 && this.func_31032_h(world, x, y - 1, z)) {
            var6 = 5;
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

        world.setBlockMetadataWithNotify(x, y, z, var6);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        super.updateTick(var1, var2, var3, var4, var5);
        if (var1.getBlockMetadata(var2, var3, var4) == 0) {
            this.onBlockAdded(var1, var2, var3, var4);
        }

    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        if (var1.isBlockNormalCube(var2 - 1, var3, var4)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 1);
        } else if (var1.isBlockNormalCube(var2 + 1, var3, var4)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 2);
        } else if (var1.isBlockNormalCube(var2, var3, var4 - 1)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 3);
        } else if (var1.isBlockNormalCube(var2, var3, var4 + 1)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 4);
        } else if (this.func_31032_h(var1, var2, var3 - 1, var4)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 5);
        }

        this.dropTorchIfCantStay(var1, var2, var3, var4);
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (this.dropTorchIfCantStay(var1, var2, var3, var4)) {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
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

            if (!this.func_31032_h(var1, var2, var3 - 1, var4) && var6 == 5) {
                var7 = true;
            }

            if (var7) {
                this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }
        }

    }

    private boolean dropTorchIfCantStay(World var1, int var2, int var3, int var4) {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D var5, Vec3D var6) {
        int var7 = world.getBlockMetadata(x, y, z) & 7;
        float var8 = 0.15F;
        if (var7 == 1) {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
        } else if (var7 == 2) {
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
        } else if (var7 == 3) {
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
        } else if (var7 == 4) {
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
        } else {
            var8 = 0.1F;
            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
        }

        return super.collisionRayTrace(world, x, y, z, var5, var6);
    }

    @Override
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        double var7 = (float) var2 + 0.5F;
        double var9 = (float) var3 + 0.7F;
        double var11 = (float) var4 + 0.5F;
        double var13 = 0.2199999988079071D;
        double var15 = 0.27000001072883606D;
        if (var6 == 1) {
            var1.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        } else if (var6 == 2) {
            var1.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        } else if (var6 == 3) {
            var1.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
        } else if (var6 == 4) {
            var1.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
        } else {
            var1.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
        }

    }
}
