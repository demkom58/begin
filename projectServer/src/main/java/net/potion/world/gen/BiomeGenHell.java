package net.potion.world.gen;

import net.potion.entity.monster.EntityGhast;
import net.potion.entity.monster.EntityPigZombie;
import net.potion.util.SpawnListEntry;

public class BiomeGenHell extends BiomeGenBase {
    public BiomeGenHell() {
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 10));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 10));
    }
}
