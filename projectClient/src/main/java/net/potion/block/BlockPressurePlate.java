package net.potion.block;

import net.potion.entity.Entity;
import net.potion.entity.EntityLiving;
import net.potion.entity.EnumMobType;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
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
        return world.isBlockNormalCube(x, y - 1, z);
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        boolean var6 = false;
        if (!var1.isBlockNormalCube(var2, var3 - 1, var4)) {
            var6 = true;
        }

        if (var6) {
            this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (!var1.multiplayerWorld) {
            if (var1.getBlockMetadata(var2, var3, var4) != 0) {
                this.setStateIfMobInteractsWithPlate(var1, var2, var3, var4);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!world.multiplayerWorld) {
            if (world.getBlockMetadata(x, y, z) != 1) {
                this.setStateIfMobInteractsWithPlate(world, x, y, z);
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
            var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
        }

    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        if (var5 > 0) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
        }

        super.onBlockRemoval(var1, var2, var3, var4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        boolean var5 = blockAccess.getBlockMetadata(x, y, z) == 1;
        float var6 = 0.0625F;
        if (var5) {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
        } else {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
        }

    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        return blockAccess.getBlockMetadata(x, y, z) > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int var5) {
        if (world.getBlockMetadata(x, y, z) == 0) {
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
    public void setBlockBoundsForItemRender() {
        float var1 = 0.5F;
        float var2 = 0.125F;
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }
}
