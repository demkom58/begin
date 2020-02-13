package net.minecraft.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.AxisAlignedBB;

import java.util.Random;

public class BlockFire extends Block {
    private int[] chanceToEncourageFire = new int[256];
    private int[] abilityToCatchFire = new int[256];

    protected BlockFire(int var1, int var2) {
        super(var1, var2, Material.FIRE);
        this.setTickOnLoad(true);
    }

    @Override
    public void setFireBurnRates() {
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isACube() {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int tickRate() {
        return 40;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        boolean var6 = world.getBlockId(x, y - 1, z) == Block.BLOOD_STONE.blockID;
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            world.setBlockWithNotify(x, y, z, 0);
        }

        if (var6 || !world.func_27068_v() || !world.canLightningStrikeAt(x, y, z) && !world.canLightningStrikeAt(x - 1, y, z) && !world.canLightningStrikeAt(x + 1, y, z) && !world.canLightningStrikeAt(x, y, z - 1) && !world.canLightningStrikeAt(x, y, z + 1)) {
            int var7 = world.getBlockMetadata(x, y, z);
            if (var7 < 15) {
                world.setBlockMetadata(x, y, z, var7 + random.nextInt(3) / 2);
            }

            world.scheduleUpdateTick(x, y, z, this.blockID, this.tickRate());
            if (!var6 && !this.func_268_g(world, x, y, z)) {
                if (!world.isBlockNormalCube(x, y - 1, z) || var7 > 3) {
                    world.setBlockWithNotify(x, y, z, 0);
                }

            } else if (!var6 && !this.canBlockCatchFire(world, x, y - 1, z) && var7 == 15 && random.nextInt(4) == 0) {
                world.setBlockWithNotify(x, y, z, 0);
            } else {
                this.tryToCatchBlockOnFire(world, x + 1, y, z, 300, random, var7);
                this.tryToCatchBlockOnFire(world, x - 1, y, z, 300, random, var7);
                this.tryToCatchBlockOnFire(world, x, y - 1, z, 250, random, var7);
                this.tryToCatchBlockOnFire(world, x, y + 1, z, 250, random, var7);
                this.tryToCatchBlockOnFire(world, x, y, z - 1, 300, random, var7);
                this.tryToCatchBlockOnFire(world, x, y, z + 1, 300, random, var7);

                for (int var8 = x - 1; var8 <= x + 1; ++var8) {
                    for (int var9 = z - 1; var9 <= z + 1; ++var9) {
                        for (int var10 = y - 1; var10 <= y + 4; ++var10) {
                            if (var8 != x || var10 != y || var9 != z) {
                                int var11 = 100;
                                if (var10 > y + 1) {
                                    var11 += (var10 - (y + 1)) * 100;
                                }

                                int var12 = this.getChanceOfNeighborsEncouragingFire(world, var8, var10, var9);
                                if (var12 > 0) {
                                    int var13 = (var12 + 40) / (var7 + 30);
                                    if (var13 > 0 && random.nextInt(var11) <= var13 && (!world.func_27068_v() || !world.canLightningStrikeAt(var8, var10, var9)) && !world.canLightningStrikeAt(var8 - 1, var10, z) && !world.canLightningStrikeAt(var8 + 1, var10, var9) && !world.canLightningStrikeAt(var8, var10, var9 - 1) && !world.canLightningStrikeAt(var8, var10, var9 + 1)) {
                                        int var14 = var7 + random.nextInt(5) / 4;
                                        if (var14 > 15) {
                                            var14 = 15;
                                        }

                                        world.setBlockAndMetadataWithNotify(var8, var10, var9, this.blockID, var14);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else {
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

    private void tryToCatchBlockOnFire(World var1, int var2, int var3, int var4, int var5, Random var6, int var7) {
        int var8 = this.abilityToCatchFire[var1.getBlockId(var2, var3, var4)];
        if (var6.nextInt(var5) < var8) {
            boolean var9 = var1.getBlockId(var2, var3, var4) == Block.TNT.blockID;
            if (var6.nextInt(var7 + 10) < 5 && !var1.canLightningStrikeAt(var2, var3, var4)) {
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

    private boolean func_268_g(World var1, int var2, int var3, int var4) {
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
    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return world.isBlockNormalCube(var2, var3 - 1, var4) || this.func_268_g(world, var2, var3, var4);
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!world.isBlockNormalCube(var2, var3 - 1, var4) && !this.func_268_g(world, var2, var3, var4)) {
            world.setBlockWithNotify(var2, var3, var4, 0);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (world.getBlockId(x, y - 1, z) != Block.OBSIDIAN.blockID || !Block.PORTAL.tryToCreatePortal(world, x, y, z)) {
            if (!world.isBlockNormalCube(x, y - 1, z) && !this.func_268_g(world, x, y, z)) {
                world.setBlockWithNotify(x, y, z, 0);
            } else {
                world.scheduleUpdateTick(x, y, z, this.blockID, this.tickRate());
            }
        }
    }
}
