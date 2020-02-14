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

    void write(DataOutput output) throws IOException {
        output.writeShort(this.shortValue);
    }

    void read(DataInput input) throws IOException {
        this.shortValue = input.readShort();
    }

    public byte getType() {
        return 2;
    }

    public String toString() {
        return "" + this.shortValue;
    }
}
