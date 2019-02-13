package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {
    void readTagContents(DataInput input) throws IOException {
    }

    void writeTagContents(DataOutput output) throws IOException {
    }

    public byte getType() {
        return 0;
    }

    public String toString() {
        return "END";
    }
}
