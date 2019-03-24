package net.minecraft.world.gen;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.SpawnListEntry;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
    public BiomeGenForest() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        if (random.nextInt(5) == 0) {
            return new WorldGenForest();
        }

        return (random.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
    }
}
