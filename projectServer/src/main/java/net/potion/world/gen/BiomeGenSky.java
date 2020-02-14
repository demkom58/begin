package net.potion.world.gen;

import net.potion.entity.passive.EntityChicken;
import net.potion.util.SpawnListEntry;

public class BiomeGenSky extends BiomeGenBase {
    public BiomeGenSky() {
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10));
    }
}
