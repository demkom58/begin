package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagLong extends Tag {
    public long longValue;

    public TagLong() {
    }

    public TagLong(long value) {
        this.longValue = value;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeLong(this.longValue);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.longValue = input.readLong();
    }

    @Override
    public byte getType() {
        return 4;
    }

    public String toString() {
        return "" + this.longValue;
    }
}
