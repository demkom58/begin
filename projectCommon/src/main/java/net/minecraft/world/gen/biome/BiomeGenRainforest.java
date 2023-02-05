package net.minecraft.world.gen.biome;

import net.minecraft.world.gen.struct.WorldGenBigTree;
import net.minecraft.world.gen.struct.WorldGenTrees;
import net.minecraft.world.gen.struct.WorldGenerator;

import java.util.Random;

public class BiomeGenRainforest extends BiomeGenBase {
    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        if (random.nextInt(3) == 0) {
            return new WorldGenBigTree();
        }

        return new WorldGenTrees();
    }
}
