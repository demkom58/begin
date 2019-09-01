package net.minecraft.tileentity;

import net.minecraft.nbt.TagCompound;

public class TileEntityRecordPlayer extends TileEntity {
    public int record;

    public void readFromNBT(TagCompound var1) {
        super.readFromNBT(var1);
        this.record = var1.getInteger("Record");
    }

    public void writeToNBT(TagCompound var1) {
        super.writeToNBT(var1);
        if (this.record > 0) {
            var1.setInteger("Record", this.record);
        }

    }
}
