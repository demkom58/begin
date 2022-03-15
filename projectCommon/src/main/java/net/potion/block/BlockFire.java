package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.material.Material;
import net.potion.util.AxisAlignedBB;
import net.potion.world.IBlockAccess;
import net.potion.world.World;

import java.util.Random;

public class BlockFire extends Block {
    private final int[] chanceToEncourageFire = new int[256];
    private final int[] abilityToCatchFire = new int[256];

    protected BlockFire(int var1, int var2) {
        super(var1, var2, Material.FIRE);
        this.setTickOnLoad(true);
    }

    @Override
    public void initializeBlock() {
        this.setBurnRate(Block.PLANKS.blockID, 5, 20);
        this.setBurnRate(Block.FENCE.blockID, 5, 20);
        this.setBurnRate(Block.STAIR_COMPACT_PLANKS.blockID, 5, 20);
        this.setBurnRate(Block.WOOD.blockID, 5, 5);
        this.setBurnRate(Block.LEAVES.blockID, 30, 60);
        this.setBurnRate(Block.BOOKSHELF.blockID, 30, 20);
        this.setBurnRate(Block.TNT.blockID, 15, 100);
        this.setBurnRate(Block.TALLGRASS.blockID, 60, 100);
        this.setBurnRate(Block.CLOTH.blockID, 30, 60);
    }

