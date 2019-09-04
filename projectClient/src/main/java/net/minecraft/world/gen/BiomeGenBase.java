package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.SpawnListEntry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeGenBase {
    public static final BiomeGenBase RAINFOREST = new BiomeGenRainforest().setColor(588342).setBiomeName("Rainforest").func_4124_a(2094168);
    public static final BiomeGenBase SWAMPLAND = new BiomeGenSwamp().setColor(522674).setBiomeName("Swampland").func_4124_a(9154376);
    public static final BiomeGenBase SEASONAL_FOREST = new BiomeGenBase().setColor(10215459).setBiomeName("Seasonal Forest");
    public static final BiomeGenBase FOREST = new BiomeGenForest().setColor(353825).setBiomeName("Forest").func_4124_a(5159473);
    public static final BiomeGenBase SAVANNA = new BiomeGenDesert().setColor(14278691).setBiomeName("Savanna");
    public static final BiomeGenBase SHRUBLAND = new BiomeGenBase().setColor(10595616).setBiomeName("Shrubland");
    public static final BiomeGenBase TAIGA = new BiomeGenTaiga().setColor(3060051).setBiomeName("Taiga").setEnableSnow().func_4124_a(8107825);
    public static final BiomeGenBase DESERT = new BiomeGenDesert().setColor(16421912).setBiomeName("Desert").setDisableRain();
    public static final BiomeGenBase PLAINS = new BiomeGenDesert().setColor(16767248).setBiomeName("Plains");
    public static final BiomeGenBase ICE_DESERT = new BiomeGenDesert().setColor(16772499).setBiomeName("Ice Desert").setEnableSnow().setDisableRain().func_4124_a(12899129);
    public static final BiomeGenBase TUNDRA = new BiomeGenBase().setColor(5762041).setBiomeName("Tundra").setEnableSnow().func_4124_a(12899129);
    public static final BiomeGenBase HELL = new BiomeGenHell().setColor(16711680).setBiomeName("Hell").setDisableRain();
    public static final BiomeGenBase SKY = new BiomeGenSky().setColor(8421631).setBiomeName("Sky").setDisableRain();
    private static BiomeGenBase[] biomeLookupTable = new BiomeGenBase[4096];

    static {
        generateBiomeLookup();
    }

    public String biomeName;
    public int color;
    public byte topBlock;
    public byte fillerBlock;
    public int field_6502_q;
    protected List<SpawnListEntry> spawnableMonsterList;
    protected List<SpawnListEntry> spawnableCreatureList;
    protected List<SpawnListEntry> spawnableWaterCreatureList;
    private boolean enableSnow;
    private boolean enableRain;

    protected BiomeGenBase() {
        this.topBlock = (byte) Block.GRASS.blockID;
        this.fillerBlock = (byte) Block.DIRT.blockID;
        this.field_6502_q = 5169201;
        this.spawnableMonsterList = new ArrayList<>();
        this.spawnableCreatureList = new ArrayList<>();
        this.spawnableWaterCreatureList = new ArrayList<>();
        this.enableRain = true;
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
        this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10));
    }

    public static void generateBiomeLookup() {
        for (int i = 0; i < 64; ++i) {
            for (int j = 0; j < 64; ++j) {
                biomeLookupTable[i + j * 64] = getBiome((float) i / 63.0F, (float) j / 63.0F);
            }
        }

        DESERT.topBlock = DESERT.fillerBlock = (byte) Block.SAND.blockID;
        ICE_DESERT.topBlock = ICE_DESERT.fillerBlock = (byte) Block.SAND.blockID;
    }

    public static BiomeGenBase getBiomeFromLookup(double var0, double var2) {
        int var4 = (int) (var0 * 63.0D);
        int var5 = (int) (var2 * 63.0D);
        return biomeLookupTable[var4 + var5 * 64];
    }

    public static BiomeGenBase getBiome(float a, float b) {
        b *= a;
        if (a < 0.1F)
            return TUNDRA;

        if (b < 0.2F) {
            if (a < 0.5F)
                return TUNDRA;
            return a < 0.95F ? SAVANNA : DESERT;
        }

        if (b > 0.5F && a < 0.7F)
            return SWAMPLAND;

        if (a < 0.5F)
            return TAIGA;

        if (a < 0.97F)
            return b < 0.35F ? SHRUBLAND : FOREST;

        if (b < 0.45F)
            return PLAINS;

        return b < 0.9F ? SEASONAL_FOREST : RAINFOREST;
    }

    private BiomeGenBase setDisableRain() {
        this.enableRain = false;
        return this;
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random) {
        return random.nextInt(10) == 0 ? new WorldGenBigTree() : new WorldGenTrees();
    }

    protected BiomeGenBase setEnableSnow() {
        this.enableSnow = true;
        return this;
    }

    protected BiomeGenBase setBiomeName(String biomeName) {
        this.biomeName = biomeName;
        return this;
    }

    protected BiomeGenBase func_4124_a(int var1) {
        this.field_6502_q = var1;
        return this;
    }

    protected BiomeGenBase setColor(int color) {
        this.color = color;
        return this;
    }

    public int getSkyColorByTemp(float var1) {
        var1 = var1 / 3.0F;
        if (var1 < -1.0F) {
            var1 = -1.0F;
        }

        if (var1 > 1.0F) {
            var1 = 1.0F;
        }

        return Color.getHSBColor(0.62222224F - var1 * 0.05F, 0.5F + var1 * 0.1F, 1.0F).getRGB();
    }

    public List<SpawnListEntry> getSpawnableList(EnumCreatureType type) {
        if (type == EnumCreatureType.MONSTER)
            return this.spawnableMonsterList;

        if (type == EnumCreatureType.CREATURE)
            return this.spawnableCreatureList;

        return type == EnumCreatureType.WATER_CREATURE ? this.spawnableWaterCreatureList : null;
    }

    public boolean getEnableSnow() {
        return this.enableSnow;
    }

    public boolean canSpawnLightningBolt() {
        return !this.enableSnow && this.enableRain;
    }
}
