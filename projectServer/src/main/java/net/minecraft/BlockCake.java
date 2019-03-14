package net.minecraft;

import java.util.Random;

public class BlockCake extends Block {
    protected BlockCake(int var1, int var2) {
        super(var1, var2, Material.CAKE);
        this.setTickOnLoad(true);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        int var5 = blockAccess.getBlockMetadata(var2, var3, var4);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        this.setBlockBounds(var7, 0.0F, var6, 1.0F - var6, var8, 1.0F - var6);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        return AxisAlignedBB.getBoundingBoxFromPool((double) ((float) x + var7), (double) y, (double) ((float) z + var6), (double) ((float) (x + 1) - var6), (double) ((float) y + var8 - var6), (double) ((float) (z + 1) - var6));
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 1) {
            return this.blockIndexInTexture;
        } else if (var1 == 0) {
            return this.blockIndexInTexture + 3;
        } else {
            return var2 > 0 && var1 == 4 ? this.blockIndexInTexture + 2 : this.blockIndexInTexture + 1;
        }
    }

    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture;
        } else {
            return var1 == 0 ? this.blockIndexInTexture + 3 : this.blockIndexInTexture + 1;
        }
    }

    public boolean isACube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.eatCakeSlice(world, var2, var3, var4, entityPlayer);
        return true;
    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.eatCakeSlice(world, var2, var3, var4, entityPlayer);
    }

    private void eatCakeSlice(World var1, int var2, int var3, int var4, EntityPlayer var5) {
        if (var5.health < 20) {
            var5.heal(3);
            int var6 = var1.getBlockMetadata(var2, var3, var4) + 1;
            if (var6 >= 6) {
                var1.setBlockWithNotify(var2, var3, var4, 0);
            } else {
                var1.setBlockMetadataWithNotify(var2, var3, var4, var6);
                var1.markBlockAsNeedsUpdate(var2, var3, var4);
            }
        }

    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return super.canPlaceBlockAt(world, var2, var3, var4) && this.canBlockStay(world, var2, var3, var4);
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!this.canBlockStay(world, var2, var3, var4)) {
            this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
            world.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y - 1, z).isSolid();
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public int idDropped(int var1, Random random) {
        return 0;
    }
}
