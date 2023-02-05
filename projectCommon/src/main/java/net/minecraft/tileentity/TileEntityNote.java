package net.minecraft.tileentity;

import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity {
    public byte note = 0;
    public boolean previousRedstoneState = false;

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte("note", this.note);
    }

    @Override
    public void readFromNBT(TagCompound tag) {
        super.readFromNBT(tag);
        this.note = tag.getByte("note");
        if (this.note < 0) {
            this.note = 0;
        }

        if (this.note > 24) {
            this.note = 24;
        }

    }

    public void changePitch() {
        this.note = (byte) ((this.note + 1) % 25);
        this.onInventoryChanged();
    }

    public void triggerNote(World world, int x, int y, int z) {
        if (world.getBlockMaterial(x, y + 1, z) == Material.AIR) {
            Material material = world.getBlockMaterial(x, y - 1, z);
            byte var6 = 0;
            if (material == Material.ROCK) {
                var6 = 1;
            }

            if (material == Material.SAND) {
                var6 = 2;
            }

            if (material == Material.GLASS) {
                var6 = 3;
            }

            if (material == Material.WOOD) {
                var6 = 4;
            }

            world.playNoteAt(x, y, z, var6, this.note);
        }
    }
}
