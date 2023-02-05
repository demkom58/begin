package net.minecraft.world.gen.biome;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.SpawnListEntry;
import net.minecraft.world.gen.struct.WorldGenTaiga1;
import net.minecraft.world.gen.struct.WorldGenTaiga2;
import net.minecraft.world.gen.struct.WorldGenerator;

import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
    public BiomeGenTaiga() {
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
    }

    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        if (random.nextInt(3) == 0) {
            return new WorldGenTaiga1();
        }

        return new WorldGenTaiga2();
    }
}
