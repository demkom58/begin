package net.minecraft.tileentity;

import net.minecraft.nbt.TagCompound;

public class TileEntityRecordPlayer extends TileEntity {
    public int record;

    @Override
    public void readFromNBT(TagCompound tag) {
        super.readFromNBT(tag);
        this.record = tag.getInteger("Record");
    }

    @Override
    public void writeToNBT(TagCompound tag) {
        super.writeToNBT(tag);
        if (this.record > 0) {
            tag.setInteger("Record", this.record);
        }

    }
}
