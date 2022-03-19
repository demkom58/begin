package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagShort extends Tag {
    public short shortValue;

    public TagShort() {
    }

    public TagShort(short value) {
        this.shortValue = value;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeShort(this.shortValue);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.shortValue = input.readShort();
    }

    @Override
    public byte getType() {
        return 2;
    }

    public String toString() {
        return "" + this.shortValue;
    }
}
