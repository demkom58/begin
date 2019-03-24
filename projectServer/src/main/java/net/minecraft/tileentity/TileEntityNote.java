package net.minecraft.tileentity;

import net.minecraft.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity {
    public byte note = 0;
    public boolean previousRedstoneState = false;

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("note", this.note);
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.note = compound.getByte("note");
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
