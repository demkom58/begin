package net.minecraft.block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecart;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;
import java.util.Random;

public class BlockDetectorRail extends BlockRail {
    public BlockDetectorRail(int var1, int var2) {
        super(var1, var2, true);
        this.setTickOnLoad(true);
    }

    public int tickRate() {
        return 20;
    }

    public boolean canProvidePower() {
        return true;
    }

    public void onEntityCollidedWithBlock(World world, int var2, int var3, int var4, Entity entity) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            if ((var6 & 8) == 0) {
                this.func_27035_f(world, var2, var3, var4, var6);
            }
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.singleplayerWorld) {
            int var6 = world.getBlockMetadata(x, y, z);
            if ((var6 & 8) != 0) {
                this.func_27035_f(world, x, y, z, var6);
            }
        }
    }

    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        return (blockAccess.getBlockMetadata(var2, var3, var4) & 8) != 0;
    }

    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        if ((world.getBlockMetadata(var2, var3, var4) & 8) == 0) {
            return false;
        } else {
            return var5 == 1;
        }
    }

    private void func_27035_f(World var1, int var2, int var3, int var4, int var5) {
        boolean var6 = (var5 & 8) != 0;
        boolean var7 = false;
        float var8 = 0.125F;
        List<Entity> var9 = var1.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBoxFromPool((double) ((float) var2 + var8), (double) var3, (double) ((float) var4 + var8), (double) ((float) (var2 + 1) - var8), (double) var3 + 0.25D, (double) ((float) (var4 + 1) - var8)));
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
            var1.scheduleUpdateTick(var2, var3, var4, this.blockID, this.tickRate());
        }

    }
}
