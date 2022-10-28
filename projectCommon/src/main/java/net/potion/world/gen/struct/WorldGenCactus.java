package net.potion.world.gen.struct;

import net.potion.block.Block;
import net.potion.world.World;

import java.util.Random;

public class WorldGenCactus extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int var6 = 0; var6 < 10; ++var6) {
            int var7 = x + random.nextInt(8) - random.nextInt(8);
            int var8 = y + random.nextInt(4) - random.nextInt(4);
            int var9 = z + random.nextInt(8) - random.nextInt(8);
            if (world.isAirBlock(var7, var8, var9)) {
                int var10 = 1 + random.nextInt(random.nextInt(3) + 1);

                for (int var11 = 0; var11 < var10; ++var11) {
                    if (Block.CACTUS.canBlockStay(world, var7, var8 + var11, var9)) {
                        world.setBlock(var7, var8 + var11, var9, Block.CACTUS.blockID);
                    }
                }
            }
        }

        return true;
    }
}
