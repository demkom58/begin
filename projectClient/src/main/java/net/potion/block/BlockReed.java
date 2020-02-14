package net.potion.block;

import net.potion.item.Item;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.World;

import java.util.Random;

public class BlockReed extends Block {
    protected BlockReed(int var1, int var2) {
        super(var1, Material.PLANTS);
        this.blockIndexInTexture = var2;
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickOnLoad(true);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var1.isAirBlock(var2, var3 + 1, var4)) {
            int var6;
            for (var6 = 1; var1.getBlockId(var2, var3 - var6, var4) == this.blockID; ++var6) {
            }

            if (var6 < 3) {
                int var7 = var1.getBlockMetadata(var2, var3, var4);
                if (var7 == 15) {
                    var1.setBlockWithNotify(var2, var3 + 1, var4, this.blockID);
                    var1.setBlockMetadataWithNotify(var2, var3, var4, 0);
                } else {
                    var1.setBlockMetadataWithNotify(var2, var3, var4, var7 + 1);
                }
            }
        }

    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int var5 = world.getBlockId(x, y - 1, z);
        if (var5 == this.blockID) {
            return true;
        } else if (var5 != Block.GRASS.blockID && var5 != Block.DIRT.blockID) {
            return false;
        } else if (world.getBlockMaterial(x - 1, y - 1, z) == Material.WATER) {
            return true;
        } else if (world.getBlockMaterial(x + 1, y - 1, z) == Material.WATER) {
            return true;
        } else if (world.getBlockMaterial(x, y - 1, z - 1) == Material.WATER) {
            return true;
        } else {
            return world.getBlockMaterial(x, y - 1, z + 1) == Material.WATER;
        }
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        this.checkBlockCoordValid(var1, var2, var3, var4);
    }

    protected final void checkBlockCoordValid(World var1, int var2, int var3, int var4) {
        if (!this.canBlockStay(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return this.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Item.REEDS.shiftedIndex;
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
        return 1;
    }
}
