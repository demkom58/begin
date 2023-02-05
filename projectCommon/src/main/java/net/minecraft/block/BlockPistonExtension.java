package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockPistonExtension extends Block {
    private int field1 = -1;

    public BlockPistonExtension(int var1, int var2) {
        super(var1, var2, Material.PISTON);
        this.setStepSound(SOUND_STONE_FOOTSTEP);
        this.setHardness(0.5F);
    }

    public static int method1(int var0) {
        return var0 & 7;
    }

    public void method2(int var1) {
        this.field1 = var1;
    }

    public void method3() {
        this.field1 = -1;
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        super.onBlockRemoval(var1, var2, var3, var4);
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        int var6 = PistonBlockTextures.field1[method1(var5)];
        var2 = var2 + PistonBlockTextures.field2[var6];
        var3 = var3 + PistonBlockTextures.field3[var6];
        var4 = var4 + PistonBlockTextures.field4[var6];
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
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        int var3 = method1(metadata);
        if (side == var3) {
            if (this.field1 >= 0) {
                return this.field1;
            } else {
                return (metadata & 8) != 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture;
            }
        } else {
            return side == PistonBlockTextures.field1[var3] ? 107 : 108;
        }
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return 17;
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
        switch (method1(var7)) {
            case 0 -> {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            }
            case 1 -> {
                this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            }
            case 2 -> {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            }
            case 3 -> {
                this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            }
            case 4 -> {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            }
            case 5 -> {
                this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
                this.setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
                super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, bbs);
            }
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int var5 = blockAccess.getBlockMetadata(x, y, z);
        switch (method1(var5)) {
            case 0 -> this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            case 1 -> this.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
            case 2 -> this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
            case 3 -> this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
            case 4 -> this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
            case 5 -> this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        int var6 = method1(world.getBlockMetadata(x, y, z));
        int var7 = world.getBlockId(x - PistonBlockTextures.field2[var6], y - PistonBlockTextures.field3[var6], z - PistonBlockTextures.field4[var6]);
        if (var7 != Block.PISTON_BASE.blockID && var7 != Block.PISTON_STICKY_BASE.blockID) {
            world.setBlockWithNotify(x, y, z, 0);
        } else {
            Block.BLOCKS_LIST[var7].onNeighborBlockChange(world, x - PistonBlockTextures.field2[var6], y - PistonBlockTextures.field3[var6], z - PistonBlockTextures.field4[var6], var5);
        }

    }
}
