package net.minecraft.nbt;

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

    @Override
    void write(DataOutput output) throws IOException {
        output.writeFloat(this.floatValue);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.floatValue = input.readFloat();
    }

    @Override
    public byte getType() {
        return 5;
    }

    public String toString() {
        return "" + this.floatValue;
    }
}
