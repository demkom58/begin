package net.minecraft;

import java.util.Random;

public class BlockGrass extends Block {
    protected BlockGrass(int var1) {
        super(var1, Material.GRASS);
        this.blockIndexInTexture = 3;
        this.setTickOnLoad(true);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.singleplayerWorld) {
            if (world.getBlockLightValue(x, y + 1, z) < 4 && Block.LIGHT_OPACITY[world.getBlockId(x, y + 1, z)] > 2) {
                if (random.nextInt(4) != 0) {
                    return;
                }

                world.setBlockWithNotify(x, y, z, Block.DIRT.blockID);
            } else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                int var6 = x + random.nextInt(3) - 1;
                int var7 = y + random.nextInt(5) - 3;
                int var8 = z + random.nextInt(3) - 1;
                int var9 = world.getBlockId(var6, var7 + 1, var8);
                if (world.getBlockId(var6, var7, var8) == Block.DIRT.blockID && world.getBlockLightValue(var6, var7 + 1, var8) >= 4 && Block.LIGHT_OPACITY[var9] <= 2) {
                    world.setBlockWithNotify(var6, var7, var8, Block.GRASS.blockID);
                }
            }

        }
    }

    public int idDropped(int var1, Random random) {
        return Block.DIRT.idDropped(0, random);
    }
}
