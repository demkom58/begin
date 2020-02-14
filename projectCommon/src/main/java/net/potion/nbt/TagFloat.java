package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagFloat extends Tag {
    public float floatValue;

    public TagFloat() {
    }

    public TagFloat(float value) {
        this.floatValue = value;
    }

    void write(DataOutput output) throws IOException {
        output.writeFloat(this.floatValue);
    }

    void read(DataInput input) throws IOException {
        this.floatValue = input.readFloat();
    }

    public byte getType() {
        return 5;
    }

    public String toString() {
        return "" + this.floatValue;
    }
}
