package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagEnd extends Tag {
    @Override
    void read(DataInput input) throws IOException {
    }

    @Override
    void write(DataOutput output) throws IOException {
    }

    @Override
    public byte getType() {
        return 0;
    }

    public String toString() {
        return "END";
    }
}
