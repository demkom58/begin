package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagByte extends Tag {
    public byte byteValue;

    public TagByte() {
    }

    public TagByte(byte value) {
        this.byteValue = value;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeByte(this.byteValue);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.byteValue = input.readByte();
    }

    @Override
    public byte getType() {
        return 1;
    }

    public String toString() {
        return "" + this.byteValue;
    }
}
