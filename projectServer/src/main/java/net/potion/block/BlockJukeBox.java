package net.potion.block;

import net.potion.entity.item.EntityItem;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.ItemStack;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityRecordPlayer;
import net.potion.world.World;

public class BlockJukeBox extends BlockContainer {
    protected BlockJukeBox(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        return this.blockIndexInTexture + (var1 == 1 ? 1 : 0);
    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (world.getBlockMetadata(var2, var3, var4) == 0) {
            return false;
        } else {
            this.func_28035_b_(world, var2, var3, var4);
            return true;
        }
    }

    public void ejectRecord(World var1, int var2, int var3, int var4, int var5) {
        if (!var1.singleplayerWorld) {
            TileEntityRecordPlayer var6 = (TileEntityRecordPlayer) var1.getBlockTileEntity(var2, var3, var4);
            var6.field_28009_a = var5;
            var6.onInventoryChanged();
            var1.setBlockMetadataWithNotify(var2, var3, var4, 1);
        }
    }

    public void func_28035_b_(World var1, int var2, int var3, int var4) {
        if (!var1.singleplayerWorld) {
            TileEntityRecordPlayer var5 = (TileEntityRecordPlayer) var1.getBlockTileEntity(var2, var3, var4);
            int var6 = var5.field_28009_a;
            if (var6 != 0) {
                var1.func_28097_e(1005, var2, var3, var4, 0);
                var1.playRecord(null, var2, var3, var4);
                var5.field_28009_a = 0;
                var5.onInventoryChanged();
                var1.setBlockMetadataWithNotify(var2, var3, var4, 0);
                float var8 = 0.7F;
                double var9 = (double) (var1.rand.nextFloat() * var8) + (double) (1.0F - var8) * 0.5D;
                double var11 = (double) (var1.rand.nextFloat() * var8) + (double) (1.0F - var8) * 0.2D + 0.6D;
                double var13 = (double) (var1.rand.nextFloat() * var8) + (double) (1.0F - var8) * 0.5D;
                EntityItem var15 = new EntityItem(var1, (double) var2 + var9, (double) var3 + var11, (double) var4 + var13, new ItemStack(var6, 1, 0));
                var15.delayBeforeCanPickup = 10;
                var1.entityJoinedWorld(var15);
            }
        }
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        this.func_28035_b_(world, x, y, z);
        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float chance) {
        if (!world.singleplayerWorld) {
            super.dropBlockAsItemWithChance(world, x, y, z, var5, chance);
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityRecordPlayer();
    }
}
