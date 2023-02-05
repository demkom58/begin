package net.minecraft.world.gen.biome;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.SpawnListEntry;
import net.minecraft.world.gen.struct.WorldGenBigTree;
import net.minecraft.world.gen.struct.WorldGenForest;
import net.minecraft.world.gen.struct.WorldGenTrees;
import net.minecraft.world.gen.struct.WorldGenerator;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
    public BiomeGenForest() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        if (random.nextInt(5) == 0) {
            return new WorldGenForest();
        }

        if (random.nextInt(3) == 0) {
            return new WorldGenBigTree();
        }

        return new WorldGenTrees();
    }
}
