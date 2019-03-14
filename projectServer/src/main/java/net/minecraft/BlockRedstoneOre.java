package net.minecraft;

import java.util.Random;

public class BlockRedstoneOre extends Block {
    private boolean field_665_a;

    public BlockRedstoneOre(int var1, int var2, boolean var3) {
        super(var1, var2, Material.ROCK);
        if (var3) {
            this.setTickOnLoad(true);
        }

        this.field_665_a = var3;
    }

    public int tickRate() {
        return 30;
    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.func_321_g(world, var2, var3, var4);
        super.onBlockClicked(world, var2, var3, var4, entityPlayer);
    }

    public void onEntityWalking(World world, int var2, int var3, int var4, Entity entity) {
        this.func_321_g(world, var2, var3, var4);
        super.onEntityWalking(world, var2, var3, var4, entity);
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        this.func_321_g(world, var2, var3, var4);
        return super.blockActivated(world, var2, var3, var4, entityPlayer);
    }

    private void func_321_g(World var1, int var2, int var3, int var4) {
        this.func_320_h(var1, var2, var3, var4);
        if (this.blockID == Block.ORE_REDSTONE.blockID) {
            var1.setBlockWithNotify(var2, var3, var4, Block.ORE_REDSTONE_GLOWING.blockID);
        }

    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (this.blockID == Block.ORE_REDSTONE_GLOWING.blockID) {
            world.setBlockWithNotify(x, y, z, Block.ORE_REDSTONE.blockID);
        }

    }

    public int idDropped(int var1, Random random) {
        return Item.REDSTONE.shiftedIndex;
    }

    public int quantityDropped(Random random) {
        return 4 + random.nextInt(2);
    }

    private void func_320_h(World var1, int var2, int var3, int var4) {
        Random var5 = var1.rand;
        double var6 = 0.0625D;

        for (int var8 = 0; var8 < 6; ++var8) {
            double var9 = (double) ((float) var2 + var5.nextFloat());
            double var11 = (double) ((float) var3 + var5.nextFloat());
            double var13 = (double) ((float) var4 + var5.nextFloat());
            if (var8 == 0 && !var1.isBlockOpaqueCube(var2, var3 + 1, var4)) {
                var11 = (double) (var3 + 1) + var6;
            }

            if (var8 == 1 && !var1.isBlockOpaqueCube(var2, var3 - 1, var4)) {
                var11 = (double) (var3 + 0) - var6;
            }

            if (var8 == 2 && !var1.isBlockOpaqueCube(var2, var3, var4 + 1)) {
                var13 = (double) (var4 + 1) + var6;
            }

            if (var8 == 3 && !var1.isBlockOpaqueCube(var2, var3, var4 - 1)) {
                var13 = (double) (var4 + 0) - var6;
            }

            if (var8 == 4 && !var1.isBlockOpaqueCube(var2 + 1, var3, var4)) {
                var9 = (double) (var2 + 1) + var6;
            }

            if (var8 == 5 && !var1.isBlockOpaqueCube(var2 - 1, var3, var4)) {
                var9 = (double) (var2 + 0) - var6;
            }

            if (var9 < (double) var2 || var9 > (double) (var2 + 1) || var11 < 0.0D || var11 > (double) (var3 + 1) || var13 < (double) var4 || var13 > (double) (var4 + 1)) {
                var1.spawnParticle("reddust", var9, var11, var13, 0.0D, 0.0D, 0.0D);
            }
        }

    }
}
