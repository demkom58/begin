package net.potion.block;

import net.potion.entity.EntityTNTPrimed;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.Item;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.world.World;

import java.util.Random;

public class BlockTNT extends Block {
    public BlockTNT(int var1, int var2) {
        super(var1, var2, Material.TNT);
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if (side == 0) {
            return this.blockIndexInTexture + 2;
        } else {
            return side == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4) {
        super.onBlockAdded(var1, var2, var3, var4);
        if (var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            this.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
        if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower() && var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            this.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
            var1.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    @Override
    public int quantityDropped(Random var1) {
        return 0;
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z) {
        EntityTNTPrimed var5 = new EntityTNTPrimed(world, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        var5.fuse = world.rand.nextInt(var5.fuse / 4) + var5.fuse / 8;
        world.entityJoinedWorld(var5);
    }

    @Override
    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
        if (!var1.multiplayerWorld) {
            if ((var5 & 1) == 0) {
                this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(Block.TNT.blockID, 1, 0));
            } else {
                EntityTNTPrimed var6 = new EntityTNTPrimed(var1, (float) var2 + 0.5F, (float) var3 + 0.5F, (float) var4 + 0.5F);
                var1.entityJoinedWorld(var6);
                var1.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
            }

        }
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == Item.FLINT_AND_STEEL.shiftedIndex) {
            world.setBlockMetadata(x, y, z, 1);
        }

        super.onBlockClicked(world, x, y, z, player);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        return super.blockActivated(world, x, y, z, player);
    }
}
