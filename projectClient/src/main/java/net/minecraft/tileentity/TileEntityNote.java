package net.minecraft.tileentity;

import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity {
    public byte note = 0;
    public boolean previousRedstoneState = false;

    @Override
    public void writeToNBT(TagCompound var1) {
        super.writeToNBT(var1);
        var1.setByte("note", this.note);
    }

    @Override
    public void readFromNBT(TagCompound var1) {
        super.readFromNBT(var1);
        this.note = var1.getByte("note");
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

    public void triggerNote(World var1, int var2, int var3, int var4) {
        if (var1.getBlockMaterial(var2, var3 + 1, var4) == Material.AIR) {
            Material var5 = var1.getBlockMaterial(var2, var3 - 1, var4);
            byte var6 = 0;
            if (var5 == Material.ROCK) {
                var6 = 1;
            }

            if (var5 == Material.SAND) {
                var6 = 2;
            }

            if (var5 == Material.GLASS) {
                var6 = 3;
            }

            if (var5 == Material.WOOD) {
                var6 = 4;
            }

            var1.playNoteAt(var2, var3, var4, var6, this.note);
        }
    }
}
