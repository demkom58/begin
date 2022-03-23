package net.potion.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.block.BlockSand;
import net.potion.material.Material;
import net.potion.util.IProgressUpdatable;
import net.potion.world.World;
import net.potion.world.gen.biome.BiomeGenBase;
import net.potion.world.gen.noise.NoiseGeneratorOctaves;
import net.potion.world.gen.struct.*;

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
        this.terrain = this.noise(this.terrain, var1 * var6, 0, var2 * var6, var8, var9, var10);

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

    public void replaceBlocksForBiome(int x, int z, byte[] chunk, BiomeGenBase[] bases) {
        byte var5 = 64;
        double var6 = 0.03125D;
        this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, x * 16, z * 16, 0.0D, 16, 16, 1, var6, var6, 1.0D);
        this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, x * 16, 109.0134D, z * 16, 16, 1, 16, var6, 1.0D, var6);
        this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, x * 16, z * 16, 0.0D, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);

        for (int iX = 0; iX < 16; ++iX) {
            for (int iZ = 0; iZ < 16; ++iZ) {
                BiomeGenBase base = bases[iX + iZ * 16];
                boolean sand = this.sandNoise[iX + iZ * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
                boolean gravel = this.gravelNoise[iX + iZ * 16] + this.rand.nextDouble() * 0.2D > 3.0D;
                int var13 = (int) (this.stoneNoise[iX + iZ * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int var14 = -1;

                byte topBlock = base.topBlock;
                byte fillerBlock = base.fillerBlock;

                for (int iY = 127; iY >= 0; --iY) {
                    int idx = (iZ * 16 + iX) * 128 + iY;
                    if (iY <= this.rand.nextInt(5)) {
                        chunk[idx] = (byte) Block.BEDROCK.blockID;
                        continue;
                    }

                    byte block = chunk[idx];
                    if (block == 0) {
                        var14 = -1;
                        continue;
                    }

                    if (block != Block.STONE.blockID) {
                        continue;
                    }

                    if (var14 == -1) {
                        if (var13 <= 0) {
                            topBlock = 0;
                            fillerBlock = (byte) Block.STONE.blockID;
                        } else if (iY >= var5 - 4 && iY <= var5 + 1) {
                            topBlock = base.topBlock;
                            fillerBlock = base.fillerBlock;
                            if (gravel) {
                                topBlock = 0;
                            }

                            if (gravel) {
                                fillerBlock = (byte) Block.GRAVEL.blockID;
                            }

                            if (sand) {
                                topBlock = (byte) Block.SAND.blockID;
                            }

                            if (sand) {
                                fillerBlock = (byte) Block.SAND.blockID;
                            }
                        }

                        if (iY < var5 && topBlock == 0) {
                            topBlock = (byte) Block.WATER_STILL.blockID;
                        }

                        var14 = var13;
                        if (iY >= var5 - 1) {
                            chunk[idx] = topBlock;
                        } else {
                            chunk[idx] = fillerBlock;
                        }
                    } else if (var14 > 0) {
                        --var14;
                        chunk[idx] = fillerBlock;
                        if (var14 == 0 && fillerBlock == Block.SAND.blockID) {
                            var14 = this.rand.nextInt(4);
                            fillerBlock = (byte) Block.SAND_STONE.blockID;
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

    private double[] noise(double[] values, int x, int z, int var4, int width, int height, int length) {
        if (values == null) {
            values = new double[width * height * length];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;

        double[] temperatures = this.world.getWorldChunkManager().temperature;
        double[] humidities = this.world.getWorldChunkManager().humidity;

        this.field4 = this.noiseGen6.generateNoiseOctaves(this.field4, x, var4, width, length, 1.121D, 1.121D, 0.5D);
        this.field5 = this.noiseGen7.generateNoiseOctaves(this.field5, x, var4, width, length, 200.0D, 200.0D, 0.5D);
        this.field1 = this.noiseGen3.generateNoiseOctaves(this.field1, x, z, var4, width, height, length, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.field2 = this.noiseGen1.generateNoiseOctaves(this.field2, x, z, var4, width, height, length, var8, var10, var8);
        this.field3 = this.noiseGen2.generateNoiseOctaves(this.field3, x, z, var4, width, height, length, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / width;

        for (int iX = 0; iX < width; ++iX) {
            int var18 = iX * var16 + var16 / 2;

            for (int iZ = 0; iZ < length; ++iZ) {
                int var20 = iZ * var16 + var16 / 2;
                double temperature = temperatures[var18 * 16 + var20];
                double humidity = humidities[var18 * 16 + var20] * temperature;
                double revHumidity = 1.0D - humidity;
                revHumidity *= revHumidity;
                revHumidity *= revHumidity;
                revHumidity = 1.0D - revHumidity;

                double var27 = (this.field4[var15] + 256.0D) / 512.0D;
                var27 = var27 * revHumidity;
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
                var29 = var29 * (double) height / 16.0D;
                double var31 = (double) height / 2.0D + var29 * 4.0D;
                ++var15;

                for (int iY = 0; iY < height; ++iY) {
                    double var34 = 0.0D;
                    double var36 = ((double) iY - var31) * 12.0D / var27;
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
                    if (iY > height - 4) {
                        double var44 = (float) (iY - (height - 4)) / 3.0F;
                        var34 = var34 * (1.0D - var44) + -10.0D * var44;
                    }

                    values[var14] = var34;
                    ++var14;
                }
            }
        }

        return values;
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
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenLakes(Block.WATER_STILL.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        if (this.rand.nextInt(8) == 0) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(this.rand.nextInt(120) + 8);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            if (lY < 64 || this.rand.nextInt(10) == 0) {
                new WorldGenLakes(Block.LAVA_STILL.blockID).generate(this.world, this.rand, lX, lY, lZ);
            }
        }

        for (int i = 0; i < 8; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenDungeons().generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 10; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenClay(32).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 20; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.DIRT.blockID, 32).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 10; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.GRAVEL.blockID, 32).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 20; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_COAL.blockID, 16).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 20; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(64);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_IRON.blockID, 8).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 2; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(32);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_GOLD.blockID, 8).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 8; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(16);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_REDSTONE.blockID, 7).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 1; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(16);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_DIAMOND.blockID, 7).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 1; ++i) {
            int lX = gX + this.rand.nextInt(16);
            int lY = this.rand.nextInt(16) + this.rand.nextInt(16);
            int lZ = gZ + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_LAPIS.blockID, 6).generate(this.world, this.rand, lX, lY, lZ);
        }

        scale = 0.5D;
        int treeRand = (int) ((this.mobSpawnerNoise.noise(x * scale, z * scale) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
        int trees = 0;
        if (this.rand.nextInt(10) == 0) {
            ++trees;
        }

        if (biome == BiomeGenBase.FOREST) {
            trees += treeRand + 5;
        } else if (biome == BiomeGenBase.RAINFOREST) {
            trees += treeRand + 5;
        } else if (biome == BiomeGenBase.SEASONAL_FOREST) {
            trees += treeRand + 2;
        } else if (biome == BiomeGenBase.TAIGA) {
            trees += treeRand + 5;
        } else if (biome == BiomeGenBase.DESERT) {
            trees -= 20;
        } else if (biome == BiomeGenBase.TUNDRA) {
            trees -= 20;
        } else if (biome == BiomeGenBase.PLAINS) {
            trees -= 20;
        }

        for (int i = 0; i < trees; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lZ = gZ + this.rand.nextInt(16) + 8;
            WorldGenerator generator = biome.getRandomWorldGenForTrees(this.rand);
            generator.setScale(1.0D, 1.0D, 1.0D);
            generator.generate(this.world, this.rand, lX, this.world.getHeightValue(lX, lZ), lZ);
        }

        byte flowers = 0;
        if (biome == BiomeGenBase.FOREST) {
            flowers = 2;
        } else if (biome == BiomeGenBase.SEASONAL_FOREST) {
            flowers = 4;
        } else if (biome == BiomeGenBase.TAIGA) {
            flowers = 2;
        } else if (biome == BiomeGenBase.PLAINS) {
            flowers = 3;
        }

        for (int i = 0; i < flowers; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.PLANT_YELLOW.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        byte grass = 0;
        if (biome == BiomeGenBase.FOREST) {
            grass = 2;
        } else if (biome == BiomeGenBase.RAINFOREST) {
            grass = 10;
        } else if (biome == BiomeGenBase.SEASONAL_FOREST) {
            grass = 2;
        } else if (biome == BiomeGenBase.TAIGA) {
            grass = 1;
        } else if (biome == BiomeGenBase.PLAINS) {
            grass = 10;
        }

        for (int i = 0; i < grass; ++i) {
            byte metadata = 1;
            if (biome == BiomeGenBase.RAINFOREST && this.rand.nextInt(3) != 0) {
                metadata = 2;
            }

            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenTallGrass(Block.TALLGRASS.blockID, metadata).generate(this.world, this.rand, lX, lY, lZ);
        }

        grass = 0;
        if (biome == BiomeGenBase.DESERT) {
            grass = 2;
        }

        for (int i = 0; i < grass; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenDeadBush(Block.DEADBUSH.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        if (this.rand.nextInt(2) == 0) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.PLANT_RED.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        if (this.rand.nextInt(4) == 0) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.MUSHROOM_BROWN.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        if (this.rand.nextInt(8) == 0) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.MUSHROOM_RED.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 10; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenReed().generate(this.world, this.rand, lX, lY, lZ);
        }

        if (this.rand.nextInt(32) == 0) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenPumpkin().generate(this.world, this.rand, lX, lY, lZ);
        }

        int cactus = 0;
        if (biome == BiomeGenBase.DESERT) {
            cactus += 10;
        }

        for (int i = 0; i < cactus; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(128);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenCactus().generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 50; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(this.rand.nextInt(120) + 8);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenLiquids(Block.WATER_MOVING.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        for (int i = 0; i < 20; ++i) {
            int lX = gX + this.rand.nextInt(16) + 8;
            int lY = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
            int lZ = gZ + this.rand.nextInt(16) + 8;
            new WorldGenLiquids(Block.LAVA_MOVING.blockID).generate(this.world, this.rand, lX, lY, lZ);
        }

        this.generatedTemperatures = this.world.getWorldChunkManager().getTemperatures(this.generatedTemperatures, x + 8, z + 8, 16, 16);

        for (int iX = gX + 8; iX < gX + 8 + 16; ++iX) {
            for (int iZ = gZ + 8; iZ < gZ + 8 + 16; ++iZ) {
                int lX = iX - (gX + 8);
                int lZ = iZ - (gZ + 8);
                int topY = this.world.findTopSolidOrLiquidBlock(iX, iZ);
                double temp = this.generatedTemperatures[lX * 16 + lZ] - (double) (topY - 64) / 64.0D * 0.3D;
                if (temp < 0.5D && topY > 0 && topY < 128
                        && this.world.isAirBlock(iX, topY, iZ)
                        && this.world.getBlockMaterial(iX, topY - 1, iZ).getIsSolid()
                        && this.world.getBlockMaterial(iX, topY - 1, iZ) != Material.ICE) {
                    this.world.setBlockWithNotify(iX, topY, iZ, Block.SNOW.blockID);
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
