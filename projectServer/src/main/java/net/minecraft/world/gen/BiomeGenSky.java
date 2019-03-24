package net.minecraft.world.gen;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.SpawnListEntry;

public class BiomeGenSky extends BiomeGenBase {
    public BiomeGenSky() {
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10));
    }
}
