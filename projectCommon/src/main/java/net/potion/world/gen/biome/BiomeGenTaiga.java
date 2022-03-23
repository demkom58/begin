package net.potion.world.gen.biome;

import net.potion.entity.passive.EntityWolf;
import net.potion.util.SpawnListEntry;
import net.potion.world.gen.struct.WorldGenTaiga1;
import net.potion.world.gen.struct.WorldGenTaiga2;
import net.potion.world.gen.struct.WorldGenerator;

import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
    public BiomeGenTaiga() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        return (random.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2());
    }
}
