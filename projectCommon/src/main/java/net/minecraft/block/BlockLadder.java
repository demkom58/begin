package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLadder extends Block {
    protected BlockLadder(int var1, int var2) {
        super(var1, var2, Material.CIRCUITS);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
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

        return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
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

        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
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
        return 8;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (world.isBlockNormalCube(x - 1, y, z)) {
            return true;
        } else if (world.isBlockNormalCube(x + 1, y, z)) {
            return true;
        } else if (world.isBlockNormalCube(x, y, z - 1)) {
            return true;
        } else {
            return world.isBlockNormalCube(x, y, z + 1);
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        int var6 = world.getBlockMetadata(x, y, z);
        if ((var6 == 0 || side == 2) && world.isBlockNormalCube(x, y, z + 1)) {
            var6 = 2;
        }

        if ((var6 == 0 || side == 3) && world.isBlockNormalCube(x, y, z - 1)) {
            var6 = 3;
        }

        if ((var6 == 0 || side == 4) && world.isBlockNormalCube(x + 1, y, z)) {
            var6 = 4;
        }

        if ((var6 == 0 || side == 5) && world.isBlockNormalCube(x - 1, y, z)) {
            var6 = 5;
        }

        world.setBlockMetadataWithNotify(x, y, z, var6);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        int var6 = world.getBlockMetadata(x, y, z);
        boolean var7 = false;
        if (var6 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
            var7 = true;
        }

        if (var6 == 3 && world.isBlockNormalCube(x, y, z - 1)) {
            var7 = true;
        }

        if (var6 == 4 && world.isBlockNormalCube(x + 1, y, z)) {
            var7 = true;
        }

        if (var6 == 5 && world.isBlockNormalCube(x - 1, y, z)) {
            var7 = true;
        }

        if (!var7) {
            this.dropBlockAsItem(world, x, y, z, var6);
            world.setBlockWithNotify(x, y, z, 0);
        }

        super.onNeighborBlockChange(world, x, y, z, var5);
    }

    @Override
    public int quantityDropped(Random var1) {
        return 1;
    }
}
