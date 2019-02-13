package net.minecraft;

import util.Vec3D;

import java.util.Random;

public abstract class BlockFluid extends Block {
    protected BlockFluid(int var1, Material var2) {
        super(var1, (var2 == Material.lava ? 14 : 12) * 16 + 13, var2);
        float var3 = 0.0F;
        float var4 = 0.0F;
        this.setBlockBounds(0.0F + var4, 0.0F + var3, 0.0F + var4, 1.0F + var4, 1.0F + var3, 1.0F + var4);
        this.setTickOnLoad(true);
    }

    public static float setFluidHeight(int var0) {
        if (var0 >= 8) {
            var0 = 0;
        }

        float var1 = (float) (var0 + 1) / 9.0F;
        return var1;
    }

    public int getBlockTextureFromSide(int var1) {
        return var1 != 0 && var1 != 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
    }

    protected int func_301_g(World var1, int var2, int var3, int var4) {
        return var1.getBlockMaterial(var2, var3, var4) != this.blockMaterial ? -1 : var1.getBlockMetadata(var2, var3, var4);
    }

    protected int func_303_b(IBlockAccess var1, int var2, int var3, int var4) {
        if (var1.getBlockMaterial(var2, var3, var4) != this.blockMaterial) {
            return -1;
        } else {
            int var5 = var1.getBlockMetadata(var2, var3, var4);
            if (var5 >= 8) {
                var5 = 0;
            }

            return var5;
        }
    }

    public boolean isACube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canCollideCheck(int var1, boolean var2) {
        return var2 && var1 == 0;
    }

    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int var5) {
        Material var6 = blockAccess.getBlockMaterial(x, y, z);
        if (var6 == this.blockMaterial) {
            return false;
        } else if (var6 == Material.ice) {
            return false;
        } else {
            return var5 == 1 || super.shouldSideBeRendered(blockAccess, x, y, z, var5);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public int idDropped(int var1, Random random) {
        return 0;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    private Vec3D func_298_c(IBlockAccess var1, int var2, int var3, int var4) {
        Vec3D var5 = Vec3D.createVector(0.0D, 0.0D, 0.0D);
        int var6 = this.func_303_b(var1, var2, var3, var4);

        for (int var7 = 0; var7 < 4; ++var7) {
            int var8 = var2;
            int var10 = var4;
            if (var7 == 0) {
                var8 = var2 - 1;
            }

            if (var7 == 1) {
                var10 = var4 - 1;
            }

            if (var7 == 2) {
                ++var8;
            }

            if (var7 == 3) {
                ++var10;
            }

            int var11 = this.func_303_b(var1, var8, var3, var10);
            if (var11 < 0) {
                if (!var1.getBlockMaterial(var8, var3, var10).getIsSolid()) {
                    var11 = this.func_303_b(var1, var8, var3 - 1, var10);
                    if (var11 >= 0) {
                        int var12 = var11 - (var6 - 8);
                        var5 = var5.addVector((double) ((var8 - var2) * var12), (double) ((var3 - var3) * var12), (double) ((var10 - var4) * var12));
                    }
                }
            } else if (var11 >= 0) {
                int var16 = var11 - var6;
                var5 = var5.addVector((double) ((var8 - var2) * var16), (double) ((var3 - var3) * var16), (double) ((var10 - var4) * var16));
            }
        }

        if (var1.getBlockMetadata(var2, var3, var4) >= 8) {
            boolean var14 = false;
            if (var14 || this.shouldSideBeRendered(var1, var2, var3, var4 - 1, 2)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2, var3, var4 + 1, 3)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2 - 1, var3, var4, 4)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2 + 1, var3, var4, 5)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2, var3 + 1, var4 - 1, 2)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2, var3 + 1, var4 + 1, 3)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2 - 1, var3 + 1, var4, 4)) {
                var14 = true;
            }

            if (var14 || this.shouldSideBeRendered(var1, var2 + 1, var3 + 1, var4, 5)) {
                var14 = true;
            }

            if (var14) {
                var5 = var5.normalize().addVector(0.0D, -6.0D, 0.0D);
            }
        }

        var5 = var5.normalize();
        return var5;
    }

    public void velocityToAddToEntity(World world, int var2, int var3, int var4, Entity entity, Vec3D vec) {
        Vec3D var7 = this.func_298_c(world, var2, var3, var4);
        vec.xCoord += var7.xCoord;
        vec.yCoord += var7.yCoord;
        vec.zCoord += var7.zCoord;
    }

    public int tickRate() {
        if (this.blockMaterial == Material.water) {
            return 5;
        } else {
            return this.blockMaterial == Material.lava ? 30 : 0;
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.checkForHarden(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        this.checkForHarden(world, var2, var3, var4);
    }

    private void checkForHarden(World var1, int var2, int var3, int var4) {
        if (var1.getBlockId(var2, var3, var4) == this.blockID) {
            if (this.blockMaterial == Material.lava) {
                boolean var5 = false;
                if (var5 || var1.getBlockMaterial(var2, var3, var4 - 1) == Material.water) {
                    var5 = true;
                }

                if (var5 || var1.getBlockMaterial(var2, var3, var4 + 1) == Material.water) {
                    var5 = true;
                }

                if (var5 || var1.getBlockMaterial(var2 - 1, var3, var4) == Material.water) {
                    var5 = true;
                }

                if (var5 || var1.getBlockMaterial(var2 + 1, var3, var4) == Material.water) {
                    var5 = true;
                }

                if (var5 || var1.getBlockMaterial(var2, var3 + 1, var4) == Material.water) {
                    var5 = true;
                }

                if (var5) {
                    int var6 = var1.getBlockMetadata(var2, var3, var4);
                    if (var6 == 0) {
                        var1.setBlockWithNotify(var2, var3, var4, Block.OBSIDIAN.blockID);
                    } else if (var6 <= 4) {
                        var1.setBlockWithNotify(var2, var3, var4, Block.COBBLESTONE.blockID);
                    }

                    this.func_300_h(var1, var2, var3, var4);
                }
            }

        }
    }

    protected void func_300_h(World var1, int var2, int var3, int var4) {
        var1.playSoundEffect((double) ((float) var2 + 0.5F), (double) ((float) var3 + 0.5F), (double) ((float) var4 + 0.5F), "random.fizz", 0.5F, 2.6F + (var1.rand.nextFloat() - var1.rand.nextFloat()) * 0.8F);

        for (int var5 = 0; var5 < 8; ++var5) {
            var1.spawnParticle("largesmoke", (double) var2 + Math.random(), (double) var3 + 1.2D, (double) var4 + Math.random(), 0.0D, 0.0D, 0.0D);
        }

    }
}
