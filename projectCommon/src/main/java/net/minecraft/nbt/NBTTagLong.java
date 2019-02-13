package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTBase {
    public long longValue;

    public NBTTagLong() {
    }

    public NBTTagLong(long value) {
        this.longValue = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeLong(this.longValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.longValue = input.readLong();
    }

    public byte getType() {
        return 4;
    }

    public String toString() {
        return "" + this.longValue;
    }
}
