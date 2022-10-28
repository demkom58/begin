package net.potion.world.gen.struct;

import net.potion.block.Block;
import net.potion.world.World;

import java.util.Random;

public class WorldGenFire extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int var6 = 0; var6 < 64; ++var6) {
            int var7 = x + random.nextInt(8) - random.nextInt(8);
            int var8 = y + random.nextInt(4) - random.nextInt(4);
            int var9 = z + random.nextInt(8) - random.nextInt(8);
            if (world.isAirBlock(var7, var8, var9) && world.getBlockId(var7, var8 - 1, var9) == Block.BLOOD_STONE.blockID) {
                world.setBlockWithNotify(var7, var8, var9, Block.FIRE.blockID);
            }
        }

        return true;
    }
}
