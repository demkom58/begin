package net.minecraft.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.World;
import net.minecraft.world.gen.noise.NoiseGeneratorOctaves;
import net.minecraft.world.gen.struct.*;

import java.util.Random;

public class ChunkProviderHell implements IChunkProvider {
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves noiseGen7;
    double[] field1;
    double[] field2;
    double[] field3;
    double[] field4;
    double[] field5;
    private Random hellRNG;
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
    private MapGenBase cavesHell = new MapGenCavesHell();

    public ChunkProviderHell(World var1, long var2) {
        this.world = var1;
        this.hellRNG = new Random(var2);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.noiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
    }

    public void method1(int var1, int var2, byte[] var3) {
        byte var4 = 4;
        byte var5 = 32;
        int var6 = var4 + 1;
        byte var7 = 17;
        int var8 = var4 + 1;
        this.terrain = this.method3(this.terrain, var1 * var4, 0, var2 * var4, var6, var7, var8);

        for (int var9 = 0; var9 < var4; ++var9) {
            for (int var10 = 0; var10 < var4; ++var10) {
                for (int var11 = 0; var11 < 16; ++var11) {
                    double var12 = 0.125D;
                    double var14 = this.terrain[((var9) * var8 + var10) * var7 + var11];
                    double var16 = this.terrain[((var9) * var8 + var10 + 1) * var7 + var11];
                    double var18 = this.terrain[((var9 + 1) * var8 + var10) * var7 + var11];
                    double var20 = this.terrain[((var9 + 1) * var8 + var10 + 1) * var7 + var11];
                    double var22 = (this.terrain[((var9) * var8 + var10) * var7 + var11 + 1] - var14) * var12;
                    double var24 = (this.terrain[((var9) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
                    double var26 = (this.terrain[((var9 + 1) * var8 + var10) * var7 + var11 + 1] - var18) * var12;
                    double var28 = (this.terrain[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var20) * var12;

                    for (int var30 = 0; var30 < 8; ++var30) {
                        double var31 = 0.25D;
                        double var33 = var14;
                        double var35 = var16;
                        double var37 = (var18 - var14) * var31;
                        double var39 = (var20 - var16) * var31;

                        for (int var41 = 0; var41 < 4; ++var41) {
                            int var42 = var41 + var9 * 4 << 11 | var10 * 4 << 7 | var11 * 8 + var30;
                            short var43 = 128;
                            double var44 = 0.25D;
                            double var46 = var33;
                            double var48 = (var35 - var33) * var44;

                            for (int var50 = 0; var50 < 4; ++var50) {
                                int var51 = 0;
                                if (var11 * 8 + var30 < var5) {
                                    var51 = Block.LAVA_STILL.blockID;
                                }

                                if (var46 > 0.0D) {
                                    var51 = Block.BLOOD_STONE.blockID;
                                }

                                var3[var42] = (byte) var51;
                                var42 += var43;
                                var46 += var48;
                            }

                            var33 += var37;
                            var35 += var39;
                        }

                        var14 += var22;
                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                    }
                }
            }
        }

    }

    public void method2(int var1, int var2, byte[] var3) {
        byte var4 = 64;
        double var5 = 0.03125D;
        this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var5, var5, 1.0D);
        this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, var1 * 16, 109.0134D, var2 * 16, 16, 1, 16, var5, 1.0D, var5);
        this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                boolean var9 = this.sandNoise[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
                boolean var10 = this.gravelNoise[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
                int var11 = (int) (this.stoneNoise[var7 + var8 * 16] / 3.0D + 3.0D + this.hellRNG.nextDouble() * 0.25D);
                int var12 = -1;
                byte var13 = (byte) Block.BLOOD_STONE.blockID;
                byte var14 = (byte) Block.BLOOD_STONE.blockID;

                for (int var15 = 127; var15 >= 0; --var15) {
                    int var16 = (var8 * 16 + var7) * 128 + var15;
                    if (var15 >= 127 - this.hellRNG.nextInt(5)) {
                        var3[var16] = (byte) Block.BEDROCK.blockID;
                    } else if (var15 <= this.hellRNG.nextInt(5)) {
                        var3[var16] = (byte) Block.BEDROCK.blockID;
                    } else {
                        byte var17 = var3[var16];
                        if (var17 == 0) {
                            var12 = -1;
                        } else if (var17 == Block.BLOOD_STONE.blockID) {
                            if (var12 == -1) {
                                if (var11 <= 0) {
                                    var13 = 0;
                                    var14 = (byte) Block.BLOOD_STONE.blockID;
                                } else if (var15 >= var4 - 4 && var15 <= var4 + 1) {
                                    var13 = (byte) Block.BLOOD_STONE.blockID;
                                    var14 = (byte) Block.BLOOD_STONE.blockID;
                                    if (var10) {
                                        var13 = (byte) Block.GRAVEL.blockID;
                                    }

                                    if (var10) {
                                        var14 = (byte) Block.BLOOD_STONE.blockID;
                                    }

                                    if (var9) {
                                        var13 = (byte) Block.SOUL_SAND.blockID;
                                    }

                                    if (var9) {
                                        var14 = (byte) Block.SOUL_SAND.blockID;
                                    }
                                }

                                if (var15 < var4 && var13 == 0) {
                                    var13 = (byte) Block.LAVA_STILL.blockID;
                                }

                                var12 = var11;
                                if (var15 >= var4 - 1) {
                                    var3[var16] = var13;
                                } else {
                                    var3[var16] = var14;
                                }
                            } else if (var12 > 0) {
                                --var12;
                                var3[var16] = var14;
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
        this.hellRNG.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        byte[] var3 = new byte[32768];
        this.method1(x, z, var3);
        this.method2(x, z, var3);
        this.cavesHell.generate(this, this.world, x, z, var3);
        return new Chunk(this.world, var3, x, z);
    }

    private double[] method3(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var1 == null) {
            var1 = new double[var5 * var6 * var7];
        }

        double var8 = 684.412D;
        double var10 = 2053.236D;
        this.field4 = this.noiseGen6.generateNoiseOctaves(this.field4, var2, var3, var4, var5, 1, var7, 1.0D, 0.0D, 1.0D);
        this.field5 = this.noiseGen7.generateNoiseOctaves(this.field5, var2, var3, var4, var5, 1, var7, 100.0D, 0.0D, 100.0D);
        this.field1 = this.noiseGen3.generateNoiseOctaves(this.field1, var2, var3, var4, var5, var6, var7, var8 / 80.0D, var10 / 60.0D, var8 / 80.0D);
        this.field2 = this.noiseGen1.generateNoiseOctaves(this.field2, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        this.field3 = this.noiseGen2.generateNoiseOctaves(this.field3, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;
        double[] var14 = new double[var6];

        for (int var15 = 0; var15 < var6; ++var15) {
            var14[var15] = Math.cos((double) var15 * Math.PI * 6.0D / (double) var6) * 2.0D;
            double var16 = var15;
            if (var15 > var6 / 2) {
                var16 = var6 - 1 - var15;
            }

            if (var16 < 4.0D) {
                var16 = 4.0D - var16;
                var14[var15] -= var16 * var16 * var16 * 10.0D;
            }
        }

        for (int var36 = 0; var36 < var5; ++var36) {
            for (int var38 = 0; var38 < var7; ++var38) {
                double var17 = (this.field4[var13] + 256.0D) / 512.0D;
                if (var17 > 1.0D) {
                    var17 = 1.0D;
                }

                double var19 = 0.0D;
                double var21 = this.field5[var13] / 8000.0D;
                if (var21 < 0.0D) {
                    var21 = -var21;
                }

                var21 = var21 * 3.0D - 3.0D;
                if (var21 < 0.0D) {
                    var21 = var21 / 2.0D;
                    if (var21 < -1.0D) {
                        var21 = -1.0D;
                    }

                    var21 = var21 / 1.4D;
                    var21 = var21 / 2.0D;
                    var17 = 0.0D;
                } else {
                    if (var21 > 1.0D) {
                        var21 = 1.0D;
                    }

                    var21 = var21 / 6.0D;
                }

                var17 = var17 + 0.5D;
                var21 = var21 * (double) var6 / 16.0D;
                ++var13;

                for (int var23 = 0; var23 < var6; ++var23) {
                    double var24 = 0.0D;
                    double var26 = var14[var23];
                    double var28 = this.field2[var12] / 512.0D;
                    double var30 = this.field3[var12] / 512.0D;
                    double var32 = (this.field1[var12] / 10.0D + 1.0D) / 2.0D;
                    if (var32 < 0.0D) {
                        var24 = var28;
                    } else if (var32 > 1.0D) {
                        var24 = var30;
                    } else {
                        var24 = var28 + (var30 - var28) * var32;
                    }

                    var24 = var24 - var26;
                    if (var23 > var6 - 4) {
                        double var34 = (float) (var23 - (var6 - 4)) / 3.0F;
                        var24 = var24 * (1.0D - var34) + -10.0D * var34;
                    }

                    if ((double) var23 < var19) {
                        double var47 = (var19 - (double) var23) / 4.0D;
                        if (var47 < 0.0D) {
                            var47 = 0.0D;
                        }

                        if (var47 > 1.0D) {
                            var47 = 1.0D;
                        }

                        var24 = var24 * (1.0D - var47) + -10.0D * var47;
                    }

                    var1[var12] = var24;
                    ++var12;
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

        for (int var6 = 0; var6 < 8; ++var6) {
            int var7 = var4 + this.hellRNG.nextInt(16) + 8;
            int var8 = this.hellRNG.nextInt(120) + 4;
            int var9 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenHellLava(Block.LAVA_MOVING.blockID)).generate(this.world, this.hellRNG, var7, var8, var9);
        }

        int var11 = this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1;

        for (int var13 = 0; var13 < var11; ++var13) {
            int var18 = var4 + this.hellRNG.nextInt(16) + 8;
            int var23 = this.hellRNG.nextInt(120) + 4;
            int var10 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFire()).generate(this.world, this.hellRNG, var18, var23, var10);
        }

        var11 = this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1);

        for (int var14 = 0; var14 < var11; ++var14) {
            int var19 = var4 + this.hellRNG.nextInt(16) + 8;
            int var24 = this.hellRNG.nextInt(120) + 4;
            int var28 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone1()).generate(this.world, this.hellRNG, var19, var24, var28);
        }

        for (int var15 = 0; var15 < 10; ++var15) {
            int var20 = var4 + this.hellRNG.nextInt(16) + 8;
            int var25 = this.hellRNG.nextInt(128);
            int var29 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone2()).generate(this.world, this.hellRNG, var20, var25, var29);
        }

        if (this.hellRNG.nextInt(1) == 0) {
            int var16 = var4 + this.hellRNG.nextInt(16) + 8;
            int var21 = this.hellRNG.nextInt(128);
            int var26 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_BROWN.blockID)).generate(this.world, this.hellRNG, var16, var21, var26);
        }

        if (this.hellRNG.nextInt(1) == 0) {
            int var17 = var4 + this.hellRNG.nextInt(16) + 8;
            int var22 = this.hellRNG.nextInt(128);
            int var27 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_RED.blockID)).generate(this.world, this.hellRNG, var17, var22, var27);
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
        return "HellRandomLevelSource";
    }
}
