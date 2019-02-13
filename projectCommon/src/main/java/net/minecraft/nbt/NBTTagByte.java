package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase {
    public byte byteValue;

    public NBTTagByte() {
    }

    public NBTTagByte(byte value) {
        this.byteValue = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeByte(this.byteValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.byteValue = input.readByte();
    }

    public byte getType() {
        return 1;
    }

    public String toString() {
        return "" + this.byteValue;
    }
}
