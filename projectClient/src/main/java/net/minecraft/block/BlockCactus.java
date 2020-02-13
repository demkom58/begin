package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCactus extends Block {
    protected BlockCactus(int var1, int var2) {
        super(var1, var2, Material.CACTUS);
        this.setTickOnLoad(true);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var1.isAirBlock(var2, var3 + 1, var4)) {
            int var6;
            for (var6 = 1; var1.getBlockId(var2, var3 - var6, var4) == this.blockID; ++var6) { }

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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        float var5 = 0.0625F;
        return AxisAlignedBB.getBoundingBoxFromPool((float) var2 + var5, var3, (float) var4 + var5, (float) (var2 + 1) - var5, (float) (var3 + 1) - var5, (float) (var4 + 1) - var5);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        float var5 = 0.0625F;
        return AxisAlignedBB.getBoundingBoxFromPool((float) x + var5, y, (float) z + var5, (float) (x + 1) - var5, y + 1, (float) (z + 1) - var5);
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 1) {
            return this.blockIndexInTexture - 1;
        } else {
            return side == 0 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 13;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (!this.canBlockStay(var1, var2, var3, var4)) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (world.getBlockMaterial(x - 1, y, z).isSolid()) {
            return false;
        } else if (world.getBlockMaterial(x + 1, y, z).isSolid()) {
            return false;
        } else if (world.getBlockMaterial(x, y, z - 1).isSolid()) {
            return false;
        } else if (world.getBlockMaterial(x, y, z + 1).isSolid()) {
            return false;
        } else {
            int var5 = world.getBlockId(x, y - 1, z);
            return var5 == Block.CACTUS.blockID || var5 == Block.SAND.blockID;
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.attackEntityFrom(null, 1);
    }
}
