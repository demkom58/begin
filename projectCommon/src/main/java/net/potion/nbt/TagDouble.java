package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagDouble extends Tag {
    public double doubleValue;

    public TagDouble() {
    }

    public TagDouble(double value) {
        this.doubleValue = value;
    }

    void write(DataOutput output) throws IOException {
        output.writeDouble(this.doubleValue);
    }

    void read(DataInput input) throws IOException {
        this.doubleValue = input.readDouble();
    }

    public byte getType() {
        return 6;
    }

    public String toString() {
        return "" + this.doubleValue;
    }
}
