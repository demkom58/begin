package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Tag {
    private String key = null;

    abstract void write(DataOutput output) throws IOException;

    abstract void read(DataInput input) throws IOException;

    public abstract byte getType();

    public static Tag readTag(DataInput input) throws IOException {
        byte tagType = input.readByte();
        if (tagType == 0)
            return new TagEnd();

        Tag type = createTag(tagType);
        type.key = input.readUTF();
        type.read(input);
        return type;
    }

    public static void writeTag(Tag tag, DataOutput output) throws IOException {
        output.writeByte(tag.getType());

        if (tag.getType() != 0) {
            output.writeUTF(tag.getKey());
            tag.write(output);
        }
    }

    public static Tag createTag(byte id) {
        switch (id) {
            case 0:
                return new TagEnd();
            case 1:
                return new TagByte();
            case 2:
                return new TagShort();
            case 3:
                return new TagInt();
            case 4:
                return new TagLong();
            case 5:
                return new TagFloat();
            case 6:
                return new TagDouble();
            case 7:
                return new TagByteArray();
            case 8:
                return new TagString();
            case 9:
                return new TagList();
            case 10:
                return new TagCompound();
            default:
                return null;
        }
    }

    public static String idToString(byte id) {
        switch (id) {
            case 0:
                return "TAG_End";
            case 1:
                return "TAG_Byte";
            case 2:
                return "TAG_Short";
            case 3:
                return "TAG_Int";
            case 4:
                return "TAG_Long";
            case 5:
                return "TAG_Float";
            case 6:
                return "TAG_Double";
            case 7:
                return "TAG_Byte_Array";
            case 8:
                return "TAG_String";
            case 9:
                return "TAG_List";
            case 10:
                return "TAG_Compound";
            default:
                return "UNKNOWN";
        }
    }

    public String getKey() {
        return this.key == null ? "" : this.key;
    }

    public Tag setKey(String key) {
        this.key = key;
        return this;
    }
}
