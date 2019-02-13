package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase {
    public int intValue;

    public NBTTagInt() {
    }

    public NBTTagInt(int value) {
        this.intValue = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeInt(this.intValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.intValue = input.readInt();
    }

    public byte getType() {
        return 3;
    }

    public String toString() {
        return "" + this.intValue;
    }
}
