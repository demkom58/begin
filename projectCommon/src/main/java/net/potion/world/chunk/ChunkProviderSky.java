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

public class ChunkProviderSky implements IChunkProvider {
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves noiseGen7;
    public NoiseGeneratorOctaves noiseGen8;
    double[] field1;
    double[] field2;
    double[] field3;
    double[] field4;
    double[] field5;
    private Random skyRNG;
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
    private MapGenBase caves = new MapGenCaves();
    private BiomeGenBase[] field6;
    private double[] field7;

    public ChunkProviderSky(World world, long seed) {
        this.world = world;
        this.skyRNG = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.skyRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.skyRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.skyRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.skyRNG, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.skyRNG, 4);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.skyRNG, 10);
        this.noiseGen7 = new NoiseGeneratorOctaves(this.skyRNG, 16);
        this.noiseGen8 = new NoiseGeneratorOctaves(this.skyRNG, 8);
    }

    public void func_28071_a(int var1, int var2, byte[] var3, BiomeGenBase[] var4, double[] var5) {
        byte var6 = 2;
        int var7 = var6 + 1;
        byte var8 = 33;
        int var9 = var6 + 1;
        this.terrain = this.method2(this.terrain, var1 * var6, 0, var2 * var6, var7, var8, var9);

        for (int var10 = 0; var10 < var6; ++var10) {
            for (int var11 = 0; var11 < var6; ++var11) {
                for (int var12 = 0; var12 < 32; ++var12) {
                    double var13 = 0.25D;
                    double var15 = this.terrain[((var10) * var9 + var11) * var8 + var12];
                    double var17 = this.terrain[((var10) * var9 + var11 + 1) * var8 + var12];
                    double var19 = this.terrain[((var10 + 1) * var9 + var11) * var8 + var12];
                    double var21 = this.terrain[((var10 + 1) * var9 + var11 + 1) * var8 + var12];
                    double var23 = (this.terrain[((var10) * var9 + var11) * var8 + var12 + 1] - var15) * var13;
                    double var25 = (this.terrain[((var10) * var9 + var11 + 1) * var8 + var12 + 1] - var17) * var13;
                    double var27 = (this.terrain[((var10 + 1) * var9 + var11) * var8 + var12 + 1] - var19) * var13;
                    double var29 = (this.terrain[((var10 + 1) * var9 + var11 + 1) * var8 + var12 + 1] - var21) * var13;

                    for (int var31 = 0; var31 < 4; ++var31) {
                        double var32 = 0.125D;
                        double var34 = var15;
                        double var36 = var17;
                        double var38 = (var19 - var15) * var32;
                        double var40 = (var21 - var17) * var32;

                        for (int var42 = 0; var42 < 8; ++var42) {
                            int var43 = var42 + var10 * 8 << 11 | var11 * 8 << 7 | var12 * 4 + var31;
                            short var44 = 128;
                            double var45 = 0.125D;
                            double var47 = var34;
                            double var49 = (var36 - var34) * var45;

                            for (int var51 = 0; var51 < 8; ++var51) {
                                int var52 = 0;
                                if (var47 > 0.0D) {
                                    var52 = Block.STONE.blockID;
                                }

                                var3[var43] = (byte) var52;
                                var43 += var44;
                                var47 += var49;
                            }

                            var34 += var38;
                            var36 += var40;
                        }

                        var15 += var23;
                        var17 += var25;
                        var19 += var27;
                        var21 += var29;
                    }
                }
            }
        }

    }

    public void method1(int var1, int var2, byte[] var3, BiomeGenBase[] var4) {
        double var5 = 0.03125D;
        this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var5, var5, 1.0D);
        this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, var1 * 16, 109.0134D, var2 * 16, 16, 1, 16, var5, 1.0D, var5);
        this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                BiomeGenBase var9 = var4[var7 + var8 * 16];
                int var10 = (int) (this.stoneNoise[var7 + var8 * 16] / 3.0D + 3.0D + this.skyRNG.nextDouble() * 0.25D);
                int var11 = -1;
                byte var12 = var9.topBlock;
                byte var13 = var9.fillerBlock;

                for (int var14 = 127; var14 >= 0; --var14) {
                    int var15 = (var8 * 16 + var7) * 128 + var14;
                    byte var16 = var3[var15];
                    if (var16 == 0) {
                        var11 = -1;
                    } else if (var16 == Block.STONE.blockID) {
                        if (var11 == -1) {
                            if (var10 <= 0) {
                                var12 = 0;
                                var13 = (byte) Block.STONE.blockID;
                            }

                            var11 = var10;
                            if (var14 >= 0) {
                                var3[var15] = var12;
                            } else {
                                var3[var15] = var13;
                            }
                        } else if (var11 > 0) {
                            --var11;
                            var3[var15] = var13;
                            if (var11 == 0 && var13 == Block.SAND.blockID) {
                                var11 = this.skyRNG.nextInt(4);
                                var13 = (byte) Block.SAND_STONE.blockID;
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
        this.skyRNG.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        byte[] var3 = new byte[32768];
        Chunk var4 = new Chunk(this.world, var3, x, z);
        this.field6 = this.world.getWorldChunkManager().loadBlockGeneratorData(this.field6, x * 16, z * 16, 16, 16);
        double[] var5 = this.world.getWorldChunkManager().temperature;
        this.func_28071_a(x, z, var3, this.field6, var5);
        this.method1(x, z, var3, this.field6);
        this.caves.generate(this, this.world, x, z, var3);
        var4.generateHeightAndSkyLightMap();
        return var4;
    }

    private double[] method2(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var1 == null) {
            var1 = new double[var5 * var6 * var7];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        double[] var12 = this.world.getWorldChunkManager().temperature;
        double[] var13 = this.world.getWorldChunkManager().humidity;
        this.field4 = this.noiseGen6.generateNoiseOctaves(this.field4, var2, var4, var5, var7, 1.121D, 1.121D, 0.5D);
        this.field5 = this.noiseGen7.generateNoiseOctaves(this.field5, var2, var4, var5, var7, 200.0D, 200.0D, 0.5D);
        var8 = var8 * 2.0D;
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
                if (var29 > 1.0D) {
                    var29 = 1.0D;
                }

                var29 = var29 / 8.0D;
                var29 = 0.0D;
                if (var27 < 0.0D) {
                    var27 = 0.0D;
                }

                var27 = var27 + 0.5D;
                var29 = var29 * (double) var6 / 16.0D;
                ++var15;
                double var31 = (double) var6 / 2.0D;

                for (int var33 = 0; var33 < var6; ++var33) {
                    double var34 = 0.0D;
                    double var36 = ((double) var33 - var31) * 8.0D / var27;
                    if (var36 < 0.0D) {
                        var36 = var36 * -1.0D;
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

                    var34 = var34 - 8.0D;
                    byte var44 = 32;
                    if (var33 > var6 - var44) {
                        double var45 = (float) (var33 - (var6 - var44)) / ((float) var44 - 1.0F);
                        var34 = var34 * (1.0D - var45) + -30.0D * var45;
                    }

                    var44 = 8;
                    if (var33 < var44) {
                        double var61 = (float) (var44 - var33) / ((float) var44 - 1.0F);
                        var34 = var34 * (1.0D - var61) + -30.0D * var61;
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
        int var4 = x * 16;
        int var5 = z * 16;
        BiomeGenBase var6 = this.world.getWorldChunkManager().getBiomeGenAt(var4 + 16, var5 + 16);
        this.skyRNG.setSeed(this.world.getRandomSeed());
        long var7 = this.skyRNG.nextLong() / 2L * 2L + 1L;
        long var9 = this.skyRNG.nextLong() / 2L * 2L + 1L;
        this.skyRNG.setSeed((long) x * var7 + (long) z * var9 ^ this.world.getRandomSeed());
        double var11 = 0.25D;
        if (this.skyRNG.nextInt(4) == 0) {
            int var13 = var4 + this.skyRNG.nextInt(16) + 8;
            int var14 = this.skyRNG.nextInt(128);
            int var15 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenLakes(Block.WATER_STILL.blockID)).generate(this.world, this.skyRNG, var13, var14, var15);
        }

        if (this.skyRNG.nextInt(8) == 0) {
            int var24 = var4 + this.skyRNG.nextInt(16) + 8;
            int var36 = this.skyRNG.nextInt(this.skyRNG.nextInt(120) + 8);
            int var48 = var5 + this.skyRNG.nextInt(16) + 8;
            if (var36 < 64 || this.skyRNG.nextInt(10) == 0) {
                (new WorldGenLakes(Block.LAVA_STILL.blockID)).generate(this.world, this.skyRNG, var24, var36, var48);
            }
        }

        for (int var25 = 0; var25 < 8; ++var25) {
            int var37 = var4 + this.skyRNG.nextInt(16) + 8;
            int var49 = this.skyRNG.nextInt(128);
            int var16 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.world, this.skyRNG, var37, var49, var16);
        }

        for (int var26 = 0; var26 < 10; ++var26) {
            int var38 = var4 + this.skyRNG.nextInt(16);
            int var50 = this.skyRNG.nextInt(128);
            int var67 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenClay(32)).generate(this.world, this.skyRNG, var38, var50, var67);
        }

        for (int var27 = 0; var27 < 20; ++var27) {
            int var39 = var4 + this.skyRNG.nextInt(16);
            int var51 = this.skyRNG.nextInt(128);
            int var68 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.DIRT.blockID, 32)).generate(this.world, this.skyRNG, var39, var51, var68);
        }

        for (int var28 = 0; var28 < 10; ++var28) {
            int var40 = var4 + this.skyRNG.nextInt(16);
            int var52 = this.skyRNG.nextInt(128);
            int var69 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.GRAVEL.blockID, 32)).generate(this.world, this.skyRNG, var40, var52, var69);
        }

        for (int var29 = 0; var29 < 20; ++var29) {
            int var41 = var4 + this.skyRNG.nextInt(16);
            int var53 = this.skyRNG.nextInt(128);
            int var70 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.ORE_COAL.blockID, 16)).generate(this.world, this.skyRNG, var41, var53, var70);
        }

        for (int var30 = 0; var30 < 20; ++var30) {
            int var42 = var4 + this.skyRNG.nextInt(16);
            int var54 = this.skyRNG.nextInt(64);
            int var71 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.ORE_IRON.blockID, 8)).generate(this.world, this.skyRNG, var42, var54, var71);
        }

        for (int var31 = 0; var31 < 2; ++var31) {
            int var43 = var4 + this.skyRNG.nextInt(16);
            int var55 = this.skyRNG.nextInt(32);
            int var72 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.ORE_GOLD.blockID, 8)).generate(this.world, this.skyRNG, var43, var55, var72);
        }

        for (int var32 = 0; var32 < 8; ++var32) {
            int var44 = var4 + this.skyRNG.nextInt(16);
            int var56 = this.skyRNG.nextInt(16);
            int var73 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.ORE_REDSTONE.blockID, 7)).generate(this.world, this.skyRNG, var44, var56, var73);
        }

        for (int var33 = 0; var33 < 1; ++var33) {
            int var45 = var4 + this.skyRNG.nextInt(16);
            int var57 = this.skyRNG.nextInt(16);
            int var74 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.ORE_DIAMOND.blockID, 7)).generate(this.world, this.skyRNG, var45, var57, var74);
        }

        for (int var34 = 0; var34 < 1; ++var34) {
            int var46 = var4 + this.skyRNG.nextInt(16);
            int var58 = this.skyRNG.nextInt(16) + this.skyRNG.nextInt(16);
            int var75 = var5 + this.skyRNG.nextInt(16);
            (new WorldGenMinable(Block.ORE_LAPIS.blockID, 6)).generate(this.world, this.skyRNG, var46, var58, var75);
        }

        var11 = 0.5D;
        int var35 = (int) ((this.noiseGen8.noise((double) var4 * var11, (double) var5 * var11) / 8.0D + this.skyRNG.nextDouble() * 4.0D + 4.0D) / 3.0D);
        int var47 = 0;
        if (this.skyRNG.nextInt(10) == 0) {
            ++var47;
        }

        if (var6 == BiomeGenBase.FOREST) {
            var47 += var35 + 5;
        }

        if (var6 == BiomeGenBase.RAINFOREST) {
            var47 += var35 + 5;
        }

        if (var6 == BiomeGenBase.SEASONAL_FOREST) {
            var47 += var35 + 2;
        }

        if (var6 == BiomeGenBase.TAIGA) {
            var47 += var35 + 5;
        }

        if (var6 == BiomeGenBase.DESERT) {
            var47 -= 20;
        }

        if (var6 == BiomeGenBase.TUNDRA) {
            var47 -= 20;
        }

        if (var6 == BiomeGenBase.PLAINS) {
            var47 -= 20;
        }

        for (int var59 = 0; var59 < var47; ++var59) {
            int var76 = var4 + this.skyRNG.nextInt(16) + 8;
            int var17 = var5 + this.skyRNG.nextInt(16) + 8;
            WorldGenerator var18 = var6.getRandomWorldGenForTrees(this.skyRNG);
            var18.setScale(1.0D, 1.0D, 1.0D);
            var18.generate(this.world, this.skyRNG, var76, this.world.getHeightValue(var76, var17), var17);
        }

        for (int var60 = 0; var60 < 2; ++var60) {
            int var77 = var4 + this.skyRNG.nextInt(16) + 8;
            int var87 = this.skyRNG.nextInt(128);
            int var97 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.PLANT_YELLOW.blockID)).generate(this.world, this.skyRNG, var77, var87, var97);
        }

        if (this.skyRNG.nextInt(2) == 0) {
            int var61 = var4 + this.skyRNG.nextInt(16) + 8;
            int var78 = this.skyRNG.nextInt(128);
            int var88 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.PLANT_RED.blockID)).generate(this.world, this.skyRNG, var61, var78, var88);
        }

        if (this.skyRNG.nextInt(4) == 0) {
            int var62 = var4 + this.skyRNG.nextInt(16) + 8;
            int var79 = this.skyRNG.nextInt(128);
            int var89 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_BROWN.blockID)).generate(this.world, this.skyRNG, var62, var79, var89);
        }

        if (this.skyRNG.nextInt(8) == 0) {
            int var63 = var4 + this.skyRNG.nextInt(16) + 8;
            int var80 = this.skyRNG.nextInt(128);
            int var90 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_RED.blockID)).generate(this.world, this.skyRNG, var63, var80, var90);
        }

        for (int var64 = 0; var64 < 10; ++var64) {
            int var81 = var4 + this.skyRNG.nextInt(16) + 8;
            int var91 = this.skyRNG.nextInt(128);
            int var98 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenReed()).generate(this.world, this.skyRNG, var81, var91, var98);
        }

        if (this.skyRNG.nextInt(32) == 0) {
            int var65 = var4 + this.skyRNG.nextInt(16) + 8;
            int var82 = this.skyRNG.nextInt(128);
            int var92 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(this.world, this.skyRNG, var65, var82, var92);
        }

        int var66 = 0;
        if (var6 == BiomeGenBase.DESERT) {
            var66 += 10;
        }

        for (int var83 = 0; var83 < var66; ++var83) {
            int var93 = var4 + this.skyRNG.nextInt(16) + 8;
            int var99 = this.skyRNG.nextInt(128);
            int var19 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenCactus()).generate(this.world, this.skyRNG, var93, var99, var19);
        }

        for (int var84 = 0; var84 < 50; ++var84) {
            int var94 = var4 + this.skyRNG.nextInt(16) + 8;
            int var100 = this.skyRNG.nextInt(this.skyRNG.nextInt(120) + 8);
            int var103 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenLiquids(Block.WATER_MOVING.blockID)).generate(this.world, this.skyRNG, var94, var100, var103);
        }

        for (int var85 = 0; var85 < 20; ++var85) {
            int var95 = var4 + this.skyRNG.nextInt(16) + 8;
            int var101 = this.skyRNG.nextInt(this.skyRNG.nextInt(this.skyRNG.nextInt(112) + 8) + 8);
            int var104 = var5 + this.skyRNG.nextInt(16) + 8;
            (new WorldGenLiquids(Block.LAVA_MOVING.blockID)).generate(this.world, this.skyRNG, var95, var101, var104);
        }

        this.field7 = this.world.getWorldChunkManager().getTemperatures(this.field7, var4 + 8, var5 + 8, 16, 16);

        for (int var86 = var4 + 8; var86 < var4 + 8 + 16; ++var86) {
            for (int var96 = var5 + 8; var96 < var5 + 8 + 16; ++var96) {
                int var102 = var86 - (var4 + 8);
                int var105 = var96 - (var5 + 8);
                int var20 = this.world.findTopSolidOrLiquidBlock(var86, var96);
                double var21 = this.field7[var102 * 16 + var105] - (double) (var20 - 64) / 64.0D * 0.3D;
                if (var21 < 0.5D && var20 > 0 && var20 < 128 && this.world.isAirBlock(var86, var20, var96) && this.world.getBlockMaterial(var86, var20 - 1, var96).getIsSolid() && this.world.getBlockMaterial(var86, var20 - 1, var96) != Material.ICE) {
                    this.world.setBlockWithNotify(var86, var20, var96, Block.SNOW.blockID);
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
