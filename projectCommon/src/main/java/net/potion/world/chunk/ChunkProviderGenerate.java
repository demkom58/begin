package net.potion.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.block.BlockSand;
import net.potion.material.Material;
import net.potion.util.IProgressUpdatable;
import net.potion.world.World;
import net.potion.world.gen.*;

import java.util.Random;

public class ChunkProviderGenerate implements IChunkProvider {
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves noiseGen7;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    double[] field1;
    double[] field2;
    double[] field3;
    double[] field4;
    double[] field5;
    private final long seed;
    private Random rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    private NoiseGeneratorOctaves noiseGen5;
    private World world;
    private double[] terrain;
    private double[] sandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] stoneNoise = new double[256];
    private MapGenBase cavesGen = new MapGenCaves();
    private BiomeGenBase[] biomesForGeneration;
    private double[] generatedTemperatures;

    public ChunkProviderGenerate(World world, long seed) {
        this.world = world;
        this.seed = seed;
        this.rand = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 4);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen7 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
    }

    public void generateTerrain(int var1, int var2, byte[] var3, BiomeGenBase[] var4, double[] var5) {
        byte var6 = 4;
        byte var7 = 64;
        int var8 = var6 + 1;
        byte var9 = 17;
        int var10 = var6 + 1;
        this.terrain = this.method1(this.terrain, var1 * var6, 0, var2 * var6, var8, var9, var10);

        for (int var11 = 0; var11 < var6; ++var11) {
            for (int var12 = 0; var12 < var6; ++var12) {
                for (int var13 = 0; var13 < 16; ++var13) {
                    double var14 = 0.125D;
                    double var16 = this.terrain[((var11) * var10 + var12) * var9 + var13];
                    double var18 = this.terrain[((var11) * var10 + var12 + 1) * var9 + var13];
                    double var20 = this.terrain[((var11 + 1) * var10 + var12) * var9 + var13];
                    double var22 = this.terrain[((var11 + 1) * var10 + var12 + 1) * var9 + var13];
                    double var24 = (this.terrain[((var11) * var10 + var12) * var9 + var13 + 1] - var16) * var14;
                    double var26 = (this.terrain[((var11) * var10 + var12 + 1) * var9 + var13 + 1] - var18) * var14;
                    double var28 = (this.terrain[((var11 + 1) * var10 + var12) * var9 + var13 + 1] - var20) * var14;
                    double var30 = (this.terrain[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 1] - var22) * var14;

                    for (int var32 = 0; var32 < 8; ++var32) {
                        double var33 = 0.25D;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 4; ++var43) {
                            int var44 = var43 + var11 * 4 << 11 | var12 * 4 << 7 | var13 * 8 + var32;
                            short var45 = 128;
                            double var46 = 0.25D;
                            double var48 = var35;
                            double var50 = (var37 - var35) * var46;

                            for (int var52 = 0; var52 < 4; ++var52) {
                                double var53 = var5[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                                int var55 = 0;
                                if (var13 * 8 + var32 < var7) {
                                    if (var53 < 0.5D && var13 * 8 + var32 >= var7 - 1) {
                                        var55 = Block.ICE.blockID;
                                    } else {
                                        var55 = Block.WATER_STILL.blockID;
                                    }
                                }

                                if (var48 > 0.0D) {
                                    var55 = Block.STONE.blockID;
                                }

                                var3[var44] = (byte) var55;
                                var44 += var45;
                                var48 += var50;
                            }

                            var35 += var39;
                            var37 += var41;
                        }

                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                        var22 += var30;
                    }
                }
            }
        }

    }

    public void replaceBlocksForBiome(int var1, int var2, byte[] var3, BiomeGenBase[] var4) {
        byte var5 = 64;
        double var6 = 0.03125D;
        this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var6, var6, 1.0D);
        this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, var1 * 16, 109.0134D, var2 * 16, 16, 1, 16, var6, 1.0D, var6);
        this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);

        for (int var8 = 0; var8 < 16; ++var8) {
            for (int var9 = 0; var9 < 16; ++var9) {
                BiomeGenBase var10 = var4[var8 + var9 * 16];
                boolean var11 = this.sandNoise[var8 + var9 * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
                boolean var12 = this.gravelNoise[var8 + var9 * 16] + this.rand.nextDouble() * 0.2D > 3.0D;
                int var13 = (int) (this.stoneNoise[var8 + var9 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int var14 = -1;
                byte var15 = var10.topBlock;
                byte var16 = var10.fillerBlock;

                for (int var17 = 127; var17 >= 0; --var17) {
                    int var18 = (var9 * 16 + var8) * 128 + var17;
                    if (var17 <= this.rand.nextInt(5)) {
                        var3[var18] = (byte) Block.BEDROCK.blockID;
                    } else {
                        byte var19 = var3[var18];
                        if (var19 == 0) {
                            var14 = -1;
                        } else if (var19 == Block.STONE.blockID) {
                            if (var14 == -1) {
                                if (var13 <= 0) {
                                    var15 = 0;
                                    var16 = (byte) Block.STONE.blockID;
                                } else if (var17 >= var5 - 4 && var17 <= var5 + 1) {
                                    var15 = var10.topBlock;
                                    var16 = var10.fillerBlock;
                                    if (var12) {
                                        var15 = 0;
                                    }

                                    if (var12) {
                                        var16 = (byte) Block.GRAVEL.blockID;
                                    }

                                    if (var11) {
                                        var15 = (byte) Block.SAND.blockID;
                                    }

                                    if (var11) {
                                        var16 = (byte) Block.SAND.blockID;
                                    }
                                }

                                if (var17 < var5 && var15 == 0) {
                                    var15 = (byte) Block.WATER_STILL.blockID;
                                }

                                var14 = var13;
                                if (var17 >= var5 - 1) {
                                    var3[var18] = var15;
                                } else {
                                    var3[var18] = var16;
                                }
                            } else if (var14 > 0) {
                                --var14;
                                var3[var18] = var16;
                                if (var14 == 0 && var16 == Block.SAND.blockID) {
                                    var14 = this.rand.nextInt(4);
                                    var16 = (byte) Block.SAND_STONE.blockID;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public Chunk prepareChunk(int x, int z) {
        return this.provideChunk(x, z);
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        byte[] var3 = new byte[32768];
        Chunk var4 = new Chunk(this.world, var3, x, z);
        this.biomesForGeneration = this.world.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        double[] var5 = this.world.getWorldChunkManager().temperature;
        this.generateTerrain(x, z, var3, this.biomesForGeneration, var5);
        this.replaceBlocksForBiome(x, z, var3, this.biomesForGeneration);
        this.cavesGen.generate(this, this.world, x, z, var3);
        var4.generateHeightAndSkyLightMap();
        return var4;
    }

    private double[] method1(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var1 == null) {
            var1 = new double[var5 * var6 * var7];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        double[] var12 = this.world.getWorldChunkManager().temperature;
        double[] var13 = this.world.getWorldChunkManager().humidity;
        this.field4 = this.noiseGen6.method2(this.field4, var2, var4, var5, var7, 1.121D, 1.121D, 0.5D);
        this.field5 = this.noiseGen7.method2(this.field5, var2, var4, var5, var7, 200.0D, 200.0D, 0.5D);
        this.field1 = this.noiseGen3.generateNoiseOctaves(this.field1, var2, var3, var4, var5, var6, var7, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.field2 = this.noiseGen1.generateNoiseOctaves(this.field2, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        this.field3 = this.noiseGen2.generateNoiseOctaves(this.field3, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / var5;

        for (int var17 = 0; var17 < var5; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for (int var19 = 0; var19 < var7; ++var19) {
                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = 1.0D - var23;
                var25 = var25 * var25;
                var25 = var25 * var25;
                var25 = 1.0D - var25;
                double var27 = (this.field4[var15] + 256.0D) / 512.0D;
                var27 = var27 * var25;
                if (var27 > 1.0D) {
                    var27 = 1.0D;
                }

                double var29 = this.field5[var15] / 8000.0D;
                if (var29 < 0.0D) {
                    var29 = -var29 * 0.3D;
                }

                var29 = var29 * 3.0D - 2.0D;
                if (var29 < 0.0D) {
                    var29 = var29 / 2.0D;
                    if (var29 < -1.0D) {
                        var29 = -1.0D;
                    }

                    var29 = var29 / 1.4D;
                    var29 = var29 / 2.0D;
                    var27 = 0.0D;
                } else {
                    if (var29 > 1.0D) {
                        var29 = 1.0D;
                    }

                    var29 = var29 / 8.0D;
                }

                if (var27 < 0.0D) {
                    var27 = 0.0D;
                }

                var27 = var27 + 0.5D;
                var29 = var29 * (double) var6 / 16.0D;
                double var31 = (double) var6 / 2.0D + var29 * 4.0D;
                ++var15;

                for (int var33 = 0; var33 < var6; ++var33) {
                    double var34 = 0.0D;
                    double var36 = ((double) var33 - var31) * 12.0D / var27;
                    if (var36 < 0.0D) {
                        var36 *= 4.0D;
                    }

                    double var38 = this.field2[var14] / 512.0D;
                    double var40 = this.field3[var14] / 512.0D;
                    double var42 = (this.field1[var14] / 10.0D + 1.0D) / 2.0D;
                    if (var42 < 0.0D) {
                        var34 = var38;
                    } else if (var42 > 1.0D) {
                        var34 = var40;
                    } else {
                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 = var34 - var36;
                    if (var33 > var6 - 4) {
                        double var44 = (float) (var33 - (var6 - 4)) / 3.0F;
                        var34 = var34 * (1.0D - var44) + -10.0D * var44;
                    }

                    var1[var14] = var34;
                    ++var14;
                }
            }
        }

        return var1;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return true;
    }

    @Override
    public void populate(IChunkProvider provider, int x, int z) {
        BlockSand.fallInstantly = true;
        int gX = x * 16;
        int gZ = z * 16;
        BiomeGenBase biome = this.world.getWorldChunkManager().getBiomeGenAt(x + 16, z + 16);
        this.rand.setSeed(this.world.getRandomSeed());
        long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) x * var7 + (long) z * var9 ^ this.world.getRandomSeed());
        double scale = 0.25D;
        if (this.rand.nextInt(4) == 0) {
            int var13 = gX + this.rand.nextInt(16) + 8;
            int var14 = this.rand.nextInt(128);
            int var15 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.WATER_STILL.blockID)).generate(this.world, this.rand, var13, var14, var15);
        }

        if (this.rand.nextInt(8) == 0) {
            int var26 = gX + this.rand.nextInt(16) + 8;
            int var38 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            int var50 = gZ + this.rand.nextInt(16) + 8;
            if (var38 < 64 || this.rand.nextInt(10) == 0) {
                (new WorldGenLakes(Block.LAVA_STILL.blockID)).generate(this.world, this.rand, var26, var38, var50);
            }
        }

        for (int i = 0; i < 8; ++i) {
            int var39 = gX + this.rand.nextInt(16) + 8;
            int var51 = this.rand.nextInt(128);
            int var16 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.world, this.rand, var39, var51, var16);
        }

        for (int i = 0; i < 10; ++i) {
            int var40 = gX + this.rand.nextInt(16);
            int var52 = this.rand.nextInt(128);
            int var63 = gZ + this.rand.nextInt(16);
            (new WorldGenClay(32)).generate(this.world, this.rand, var40, var52, var63);
        }

        for (int i = 0; i < 20; ++i) {
            int var41 = gX + this.rand.nextInt(16);
            int var53 = this.rand.nextInt(128);
            int var64 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.DIRT.blockID, 32)).generate(this.world, this.rand, var41, var53, var64);
        }

        for (int i = 0; i < 10; ++i) {
            int var42 = gX + this.rand.nextInt(16);
            int var54 = this.rand.nextInt(128);
            int var65 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.GRAVEL.blockID, 32)).generate(this.world, this.rand, var42, var54, var65);
        }

        for (int i = 0; i < 20; ++i) {
            int var43 = gX + this.rand.nextInt(16);
            int var55 = this.rand.nextInt(128);
            int var66 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_COAL.blockID, 16)).generate(this.world, this.rand, var43, var55, var66);
        }

        for (int i = 0; i < 20; ++i) {
            int var44 = gX + this.rand.nextInt(16);
            int var56 = this.rand.nextInt(64);
            int var67 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_IRON.blockID, 8)).generate(this.world, this.rand, var44, var56, var67);
        }

        for (int i = 0; i < 2; ++i) {
            int var45 = gX + this.rand.nextInt(16);
            int var57 = this.rand.nextInt(32);
            int var68 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_GOLD.blockID, 8)).generate(this.world, this.rand, var45, var57, var68);
        }

        for (int i = 0; i < 8; ++i) {
            int var46 = gX + this.rand.nextInt(16);
            int var58 = this.rand.nextInt(16);
            int var69 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_REDSTONE.blockID, 7)).generate(this.world, this.rand, var46, var58, var69);
        }

        for (int i = 0; i < 1; ++i) {
            int var47 = gX + this.rand.nextInt(16);
            int var59 = this.rand.nextInt(16);
            int var70 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_DIAMOND.blockID, 7)).generate(this.world, this.rand, var47, var59, var70);
        }

        for (int i = 0; i < 1; ++i) {
            int var48 = gX + this.rand.nextInt(16);
            int var60 = this.rand.nextInt(16) + this.rand.nextInt(16);
            int var71 = gZ + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_LAPIS.blockID, 6)).generate(this.world, this.rand, var48, var60, var71);
        }

        scale = 0.5D;
        int var37 = (int) ((this.mobSpawnerNoise.method1((double) x * scale, (double) z * scale) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
        int var49 = 0;
        if (this.rand.nextInt(10) == 0) {
            ++var49;
        }

        if (biome == BiomeGenBase.FOREST) {
            var49 += var37 + 5;
        }

        if (biome == BiomeGenBase.RAINFOREST) {
            var49 += var37 + 5;
        }

        if (biome == BiomeGenBase.SEASONAL_FOREST) {
            var49 += var37 + 2;
        }

        if (biome == BiomeGenBase.TAIGA) {
            var49 += var37 + 5;
        }

        if (biome == BiomeGenBase.DESERT) {
            var49 -= 20;
        }

        if (biome == BiomeGenBase.TUNDRA) {
            var49 -= 20;
        }

        if (biome == BiomeGenBase.PLAINS) {
            var49 -= 20;
        }

        for (int var61 = 0; var61 < var49; ++var61) {
            int var72 = gX + this.rand.nextInt(16) + 8;
            int var17 = gZ + this.rand.nextInt(16) + 8;
            WorldGenerator var18 = biome.getRandomWorldGenForTrees(this.rand);
            var18.setScale(1.0D, 1.0D, 1.0D);
            var18.generate(this.world, this.rand, var72, this.world.getHeightValue(var72, var17), var17);
        }

        byte var62 = 0;
        if (biome == BiomeGenBase.FOREST) {
            var62 = 2;
        }

        if (biome == BiomeGenBase.SEASONAL_FOREST) {
            var62 = 4;
        }

        if (biome == BiomeGenBase.TAIGA) {
            var62 = 2;
        }

        if (biome == BiomeGenBase.PLAINS) {
            var62 = 3;
        }

        for (int i = 0; i < var62; ++i) {
            int var76 = gX + this.rand.nextInt(16) + 8;
            int var85 = this.rand.nextInt(128);
            int var19 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.PLANT_YELLOW.blockID)).generate(this.world, this.rand, var76, var85, var19);
        }

        byte var74 = 0;
        if (biome == BiomeGenBase.FOREST) {
            var74 = 2;
        }

        if (biome == BiomeGenBase.RAINFOREST) {
            var74 = 10;
        }

        if (biome == BiomeGenBase.SEASONAL_FOREST) {
            var74 = 2;
        }

        if (biome == BiomeGenBase.TAIGA) {
            var74 = 1;
        }

        if (biome == BiomeGenBase.PLAINS) {
            var74 = 10;
        }

        for (int i = 0; i < var74; ++i) {
            byte var86 = 1;
            if (biome == BiomeGenBase.RAINFOREST && this.rand.nextInt(3) != 0) {
                var86 = 2;
            }

            int var97 = gX + this.rand.nextInt(16) + 8;
            int var20 = this.rand.nextInt(128);
            int var21 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenTallGrass(Block.TALLGRASS.blockID, var86)).generate(this.world, this.rand, var97, var20, var21);
        }

        var74 = 0;
        if (biome == BiomeGenBase.DESERT) {
            var74 = 2;
        }

        for (int i = 0; i < var74; ++i) {
            int var87 = gX + this.rand.nextInt(16) + 8;
            int var98 = this.rand.nextInt(128);
            int var108 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenDeadBush(Block.DEADBUSH.blockID)).generate(this.world, this.rand, var87, var98, var108);
        }

        if (this.rand.nextInt(2) == 0) {
            int var79 = gX + this.rand.nextInt(16) + 8;
            int var88 = this.rand.nextInt(128);
            int var99 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.PLANT_RED.blockID)).generate(this.world, this.rand, var79, var88, var99);
        }

        if (this.rand.nextInt(4) == 0) {
            int var80 = gX + this.rand.nextInt(16) + 8;
            int var89 = this.rand.nextInt(128);
            int var100 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_BROWN.blockID)).generate(this.world, this.rand, var80, var89, var100);
        }

        if (this.rand.nextInt(8) == 0) {
            int var81 = gX + this.rand.nextInt(16) + 8;
            int var90 = this.rand.nextInt(128);
            int var101 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_RED.blockID)).generate(this.world, this.rand, var81, var90, var101);
        }

        for (int i = 0; i < 10; ++i) {
            int var91 = gX + this.rand.nextInt(16) + 8;
            int var102 = this.rand.nextInt(128);
            int var109 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenReed()).generate(this.world, this.rand, var91, var102, var109);
        }

        if (this.rand.nextInt(32) == 0) {
            int var83 = gX + this.rand.nextInt(16) + 8;
            int var92 = this.rand.nextInt(128);
            int var103 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(this.world, this.rand, var83, var92, var103);
        }

        int var84 = 0;
        if (biome == BiomeGenBase.DESERT) {
            var84 += 10;
        }

        for (int i = 0; i < var84; ++i) {
            int var104 = gX + this.rand.nextInt(16) + 8;
            int var110 = this.rand.nextInt(128);
            int var114 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(this.world, this.rand, var104, var110, var114);
        }

        for (int i = 0; i < 50; ++i) {
            int var105 = gX + this.rand.nextInt(16) + 8;
            int var111 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            int var115 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.WATER_MOVING.blockID)).generate(this.world, this.rand, var105, var111, var115);
        }

        for (int i = 0; i < 20; ++i) {
            int var106 = gX + this.rand.nextInt(16) + 8;
            int var112 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
            int var116 = gZ + this.rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.LAVA_MOVING.blockID)).generate(this.world, this.rand, var106, var112, var116);
        }

        this.generatedTemperatures = this.world.getWorldChunkManager().getTemperatures(this.generatedTemperatures, x + 8, z + 8, 16, 16);

        for (int iX = gX + 8; iX < gX + 8 + 16; ++iX) {
            for (int iZ = gZ + 8; iZ < gZ + 8 + 16; ++iZ) {
                int var113 = iX - (gX + 8);
                int var117 = iZ - (gZ + 8);
                int var22 = this.world.findTopSolidOrLiquidBlock(iX, iZ);
                double var23 = this.generatedTemperatures[var113 * 16 + var117] - (double) (var22 - 64) / 64.0D * 0.3D;
                if (var23 < 0.5D && var22 > 0 && var22 < 128 && this.world.isAirBlock(iX, var22, iZ) && this.world.getBlockMaterial(iX, var22 - 1, iZ).getIsSolid() && this.world.getBlockMaterial(iX, var22 - 1, iZ) != Material.ICE) {
                    this.world.setBlockWithNotify(iX, var22, iZ, Block.SNOW.blockID);
                }
            }
        }

        BlockSand.fallInstantly = false;
    }

    @Override
    public boolean saveChunks(boolean forceSave, IProgressUpdatable updatable) {
        return true;
    }

    @Override
    public boolean unload100OldestChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String makeString() {
        return "RandomLevelSource";
    }
}
