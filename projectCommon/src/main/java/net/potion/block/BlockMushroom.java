package net.potion.block;

import net.potion.world.World;

import java.util.Random;

public class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int var1, int var2) {
        super(var1, var2);
        float var3 = 0.2F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setTickOnLoad(true);
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var5.nextInt(100) == 0) {
            int var6 = var2 + var5.nextInt(3) - 1;
            int var7 = var3 + var5.nextInt(2) - var5.nextInt(2);
            int var8 = var4 + var5.nextInt(3) - 1;
            if (var1.isAirBlock(var6, var7, var8) && this.canBlockStay(var1, var6, var7, var8)) {
                int var10000 = var2 + (var5.nextInt(3) - 1);
                var10000 = var4 + (var5.nextInt(3) - 1);
                if (var1.isAirBlock(var6, var7, var8) && this.canBlockStay(var1, var6, var7, var8)) {
                    var1.setBlockWithNotify(var6, var7, var8, this.blockID);
                }
            }
        }

    }

    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int var1) {
        return Block.OPAQUE_CUBE_LOOKUP[var1];
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (y >= 0 && y < 128) {
            return world.getFullBlockLightValue(x, y, z) < 13 && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
        } else {
            return false;
        }
    }
}
