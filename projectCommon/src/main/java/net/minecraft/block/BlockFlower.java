package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
    }

    protected boolean canThisPlantGrowOnThisBlockID(int var1) {
        return var1 == Block.GRASS.blockID || var1 == Block.DIRT.blockID || var1 == Block.FARMLAND.blockID;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        super.onNeighborBlockChange(world, x, y, z, var5);
        this.updateState(world, x, y, z);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        this.updateState(var1, var2, var3, var4);
    }

    protected final void updateState(World var1, int var2, int var3, int var4) {
        if (!this.canBlockStay(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
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
        return 1;
    }
}
