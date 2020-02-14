package net.potion.block;

import net.potion.material.Material;
import net.potion.world.World;
import net.potion.util.AxisAlignedBB;

import java.util.Random;

public class BlockFlower extends Block {
    protected BlockFlower(int var1, int var2) {
        super(var1, Material.PLANTS);
        this.blockIndexInTexture = var2;
        this.setTickOnLoad(true);
        float var3 = 0.2F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return super.canPlaceBlockAt(world, var2, var3, var4) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(var2, var3 - 1, var4));
    }

    protected boolean canThisPlantGrowOnThisBlockID(int var1) {
        return var1 == Block.GRASS.blockID || var1 == Block.DIRT.blockID || var1 == Block.FARMLAND.blockID;
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        super.onNeighborBlockChange(world, var2, var3, var4, var5);
        this.func_276_g(world, var2, var3, var4);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        this.func_276_g(world, x, y, z);
    }

    protected final void func_276_g(World var1, int var2, int var3, int var4) {
        if (!this.canBlockStay(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return (world.getBlockLightValueNoChecks(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
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
}
