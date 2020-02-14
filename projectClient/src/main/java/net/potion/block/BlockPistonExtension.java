package net.potion.block;

import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.List;
import java.util.Random;

public class BlockPistonExtension extends Block {
    private int field_31053_a = -1;

    public BlockPistonExtension(int var1, int var2) {
        super(var1, var2, Material.PISTON);
        this.setStepSound(SOUND_STONE_FOOTSTEP);
        this.setHardness(0.5F);
    }

    public static int func_31050_c(int var0) {
        return var0 & 7;
    }

    public void func_31052_a_(int var1) {
        this.field_31053_a = var1;
    }

    public void func_31051_a() {
        this.field_31053_a = -1;
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        super.onBlockRemoval(var1, var2, var3, var4);
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        int var6 = PistonBlockTextures.field_31057_a[func_31050_c(var5)];
        var2 = var2 + PistonBlockTextures.field_31056_b[var6];
        var3 = var3 + PistonBlockTextures.field_31059_c[var6];
        var4 = var4 + PistonBlockTextures.field_31058_d[var6];
        int var7 = var1.getBlockId(var2, var3, var4);
        if (var7 == Block.PISTON_BASE.blockID || var7 == Block.PISTON_STICKY_BASE.blockID) {
            var5 = var1.getBlockMetadata(var2, var3, var4);
            if (BlockPistonBase.isPowered(var5)) {
                Block.BLOCKS_LIST[var7].dropBlockAsItem(var1, var2, var3, var4, var5);
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }
        }

    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        int var3 = func_31050_c(metadata);
        if (side == var3) {
            if (this.field_31053_a >= 0) {
                return this.field_31053_a;
            } else {
                return (metadata & 8) != 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture;
            }
        } else {
            return side == PistonBlockTextures.field_31057_a[var3] ? 107 : 108;
        }
    }

    @Override
    public int getRenderType() {
        return 17;
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int var5) {
        return false;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, List<AxisAlignedBB> bbs) {
        int var7 = var1.getBlockMetadata(var2, var3, var4);
        switch (func_31050_c(var7)) {
            case 0:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                break;
            case 1:
                this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                break;
            case 2:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                break;
            case 3:
                this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                break;
            case 4:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                break;
            case 5:
                this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z);
        switch (func_31050_c(var5)) {
            case 0:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                break;
            case 1:
                this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case 2:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                break;
            case 3:
                this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                break;
            case 4:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                break;
            case 5:
                this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        int var6 = func_31050_c(var1.getBlockMetadata(var2, var3, var4));
        int var7 = var1.getBlockId(var2 - PistonBlockTextures.field_31056_b[var6], var3 - PistonBlockTextures.field_31059_c[var6], var4 - PistonBlockTextures.field_31058_d[var6]);
        if (var7 != Block.PISTON_BASE.blockID && var7 != Block.PISTON_STICKY_BASE.blockID) {
            var1.setBlockWithNotify(var2, var3, var4, 0);
        } else {
            Block.BLOCKS_LIST[var7].onNeighborBlockChange(var1, var2 - PistonBlockTextures.field_31056_b[var6], var3 - PistonBlockTextures.field_31059_c[var6], var4 - PistonBlockTextures.field_31058_d[var6], var5);
        }

    }
}
