package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumMobType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockPressurePlate extends Block {
    private EnumMobType triggerMobType;

    protected BlockPressurePlate(int var1, int var2, EnumMobType var3, Material var4) {
        super(var1, var2, var4);
        this.triggerMobType = var3;
        this.setTickOnLoad(true);
        float var5 = 0.0625F;
        this.setBlockBounds(var5, 0.0F, var5, 1.0F - var5, 0.03125F, 1.0F - var5);
    }

    @Override
    public int tickRate() {
        return 20;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
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
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return world.isBlockNormalCube(var2, var3 - 1, var4);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        boolean var6 = false;
        if (!world.isBlockNormalCube(var2, var3 - 1, var4)) {
            var6 = true;
        }

        if (var6) {
            this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
            world.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.singleplayerWorld) {
            if (world.getBlockMetadata(x, y, z) != 0) {
                this.setStateIfMobInteractsWithPlate(world, x, y, z);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        if (!world.singleplayerWorld) {
            if (world.getBlockMetadata(var2, var3, var4) != 1) {
                this.setStateIfMobInteractsWithPlate(world, var2, var3, var4);
            }
        }
    }

    private void setStateIfMobInteractsWithPlate(World var1, int var2, int var3, int var4) {
        boolean var5 = var1.getBlockMetadata(var2, var3, var4) == 1;
        boolean var6 = false;
        float var7 = 0.125F;
        List<Entity> var8 = null;
        if (this.triggerMobType == EnumMobType.EVERYTHING) {
            var8 = var1.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool((float) var2 + var7, var3, (float) var4 + var7, (float) (var2 + 1) - var7, (double) var3 + 0.25D, (float) (var4 + 1) - var7));
        }

        if (this.triggerMobType == EnumMobType.MOBS) {
            var8 = var1.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool((float) var2 + var7, var3, (float) var4 + var7, (float) (var2 + 1) - var7, (double) var3 + 0.25D, (float) (var4 + 1) - var7));
        }

        if (this.triggerMobType == EnumMobType.PLAYERS) {
            var8 = var1.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBoxFromPool((float) var2 + var7, var3, (float) var4 + var7, (float) (var2 + 1) - var7, (double) var3 + 0.25D, (float) (var4 + 1) - var7));
        }

        if (var8.size() > 0) {
            var6 = true;
        }

        if (var6 && !var5) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 1);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
            var1.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.1D, (double) var4 + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!var6 && var5) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 0);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
            var1.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.1D, (double) var4 + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (var6) {
            var1.scheduleUpdateTick(var2, var3, var4, this.blockID, this.tickRate());
        }

    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        int var5 = world.getBlockMetadata(x, y, z);
        if (var5 > 0) {
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
        }

        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int var2, int var3, int var4) {
        boolean var5 = blockAccess.getBlockMetadata(var2, var3, var4) == 1;
        float var6 = 0.0625F;
        if (var5) {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
        } else {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
        }

    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        return blockAccess.getBlockMetadata(var2, var3, var4) > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        if (world.getBlockMetadata(var2, var3, var4) == 0) {
            return false;
        } else {
            return var5 == 1;
        }
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
