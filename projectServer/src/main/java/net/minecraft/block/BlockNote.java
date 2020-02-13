package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer {
    public BlockNote(int var1) {
        super(var1, 74, Material.WOOD);
    }

    @Override
    public int getBlockTextureFromSide(int var1) {
        return this.blockIndexInTexture;
    }

    @Override
    public void onNeighborBlockChange(World world, int var2, int var3, int var4, int var5) {
        if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
            boolean var6 = world.isBlockGettingPowered(var2, var3, var4);
            TileEntityNote var7 = (TileEntityNote) world.getBlockTileEntity(var2, var3, var4);
            if (var7.previousRedstoneState != var6) {
                if (var6) {
                    var7.triggerNote(world, var2, var3, var4);
                }

                var7.previousRedstoneState = var6;
            }
        }

    }

    @Override
    public boolean blockActivated(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (world.singleplayerWorld) {
            return true;
        } else {
            TileEntityNote var6 = (TileEntityNote) world.getBlockTileEntity(var2, var3, var4);
            var6.changePitch();
            var6.triggerNote(world, var2, var3, var4);
            return true;
        }
    }

    @Override
    public void onBlockClicked(World world, int var2, int var3, int var4, EntityPlayer entityPlayer) {
        if (!world.singleplayerWorld) {
            TileEntityNote var6 = (TileEntityNote) world.getBlockTileEntity(var2, var3, var4);
            var6.triggerNote(world, var2, var3, var4);
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityNote();
    }

    @Override
    public void playBlock(World world, int var2, int var3, int var4, int var5, int var6) {
        float var7 = (float) Math.pow(2.0D, (double) (var6 - 12) / 12.0D);
        String var8 = "harp";
        if (var5 == 1) {
            var8 = "bd";
        }

        if (var5 == 2) {
            var8 = "snare";
        }

        if (var5 == 3) {
            var8 = "hat";
        }

        if (var5 == 4) {
            var8 = "bassattack";
        }

        world.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.5D, (double) var4 + 0.5D, "note." + var8, 3.0F, var7);
        world.spawnParticle("note", (double) var2 + 0.5D, (double) var3 + 1.2D, (double) var4 + 0.5D, (double) var6 / 24.0D, 0.0D, 0.0D);
    }
}
