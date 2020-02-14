package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagInt extends Tag {
    public int intValue;

    public TagInt() {
    }

    public TagInt(int value) {
        this.intValue = value;
    }

    void write(DataOutput output) throws IOException {
        output.writeInt(this.intValue);
    }

    void read(DataInput input) throws IOException {
        this.intValue = input.readInt();
    }

    public byte getType() {
        return 3;
    }

    public String toString() {
        return "" + this.intValue;
    }
}
