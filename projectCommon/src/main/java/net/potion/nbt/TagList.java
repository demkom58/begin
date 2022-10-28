package net.potion.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagList extends Tag {
    private List<Tag> tagList = new ArrayList<>();
    private byte tagType;

    @Override
    void write(DataOutput output) throws IOException {
        if (this.tagList.size() > 0) {
            this.tagType = this.tagList.get(0).getType();
        } else {
            this.tagType = 1;
        }

        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());

        for (int i = 0; i < this.tagList.size(); ++i) {
            this.tagList.get(i).write(output);
        }

    }

    @Override
    void read(DataInput input) throws IOException {
        this.tagType = input.readByte();
        int size = input.readInt();
        this.tagList = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            Tag tag = Tag.createTag(this.tagType);
            tag.read(input);
            this.tagList.add(tag);
        }

    }

    @Override
    public byte getType() {
        return 9;
    }

    public String toString() {
        return "" + this.tagList.size() + " entries of type " + Tag.idToString(this.tagType);
    }

    public void setTag(Tag var1) {
        this.tagType = var1.getType();
        this.tagList.add(var1);
    }

    public Tag tagAt(int var1) {
        return this.tagList.get(var1);
    }

    public int tagCount() {
        return this.tagList.size();
    }
}
