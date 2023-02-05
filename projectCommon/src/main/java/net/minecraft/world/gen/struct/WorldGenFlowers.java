package net.minecraft.world.gen.struct;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenFlowers extends WorldGenerator {
    private int plantBlockId;

    public WorldGenFlowers(int var1) {
        this.plantBlockId = var1;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int var6 = 0; var6 < 64; ++var6) {
            int var7 = x + random.nextInt(8) - random.nextInt(8);
            int var8 = y + random.nextInt(4) - random.nextInt(4);
            int var9 = z + random.nextInt(8) - random.nextInt(8);
            if (world.isAirBlock(var7, var8, var9) && Block.BLOCKS_LIST[this.plantBlockId].canBlockStay(world, var7, var8, var9)) {
                world.setBlock(var7, var8, var9, this.plantBlockId);
            }
        }

        return true;
    }
}
