package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagByteArray extends Tag {
    public byte[] byteArray;

    public TagByteArray() {
    }

    public TagByteArray(byte[] value) {
        this.byteArray = value;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.byteArray.length);
        output.write(this.byteArray);
    }

    @Override
    void read(DataInput input) throws IOException {
        int var2 = input.readInt();
        this.byteArray = new byte[var2];
        input.readFully(this.byteArray);
    }

    @Override
    public byte getType() {
        return 7;
    }

    public String toString() {
        return "[" + this.byteArray.length + " bytes]";
    }
}
