package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TagCompound extends Tag {
    private final Map<Object, Tag> tagMap = new HashMap<>();

    @Override
    void write(DataOutput output) throws IOException {
        for (Tag tag : this.tagMap.values()) {
            Tag.writeTag(tag, output);
        }

        output.writeByte(0);
    }

    @Override
    void read(DataInput input) throws IOException {
        this.tagMap.clear();

        Tag tag;
        while ((tag = Tag.readTag(input)).getType() != 0) {
            this.tagMap.put(tag.getKey(), tag);
        }

    }

    public Collection<Tag> tags() {
        return this.tagMap.values();
    }

    @Override
    public byte getType() {
        return 10;
    }

    public void setTag(String key, Tag value) {
        this.tagMap.put(key, value.setKey(key));
    }

    public void setByte(String key, byte value) {
        this.tagMap.put(key, new TagByte(value).setKey(key));
    }

    public void setShort(String key, short value) {
        this.tagMap.put(key, new TagShort(value).setKey(key));
    }

    public void setInteger(String key, int value) {
        this.tagMap.put(key, new TagInt(value).setKey(key));
    }

    public void setLong(String key, long value) {
        this.tagMap.put(key, new TagLong(value).setKey(key));
    }

    public void setFloat(String key, float value) {
        this.tagMap.put(key, new TagFloat(value).setKey(key));
    }

    public void setDouble(String key, double value) {
        this.tagMap.put(key, new TagDouble(value).setKey(key));
    }

    public void setString(String key, String value) {
        this.tagMap.put(key, new TagString(value).setKey(key));
    }

    public void setByteArray(String key, byte[] value) {
        this.tagMap.put(key, new TagByteArray(value).setKey(key));
    }

    public void setIntArray(String key, int[] value) {
        this.tagMap.put(key, new TagIntArray(value).setKey(key));
    }

    public void setCompoundTag(String key, TagCompound value) {
        this.tagMap.put(key, value.setKey(key));
    }

    public void setBoolean(String key, boolean value) {
        this.setByte(key, (byte) (value ? 1 : 0));
    }

    public boolean hasKey(String key) {
        return this.tagMap.containsKey(key);
    }

    public byte getByte(String key) {
        return !this.tagMap.containsKey(key) ? 0 : ((TagByte) this.tagMap.get(key)).byteValue;
    }

    public short getShort(String key) {
        return !this.tagMap.containsKey(key) ? 0 : ((TagShort) this.tagMap.get(key)).shortValue;
    }

    public int getInteger(String key) {
        return !this.tagMap.containsKey(key) ? 0 : ((TagInt) this.tagMap.get(key)).intValue;
    }

    public long getLong(String key) {
        return !this.tagMap.containsKey(key) ? 0L : ((TagLong) this.tagMap.get(key)).longValue;
    }

    public float getFloat(String key) {
        return !this.tagMap.containsKey(key) ? 0.0F : ((TagFloat) this.tagMap.get(key)).floatValue;
    }

    public double getDouble(String key) {
        return !this.tagMap.containsKey(key) ? 0.0D : ((TagDouble) this.tagMap.get(key)).doubleValue;
    }

    public String getString(String key) {
        return !this.tagMap.containsKey(key) ? "" : ((TagString) this.tagMap.get(key)).stringValue;
    }

    public byte[] getByteArray(String key) {
        return !this.tagMap.containsKey(key) ? new byte[0] : ((TagByteArray) this.tagMap.get(key)).byteArray;
    }

    public int[] getIntArray(String key) {
        return !this.tagMap.containsKey(key) ? new int[0] : ((TagIntArray) this.tagMap.get(key)).intArray;
    }

    public TagCompound getCompoundTag(String key) {
        return !this.tagMap.containsKey(key) ? new TagCompound() : (TagCompound) this.tagMap.get(key);
    }

    public TagList getTagList(String key) {
        return !this.tagMap.containsKey(key) ? new TagList() : (TagList) this.tagMap.get(key);
    }

    public boolean getBoolean(String key) {
        return this.getByte(key) != 0;
    }

    public String toString() {
        return this.tagMap.size() + " entries";
    }

}
