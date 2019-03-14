package net.minecraft;

import java.util.Random;

public class BlockReed extends Block {
    protected BlockReed(int var1, int var2) {
        super(var1, Material.PLANTS);
        this.blockIndexInTexture = var2;
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickOnLoad(true);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isAirBlock(x, y + 1, z)) {
            int var6;
            for (var6 = 1; world.getBlockId(x, y - var6, z) == this.blockID; ++var6) {
            }

            if (var6 < 3) {
                int var7 = world.getBlockMetadata(x, y, z);
                if (var7 == 15) {
                    world.setBlockWithNotify(x, y + 1, z, this.blockID);
                    world.setBlockMetadataWithNotify(x, y, z, 0);
                } else {
                    world.setBlockMetadataWithNotify(x, y, z, var7 + 1);
                }
            }
        }

    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        int var5 = world.getBlockId(var2, var3 - 1, var4);
        if (var5 == this.blockID) {
            return true;
        } else if (var5 != Block.GRASS.blockID && var5 != Block.DIRT.blockID) {
            return false;
        } else if (world.getBlockMaterial(var2 - 1, var3 - 1, var4) == Material.WATER) {
            return true;
        } else if (world.getBlockMaterial(var2 + 1, var3 - 1, var4) == Material.WATER) {
            return true;
        } else if (world.getBlockMaterial(var2, var3 - 1, var4 - 1) == Material.WATER) {
            return true;
        } else {
            return world.getBlockMaterial(var2, var3 - 1, var4 + 1) == Material.WATER;
        }
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        this.checkBlockCoordValid(world, var2, var3, var4);
    }

    protected final void checkBlockCoordValid(World var1, int var2, int var3, int var4) {
        if (!this.canBlockStay(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return this.canPlaceBlockAt(world, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public int idDropped(int var1, Random random) {
        return Item.REEDS.shiftedIndex;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }
}
