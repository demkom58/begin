package net.potion.block;

import net.potion.entity.player.EntityPlayer;
import net.potion.world.IBlockAccess;
import net.potion.material.Material;
import net.potion.world.World;
import net.potion.util.AxisAlignedBB;

import java.util.Random;

public class BlockCake extends Block {
    protected BlockCake(int var1, int var2) {
        super(var1, var2, Material.CAKE);
        this.setTickOnLoad(true);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        int var5 = blockAccess.getBlockMetadata(var2, var3, var4);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        this.setBlockBounds(var7, 0.0F, var6, 1.0F - var6, var8, 1.0F - var6);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        return AxisAlignedBB.getBoundingBoxFromPool((float) x + var7, y, (float) z + var6, (float) (x + 1) - var6, (float) y + var8 - var6, (float) (z + 1) - var6);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 1) {
            return this.blockIndexInTexture;
        } else if (var1 == 0) {
            return this.blockIndexInTexture + 3;
        } else {
            return var2 > 0 && var1 == 4 ? this.blockIndexInTexture + 2 : this.blockIndexInTexture + 1;
        }
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture;
        } else {
            return var1 == 0 ? this.blockIndexInTexture + 3 : this.blockIndexInTexture + 1;
        }
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.eatCakeSlice(world, var2, var3, var4, entityPlayer);
        return true;
    }

    @Override
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

    @Override
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return super.canPlaceBlockAt(world, var2, var3, var4) && this.canBlockStay(world, var2, var3, var4);
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!this.canBlockStay(world, var2, var3, var4)) {
            this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
            world.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y - 1, z).isSolid();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int idDropped(int var1, Random random) {
        return 0;
    }
}
