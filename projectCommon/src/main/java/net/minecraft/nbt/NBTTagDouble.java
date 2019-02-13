package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTBase {
    public double doubleValue;

    public NBTTagDouble() {
    }

    public NBTTagDouble(double value) {
        this.doubleValue = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeDouble(this.doubleValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.doubleValue = input.readDouble();
    }

    public byte getType() {
        return 6;
    }

    public String toString() {
        return "" + this.doubleValue;
    }
}
