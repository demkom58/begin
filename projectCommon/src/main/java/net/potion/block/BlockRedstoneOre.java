package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.Entity;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.material.Material;
import net.potion.world.World;

import java.util.Random;

public class BlockRedstoneOre extends Block {
    private boolean field1;

    public BlockRedstoneOre(int var1, int var2, boolean var3) {
        super(var1, var2, Material.ROCK);
        if (var3) {
            this.setTickOnLoad(true);
        }

        this.field1 = var3;
    }

    @Override
    public int tickRate() {
        return 30;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.method1(world, x, y, z);
        super.onBlockClicked(world, x, y, z, player);
    }

    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        this.method1(world, x, y, z);
        super.onEntityWalking(world, x, y, z, entity);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        this.method1(world, x, y, z);
        return super.blockActivated(world, x, y, z, player);
    }

    private void method1(World var1, int var2, int var3, int var4) {
        this.method2(var1, var2, var3, var4);
        if (this.blockID == Block.ORE_REDSTONE.blockID) {
            var1.setBlockWithNotify(var2, var3, var4, Block.ORE_REDSTONE_GLOWING.blockID);
        }

    }

    @Override
    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
        if (this.blockID == Block.ORE_REDSTONE_GLOWING.blockID) {
            var1.setBlockWithNotify(var2, var3, var4, Block.ORE_REDSTONE.blockID);
        }

    }

    @Override
    public int idDropped(int var1, Random var2) {
        return Item.REDSTONE.shiftedIndex;
    }

    @Override
    public int quantityDropped(Random var1) {
        return 4 + var1.nextInt(2);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
        if (this.field1) {
            this.method2(var1, var2, var3, var4);
        }

    }

    private void method2(World var1, int var2, int var3, int var4) {
        Random var5 = var1.rand;
        double var6 = 0.0625D;

        for (int var8 = 0; var8 < 6; ++var8) {
            double var9 = (float) var2 + var5.nextFloat();
            double var11 = (float) var3 + var5.nextFloat();
            double var13 = (float) var4 + var5.nextFloat();
            if (var8 == 0 && !var1.isBlockOpaqueCube(var2, var3 + 1, var4)) {
                var11 = (double) (var3 + 1) + var6;
            }

            if (var8 == 1 && !var1.isBlockOpaqueCube(var2, var3 - 1, var4)) {
                var11 = (double) (var3) - var6;
            }

            if (var8 == 2 && !var1.isBlockOpaqueCube(var2, var3, var4 + 1)) {
                var13 = (double) (var4 + 1) + var6;
            }

            if (var8 == 3 && !var1.isBlockOpaqueCube(var2, var3, var4 - 1)) {
                var13 = (double) (var4) - var6;
            }

            if (var8 == 4 && !var1.isBlockOpaqueCube(var2 + 1, var3, var4)) {
                var9 = (double) (var2 + 1) + var6;
            }

            if (var8 == 5 && !var1.isBlockOpaqueCube(var2 - 1, var3, var4)) {
                var9 = (double) (var2) - var6;
            }

            if (var9 < (double) var2 || var9 > (double) (var2 + 1) || var11 < 0.0D || var11 > (double) (var3 + 1) || var13 < (double) var4 || var13 > (double) (var4 + 1)) {
                var1.spawnParticle("reddust", var9, var11, var13, 0.0D, 0.0D, 0.0D);
            }
        }

    }
}
