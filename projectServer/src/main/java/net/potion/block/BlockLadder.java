package net.potion.block;

import net.potion.material.Material;
import net.potion.world.World;
import net.potion.util.AxisAlignedBB;

import java.util.Random;

public class BlockLadder extends Block {
    protected BlockLadder(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        float var6 = 0.125F;
        if (var5 == 2) {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var6, 1.0F, 1.0F, 1.0F);
        }

        if (var5 == 3) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var6);
        }

        if (var5 == 4) {
            this.setBlockBounds(1.0F - var6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (var5 == 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var6, 1.0F, 1.0F);
        }

        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
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
        if ((var6 == 0 || var5 == 2) && world.isBlockNormalCube(var2, var3, var4 + 1)) {
            var6 = 2;
        }

        if ((var6 == 0 || var5 == 3) && world.isBlockNormalCube(var2, var3, var4 - 1)) {
            var6 = 3;
        }

        if ((var6 == 0 || var5 == 4) && world.isBlockNormalCube(var2 + 1, var3, var4)) {
            var6 = 4;
        }

        if ((var6 == 0 || var5 == 5) && world.isBlockNormalCube(var2 - 1, var3, var4)) {
            var6 = 5;
        }

        world.setBlockMetadataWithNotify(var2, var3, var4, var6);
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        boolean var7 = false;
        if (var6 == 2 && world.isBlockNormalCube(var2, var3, var4 + 1)) {
            var7 = true;
        }

        if (var6 == 3 && world.isBlockNormalCube(var2, var3, var4 - 1)) {
            var7 = true;
        }

        if (var6 == 4 && world.isBlockNormalCube(var2 + 1, var3, var4)) {
            var7 = true;
        }

        if (var6 == 5 && world.isBlockNormalCube(var2 - 1, var3, var4)) {
            var7 = true;
        }

        if (!var7) {
            this.dropBlockAsItem(world, var2, var3, var4, var6);
            world.setBlockWithNotify(var2, var3, var4, 0);
        }

        super.onNeighborBlockChange(world, var2, var3, var4, var5);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
