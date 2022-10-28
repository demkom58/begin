package net.potion.block;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.entity.player.EntityPlayer;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.tileentity.TileEntityNote;
import net.potion.world.World;

public class BlockNote extends BlockContainer {
    public BlockNote(int var1) {
        super(var1, 74, Material.WOOD);
    }

    @Override
    @Side(CodeSide.CLIENT)
    public int getBlockTextureFromSide(int side) {
        return this.blockIndexInTexture;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int var5) {
        if (var5 > 0 && Block.BLOCKS_LIST[var5].canProvidePower()) {
            boolean var6 = world.isBlockGettingPowered(x, y, z);
            TileEntityNote var7 = (TileEntityNote) world.getBlockTileEntity(x, y, z);
            if (var7.previousRedstoneState != var6) {
                if (var6) {
                    var7.triggerNote(world, x, y, z);
                }

                var7.previousRedstoneState = var6;
            }
        }

    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.localWorld) {
            TileEntityNote var6 = (TileEntityNote) world.getBlockTileEntity(x, y, z);
            var6.changePitch();
            var6.triggerNote(world, x, y, z);
        }

        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.localWorld) {
            TileEntityNote var6 = (TileEntityNote) world.getBlockTileEntity(x, y, z);
            var6.triggerNote(world, x, y, z);
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityNote();
    }

    @Override
    public void playBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
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

        var1.playSoundEffect((double) var2 + 0.5D, (double) var3 + 0.5D, (double) var4 + 0.5D, "note." + var8, 3.0F, var7);
        var1.spawnParticle("note", (double) var2 + 0.5D, (double) var3 + 1.2D, (double) var4 + 0.5D, (double) var6 / 24.0D, 0.0D, 0.0D);
    }
}
