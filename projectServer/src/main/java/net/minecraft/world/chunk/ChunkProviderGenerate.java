package net.minecraft.world.chunk;

import net.minecraft.util.IProgressUpdate;
import net.minecraft.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.world.World;
import net.minecraft.world.gen.*;

import java.util.Random;

public class ChunkProviderGenerate implements IChunkProvider {
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves noiseGen7;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    double[] field_4229_d;
    double[] field_4228_e;
    double[] field_4227_f;
    double[] field_4226_g;
    double[] field_4225_h;
    int[][] field_707_i = new int[32][32];
    private Random rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    private NoiseGeneratorOctaves noiseGen5;
    private World worldObj;
    private double[] terrain;
    private double[] sandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] stoneNoise = new double[256];
    private MapGenBase cavesGen = new MapGenCaves();
    private BiomeGenBase[] biomesForGeneration;
    private double[] generatedTemperatures;

    public ChunkProviderGenerate(World world, long randSeed) {
        this.worldObj = world;
        this.rand = new Random(randSeed);
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
        this.terrain = this.func_4058_a(this.terrain, var1 * var6, 0, var2 * var6, var8, var9, var10);

        for (int var11 = 0; var11 < var6; ++var11) {
            for (int var12 = 0; var12 < var6; ++var12) {
                for (int var13 = 0; var13 < 16; ++var13) {
                    double var14 = 0.125D;
                    double var16 = this.terrain[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 0];
                    double var18 = this.terrain[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 0];
                    double var20 = this.terrain[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 0];
                    double var22 = this.terrain[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 0];
                    double var24 = (this.terrain[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 1] - var16) * var14;
                    double var26 = (this.terrain[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 1] - var18) * var14;
                    double var28 = (this.terrain[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 1] - var20) * var14;
                    double var30 = (this.terrain[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 1] - var22) * var14;

                    for (int var32 = 0; var32 < 8; ++var32) {
                        double var33 = 0.25D;
                        double var35 = var16;
                        double var37 = var18;
                        double var39 = (var20 - var16) * var33;
                        double var41 = (var22 - var18) * var33;

                        for (int var43 = 0; var43 < 4; ++var43) {
                            int var44 = var43 + var11 * 4 << 11 | 0 + var12 * 4 << 7 | var13 * 8 + var32;
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

    public void replaceBlocksForBiome(int var1, int var2, byte[] var3, BiomeGenBase[] genBases) {
        byte var5 = 64;
        double var6 = 0.03125D;
        this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, (double) (var1 * 16), (double) (var2 * 16), 0.0D, 16, 16, 1, var6, var6, 1.0D);
        this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, (double) (var1 * 16), 109.0134D, (double) (var2 * 16), 16, 1, 16, var6, 1.0D, var6);
        this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, (double) (var1 * 16), (double) (var2 * 16), 0.0D, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);

        for (int var8 = 0; var8 < 16; ++var8) {
            for (int var9 = 0; var9 < 16; ++var9) {
                BiomeGenBase genBase = genBases[var8 + var9 * 16];
                boolean var11 = this.sandNoise[var8 + var9 * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
                boolean var12 = this.gravelNoise[var8 + var9 * 16] + this.rand.nextDouble() * 0.2D > 3.0D;
                int var13 = (int) (this.stoneNoise[var8 + var9 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int var14 = -1;
                byte var15 = genBase.topBlock;
                byte var16 = genBase.fillerBlock;

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
                                    var15 = genBase.topBlock;
                                    var16 = genBase.fillerBlock;
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

    public Chunk prepareChunk(int var1, int var2) {
        return this.provideChunk(var1, var2);
    }

    public Chunk provideChunk(int var1, int var2) {
        this.rand.setSeed((long) var1 * 341873128712L + (long) var2 * 132897987541L);
        byte[] var3 = new byte['\u8000'];
        Chunk var4 = new Chunk(this.worldObj, var3, var1, var2);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, var1 * 16, var2 * 16, 16, 16);
        double[] var5 = this.worldObj.getWorldChunkManager().temperature;
        this.generateTerrain(var1, var2, var3, this.biomesForGeneration, var5);
        this.replaceBlocksForBiome(var1, var2, var3, this.biomesForGeneration);
        this.cavesGen.func_667_a(this, this.worldObj, var1, var2, var3);
        var4.generateHeightMap();
        return var4;
    }

    private double[] func_4058_a(double[] terrain, int var2, int var3, int var4, int x, int y, int z) {
        if (terrain == null) {
            terrain = new double[x * y * z];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        double[] var12 = this.worldObj.getWorldChunkManager().temperature;
        double[] var13 = this.worldObj.getWorldChunkManager().humidity;
        this.field_4226_g = this.noiseGen6.func_4103_a(this.field_4226_g, var2, var4, x, z, 1.121D, 1.121D, 0.5D);
        this.field_4225_h = this.noiseGen7.func_4103_a(this.field_4225_h, var2, var4, x, z, 200.0D, 200.0D, 0.5D);
        this.field_4229_d = this.noiseGen3.generateNoiseOctaves(this.field_4229_d, (double) var2, (double) var3, (double) var4, x, y, z, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.field_4228_e = this.noiseGen1.generateNoiseOctaves(this.field_4228_e, (double) var2, (double) var3, (double) var4, x, y, z, var8, var10, var8);
        this.field_4227_f = this.noiseGen2.generateNoiseOctaves(this.field_4227_f, (double) var2, (double) var3, (double) var4, x, y, z, var8, var10, var8);
        int var14 = 0;
        int var15 = 0;
        int var16 = 16 / x;

        for (int var17 = 0; var17 < x; ++var17) {
            int var18 = var17 * var16 + var16 / 2;

            for (int var19 = 0; var19 < z; ++var19) {
                int var20 = var19 * var16 + var16 / 2;
                double var21 = var12[var18 * 16 + var20];
                double var23 = var13[var18 * 16 + var20] * var21;
                double var25 = 1.0D - var23;
                var25 = var25 * var25;
                var25 = var25 * var25;
                var25 = 1.0D - var25;
                double var27 = (this.field_4226_g[var15] + 256.0D) / 512.0D;
                var27 = var27 * var25;
                if (var27 > 1.0D) {
                    var27 = 1.0D;
                }

                double var29 = this.field_4225_h[var15] / 8000.0D;
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
                var29 = var29 * (double) y / 16.0D;
                double var31 = (double) y / 2.0D + var29 * 4.0D;
                ++var15;

                for (int var33 = 0; var33 < y; ++var33) {
                    double var34 = 0.0D;
                    double var36 = ((double) var33 - var31) * 12.0D / var27;
                    if (var36 < 0.0D) {
                        var36 *= 4.0D;
                    }

                    double var38 = this.field_4228_e[var14] / 512.0D;
                    double var40 = this.field_4227_f[var14] / 512.0D;
                    double var42 = (this.field_4229_d[var14] / 10.0D + 1.0D) / 2.0D;
                    if (var42 < 0.0D) {
                        var34 = var38;
                    } else if (var42 > 1.0D) {
                        var34 = var40;
                    } else {
                        var34 = var38 + (var40 - var38) * var42;
                    }

                    var34 = var34 - var36;
                    if (var33 > y - 4) {
                        double var44 = (double) ((float) (var33 - (y - 4)) / 3.0F);
                        var34 = var34 * (1.0D - var44) + -10.0D * var44;
                    }

                    terrain[var14] = var34;
                    ++var14;
                }
            }
        }

        return terrain;
    }

    public boolean chunkExists(int var1, int var2) {
        return true;
    }

    public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
        BlockSand.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BiomeGenBase biomeGenBase = this.worldObj.getWorldChunkManager().getBiomeGenAt(x + 16, z + 16);
        this.rand.setSeed(this.worldObj.getRandomSeed());
        long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) chunkX * var7 + (long) chunkZ * var9 ^ this.worldObj.getRandomSeed());
        double var11 = 0.25D;
        if (this.rand.nextInt(4) == 0) {
            int var13 = x + this.rand.nextInt(16) + 8;
            int var14 = this.rand.nextInt(128);
            int var15 = z + this.rand.nextInt(16) + 8;
            new WorldGenLakes(Block.WATER_STILL.blockID).generate(this.worldObj, this.rand, var13, var14, var15);
        }

        if (this.rand.nextInt(8) == 0) {
            int var26 = x + this.rand.nextInt(16) + 8;
            int var38 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            int var50 = z + this.rand.nextInt(16) + 8;
            if (var38 < 64 || this.rand.nextInt(10) == 0) {
                new WorldGenLakes(Block.LAVA_STILL.blockID).generate(this.worldObj, this.rand, var26, var38, var50);
            }
        }

        for (int var27 = 0; var27 < 8; ++var27) {
            int var39 = x + this.rand.nextInt(16) + 8;
            int var51 = this.rand.nextInt(128);
            int var16 = z + this.rand.nextInt(16) + 8;
            new WorldGenDungeons().generate(this.worldObj, this.rand, var39, var51, var16);
        }

        for (int var28 = 0; var28 < 10; ++var28) {
            int var40 = x + this.rand.nextInt(16);
            int var52 = this.rand.nextInt(128);
            int var63 = z + this.rand.nextInt(16);
            new WorldGenClay(32).generate(this.worldObj, this.rand, var40, var52, var63);
        }

        for (int var29 = 0; var29 < 20; ++var29) {
            int var41 = x + this.rand.nextInt(16);
            int var53 = this.rand.nextInt(128);
            int var64 = z + this.rand.nextInt(16);
            new WorldGenMinable(Block.DIRT.blockID, 32).generate(this.worldObj, this.rand, var41, var53, var64);
        }

        for (int var30 = 0; var30 < 10; ++var30) {
            int var42 = x + this.rand.nextInt(16);
            int var54 = this.rand.nextInt(128);
            int var65 = z + this.rand.nextInt(16);
            new WorldGenMinable(Block.GRAVEL.blockID, 32).generate(this.worldObj, this.rand, var42, var54, var65);
        }

        for (int var31 = 0; var31 < 20; ++var31) {
            int var43 = x + this.rand.nextInt(16);
            int var55 = this.rand.nextInt(128);
            int var66 = z + this.rand.nextInt(16);
            new WorldGenMinable(Block.ORE_COAL.blockID, 16).generate(this.worldObj, this.rand, var43, var55, var66);
        }

        for (int var32 = 0; var32 < 20; ++var32) {
            int var44 = x + this.rand.nextInt(16);
            int var56 = this.rand.nextInt(64);
            int var67 = z + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_IRON.blockID, 8)).generate(this.worldObj, this.rand, var44, var56, var67);
        }

        for (int var33 = 0; var33 < 2; ++var33) {
            int var45 = x + this.rand.nextInt(16);
            int var57 = this.rand.nextInt(32);
            int var68 = z + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_GOLD.blockID, 8)).generate(this.worldObj, this.rand, var45, var57, var68);
        }

        for (int var34 = 0; var34 < 8; ++var34) {
            int var46 = x + this.rand.nextInt(16);
            int var58 = this.rand.nextInt(16);
            int var69 = z + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_REDSTONE.blockID, 7)).generate(this.worldObj, this.rand, var46, var58, var69);
        }

        for (int var35 = 0; var35 < 1; ++var35) {
            int var47 = x + this.rand.nextInt(16);
            int var59 = this.rand.nextInt(16);
            int var70 = z + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_DIAMOND.blockID, 7)).generate(this.worldObj, this.rand, var47, var59, var70);
        }

        for (int var36 = 0; var36 < 1; ++var36) {
            int var48 = x + this.rand.nextInt(16);
            int var60 = this.rand.nextInt(16) + this.rand.nextInt(16);
            int var71 = z + this.rand.nextInt(16);
            (new WorldGenMinable(Block.ORE_LAPIS.blockID, 6)).generate(this.worldObj, this.rand, var48, var60, var71);
        }

        var11 = 0.5D;
        int var37 = (int) ((this.mobSpawnerNoise.func_647_a((double) x * var11, (double) z * var11) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
        int var49 = 0;
        if (this.rand.nextInt(10) == 0) {
            ++var49;
        }

        if (biomeGenBase == BiomeGenBase.FOREST) {
            var49 += var37 + 5;
        }

        if (biomeGenBase == BiomeGenBase.RAINFOREST) {
            var49 += var37 + 5;
        }

        if (biomeGenBase == BiomeGenBase.SEASONAL_FOREST) {
            var49 += var37 + 2;
        }

        if (biomeGenBase == BiomeGenBase.TAIGA) {
            var49 += var37 + 5;
        }

        if (biomeGenBase == BiomeGenBase.DESERT) {
            var49 -= 20;
        }

        if (biomeGenBase == BiomeGenBase.TUNDRA) {
            var49 -= 20;
        }

        if (biomeGenBase == BiomeGenBase.PLAINS) {
            var49 -= 20;
        }

        for (int var61 = 0; var61 < var49; ++var61) {
            int var72 = x + this.rand.nextInt(16) + 8;
            int var17 = z + this.rand.nextInt(16) + 8;
            WorldGenerator var18 = biomeGenBase.getRandomWorldGenForTrees(this.rand);
            var18.func_420_a(1.0D, 1.0D, 1.0D);
            var18.generate(this.worldObj, this.rand, var72, this.worldObj.getHeightValue(var72, var17), var17);
        }

        byte var62 = 0;
        if (biomeGenBase == BiomeGenBase.FOREST) {
            var62 = 2;
        }

        if (biomeGenBase == BiomeGenBase.SEASONAL_FOREST) {
            var62 = 4;
        }

        if (biomeGenBase == BiomeGenBase.TAIGA) {
            var62 = 2;
        }

        if (biomeGenBase == BiomeGenBase.PLAINS) {
            var62 = 3;
        }

        for (int var73 = 0; var73 < var62; ++var73) {
            int var76 = x + this.rand.nextInt(16) + 8;
            int var85 = this.rand.nextInt(128);
            int var19 = z + this.rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.PLANT_YELLOW.blockID)).generate(this.worldObj, this.rand, var76, var85, var19);
        }

        byte var74 = 0;
        if (biomeGenBase == BiomeGenBase.FOREST) {
            var74 = 2;
        }

        if (biomeGenBase == BiomeGenBase.RAINFOREST) {
            var74 = 10;
        }

        if (biomeGenBase == BiomeGenBase.SEASONAL_FOREST) {
            var74 = 2;
        }

        if (biomeGenBase == BiomeGenBase.TAIGA) {
            var74 = 1;
        }

        if (biomeGenBase == BiomeGenBase.PLAINS) {
            var74 = 10;
        }

        for (int var77 = 0; var77 < var74; ++var77) {
            byte var86 = 1;
            if (biomeGenBase == BiomeGenBase.RAINFOREST && this.rand.nextInt(3) != 0) {
                var86 = 2;
            }

            int var97 = x + this.rand.nextInt(16) + 8;
            int var20 = this.rand.nextInt(128);
            int var21 = z + this.rand.nextInt(16) + 8;
            new WorldGenTallGrass(Block.TALLGRASS.blockID, var86).generate(this.worldObj, this.rand, var97, var20, var21);
        }

        var74 = 0;
        if (biomeGenBase == BiomeGenBase.DESERT) {
            var74 = 2;
        }

        for (int var78 = 0; var78 < var74; ++var78) {
            int var87 = x + this.rand.nextInt(16) + 8;
            int var98 = this.rand.nextInt(128);
            int var108 = z + this.rand.nextInt(16) + 8;
            new WorldGenDeadBush(Block.DEADBUSH.blockID).generate(this.worldObj, this.rand, var87, var98, var108);
        }

        if (this.rand.nextInt(2) == 0) {
            int var79 = x + this.rand.nextInt(16) + 8;
            int var88 = this.rand.nextInt(128);
            int var99 = z + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.PLANT_RED.blockID).generate(this.worldObj, this.rand, var79, var88, var99);
        }

        if (this.rand.nextInt(4) == 0) {
            int var80 = x + this.rand.nextInt(16) + 8;
            int var89 = this.rand.nextInt(128);
            int var100 = z + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.MUSHROOM_BROWN.blockID).generate(this.worldObj, this.rand, var80, var89, var100);
        }

        if (this.rand.nextInt(8) == 0) {
            int var81 = x + this.rand.nextInt(16) + 8;
            int var90 = this.rand.nextInt(128);
            int var101 = z + this.rand.nextInt(16) + 8;
            new WorldGenFlowers(Block.MUSHROOM_RED.blockID).generate(this.worldObj, this.rand, var81, var90, var101);
        }

        for (int var82 = 0; var82 < 10; ++var82) {
            int var91 = x + this.rand.nextInt(16) + 8;
            int var102 = this.rand.nextInt(128);
            int var109 = z + this.rand.nextInt(16) + 8;
            new WorldGenReed().generate(this.worldObj, this.rand, var91, var102, var109);
        }

        if (this.rand.nextInt(32) == 0) {
            int var83 = x + this.rand.nextInt(16) + 8;
            int var92 = this.rand.nextInt(128);
            int var103 = z + this.rand.nextInt(16) + 8;
            new WorldGenPumpkin().generate(this.worldObj, this.rand, var83, var92, var103);
        }

        int var84 = 0;
        if (biomeGenBase == BiomeGenBase.DESERT) {
            var84 += 10;
        }

        for (int var93 = 0; var93 < var84; ++var93) {
            int var104 = x + this.rand.nextInt(16) + 8;
            int var110 = this.rand.nextInt(128);
            int var114 = z + this.rand.nextInt(16) + 8;
            new WorldGenCactus().generate(this.worldObj, this.rand, var104, var110, var114);
        }

        for (int var94 = 0; var94 < 50; ++var94) {
            int var105 = x + this.rand.nextInt(16) + 8;
            int var111 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            int var115 = z + this.rand.nextInt(16) + 8;
            new WorldGenLiquids(Block.WATER_MOVING.blockID).generate(this.worldObj, this.rand, var105, var111, var115);
        }

        for (int var95 = 0; var95 < 20; ++var95) {
            int var106 = x + this.rand.nextInt(16) + 8;
            int var112 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
            int var116 = z + this.rand.nextInt(16) + 8;
            new WorldGenLiquids(Block.LAVA_MOVING.blockID).generate(this.worldObj, this.rand, var106, var112, var116);
        }

        this.generatedTemperatures = this.worldObj.getWorldChunkManager().getTemperatures(this.generatedTemperatures, x + 8, z + 8, 16, 16);

        for (int var96 = x + 8; var96 < x + 8 + 16; ++var96) {
            for (int var107 = z + 8; var107 < z + 8 + 16; ++var107) {
                int var113 = var96 - (x + 8);
                int var117 = var107 - (z + 8);
                int var22 = this.worldObj.getTopSolidOrLiquidBlock(var96, var107);
                double var23 = this.generatedTemperatures[var113 * 16 + var117] - (double) (var22 - 64) / 64.0D * 0.3D;
                if (var23 < 0.5D && var22 > 0 && var22 < 128 && this.worldObj.isAirBlock(var96, var22, var107) && this.worldObj.getBlockMaterial(var96, var22 - 1, var107).getIsSolid() && this.worldObj.getBlockMaterial(var96, var22 - 1, var107) != Material.ICE) {
                    this.worldObj.setBlockWithNotify(var96, var22, var107, Block.SNOW.blockID);
                }
            }
        }

        BlockSand.fallInstantly = false;
    }

    public boolean saveChunks(boolean var1, IProgressUpdate progressUpdate) {
        return true;
    }

    public boolean unload100OldestChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }
}
