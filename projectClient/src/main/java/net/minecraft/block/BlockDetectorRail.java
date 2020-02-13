package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecart;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockDetectorRail extends BlockRail {
    public BlockDetectorRail(int var1, int var2) {
        super(var1, var2, true);
        this.setTickOnLoad(true);
    }

    @Override
    public int tickRate() {
        return 20;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!world.multiplayerWorld) {
            int var6 = world.getBlockMetadata(x, y, z);
            if ((var6 & 8) == 0) {
                this.setStateIfMinecartInteractsWithRail(world, x, y, z, var6);
            }
        }
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (!var1.multiplayerWorld) {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            if ((var6 & 8) != 0) {
                this.setStateIfMinecartInteractsWithRail(var1, var2, var3, var4, var6);
            }
        }
    }

    @Override
    public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        return (blockAccess.getBlockMetadata(x, y, z) & 8) != 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int var5) {
        if ((world.getBlockMetadata(x, y, z) & 8) == 0) {
            return false;
        } else {
            return var5 == 1;
        }
    }

    private void setStateIfMinecartInteractsWithRail(World var1, int var2, int var3, int var4, int var5) {
        boolean var6 = (var5 & 8) != 0;
        boolean var7 = false;
        float var8 = 0.125F;
        List<Entity> var9 = var1.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBoxFromPool((float) var2 + var8, var3, (float) var4 + var8, (float) (var2 + 1) - var8, (double) var3 + 0.25D, (float) (var4 + 1) - var8));
        if (var9.size() > 0) {
            var7 = true;
        }

        if (var7 && !var6) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var5 | 8);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
        }

        if (!var7 && var6) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var5 & 7);
            var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
            var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
        }

        if (var7) {
            var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
        }

    }
}