    private void setBurnRate(int var1, int var2, int var3) {
        this.chanceToEncourageFire[var1] = var2;
        this.abilityToCatchFire[var1] = var3;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getRenderType() {
        return 3;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public int tickRate() {
        return 40;
    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        boolean var6 = var1.getBlockId(var2, var3 - 1, var4) == Block.BLOOD_STONE.blockID;
        if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

        if (var6 || !var1.isSmallRain() || !var1.canBlockBeRainedOn(var2, var3, var4) && !var1.canBlockBeRainedOn(var2 - 1, var3, var4) && !var1.canBlockBeRainedOn(var2 + 1, var3, var4) && !var1.canBlockBeRainedOn(var2, var3, var4 - 1) && !var1.canBlockBeRainedOn(var2, var3, var4 + 1)) {
            int var7 = var1.getBlockMetadata(var2, var3, var4);
            if (var7 < 15) {
                var1.setBlockMetadata(var2, var3, var4, var7 + var5.nextInt(3) / 2);
            }

            var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
            if (!var6 && !this.canSpreadFire(var1, var2, var3, var4)) {
                if (!var1.isBlockNormalCube(var2, var3 - 1, var4) || var7 > 3) {
                    var1.setBlockWithNotify(var2, var3, var4, 0);
                }

            } else if (!var6 && !this.canBlockCatchFire(var1, var2, var3 - 1, var4) && var7 == 15 && var5.nextInt(4) == 0) {
                var1.setBlockWithNotify(var2, var3, var4, 0);
            } else {
                this.tryToCatchBlockOnFire(var1, var2 + 1, var3, var4, 300, var5, var7);
                this.tryToCatchBlockOnFire(var1, var2 - 1, var3, var4, 300, var5, var7);
                this.tryToCatchBlockOnFire(var1, var2, var3 - 1, var4, 250, var5, var7);
                this.tryToCatchBlockOnFire(var1, var2, var3 + 1, var4, 250, var5, var7);
                this.tryToCatchBlockOnFire(var1, var2, var3, var4 - 1, 300, var5, var7);
                this.tryToCatchBlockOnFire(var1, var2, var3, var4 + 1, 300, var5, var7);

                for (int var8 = var2 - 1; var8 <= var2 + 1; ++var8) {
                    for (int var9 = var4 - 1; var9 <= var4 + 1; ++var9) {
                        for (int var10 = var3 - 1; var10 <= var3 + 4; ++var10) {
                            if (var8 != var2 || var10 != var3 || var9 != var4) {
                                int var11 = 100;
                                if (var10 > var3 + 1) {
                                    var11 += (var10 - (var3 + 1)) * 100;
                                }

                                int var12 = this.getChanceOfNeighborsEncouragingFire(var1, var8, var10, var9);
                                if (var12 > 0) {
                                    int var13 = (var12 + 40) / (var7 + 30);
                                    if (var13 > 0 && var5.nextInt(var11) <= var13 && (!var1.isSmallRain() || !var1.canBlockBeRainedOn(var8, var10, var9)) && !var1.canBlockBeRainedOn(var8 - 1, var10, var4) && !var1.canBlockBeRainedOn(var8 + 1, var10, var9) && !var1.canBlockBeRainedOn(var8, var10, var9 - 1) && !var1.canBlockBeRainedOn(var8, var10, var9 + 1)) {
                                        int var14 = var7 + var5.nextInt(5) / 4;
                                        if (var14 > 15) {
                                            var14 = 15;
                                        }

                                        var1.setBlockAndMetadataWithNotify(var8, var10, var9, this.blockID, var14);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else {
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }
    }

    private void tryToCatchBlockOnFire(World var1, int var2, int var3, int var4, int var5, Random var6, int var7) {
        int var8 = this.abilityToCatchFire[var1.getBlockId(var2, var3, var4)];
        if (var6.nextInt(var5) < var8) {
            boolean var9 = var1.getBlockId(var2, var3, var4) == Block.TNT.blockID;
            if (var6.nextInt(var7 + 10) < 5 && !var1.canBlockBeRainedOn(var2, var3, var4)) {
                int var10 = var7 + var6.nextInt(5) / 4;
                if (var10 > 15) {
                    var10 = 15;
                }

                var1.setBlockAndMetadataWithNotify(var2, var3, var4, this.blockID, var10);
            } else {
                var1.setBlockWithNotify(var2, var3, var4, 0);
            }

            if (var9) {
                Block.TNT.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
            }
        }

    }

    private boolean canSpreadFire(World var1, int var2, int var3, int var4) {
        if (this.canBlockCatchFire(var1, var2 + 1, var3, var4)) {
            return true;
        } else if (this.canBlockCatchFire(var1, var2 - 1, var3, var4)) {
            return true;
        } else if (this.canBlockCatchFire(var1, var2, var3 - 1, var4)) {
            return true;
        } else if (this.canBlockCatchFire(var1, var2, var3 + 1, var4)) {
            return true;
        } else if (this.canBlockCatchFire(var1, var2, var3, var4 - 1)) {
            return true;
        } else {
            return this.canBlockCatchFire(var1, var2, var3, var4 + 1);
        }
    }

    private int getChanceOfNeighborsEncouragingFire(World var1, int var2, int var3, int var4) {
        int var5 = 0;
        if (!var1.isAirBlock(var2, var3, var4)) {
            return 0;
        } else {
            var5 = this.getChanceToEncourageFire(var1, var2 + 1, var3, var4, var5);
            var5 = this.getChanceToEncourageFire(var1, var2 - 1, var3, var4, var5);
            var5 = this.getChanceToEncourageFire(var1, var2, var3 - 1, var4, var5);
            var5 = this.getChanceToEncourageFire(var1, var2, var3 + 1, var4, var5);
            var5 = this.getChanceToEncourageFire(var1, var2, var3, var4 - 1, var5);
            var5 = this.getChanceToEncourageFire(var1, var2, var3, var4 + 1, var5);
            return var5;
        }
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public boolean canBlockCatchFire(IBlockAccess var1, int var2, int var3, int var4) {
        return this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)] > 0;
    }

    public int getChanceToEncourageFire(World var1, int var2, int var3, int var4, int var5) {
        int var6 = this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)];
        return Math.max(var6, var5);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isBlockNormalCube(x, y - 1, z) || this.canSpreadFire(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (!var1.isBlockNormalCube(var2, var3 - 1, var4) && !this.canSpreadFire(var1, var2, var3, var4)) {
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        if (var1.getBlockId(var2, var3 - 1, var4) != Block.OBSIDIAN.blockID || !Block.PORTAL.tryToCreatePortal(var1, var2, var3, var4)) {
            if (!var1.isBlockNormalCube(var2, var3 - 1, var4) && !this.canSpreadFire(var1, var2, var3, var4)) {
                var1.setBlockWithNotify(var2, var3, var4, 0);
            } else {
                var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
            }
        }
    }

    @Side(CodeSide.CLIENT)
    @Override
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        if (var5.nextInt(24) == 0) {
            var1.playSoundEffect((float) var2 + 0.5F, (float) var3 + 0.5F, (float) var4 + 0.5F, "fire.fire", 1.0F + var5.nextFloat(), var5.nextFloat() * 0.7F + 0.3F);
        }

        if (!var1.isBlockNormalCube(var2, var3 - 1, var4) && !Block.FIRE.canBlockCatchFire(var1, var2, var3 - 1, var4)) {
            if (Block.FIRE.canBlockCatchFire(var1, var2 - 1, var3, var4)) {
                for (int var10 = 0; var10 < 2; ++var10) {
                    float var15 = (float) var2 + var5.nextFloat() * 0.1F;
                    float var20 = (float) var3 + var5.nextFloat();
                    float var25 = (float) var4 + var5.nextFloat();
                    var1.spawnParticle("largesmoke", var15, var20, var25, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.FIRE.canBlockCatchFire(var1, var2 + 1, var3, var4)) {
                for (int var11 = 0; var11 < 2; ++var11) {
                    float var16 = (float) (var2 + 1) - var5.nextFloat() * 0.1F;
                    float var21 = (float) var3 + var5.nextFloat();
                    float var26 = (float) var4 + var5.nextFloat();
                    var1.spawnParticle("largesmoke", var16, var21, var26, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.FIRE.canBlockCatchFire(var1, var2, var3, var4 - 1)) {
                for (int var12 = 0; var12 < 2; ++var12) {
                    float var17 = (float) var2 + var5.nextFloat();
                    float var22 = (float) var3 + var5.nextFloat();
                    float var27 = (float) var4 + var5.nextFloat() * 0.1F;
                    var1.spawnParticle("largesmoke", var17, var22, var27, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.FIRE.canBlockCatchFire(var1, var2, var3, var4 + 1)) {
                for (int var13 = 0; var13 < 2; ++var13) {
                    float var18 = (float) var2 + var5.nextFloat();
                    float var23 = (float) var3 + var5.nextFloat();
                    float var28 = (float) (var4 + 1) - var5.nextFloat() * 0.1F;
                    var1.spawnParticle("largesmoke", var18, var23, var28, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.FIRE.canBlockCatchFire(var1, var2, var3 + 1, var4)) {
                for (int var14 = 0; var14 < 2; ++var14) {
                    float var19 = (float) var2 + var5.nextFloat();
                    float var24 = (float) (var3 + 1) - var5.nextFloat() * 0.1F;
                    float var29 = (float) var4 + var5.nextFloat();
                    var1.spawnParticle("largesmoke", var19, var24, var29, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            for (int var6 = 0; var6 < 3; ++var6) {
                float var7 = (float) var2 + var5.nextFloat();
                float var8 = (float) var3 + var5.nextFloat() * 0.5F + 0.5F;
                float var9 = (float) var4 + var5.nextFloat();
                var1.spawnParticle("largesmoke", var7, var8, var9, 0.0D, 0.0D, 0.0D);
            }
        }

    }
}
