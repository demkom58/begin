package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTBase {
    private List<NBTBase> tagList = new ArrayList<>();
    private byte tagType;

    void writeTagContents(DataOutput output) throws IOException {
        if (this.tagList.size() > 0) {
            this.tagType = this.tagList.get(0).getType();
        } else {
            this.tagType = 1;
        }

        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());

        for (int i = 0; i < this.tagList.size(); ++i) {
            this.tagList.get(i).writeTagContents(output);
        }

    }

    void readTagContents(DataInput input) throws IOException {
        this.tagType = input.readByte();
        int size = input.readInt();
        this.tagList = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            NBTBase nbtBase = NBTBase.createTagOfType(this.tagType);
            nbtBase.readTagContents(input);
            this.tagList.add(nbtBase);
        }

    }

    public byte getType() {
        return 9;
    }

    public String toString() {
        return "" + this.tagList.size() + " entries of type " + NBTBase.getTagName(this.tagType);
    }

    public void setTag(NBTBase var1) {
        this.tagType = var1.getType();
        this.tagList.add(var1);
    }

    public NBTBase tagAt(int var1) {
        return this.tagList.get(var1);
    }

    public int tagCount() {
        return this.tagList.size();
    }
}
