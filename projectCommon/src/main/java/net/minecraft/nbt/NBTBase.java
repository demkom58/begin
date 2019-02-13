package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
    private String key = null;

    public static NBTBase readTag(DataInput input) throws IOException {
        byte aByte = input.readByte();
        if (aByte == 0) {
            return new NBTTagEnd();
        }

        NBTBase type = createTagOfType(aByte);
        type.key = input.readUTF();
        type.readTagContents(input);
        return type;
    }

    public static void writeTag(NBTBase nbtBase, DataOutput output) throws IOException {
        output.writeByte(nbtBase.getType());

        if (nbtBase.getType() != 0) {
            output.writeUTF(nbtBase.getKey());
            nbtBase.writeTagContents(output);
        }
    }

    public static NBTBase createTagOfType(byte id) {
        switch (id) {
            case 0:
                return new NBTTagEnd();
            case 1:
                return new NBTTagByte();
            case 2:
                return new NBTTagShort();
            case 3:
                return new NBTTagInt();
            case 4:
                return new NBTTagLong();
            case 5:
                return new NBTTagFloat();
            case 6:
                return new NBTTagDouble();
            case 7:
                return new NBTTagByteArray();
            case 8:
                return new NBTTagString();
            case 9:
                return new NBTTagList();
            case 10:
                return new NBTTagCompound();
            default:
                return null;
        }
    }

    public static String getTagName(byte id) {
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

    abstract void writeTagContents(DataOutput output) throws IOException;

    abstract void readTagContents(DataInput input) throws IOException;

    public abstract byte getType();

    public String getKey() {
        return this.key == null ? "" : this.key;
    }

    public NBTBase setKey(String key) {
        this.key = key;
        return this;
    }
}
