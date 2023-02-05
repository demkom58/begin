package net.minecraft.world.gen.biome;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.SpawnListEntry;
import net.minecraft.world.gen.struct.WorldGenBigTree;
import net.minecraft.world.gen.struct.WorldGenTrees;
import net.minecraft.world.gen.struct.WorldGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeGenBase {
    public static final BiomeGenBase RAINFOREST = new BiomeGenRainforest().setColor(0x8fa36).setBiomeName("Rainforest");
    public static final BiomeGenBase SWAMPLAND = new BiomeGenSwamp().setColor(0x7f9b2).setBiomeName("Swampland");
    public static final BiomeGenBase SEASONAL_FOREST = new BiomeGenBase().setColor(0x9be023).setBiomeName("Seasonal Forest");
    public static final BiomeGenBase FOREST = new BiomeGenForest().setColor(0x56621).setBiomeName("Forest");
    public static final BiomeGenBase SAVANNA = new BiomeGenDesert().setColor(0xd9e023).setBiomeName("Savanna");
    public static final BiomeGenBase SHRUBLAND = new BiomeGenBase().setColor(0xa1ad20).setBiomeName("Shrubland");
    public static final BiomeGenBase TAIGA = new BiomeGenTaiga().setColor(0x2eb153).setBiomeName("Taiga").setEnableSnow();
    public static final BiomeGenBase DESERT = new BiomeGenDesert().setColor(0xfa9418).setBiomeName("Desert").setDisableRain();
    public static final BiomeGenBase PLAINS = new BiomeGenDesert().setColor(0xffd910).setBiomeName("Plains");
    public static final BiomeGenBase ICE_DESERT = new BiomeGenDesert().setColor(0xffed93).setBiomeName("Ice Desert").setEnableSnow().setDisableRain();
    public static final BiomeGenBase TUNDRA = new BiomeGenBase().setColor(0x57ebf9).setBiomeName("Tundra").setEnableSnow();
    public static final BiomeGenBase HELL = new BiomeGenHell().setColor(0xff0000).setBiomeName("Hell").setDisableRain();
    public static final BiomeGenBase SKY = new BiomeGenSky().setColor(0x8080ff).setBiomeName("Sky").setDisableRain();
    private static final BiomeGenBase[] biomeLookupTable = new BiomeGenBase[4096];

    static {
        generateBiomeLookup();
    }

    public String biomeName;
    public int color;
    public byte topBlock;
    public byte fillerBlock;
    protected List<SpawnListEntry> spawnableMonsterList;
    protected List<SpawnListEntry> spawnableCreatureList;
    protected List<SpawnListEntry> spawnableWaterCreatureList;
    private boolean enableSnow;
    private boolean enableRain;

    protected BiomeGenBase() {
        this.topBlock = (byte) Block.GRASS.blockID;
        this.fillerBlock = (byte) Block.DIRT.blockID;
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

        if (a < 0.1F) {
            return TUNDRA;
        }

        if (b < 0.2F) {
            if (a < 0.5F) {
                return TUNDRA;
            }
            return a < 0.95F ? SAVANNA : DESERT;
        }

        if (b > 0.5F && a < 0.7F) {
            return SWAMPLAND;
        }

        if (a < 0.5F) {
            return TAIGA;
        }

        if (a < 0.97F) {
            return b < 0.35F ? SHRUBLAND : FOREST;
        }

        if (b < 0.45F) {
            return PLAINS;
        }

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

    protected BiomeGenBase setColor(int color) {
        this.color = color;
        return this;
    }

    @Side(CodeSide.CLIENT)
    public int getSkyColorByTemp(float temp) {
        temp = temp / 3.0F;
        if (temp < -1.0F) {
            temp = -1.0F;
        }

        if (temp > 1.0F) {
            temp = 1.0F;
        }

        return Color.getHSBColor(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F).getRGB();
    }

    public List<SpawnListEntry> getSpawnableList(EnumCreatureType type) {
        if (type == EnumCreatureType.MONSTER) {
            return this.spawnableMonsterList;
        }

        if (type == EnumCreatureType.CREATURE) {
            return this.spawnableCreatureList;
        }

        if (type == EnumCreatureType.WATER_CREATURE) {
            return this.spawnableWaterCreatureList;
        }

        return null;
    }

    public boolean getEnableSnow() {
        return this.enableSnow;
    }

    public boolean canSpawnLightningBolt() {
        return !this.enableSnow && this.enableRain;
    }
}
