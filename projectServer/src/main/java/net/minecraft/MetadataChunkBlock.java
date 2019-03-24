package net.minecraft;

public class MetadataChunkBlock {
    public final EnumSkyBlock field_957_a;
    public int field_956_b;
    public int field_962_c;
    public int field_961_d;
    public int field_960_e;
    public int field_959_f;
    public int field_958_g;

    public MetadataChunkBlock(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.field_957_a = var1;
        this.field_956_b = var2;
        this.field_962_c = var3;
        this.field_961_d = var4;
        this.field_960_e = var5;
        this.field_959_f = var6;
        this.field_958_g = var7;
    }

    public void func_4107_a(World var1) {
        int var2 = this.field_960_e - this.field_956_b + 1;
        int var3 = this.field_959_f - this.field_962_c + 1;
        int var4 = this.field_958_g - this.field_961_d + 1;
        int var5 = var2 * var3 * var4;

        if (var5 > 32768) {
            System.out.println("Light too large, skipping!");
            return;
        }

        int var6 = 0;
        int var7 = 0;
        boolean var8 = false;
        boolean var9 = false;

        for (int x = this.field_956_b; x <= this.field_960_e; ++x) {
            for (int z = this.field_961_d; z <= this.field_958_g; ++z) {
                int var12 = x >> 4;
                int var13 = z >> 4;
                boolean var14 = false;

                if (var8 && var12 == var6 && var13 == var7) {
                    var14 = var9;
                } else {
                    var14 = var1.doChunksNearChunkExist(x, 0, z, 1);
                    if (var14) {
                        Chunk var15 = var1.getChunkFromChunkCoords(x >> 4, z >> 4);
                        if (var15.func_21101_g()) {
                            var14 = false;
                        }
                    }

                    var9 = var14;
                    var6 = var12;
                    var7 = var13;
                }

                if (var14) {
                    if (this.field_962_c < 0) {
                        this.field_962_c = 0;
                    }

                    if (this.field_959_f >= 128) {
                        this.field_959_f = 127;
                    }

                    for (int var28 = this.field_962_c; var28 <= this.field_959_f; ++var28) {
                        int var16 = var1.getSavedLightValue(this.field_957_a, x, var28, z);
                        int var17 = 0;
                        int var18 = var1.getBlockId(x, var28, z);
                        int var19 = Block.LIGHT_OPACITY[var18];
                        if (var19 == 0) {
                            var19 = 1;
                        }

                        int var20 = 0;
                        if (this.field_957_a == EnumSkyBlock.SKY) {
                            if (var1.canExistingBlockSeeTheSky(x, var28, z)) {
                                var20 = 15;
                            }
                        } else if (this.field_957_a == EnumSkyBlock.BLOCK) {
                            var20 = Block.LIGHT_VALUE[var18];
                        }

                        if (var19 >= 15 && var20 == 0) {
                            var17 = 0;
                        } else {
                            int var21 = var1.getSavedLightValue(this.field_957_a, x - 1, var28, z);
                            int var22 = var1.getSavedLightValue(this.field_957_a, x + 1, var28, z);
                            int var23 = var1.getSavedLightValue(this.field_957_a, x, var28 - 1, z);
                            int var24 = var1.getSavedLightValue(this.field_957_a, x, var28 + 1, z);
                            int var25 = var1.getSavedLightValue(this.field_957_a, x, var28, z - 1);
                            int var26 = var1.getSavedLightValue(this.field_957_a, x, var28, z + 1);
                            var17 = var21;
                            if (var22 > var21) {
                                var17 = var22;
                            }

                            if (var23 > var17) {
                                var17 = var23;
                            }

                            if (var24 > var17) {
                                var17 = var24;
                            }

                            if (var25 > var17) {
                                var17 = var25;
                            }

                            if (var26 > var17) {
                                var17 = var26;
                            }

                            var17 = var17 - var19;
                            if (var17 < 0) {
                                var17 = 0;
                            }

                            if (var20 > var17) {
                                var17 = var20;
                            }
                        }

                        if (var16 != var17) {
                            var1.setLightValue(this.field_957_a, x, var28, z, var17);
                            int var31 = var17 - 1;
                            if (var31 < 0) {
                                var31 = 0;
                            }

                            var1.neighborLightPropagationChanged(this.field_957_a, x - 1, var28, z, var31);
                            var1.neighborLightPropagationChanged(this.field_957_a, x, var28 - 1, z, var31);
                            var1.neighborLightPropagationChanged(this.field_957_a, x, var28, z - 1, var31);
                            if (x + 1 >= this.field_960_e) {
                                var1.neighborLightPropagationChanged(this.field_957_a, x + 1, var28, z, var31);
                            }

                            if (var28 + 1 >= this.field_959_f) {
                                var1.neighborLightPropagationChanged(this.field_957_a, x, var28 + 1, z, var31);
                            }

                            if (z + 1 >= this.field_958_g) {
                                var1.neighborLightPropagationChanged(this.field_957_a, x, var28, z + 1, var31);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean func_692_a(int var1, int var2, int var3, int var4, int var5, int var6) {
        if (var1 >= this.field_956_b && var2 >= this.field_962_c && var3 >= this.field_961_d && var4 <= this.field_960_e && var5 <= this.field_959_f && var6 <= this.field_958_g) {
            return true;
        } else {
            byte var7 = 1;
            if (var1 >= this.field_956_b - var7 && var2 >= this.field_962_c - var7 && var3 >= this.field_961_d - var7 && var4 <= this.field_960_e + var7 && var5 <= this.field_959_f + var7 && var6 <= this.field_958_g + var7) {
                int var8 = this.field_960_e - this.field_956_b;
                int var9 = this.field_959_f - this.field_962_c;
                int var10 = this.field_958_g - this.field_961_d;
                if (var1 > this.field_956_b) {
                    var1 = this.field_956_b;
                }

                if (var2 > this.field_962_c) {
                    var2 = this.field_962_c;
                }

                if (var3 > this.field_961_d) {
                    var3 = this.field_961_d;
                }

                if (var4 < this.field_960_e) {
                    var4 = this.field_960_e;
                }

                if (var5 < this.field_959_f) {
                    var5 = this.field_959_f;
                }

                if (var6 < this.field_958_g) {
                    var6 = this.field_958_g;
                }

                int var11 = var4 - var1;
                int var12 = var5 - var2;
                int var13 = var6 - var3;
                int var14 = var8 * var9 * var10;
                int var15 = var11 * var12 * var13;
                if (var15 - var14 <= 2) {
                    this.field_956_b = var1;
                    this.field_962_c = var2;
                    this.field_961_d = var3;
                    this.field_960_e = var4;
                    this.field_959_f = var5;
                    this.field_958_g = var6;
                    return true;
                }
            }

            return false;
        }
    }
}
