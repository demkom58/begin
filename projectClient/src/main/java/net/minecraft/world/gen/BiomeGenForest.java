package net.minecraft.world.gen;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.SpawnListEntry;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
    public BiomeGenForest() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random var1) {
        if (var1.nextInt(5) == 0) {
            return new WorldGenForest();
        } else {
            return (var1.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
        }
    }
}
