package net.minecraft.world.gen.biome;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.SpawnListEntry;

public class BiomeGenHell extends BiomeGenBase {
    public BiomeGenHell() {
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 10));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 10));
    }
}
