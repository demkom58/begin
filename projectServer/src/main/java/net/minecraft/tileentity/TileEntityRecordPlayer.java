package net.minecraft.tileentity;

import net.minecraft.nbt.TagCompound;

public class TileEntityRecordPlayer extends TileEntity {
    public int field_28009_a;

    public void readFromNBT(TagCompound compound) {
        super.readFromNBT(compound);
        this.field_28009_a = compound.getInteger("Record");
    }

    public void writeToNBT(TagCompound compound) {
        super.writeToNBT(compound);
        if (this.field_28009_a > 0) {
            compound.setInteger("Record", this.field_28009_a);
        }

    }
}
