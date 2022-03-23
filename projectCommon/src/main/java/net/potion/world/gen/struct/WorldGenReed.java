package net.potion.world.gen.struct;

import net.potion.block.Block;
import net.potion.material.Material;
import net.potion.world.World;

import java.util.Random;

public class WorldGenReed extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        for (int var6 = 0; var6 < 20; ++var6) {
            int var7 = x + random.nextInt(4) - random.nextInt(4);
            int var9 = z + random.nextInt(4) - random.nextInt(4);
            if (world.isAirBlock(var7, y, var9) && (world.getBlockMaterial(var7 - 1, y - 1, var9) == Material.WATER || world.getBlockMaterial(var7 + 1, y - 1, var9) == Material.WATER || world.getBlockMaterial(var7, y - 1, var9 - 1) == Material.WATER || world.getBlockMaterial(var7, y - 1, var9 + 1) == Material.WATER)) {
                int var10 = 2 + random.nextInt(random.nextInt(3) + 1);

                for (int var11 = 0; var11 < var10; ++var11) {
                    if (Block.REEDS.canBlockStay(world, var7, y + var11, var9)) {
                        world.setBlock(var7, y + var11, var9, Block.REEDS.blockID);
                    }
                }
            }
        }

        return true;
    }
}
