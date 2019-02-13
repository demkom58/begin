package net.minecraft;

import java.util.Random;

public class BlockCactus extends Block {
    protected BlockCactus(int var1, int var2) {
        super(var1, var2, Material.cactus);
        this.setTickOnLoad(true);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isAirBlock(x, y + 1, z)) {
            int var6;
            for (var6 = 1; world.getBlockId(x, y - var6, z) == this.blockID; ++var6) {
            }

            if (var6 < 3) {
                int var7 = world.getBlockMetadata(x, y, z);
                if (var7 == 15) {
                    world.setBlockWithNotify(x, y + 1, z, this.blockID);
                    world.setBlockMetadataWithNotify(x, y, z, 0);
                } else {
                    world.setBlockMetadataWithNotify(x, y, z, var7 + 1);
                }
            }
        }

    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        float var5 = 0.0625F;
        return AxisAlignedBB.getBoundingBoxFromPool((double) ((float) x + var5), (double) y, (double) ((float) z + var5), (double) ((float) (x + 1) - var5), (double) ((float) (y + 1) - var5), (double) ((float) (z + 1) - var5));
    }

    public int getBlockTextureFromSide(int var1) {
        if (var1 == 1) {
            return this.blockIndexInTexture - 1;
        } else {
            return var1 == 0 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    public boolean isACube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
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
            return var5 == Block.cactus.blockID || var5 == Block.sand.blockID;
        }
    }

    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        entity.attackEntityFrom(null, 1);
    }
}
