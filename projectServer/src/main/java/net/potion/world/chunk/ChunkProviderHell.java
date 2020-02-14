package net.potion.world.chunk;

import net.potion.util.IProgressUpdatable;
import net.potion.block.Block;
import net.potion.block.BlockSand;
import net.potion.world.World;
import net.potion.world.gen.*;

import java.util.Random;

public class ChunkProviderHell implements IChunkProvider {
    public NoiseGeneratorOctaves field_4248_a;
    public NoiseGeneratorOctaves field_4247_b;
    double[] field_4246_c;
    double[] field_4245_d;
    double[] field_4244_e;
    double[] field_4243_f;
    double[] field_4242_g;
    private Random hellRNG;
    private NoiseGeneratorOctaves field_4240_i;
    private NoiseGeneratorOctaves field_4239_j;
    private NoiseGeneratorOctaves field_4238_k;
    private NoiseGeneratorOctaves field_4237_l;
    private NoiseGeneratorOctaves field_4236_m;
    private World worldObj;
    private double[] field_4234_o;
    private double[] field_4233_p = new double[256];
    private double[] field_4232_q = new double[256];
    private double[] field_4231_r = new double[256];
    private MapGenBase field_4230_s = new MapGenCavesHell();

    public ChunkProviderHell(World world, long randSeed) {
        this.worldObj = world;
        this.hellRNG = new Random(randSeed);
        this.field_4240_i = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.field_4239_j = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.field_4238_k = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.field_4237_l = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.field_4236_m = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.field_4248_a = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.field_4247_b = new NoiseGeneratorOctaves(this.hellRNG, 16);
    }

