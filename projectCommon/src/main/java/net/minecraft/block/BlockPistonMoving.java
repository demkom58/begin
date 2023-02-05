package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPistonMoving extends BlockContainer {
    public BlockPistonMoving(int var1) {
        super(var1, Material.PISTON);
        this.setHardness(-1.0F);
    }

    public static TileEntity getTileEntity(int var0, int var1, int var2, boolean var3, boolean var4) {
        return new TileEntityPiston(var0, var1, var2, var3, var4);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return null;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        TileEntity var5 = var1.getBlockTileEntity(var2, var3, var4);
        if (var5 instanceof TileEntityPiston) {
            ((TileEntityPiston) var5).clearPistonTileEntity();
        } else {
            super.onBlockRemoval(var1, var2, var3, var4);
        }

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
    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return -1;
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
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.localWorld && world.getBlockTileEntity(x, y, z) == null) {
            world.setBlockWithNotify(x, y, z, 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int idDropped(int var1, Random var2) {
        return 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float failChance) {
        if (!world.localWorld) {
            TileEntityPiston var7 = this.getTileEntityAtLocation(world, x, y, z);
            if (var7 != null) {
                Block.BLOCKS_LIST[var7.getStoredBlockID()].dropBlockAsItem(world, x, y, z, var7.getBlockMetadata());
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        if (!world.localWorld) {
            world.getBlockTileEntity(x, y, z);
        }

    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        TileEntityPiston var5 = this.getTileEntityAtLocation(var1, var2, var3, var4);
        if (var5 == null) {
            return null;
        } else {
            float var6 = var5.getProgress(0.0F);
            if (var5.isExtending()) {
                var6 = 1.0F - var6;
            }

            return this.method1(var1, var2, var3, var4, var5.getStoredBlockID(), var6, var5.getStoredOrientation());
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntityPiston var5 = this.getTileEntityAtLocation(blockAccess, x, y, z);
        if (var5 != null) {
            Block var6 = Block.BLOCKS_LIST[var5.getStoredBlockID()];
            if (var6 == null || var6 == this) {
                return;
            }

            var6.setBlockBoundsBasedOnState(blockAccess, x, y, z);
            float var7 = var5.getProgress(0.0F);
            if (var5.isExtending()) {
                var7 = 1.0F - var7;
            }

            int var8 = var5.getStoredOrientation();
            this.minX = var6.minX - (double) ((float) PistonBlockTextures.field2[var8] * var7);
            this.minY = var6.minY - (double) ((float) PistonBlockTextures.field3[var8] * var7);
            this.minZ = var6.minZ - (double) ((float) PistonBlockTextures.field4[var8] * var7);
            this.maxX = var6.maxX - (double) ((float) PistonBlockTextures.field2[var8] * var7);
            this.maxY = var6.maxY - (double) ((float) PistonBlockTextures.field3[var8] * var7);
            this.maxZ = var6.maxZ - (double) ((float) PistonBlockTextures.field4[var8] * var7);
        }

    }

    public AxisAlignedBB method1(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
        if (var5 != 0 && var5 != this.blockID) {
            AxisAlignedBB var8 = Block.BLOCKS_LIST[var5].getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
            if (var8 == null) {
                return null;
            } else {
                var8.minX -= (float) PistonBlockTextures.field2[var7] * var6;
                var8.maxX -= (float) PistonBlockTextures.field2[var7] * var6;
                var8.minY -= (float) PistonBlockTextures.field3[var7] * var6;
                var8.maxY -= (float) PistonBlockTextures.field3[var7] * var6;
                var8.minZ -= (float) PistonBlockTextures.field4[var7] * var6;
                var8.maxZ -= (float) PistonBlockTextures.field4[var7] * var6;
                return var8;
            }
        } else {
            return null;
        }
    }

    private TileEntityPiston getTileEntityAtLocation(IBlockAccess var1, int var2, int var3, int var4) {
        TileEntity var5 = var1.getBlockTileEntity(var2, var3, var4);
        return var5 instanceof TileEntityPiston ? (TileEntityPiston) var5 : null;
    }
}
