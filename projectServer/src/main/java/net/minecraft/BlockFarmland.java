package net.minecraft;

import java.util.Random;

public class BlockFarmland extends Block {
    protected BlockFarmland(int var1) {
        super(var1, Material.ground);
        this.blockIndexInTexture = 87;
        this.setTickOnLoad(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool((double) (x + 0), (double) (y + 0), (double) (z + 0), (double) (x + 1), (double) (y + 1), (double) (z + 1));
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isACube() {
        return false;
    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 1 && var2 > 0) {
            return this.blockIndexInTexture - 1;
        } else {
            return var1 == 1 ? this.blockIndexInTexture : 2;
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (random.nextInt(5) == 0) {
            if (!this.isWaterNearby(world, x, y, z) && !world.canLightningStrikeAt(x, y + 1, z)) {
                int var6 = world.getBlockMetadata(x, y, z);
                if (var6 > 0) {
                    world.setBlockMetadataWithNotify(x, y, z, var6 - 1);
                } else if (!this.isCropsNearby(world, x, y, z)) {
                    world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
                }
            } else {
                world.setBlockMetadataWithNotify(x, y, z, 7);
            }
        }

    }

    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
        if (world.rand.nextInt(4) == 0) {
            world.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
        }

    }

    private boolean isCropsNearby(World var1, int var2, int var3, int var4) {
        byte var5 = 0;

        for (int var6 = var2 - var5; var6 <= var2 + var5; ++var6) {
            for (int var7 = var4 - var5; var7 <= var4 + var5; ++var7) {
                if (var1.getBlockId(var6, var3 + 1, var7) == Block.crops.blockID) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isWaterNearby(World var1, int var2, int var3, int var4) {
        for (int var5 = var2 - 4; var5 <= var2 + 4; ++var5) {
            for (int var6 = var3; var6 <= var3 + 1; ++var6) {
                for (int var7 = var4 - 4; var7 <= var4 + 4; ++var7) {
                    if (var1.getBlockMaterial(var5, var6, var7) == Material.water) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        super.onNeighborBlockChange(world, var2, var3, var4, var5);
        Material var6 = world.getBlockMaterial(var2, var3 + 1, var4);
        if (var6.isSolid()) {
            world.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
        }

    }

    public int idDropped(int var1, Random random) {
        return Block.dirt.idDropped(0, random);
    }
}
