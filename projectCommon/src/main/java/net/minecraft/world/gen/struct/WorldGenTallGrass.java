package net.minecraft.world.gen.struct;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTallGrass extends WorldGenerator {
    private int blockId;
    private int metadata;

    public WorldGenTallGrass(int blockId, int metadata) {
        this.blockId = blockId;
        this.metadata = metadata;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int var11 = 0;
        while (((var11 = world.getBlockId(x, y, z)) == 0 || var11 == Block.LEAVES.blockID) && y > 0) {
            --y;
        }

        for (int var7 = 0; var7 < 128; ++var7) {
            int var8 = x + random.nextInt(8) - random.nextInt(8);
            int var9 = y + random.nextInt(4) - random.nextInt(4);
            int var10 = z + random.nextInt(8) - random.nextInt(8);
            if (world.isAirBlock(var8, var9, var10) && Block.BLOCKS_LIST[this.blockId].canBlockStay(world, var8, var9, var10)) {
                world.setBlockAndMetadata(var8, var9, var10, this.blockId, this.metadata);
            }
        }

        return true;
    }
}