    public void func_4062_a(int var1, int var2, byte[] var3) {
        byte var4 = 4;
        byte var5 = 32;
        int var6 = var4 + 1;
        byte var7 = 17;
        int var8 = var4 + 1;
        this.field_4234_o = this.func_4060_a(this.field_4234_o, var1 * var4, 0, var2 * var4, var6, var7, var8);

        for (int var9 = 0; var9 < var4; ++var9) {
            for (int var10 = 0; var10 < var4; ++var10) {
                for (int var11 = 0; var11 < 16; ++var11) {
                    double var12 = 0.125D;
                    double var14 = this.field_4234_o[((var9) * var8 + var10) * var7 + var11];
                    double var16 = this.field_4234_o[((var9) * var8 + var10 + 1) * var7 + var11];
                    double var18 = this.field_4234_o[((var9 + 1) * var8 + var10) * var7 + var11];
                    double var20 = this.field_4234_o[((var9 + 1) * var8 + var10 + 1) * var7 + var11];
                    double var22 = (this.field_4234_o[((var9) * var8 + var10) * var7 + var11 + 1] - var14) * var12;
                    double var24 = (this.field_4234_o[((var9) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
                    double var26 = (this.field_4234_o[((var9 + 1) * var8 + var10) * var7 + var11 + 1] - var18) * var12;
                    double var28 = (this.field_4234_o[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var20) * var12;

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

    public void func_4061_b(int var1, int var2, byte[] var3) {
        byte var4 = 64;
        double var5 = 0.03125D;
        this.field_4233_p = this.field_4237_l.generateNoiseOctaves(this.field_4233_p, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var5, var5, 1.0D);
        this.field_4232_q = this.field_4237_l.generateNoiseOctaves(this.field_4232_q, var1 * 16, 109.0134D, var2 * 16, 16, 1, 16, var5, 1.0D, var5);
        this.field_4231_r = this.field_4236_m.generateNoiseOctaves(this.field_4231_r, var1 * 16, var2 * 16, 0.0D, 16, 16, 1, var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                boolean var9 = this.field_4233_p[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
                boolean var10 = this.field_4232_q[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
                int var11 = (int) (this.field_4231_r[var7 + var8 * 16] / 3.0D + 3.0D + this.hellRNG.nextDouble() * 0.25D);
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
    public Chunk prepareChunk(int var1, int var2) {
        return this.provideChunk(var1, var2);
    }

    @Override
    public Chunk provideChunk(int var1, int var2) {
        this.hellRNG.setSeed((long) var1 * 341873128712L + (long) var2 * 132897987541L);
        byte[] var3 = new byte['\u8000'];
        this.func_4062_a(var1, var2, var3);
        this.func_4061_b(var1, var2, var3);
        this.field_4230_s.func_667_a(this, this.worldObj, var1, var2, var3);
        Chunk var4 = new Chunk(this.worldObj, var3, var1, var2);
        return var4;
    }

    private double[] func_4060_a(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        if (var1 == null) {
            var1 = new double[var5 * var6 * var7];
        }

        double var8 = 684.412D;
        double var10 = 2053.236D;
        this.field_4243_f = this.field_4248_a.generateNoiseOctaves(this.field_4243_f, var2, var3, var4, var5, 1, var7, 1.0D, 0.0D, 1.0D);
        this.field_4242_g = this.field_4247_b.generateNoiseOctaves(this.field_4242_g, var2, var3, var4, var5, 1, var7, 100.0D, 0.0D, 100.0D);
        this.field_4246_c = this.field_4238_k.generateNoiseOctaves(this.field_4246_c, var2, var3, var4, var5, var6, var7, var8 / 80.0D, var10 / 60.0D, var8 / 80.0D);
        this.field_4245_d = this.field_4240_i.generateNoiseOctaves(this.field_4245_d, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        this.field_4244_e = this.field_4239_j.generateNoiseOctaves(this.field_4244_e, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;
        double[] var14 = new double[var6];

        for (int var15 = 0; var15 < var6; ++var15) {
            var14[var15] = Math.cos((double) var15 * 3.141592653589793D * 6.0D / (double) var6) * 2.0D;
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
                double var17 = (this.field_4243_f[var13] + 256.0D) / 512.0D;
                if (var17 > 1.0D) {
                    var17 = 1.0D;
                }

                double var19 = 0.0D;
                double var21 = this.field_4242_g[var13] / 8000.0D;
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
                    double var28 = this.field_4245_d[var12] / 512.0D;
                    double var30 = this.field_4244_e[var12] / 512.0D;
                    double var32 = (this.field_4246_c[var12] / 10.0D + 1.0D) / 2.0D;
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
    public boolean chunkExists(int var1, int var2) {
        return true;
    }

    @Override
    public void populate(IChunkProvider var1, int var2, int var3) {
        BlockSand.fallInstantly = true;
        int var4 = var2 * 16;
        int var5 = var3 * 16;

        for (int var6 = 0; var6 < 8; ++var6) {
            int var7 = var4 + this.hellRNG.nextInt(16) + 8;
            int var8 = this.hellRNG.nextInt(120) + 4;
            int var9 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenHellLava(Block.LAVA_MOVING.blockID)).generate(this.worldObj, this.hellRNG, var7, var8, var9);
        }

        int var11 = this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1;

        for (int var13 = 0; var13 < var11; ++var13) {
            int var18 = var4 + this.hellRNG.nextInt(16) + 8;
            int var23 = this.hellRNG.nextInt(120) + 4;
            int var10 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFire()).generate(this.worldObj, this.hellRNG, var18, var23, var10);
        }

        var11 = this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1);

        for (int var14 = 0; var14 < var11; ++var14) {
            int var19 = var4 + this.hellRNG.nextInt(16) + 8;
            int var24 = this.hellRNG.nextInt(120) + 4;
            int var28 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone1()).generate(this.worldObj, this.hellRNG, var19, var24, var28);
        }

        for (int var15 = 0; var15 < 10; ++var15) {
            int var20 = var4 + this.hellRNG.nextInt(16) + 8;
            int var25 = this.hellRNG.nextInt(128);
            int var29 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone2()).generate(this.worldObj, this.hellRNG, var20, var25, var29);
        }

        if (this.hellRNG.nextInt(1) == 0) {
            int var16 = var4 + this.hellRNG.nextInt(16) + 8;
            int var21 = this.hellRNG.nextInt(128);
            int var26 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_BROWN.blockID)).generate(this.worldObj, this.hellRNG, var16, var21, var26);
        }

        if (this.hellRNG.nextInt(1) == 0) {
            int var17 = var4 + this.hellRNG.nextInt(16) + 8;
            int var22 = this.hellRNG.nextInt(128);
            int var27 = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.MUSHROOM_RED.blockID)).generate(this.worldObj, this.hellRNG, var17, var22, var27);
        }

        BlockSand.fallInstantly = false;
    }

    @Override
    public boolean saveChunks(boolean var1, IProgressUpdatable var2) {
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
}
