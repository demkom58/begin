package net.potion.world.gen.biome;

import net.potion.entity.passive.EntityWolf;
import net.potion.util.SpawnListEntry;
import net.potion.world.gen.struct.WorldGenBigTree;
import net.potion.world.gen.struct.WorldGenForest;
import net.potion.world.gen.struct.WorldGenTrees;
import net.potion.world.gen.struct.WorldGenerator;

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
