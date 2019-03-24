package net.minecraft.world.gen;

import java.util.Random;

public class BiomeGenRainforest extends BiomeGenBase {
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        return (random.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
    }
}
