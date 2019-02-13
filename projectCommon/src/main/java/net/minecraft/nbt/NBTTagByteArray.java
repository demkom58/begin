package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByteArray extends NBTBase {
    public byte[] byteArray;

    public NBTTagByteArray() {
    }

    public NBTTagByteArray(byte[] value) {
        this.byteArray = value;
    }

    void writeTagContents(DataOutput output) throws IOException {
        output.writeInt(this.byteArray.length);
        output.write(this.byteArray);
    }

    void readTagContents(DataInput input) throws IOException {
        int var2 = input.readInt();
        this.byteArray = new byte[var2];
        input.readFully(this.byteArray);
    }

    public byte getType() {
        return 7;
    }

    public String toString() {
        return "[" + this.byteArray.length + " bytes]";
    }
}
