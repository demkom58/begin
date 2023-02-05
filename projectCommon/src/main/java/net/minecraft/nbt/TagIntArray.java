package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TagIntArray extends Tag {
    public int[] intArray;

    public TagIntArray() {
    }

    public TagIntArray(int[] value) {
        this.intArray = value;
    }

    @Override
    void write(DataOutput output) throws IOException {
        int length = this.intArray.length;

        output.writeInt(length);
        for (int i = 0; i < length; i++) {
            output.writeInt(this.intArray[i]);
        }
    }

    @Override
    void read(DataInput input) throws IOException {
        int length = input.readInt();
        this.intArray = new int[length];
        for (int i = 0; i < length; i++) {
            this.intArray[i] = input.readInt();
        }
    }

    @Override
    public byte getType() {
        return 11;
    }

    public String toString() {
        return "[" + this.intArray.length + " ints]";
    }
}
