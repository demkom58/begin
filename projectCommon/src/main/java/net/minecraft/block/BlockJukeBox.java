package net.minecraft.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityRecordPlayer;
import net.minecraft.world.World;

public class BlockJukeBox extends BlockContainer {
    protected BlockJukeBox(int var1, int var2) {
        super(var1, var2, Material.WOOD);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        return this.blockIndexInTexture + (side == 1 ? 1 : 0);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.getBlockMetadata(x, y, z) == 0)
            return false;

        this.dropRecord(world, x, y, z);
        return true;
    }

    public void ejectRecord(World world, int x, int y, int z, int record) {
        if (world.localWorld)
            return;

        TileEntityRecordPlayer recordPlayer = (TileEntityRecordPlayer) world.getBlockTileEntity(x, y, z);
        recordPlayer.record = record;
        recordPlayer.onInventoryChanged();
        world.setBlockMetadataWithNotify(x, y, z, 1);
    }

    public void dropRecord(World world, int x, int y, int z) {
        if (world.localWorld) {
            return;
        }

        TileEntityRecordPlayer recordPlayer = (TileEntityRecordPlayer) world.getBlockTileEntity(x, y, z);
        int record = recordPlayer.record;
        if (record == 0) {
            return;
        }

        world.playEffects(1005, x, y, z, 0);
        world.playRecord(null, x, y, z);
        recordPlayer.record = 0;
        recordPlayer.onInventoryChanged();
        world.setBlockMetadataWithNotify(x, y, z, 0);

        float dispersion = 0.7F;
        double xRand = (double) (world.rand.nextFloat() * dispersion) + (double) (1.0F - dispersion) * 0.5D;
        double yRand = (double) (world.rand.nextFloat() * dispersion) + (double) (1.0F - dispersion) * 0.2D + 0.6D;
        double zRand = (double) (world.rand.nextFloat() * dispersion) + (double) (1.0F - dispersion) * 0.5D;

        EntityItem item = new EntityItem(world, (double) x + xRand, (double) y + yRand, (double) z + zRand, new ItemStack(record, 1, 0));
        item.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(item);
    }

    @Override
    public void onBlockRemoval(World var1, int var2, int var3, int var4) {
        this.dropRecord(var1, var2, var3, var4);
        super.onBlockRemoval(var1, var2, var3, var4);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float failChance) {
        if (!world.localWorld) {
            super.dropBlockAsItemWithChance(world, x, y, z, var5, failChance);
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityRecordPlayer();
    }
}
