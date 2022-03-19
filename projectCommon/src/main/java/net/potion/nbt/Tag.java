package net.potion.nbt;

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
        return switch (id) {
            case 0 -> new TagEnd();
            case 1 -> new TagByte();
            case 2 -> new TagShort();
            case 3 -> new TagInt();
            case 4 -> new TagLong();
            case 5 -> new TagFloat();
            case 6 -> new TagDouble();
            case 7 -> new TagByteArray();
            case 8 -> new TagString();
            case 9 -> new TagList();
            case 10 -> new TagCompound();
            case 11 -> new TagIntArray();
            default -> null;
        };
    }

    public static String idToString(byte id) {
        return switch (id) {
            case 0 -> "TAG_End";
            case 1 -> "TAG_Byte";
            case 2 -> "TAG_Short";
            case 3 -> "TAG_Int";
            case 4 -> "TAG_Long";
            case 5 -> "TAG_Float";
            case 6 -> "TAG_Double";
            case 7 -> "TAG_Byte_Array";
            case 8 -> "TAG_String";
            case 9 -> "TAG_List";
            case 10 -> "TAG_Compound";
            case 11 -> "TAG_Int_Array";
            case 12 -> "TAG_Byte_3Array";
            case 13 -> "TAG_Int_3Array";
            default -> "UNKNOWN";
        };
    }

    public String getKey() {
        return this.key == null ? "" : this.key;
    }

    public Tag setKey(String key) {
        this.key = key;
        return this;
    }
}
