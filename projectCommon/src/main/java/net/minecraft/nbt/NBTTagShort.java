package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase {
    public short shortValue;

    public NBTTagShort() {
    }

    public NBTTagShort(short value) {
        this.shortValue = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeShort(this.shortValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.shortValue = input.readShort();
    }

    public byte getType() {
        return 2;
    }

    public String toString() {
        return "" + this.shortValue;
    }
}
