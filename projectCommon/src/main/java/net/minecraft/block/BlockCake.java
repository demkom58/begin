package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCake extends Block {
    protected BlockCake(int var1, int var2) {
        super(var1, var2, Material.CAKE);
        this.setTickOnLoad(true);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        this.setBlockBounds(var7, 0.0F, var6, 1.0F - var6, var8, 1.0F - var6);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void setBlockBoundsForItemRender() {
        float var1 = 0.0625F;
        float var2 = 0.5F;
        this.setBlockBounds(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        return AxisAlignedBB.getBoundingBoxFromPool((float) var2 + var7, var3, (float) var4 + var6, (float) (var2 + 1) - var6, (float) var3 + var8 - var6, (float) (var4 + 1) - var6);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float) (1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        return AxisAlignedBB.getBoundingBoxFromPool((float) x + var7, y, (float) z + var6, (float) (x + 1) - var6, (float) y + var8, (float) (z + 1) - var6);
    }

    @Side(CodeSide.CLIENT)
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        if (side == 1) {
            return this.blockIndexInTexture;
        } else if (side == 0) {
            return this.blockIndexInTexture + 3;
        } else {
            return metadata > 0 && side == 4 ? this.blockIndexInTexture + 2 : this.blockIndexInTexture + 1;
        }
    }

    @Side(CodeSide.CLIENT)
    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture;
        } else {
            return side == 0 ? this.blockIndexInTexture + 3 : this.blockIndexInTexture + 1;
        }
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        this.eatCakeSlice(world, x, y, z, player);
        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.eatCakeSlice(world, x, y, z, player);
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }

    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlockMaterial(x, y - 1, z).isSolid();
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return 0;
    }
}
