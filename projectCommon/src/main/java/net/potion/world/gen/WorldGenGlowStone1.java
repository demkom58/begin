package net.potion.world.gen;

import net.potion.block.Block;
import net.potion.world.World;

import java.util.Random;

public class WorldGenGlowStone1 extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        if (!world.isAirBlock(x, y, z)) {
            return false;
        }

        if (world.getBlockId(x, y + 1, z) != Block.BLOOD_STONE.blockID) {
            return false;
        }

        world.setBlockWithNotify(x, y, z, Block.GLOW_STONE.blockID);

        for (int var6 = 0; var6 < 1500; ++var6) {
            int var7 = x + random.nextInt(8) - random.nextInt(8);
            int var8 = y - random.nextInt(12);
            int var9 = z + random.nextInt(8) - random.nextInt(8);
            if (world.getBlockId(var7, var8, var9) != 0) {
                continue;
            }

            int var10 = 0;
            for (int var11 = 0; var11 < 6; ++var11) {
                int var12 = 0;
                if (var11 == 0) {
                    var12 = world.getBlockId(var7 - 1, var8, var9);
                }

                if (var11 == 1) {
                    var12 = world.getBlockId(var7 + 1, var8, var9);
                }

                if (var11 == 2) {
                    var12 = world.getBlockId(var7, var8 - 1, var9);
                }

                if (var11 == 3) {
                    var12 = world.getBlockId(var7, var8 + 1, var9);
                }

                if (var11 == 4) {
                    var12 = world.getBlockId(var7, var8, var9 - 1);
                }

                if (var11 == 5) {
                    var12 = world.getBlockId(var7, var8, var9 + 1);
                }

                if (var12 == Block.GLOW_STONE.blockID) {
                    ++var10;
                }
            }

            if (var10 == 1) {
                world.setBlockWithNotify(var7, var8, var9, Block.GLOW_STONE.blockID);
            }
        }

        return true;
    }
}
