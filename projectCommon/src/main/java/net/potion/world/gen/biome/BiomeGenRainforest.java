package net.potion.world.gen.biome;

import net.potion.world.gen.struct.WorldGenBigTree;
import net.potion.world.gen.struct.WorldGenTrees;
import net.potion.world.gen.struct.WorldGenerator;

import java.util.Random;

public class BiomeGenRainforest extends BiomeGenBase {
    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        return (random.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
    }
}
