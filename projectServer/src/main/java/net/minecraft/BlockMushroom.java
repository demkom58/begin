package net.minecraft;

import java.util.Random;

public class BlockMushroom extends BlockFlower {
    protected BlockMushroom(int var1, int var2) {
        super(var1, var2);
        float var3 = 0.2F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
        this.setTickOnLoad(true);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (random.nextInt(100) == 0) {
            int var6 = x + random.nextInt(3) - 1;
            int var7 = y + random.nextInt(2) - random.nextInt(2);
            int var8 = z + random.nextInt(3) - 1;
            if (world.isAirBlock(var6, var7, var8) && this.canBlockStay(world, var6, var7, var8)) {
                int var10000 = x + (random.nextInt(3) - 1);
                var10000 = z + (random.nextInt(3) - 1);
                if (world.isAirBlock(var6, var7, var8) && this.canBlockStay(world, var6, var7, var8)) {
                    world.setBlockWithNotify(var6, var7, var8, this.blockID);
                }
            }
        }

    }

    protected boolean canThisPlantGrowOnThisBlockID(int var1) {
        return Block.opaqueCubeLookup[var1];
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        if (y >= 0 && y < 128) {
            return world.getBlockLightValueNoChecks(x, y, z) < 13 && this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
        } else {
            return false;
        }
    }
}
