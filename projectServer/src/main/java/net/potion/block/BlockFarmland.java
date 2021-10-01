package net.potion.block;

import net.potion.entity.Entity;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.World;

import java.util.Random;

public class BlockFarmland extends Block {
    protected BlockFarmland(int var1) {
        super(var1, Material.GROUND);
        this.blockIndexInTexture = 87;
        this.setTickOnLoad(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBoxFromPool(x, y, z, x + 1, y + 1, z + 1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 1 && var2 > 0) {
            return this.blockIndexInTexture - 1;
        } else {
            return var1 == 1 ? this.blockIndexInTexture : 2;
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (random.nextInt(5) == 0) {
            if (!this.isWaterNearby(world, x, y, z) && !world.canLightningStrikeAt(x, y + 1, z)) {
                int var6 = world.getBlockMetadata(x, y, z);
                if (var6 > 0) {
                    world.setBlockMetadataWithNotify(x, y, z, var6 - 1);
                } else if (!this.isCropsNearby(world, x, y, z)) {
                    world.setBlockWithNotify(x, y, z, Block.DIRT.blockID);
                }
            } else {
                world.setBlockMetadataWithNotify(x, y, z, 7);
            }
        }

    }

    @Override
    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
        if (world.rand.nextInt(4) == 0) {
            world.setBlockWithNotify(var2, var3, var4, Block.DIRT.blockID);
        }

    }

    private boolean isCropsNearby(World var1, int var2, int var3, int var4) {
        byte var5 = 0;

        for (int var6 = var2 - var5; var6 <= var2 + var5; ++var6) {
            for (int var7 = var4 - var5; var7 <= var4 + var5; ++var7) {
                if (var1.getBlockId(var6, var3 + 1, var7) == Block.CROPS.blockID) {
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
                    if (var1.getBlockMaterial(var5, var6, var7) == Material.WATER) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        super.onNeighborBlockChange(world, var2, var3, var4, var5);
        Material var6 = world.getBlockMaterial(var2, var3 + 1, var4);
        if (var6.isSolid()) {
            world.setBlockWithNotify(var2, var3, var4, Block.DIRT.blockID);
        }

    }

    @Override
    public int idDropped(int var1, Random random) {
        return Block.DIRT.idDropped(0, random);
    }
}
