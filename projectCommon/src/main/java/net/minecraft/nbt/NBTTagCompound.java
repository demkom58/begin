package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NBTTagCompound extends NBTBase {
    private Map<Object, NBTBase> tagMap = new HashMap<>();

    void writeTagContents(DataOutput output) throws IOException {
        for (NBTBase nbtBase : this.tagMap.values()) {
            NBTBase.writeTag(nbtBase, output);
        }

        output.writeByte(0);
    }

    void readTagContents(DataInput input) throws IOException {
        this.tagMap.clear();

        NBTBase nbtBase;
        while ((nbtBase = NBTBase.readTag(input)).getType() != 0) {
            this.tagMap.put(nbtBase.getKey(), nbtBase);
        }

    }

    public Collection<NBTBase> tags() {
        return this.tagMap.values();
    }

    public byte getType() {
        return 10;
    }

    public void setTag(String key, NBTBase value) {
        this.tagMap.put(key, value.setKey(key));
    }

    public void setByte(String key, byte value) {
        this.tagMap.put(key, new NBTTagByte(value).setKey(key));
    }

    public void setShort(String key, short value) {
        this.tagMap.put(key, new NBTTagShort(value).setKey(key));
    }

    public void setInteger(String key, int value) {
        this.tagMap.put(key, new NBTTagInt(value).setKey(key));
    }

    public void setLong(String key, long value) {
        this.tagMap.put(key, new NBTTagLong(value).setKey(key));
    }

    public void setFloat(String key, float value) {
        this.tagMap.put(key, new NBTTagFloat(value).setKey(key));
    }

    public void setDouble(String key, double value) {
        this.tagMap.put(key, new NBTTagDouble(value).setKey(key));
    }

    public void setString(String key, String value) {
        this.tagMap.put(key, new NBTTagString(value).setKey(key));
    }

    public void setByteArray(String key, byte[] value) {
        this.tagMap.put(key, new NBTTagByteArray(value).setKey(key));
    }

    public void setCompoundTag(String key, NBTTagCompound value) {
        this.tagMap.put(key, value.setKey(key));
    }

    public void setBoolean(String key, boolean value) {
        this.setByte(key, (byte) (value ? 1 : 0));
    }

    public boolean hasKey(String key) {
        return this.tagMap.containsKey(key);
    }

    public byte getByte(String key) {
        return !this.tagMap.containsKey(key) ? 0 : ((NBTTagByte) this.tagMap.get(key)).byteValue;
    }

    public short getShort(String key) {
        return !this.tagMap.containsKey(key) ? 0 : ((NBTTagShort) this.tagMap.get(key)).shortValue;
    }

    public int getInteger(String key) {
        return !this.tagMap.containsKey(key) ? 0 : ((NBTTagInt) this.tagMap.get(key)).intValue;
    }

    public long getLong(String key) {
        return !this.tagMap.containsKey(key) ? 0L : ((NBTTagLong) this.tagMap.get(key)).longValue;
    }

    public float getFloat(String key) {
        return !this.tagMap.containsKey(key) ? 0.0F : ((NBTTagFloat) this.tagMap.get(key)).floatValue;
    }

    public double getDouble(String key) {
        return !this.tagMap.containsKey(key) ? 0.0D : ((NBTTagDouble) this.tagMap.get(key)).doubleValue;
    }

    public String getString(String key) {
        return !this.tagMap.containsKey(key) ? "" : ((NBTTagString) this.tagMap.get(key)).stringValue;
    }

    public byte[] getByteArray(String key) {
        return !this.tagMap.containsKey(key) ? new byte[0] : ((NBTTagByteArray) this.tagMap.get(key)).byteArray;
    }

    public NBTTagCompound getCompoundTag(String key) {
        return !this.tagMap.containsKey(key) ? new NBTTagCompound() : (NBTTagCompound) this.tagMap.get(key);
    }

    public NBTTagList getTagList(String key) {
        return !this.tagMap.containsKey(key) ? new NBTTagList() : (NBTTagList) this.tagMap.get(key);
    }

    public boolean getBoolean(String key) {
        return this.getByte(key) != 0;
    }

    public String toString() {
        return this.tagMap.size() + " entries";
    }

}
