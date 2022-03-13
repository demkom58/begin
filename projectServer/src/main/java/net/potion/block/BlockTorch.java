package net.potion.block;

import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.util.MovingObjectPosition;
import net.potion.world.World;
import net.hypnosis.util.math.Vec3d;

import java.util.Random;

public class BlockTorch extends Block {
    protected BlockTorch(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
        this.setTickOnLoad(true);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    private boolean func_31028_g(World var1, int var2, int var3, int var4) {
        return var1.isBlockNormalCube(var2, var3, var4) || var1.getBlockId(var2, var3, var4) == Block.FENCE.blockID;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        if (world.isBlockNormalCube(var2 - 1, var3, var4)) {
            return true;
        } else if (world.isBlockNormalCube(var2 + 1, var3, var4)) {
            return true;
        } else if (world.isBlockNormalCube(var2, var3, var4 - 1)) {
            return true;
        } else if (world.isBlockNormalCube(var2, var3, var4 + 1)) {
            return true;
        } else {
            return this.func_31028_g(world, var2, var3 - 1, var4);
        }
    }

    @Override
    public void onBlockPlaced(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        if (var5 == 1 && this.func_31028_g(world, var2, var3 - 1, var4)) {
            var6 = 5;
        }

        if (var5 == 2 && world.isBlockNormalCube(var2, var3, var4 + 1)) {
            var6 = 4;
        }

        if (var5 == 3 && world.isBlockNormalCube(var2, var3, var4 - 1)) {
            var6 = 3;
        }

        if (var5 == 4 && world.isBlockNormalCube(var2 + 1, var3, var4)) {
            var6 = 2;
        }

        if (var5 == 5 && world.isBlockNormalCube(var2 - 1, var3, var4)) {
            var6 = 1;
        }

        world.setBlockMetadataWithNotify(var2, var3, var4, var6);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        if (world.getBlockMetadata(x, y, z) == 0) {
            this.onBlockAdded(world, x, y, z);
        }

    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (world.isBlockNormalCube(x - 1, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 1);
        } else if (world.isBlockNormalCube(x + 1, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 2);
        } else if (world.isBlockNormalCube(x, y, z - 1)) {
            world.setBlockMetadataWithNotify(x, y, z, 3);
        } else if (world.isBlockNormalCube(x, y, z + 1)) {
            world.setBlockMetadataWithNotify(x, y, z, 4);
        } else if (this.func_31028_g(world, x, y - 1, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 5);
        }

        this.dropTorchIfCantStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (this.dropTorchIfCantStay(world, var2, var3, var4)) {
            int var6 = world.getBlockMetadata(var2, var3, var4);
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

            if (!this.func_31028_g(world, var2, var3 - 1, var4) && var6 == 5) {
                var7 = true;
            }

            if (var7) {
                this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
                world.setBlockWithNotify(var2, var3, var4, 0);
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
    public MovingObjectPosition collisionRayTrace(World world, int var2, int var3, int var4, Vec3d var5, Vec3d var6) {
        int var7 = world.getBlockMetadata(var2, var3, var4) & 7;
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

        return super.collisionRayTrace(world, var2, var3, var4, var5, var6);
    }
}
