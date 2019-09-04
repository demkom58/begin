package net.minecraft.world.gen;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.SpawnListEntry;

import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
    public BiomeGenTaiga() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        return (random.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2());
    }
}
