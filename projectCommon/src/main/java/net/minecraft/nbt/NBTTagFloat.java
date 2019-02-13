package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTBase {
    public float floatValue;

    public NBTTagFloat() {
    }

    public NBTTagFloat(float value) {
        this.floatValue = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeFloat(this.floatValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.floatValue = input.readFloat();
    }

    public byte getType() {
        return 5;
    }

    public String toString() {
        return "" + this.floatValue;
    }
}
