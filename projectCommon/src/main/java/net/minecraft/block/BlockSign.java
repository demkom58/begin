package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.item.Item;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSign extends BlockContainer {
    private Class signEntityClass;
    private boolean isFreestanding;

    protected BlockSign(int var1, Class var2, boolean var3) {
        super(var1, Material.WOOD);
        this.isFreestanding = var3;
        this.blockIndexInTexture = 4;
        this.signEntityClass = var2;
        float var4 = 0.25F;
        float var5 = 1.0F;
        this.setBlockBounds(0.5F - var4, 0.0F, 0.5F - var4, 0.5F + var4, var5, 0.5F + var4);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        if (this.isFreestanding) {
            return;
        }

        int var5 = blockAccess.getBlockMetadata(x, y, z);
        float var6 = 0.28125F;
        float var7 = 0.78125F;
        float var8 = 0.0F;
        float var9 = 1.0F;
        float var10 = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        if (var5 == 2) {
            this.setBlockBounds(var8, var6, 1.0F - var10, var9, var7, 1.0F);
        }

        if (var5 == 3) {
            this.setBlockBounds(var8, var6, 0.0F, var9, var7, var10);
        }

        if (var5 == 4) {
            this.setBlockBounds(1.0F - var10, var6, var8, 1.0F, var7, var9);
        }

        if (var5 == 5) {
            this.setBlockBounds(0.0F, var6, var8, var10, var7, var9);
        }

    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return -1;
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
    @SuppressWarnings("unchecked")
    protected TileEntity getBlockEntity() {
        try {
            return (TileEntity) this.signEntityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Item.SIGN.shiftedIndex;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        boolean var6 = false;
        if (this.isFreestanding) {
            if (!world.getBlockMaterial(x, y - 1, z).isSolid()) {
                var6 = true;
            }
        } else {
            int var7 = world.getBlockMetadata(x, y, z);
            var6 = var7 != 2 || !world.getBlockMaterial(x, y, z + 1).isSolid();

            if (var7 == 3 && world.getBlockMaterial(x, y, z - 1).isSolid()) {
                var6 = false;
            }

            if (var7 == 4 && world.getBlockMaterial(x + 1, y, z).isSolid()) {
                var6 = false;
            }

            if (var7 == 5 && world.getBlockMaterial(x - 1, y, z).isSolid()) {
                var6 = false;
            }
        }

        if (var6) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }

        super.onNeighborBlockChange(world, x, y, z, var5);
    }
}
