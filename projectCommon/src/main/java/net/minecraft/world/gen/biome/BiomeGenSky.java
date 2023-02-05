package net.minecraft.world.gen.biome;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.SpawnListEntry;

public class BiomeGenSky extends BiomeGenBase {
    public BiomeGenSky() {
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10));
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getSkyColorByTemp(float temp) {
        return 0xc0c0ff;
    }
}
