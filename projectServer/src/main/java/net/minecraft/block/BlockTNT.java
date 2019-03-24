package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityTNTPrimed;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTNT extends Block {
    public BlockTNT(int var1, int var2) {
        super(var1, var2, Material.TNT);
    }

    public int getBlockTextureFromSide(int var1) {
        if (var1 == 0) {
            return this.blockIndexInTexture + 2;
        } else {
            return var1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            world.setBlockWithNotify(x, y, z, 0);
        }

    }

    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower() && world.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            this.onBlockDestroyedByPlayer(world, var2, var3, var4, 1);
            world.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
        EntityTNTPrimed var5 = new EntityTNTPrimed(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F));
        var5.fuse = world.rand.nextInt(var5.fuse / 4) + var5.fuse / 8;
        world.entityJoinedWorld(var5);
    }

    public void onBlockDestroyedByPlayer(World world, int var2, int var3, int var4, int var5) {
        if (!world.singleplayerWorld) {
            if ((var5 & 1) == 0) {
                this.dropBlockAsItem_do(world, var2, var3, var4, new ItemStack(Block.TNT.blockID, 1, 0));
            } else {
                EntityTNTPrimed var6 = new EntityTNTPrimed(world, (double) ((float) var2 + 0.5F), (double) ((float) var3 + 0.5F), (double) ((float) var4 + 0.5F));
                world.entityJoinedWorld(var6);
                world.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
            }

        }
    }

    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (entityPlayer.getCurrentEquippedItem() != null && entityPlayer.getCurrentEquippedItem().itemID == Item.FLINT_AND_STEEL.shiftedIndex) {
            world.setBlockMetadata(var2, var3, var4, 1);
        }

        super.onBlockClicked(world, var2, var3, var4, entityPlayer);
    }

    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        return super.blockActivated(world, var2, var3, var4, entityPlayer);
    }
}
