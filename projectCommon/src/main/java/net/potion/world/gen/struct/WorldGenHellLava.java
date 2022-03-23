package net.potion.world.gen.struct;

import net.potion.block.Block;
import net.potion.world.World;

import java.util.Random;

public class WorldGenHellLava extends WorldGenerator {
    private int blockId;

    public WorldGenHellLava(int var1) {
        this.blockId = var1;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        if (world.getBlockId(x, y + 1, z) != Block.BLOOD_STONE.blockID) {
            return false;
        }

        if (world.getBlockId(x, y, z) != 0 && world.getBlockId(x, y, z) != Block.BLOOD_STONE.blockID) {
            return false;
        }

        int var6 = 0;
        if (world.getBlockId(x - 1, y, z) == Block.BLOOD_STONE.blockID) {
            ++var6;
        }

        if (world.getBlockId(x + 1, y, z) == Block.BLOOD_STONE.blockID) {
            ++var6;
        }

        if (world.getBlockId(x, y, z - 1) == Block.BLOOD_STONE.blockID) {
            ++var6;
        }

        if (world.getBlockId(x, y, z + 1) == Block.BLOOD_STONE.blockID) {
            ++var6;
        }

        if (world.getBlockId(x, y - 1, z) == Block.BLOOD_STONE.blockID) {
            ++var6;
        }

        int var7 = 0;
        if (world.isAirBlock(x - 1, y, z)) {
            ++var7;
        }

        if (world.isAirBlock(x + 1, y, z)) {
            ++var7;
        }

        if (world.isAirBlock(x, y, z - 1)) {
            ++var7;
        }

        if (world.isAirBlock(x, y, z + 1)) {
            ++var7;
        }

        if (world.isAirBlock(x, y - 1, z)) {
            ++var7;
        }

        if (var6 == 4 && var7 == 1) {
            world.setBlockWithNotify(x, y, z, this.blockId);
            world.scheduledUpdatesAreImmediate = true;
            Block.BLOCKS_LIST[this.blockId].updateTick(world, x, y, z, random);
            world.scheduledUpdatesAreImmediate = false;
        }

        return true;
    }
}
