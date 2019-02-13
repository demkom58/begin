package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
    public String stringValue;

    public NBTTagString() {
    }

    public NBTTagString(String value) {
        this.stringValue = value;
        if (value == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeUTF(this.stringValue);
    }

    void readTagContents(DataInput input) throws IOException {
        this.stringValue = input.readUTF();
    }

    public byte getType() {
        return 8;
    }

    public String toString() {
        return "" + this.stringValue;
    }
}
