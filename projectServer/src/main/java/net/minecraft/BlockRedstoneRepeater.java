package net.minecraft;

import util.MathHelper;

import java.util.Random;

public class BlockRedstoneRepeater extends Block {
    public static final double[] field_22014_a = new double[]{-0.0625D, 0.0625D, 0.1875D, 0.3125D};
    private static final int[] field_22013_b = new int[]{1, 2, 3, 4};
    private final boolean field_22015_c;

    protected BlockRedstoneRepeater(int var1, boolean var2) {
        super(var1, 6, Material.CIRCUITS);
        this.field_22015_c = var2;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean isACube() {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int var2, int var3, int var4) {
        return world.isBlockNormalCube(var2, var3 - 1, var4) && super.canPlaceBlockAt(world, var2, var3, var4);
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.isBlockNormalCube(x, y - 1, z) && super.canBlockStay(world, x, y, z);
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        int var6 = world.getBlockMetadata(x, y, z);
        boolean var7 = this.func_22012_g(world, x, y, z, var6);
        if (this.field_22015_c && !var7) {
            world.setBlockAndMetadataWithNotify(x, y, z, Block.REDSTONE_REPEATER_IDLE.blockID, var6);
        } else if (!this.field_22015_c) {
            world.setBlockAndMetadataWithNotify(x, y, z, Block.REDSTONE_REPEATER_ACTIVE.blockID, var6);
            if (!var7) {
                int var8 = (var6 & 12) >> 2;
                world.scheduleUpdateTick(x, y, z, Block.REDSTONE_REPEATER_ACTIVE.blockID, field_22013_b[var8] * 2);
            }
        }

    }

    public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
        if (var1 == 0) {
            return this.field_22015_c ? 99 : 115;
        } else if (var1 == 1) {
            return this.field_22015_c ? 147 : 131;
        } else {
            return 5;
        }
    }

    public int getBlockTextureFromSide(int var1) {
        return this.getBlockTextureFromSideAndMetadata(var1, 0);
    }

    public boolean isIndirectlyPoweringTo(World world, int var2, int var3, int var4, int var5) {
        return this.isPoweringTo(world, var2, var3, var4, var5);
    }

    public boolean isPoweringTo(IBlockAccess blockAccess, int var2, int var3, int var4, int var5) {
        if (!this.field_22015_c) {
            return false;
        } else {
            int var6 = blockAccess.getBlockMetadata(var2, var3, var4) & 3;
            if (var6 == 0 && var5 == 3) {
                return true;
            } else if (var6 == 1 && var5 == 4) {
                return true;
            } else if (var6 == 2 && var5 == 2) {
                return true;
            } else {
                return var6 == 3 && var5 == 5;
            }
        }
    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (!this.canBlockStay(world, var2, var3, var4)) {
            this.dropBlockAsItem(world, var2, var3, var4, world.getBlockMetadata(var2, var3, var4));
            world.setBlockWithNotify(var2, var3, var4, 0);
        } else {
            int var6 = world.getBlockMetadata(var2, var3, var4);
            boolean var7 = this.func_22012_g(world, var2, var3, var4, var6);
            int var8 = (var6 & 12) >> 2;
            if (this.field_22015_c && !var7) {
                world.scheduleUpdateTick(var2, var3, var4, this.blockID, field_22013_b[var8] * 2);
            } else if (!this.field_22015_c && var7) {
                world.scheduleUpdateTick(var2, var3, var4, this.blockID, field_22013_b[var8] * 2);
            }

        }
    }

    private boolean func_22012_g(World var1, int var2, int var3, int var4, int var5) {
        int var6 = var5 & 3;
        switch (var6) {
            case 0:
                return var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4 + 1, 3) || var1.getBlockId(var2, var3, var4 + 1) == Block.REDSTONE_WIRE.blockID && var1.getBlockMetadata(var2, var3, var4 + 1) > 0;
            case 1:
                return var1.isBlockIndirectlyProvidingPowerTo(var2 - 1, var3, var4, 4) || var1.getBlockId(var2 - 1, var3, var4) == Block.REDSTONE_WIRE.blockID && var1.getBlockMetadata(var2 - 1, var3, var4) > 0;
            case 2:
                return var1.isBlockIndirectlyProvidingPowerTo(var2, var3, var4 - 1, 2) || var1.getBlockId(var2, var3, var4 - 1) == Block.REDSTONE_WIRE.blockID && var1.getBlockMetadata(var2, var3, var4 - 1) > 0;
            case 3:
                return var1.isBlockIndirectlyProvidingPowerTo(var2 + 1, var3, var4, 5) || var1.getBlockId(var2 + 1, var3, var4) == Block.REDSTONE_WIRE.blockID && var1.getBlockMetadata(var2 + 1, var3, var4) > 0;
            default:
                return false;
        }
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        int var6 = world.getBlockMetadata(var2, var3, var4);
        int var7 = (var6 & 12) >> 2;
        var7 = var7 + 1 << 2 & 12;
        world.setBlockMetadataWithNotify(var2, var3, var4, var7 | var6 & 3);
        return true;
    }

    public boolean canProvidePower() {
        return false;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int var6 = ((MathHelper.floor_double((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
        world.setBlockMetadataWithNotify(x, y, z, var6);
        boolean var7 = this.func_22012_g(world, x, y, z, var6);
        if (var7) {
            world.scheduleUpdateTick(x, y, z, this.blockID, 1);
        }

    }

    public void onBlockAdded(World world, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int idDropped(int var1, Random random) {
        return Item.REDSTONE_REPEATER.shiftedIndex;
    }
}
