package net.minecraft.block;

import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFlowing extends BlockFluid {
    int field_659_a = 0;
    boolean[] field_658_b = new boolean[4];
    int[] field_660_c = new int[4];

    protected BlockFlowing(int var1, Material var2) {
        super(var1, var2);
    }

    private void func_30004_i(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        var1.setBlockAndMetadata(var2, var3, var4, this.blockID + 1, var5);
        var1.markBlocksDirty(var2, var3, var4, var2, var3, var4);
        var1.markBlockNeedsUpdate(var2, var3, var4);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        int var6 = this.func_301_g(world, x, y, z);
        byte var7 = 1;
        if (this.blockMaterial == Material.LAVA && !world.worldProvider.isHellWorld) {
            var7 = 2;
        }

        boolean var8 = true;
        if (var6 > 0) {
            int var9 = -100;
            this.field_659_a = 0;
            var9 = this.func_307_e(world, x - 1, y, z, var9);
            var9 = this.func_307_e(world, x + 1, y, z, var9);
            var9 = this.func_307_e(world, x, y, z - 1, var9);
            var9 = this.func_307_e(world, x, y, z + 1, var9);
            int var10 = var9 + var7;
            if (var10 >= 8 || var9 < 0) {
                var10 = -1;
            }

            if (this.func_301_g(world, x, y + 1, z) >= 0) {
                int var11 = this.func_301_g(world, x, y + 1, z);
                if (var11 >= 8) {
                    var10 = var11;
                } else {
                    var10 = var11 + 8;
                }
            }

            if (this.field_659_a >= 2 && this.blockMaterial == Material.WATER) {
                if (world.getBlockMaterial(x, y - 1, z).isSolid()) {
                    var10 = 0;
                } else if (world.getBlockMaterial(x, y - 1, z) == this.blockMaterial && world.getBlockMetadata(x, y, z) == 0) {
                    var10 = 0;
                }
            }

            if (this.blockMaterial == Material.LAVA && var10 < 8 && var10 > var6 && random.nextInt(4) != 0) {
                var10 = var6;
                var8 = false;
            }

            if (var10 != var6) {
                var6 = var10;
                if (var10 < 0) {
                    world.setBlockWithNotify(x, y, z, 0);
                } else {
                    world.setBlockMetadataWithNotify(x, y, z, var10);
                    world.scheduleUpdateTick(x, y, z, this.blockID, this.tickRate());
                    world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
                }
            } else if (var8) {
                this.func_30004_i(world, x, y, z);
            }
        } else {
            this.func_30004_i(world, x, y, z);
        }

        if (this.func_312_l(world, x, y - 1, z)) {
            if (var6 >= 8) {
                world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, var6);
            } else {
                world.setBlockAndMetadataWithNotify(x, y - 1, z, this.blockID, var6 + 8);
            }
        } else if (var6 >= 0 && (var6 == 0 || this.func_309_k(world, x, y - 1, z))) {
            boolean[] var16 = this.func_4035_j(world, x, y, z);
            int var17 = var6 + var7;
            if (var6 >= 8) {
                var17 = 1;
            }

            if (var17 >= 8) {
                return;
            }

            if (var16[0]) {
                this.func_311_f(world, x - 1, y, z, var17);
            }

            if (var16[1]) {
                this.func_311_f(world, x + 1, y, z, var17);
            }

            if (var16[2]) {
                this.func_311_f(world, x, y, z - 1, var17);
            }

            if (var16[3]) {
                this.func_311_f(world, x, y, z + 1, var17);
            }
        }

    }

    private void func_311_f(World var1, int var2, int var3, int var4, int var5) {
        if (this.func_312_l(var1, var2, var3, var4)) {
            int var6 = var1.getBlockId(var2, var3, var4);
            if (var6 > 0) {
                if (this.blockMaterial == Material.LAVA) {
                    this.func_300_h(var1, var2, var3, var4);
                } else {
                    Block.BLOCKS_LIST[var6].dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
                }
            }

            var1.setBlockAndMetadataWithNotify(var2, var3, var4, this.blockID, var5);
        }

    }

    private int func_4034_a(World var1, int var2, int var3, int var4, int var5, int var6) {
        int var7 = 1000;

        for (int var8 = 0; var8 < 4; ++var8) {
            if ((var8 != 0 || var6 != 1) && (var8 != 1 || var6 != 0) && (var8 != 2 || var6 != 3) && (var8 != 3 || var6 != 2)) {
                int var9 = var2;
                int var11 = var4;
                if (var8 == 0) {
                    var9 = var2 - 1;
                }

                if (var8 == 1) {
                    ++var9;
                }

                if (var8 == 2) {
                    var11 = var4 - 1;
                }

                if (var8 == 3) {
                    ++var11;
                }

                if (!this.func_309_k(var1, var9, var3, var11) && (var1.getBlockMaterial(var9, var3, var11) != this.blockMaterial || var1.getBlockMetadata(var9, var3, var11) != 0)) {
                    if (!this.func_309_k(var1, var9, var3 - 1, var11)) {
                        return var5;
                    }

                    if (var5 < 4) {
                        int var12 = this.func_4034_a(var1, var9, var3, var11, var5 + 1, var8);
                        if (var12 < var7) {
                            var7 = var12;
                        }
                    }
                }
            }
        }

        return var7;
    }

    private boolean[] func_4035_j(World var1, int var2, int var3, int var4) {
        for (int var5 = 0; var5 < 4; ++var5) {
            this.field_660_c[var5] = 1000;
            int var6 = var2;
            int var8 = var4;
            if (var5 == 0) {
                var6 = var2 - 1;
            }

            if (var5 == 1) {
                ++var6;
            }

            if (var5 == 2) {
                var8 = var4 - 1;
            }

            if (var5 == 3) {
                ++var8;
            }

            if (!this.func_309_k(var1, var6, var3, var8) && (var1.getBlockMaterial(var6, var3, var8) != this.blockMaterial || var1.getBlockMetadata(var6, var3, var8) != 0)) {
                if (!this.func_309_k(var1, var6, var3 - 1, var8)) {
                    this.field_660_c[var5] = 0;
                } else {
                    this.field_660_c[var5] = this.func_4034_a(var1, var6, var3, var8, 1, var5);
                }
            }
        }

        int var9 = this.field_660_c[0];

        for (int var10 = 1; var10 < 4; ++var10) {
            if (this.field_660_c[var10] < var9) {
                var9 = this.field_660_c[var10];
            }
        }

        for (int var11 = 0; var11 < 4; ++var11) {
            this.field_658_b[var11] = this.field_660_c[var11] == var9;
        }

        return this.field_658_b;
    }

    private boolean func_309_k(World var1, int var2, int var3, int var4) {
        int var5 = var1.getBlockId(var2, var3, var4);
        if (var5 != Block.DOOR_WOOD.blockID && var5 != Block.DOOR_STEEL.blockID && var5 != Block.SIGN.blockID && var5 != Block.LADDER.blockID && var5 != Block.REEDS.blockID) {
            if (var5 == 0) {
                return false;
            } else {
                Material var6 = Block.BLOCKS_LIST[var5].blockMaterial;
                return var6.getIsSolid();
            }
        } else {
            return true;
        }
    }

    protected int func_307_e(World var1, int var2, int var3, int var4, int var5) {
        int var6 = this.func_301_g(var1, var2, var3, var4);
        if (var6 < 0) {
            return var5;
        } else {
            if (var6 == 0) {
                ++this.field_659_a;
            }

            if (var6 >= 8) {
                var6 = 0;
            }

            return var5 >= 0 && var6 >= var5 ? var5 : var6;
        }
    }

    private boolean func_312_l(World var1, int var2, int var3, int var4) {
        Material var5 = var1.getBlockMaterial(var2, var3, var4);
        if (var5 == this.blockMaterial) {
            return false;
        } else if (var5 == Material.LAVA) {
            return false;
        } else {
            return !this.func_309_k(var1, var2, var3, var4);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (world.getBlockId(x, y, z) == this.blockID) {
            world.scheduleUpdateTick(x, y, z, this.blockID, this.tickRate());
        }

    }
}
