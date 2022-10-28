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

    @Override
    void write(DataOutput output) throws IOException {
        output.writeDouble(this.doubleValue);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.doubleValue = input.readDouble();
    }

    @Override
    public byte getType() {
        return 6;
    }

    public String toString() {
        return "" + this.doubleValue;
    }
}
