package net.potion.world.gen;

import net.potion.entity.passive.EntityWolf;
import net.potion.util.SpawnListEntry;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
    public BiomeGenForest() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        if (random.nextInt(5) == 0) {
            return new WorldGenForest();
        } else {
            return (random.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
        }
    }
}
