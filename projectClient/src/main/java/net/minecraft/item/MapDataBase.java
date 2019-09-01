package net.minecraft.item;

import net.minecraft.nbt.TagCompound;

public abstract class MapDataBase {
    public final String field_28168_a;
    private boolean dirty;

    public MapDataBase(String var1) {
        this.field_28168_a = var1;
    }

    public abstract void readFromNBT(TagCompound var1);

    public abstract void writeToNBT(TagCompound var1);

    public void markDirty() {
        this.setDirty(true);
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean var1) {
        this.dirty = var1;
    }
}
