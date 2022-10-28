package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagString extends Tag {
    public String stringValue;

    public TagString() {
    }

    public TagString(String value) {
        this.stringValue = value;
        if (value == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeUTF(this.stringValue);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.stringValue = input.readUTF();
    }

    @Override
    public byte getType() {
        return 8;
    }

    public String toString() {
        return "" + this.stringValue;
    }
}
