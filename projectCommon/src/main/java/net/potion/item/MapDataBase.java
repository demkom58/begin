package net.potion.item;

import net.potion.nbt.TagCompound;

public abstract class MapDataBase {
    public final String mapId;
    private boolean dirty;

    public MapDataBase(String var1) {
        this.mapId = var1;
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
