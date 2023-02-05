package net.minecraft.util;

public class WatchableObject {
    private final int objectType;
    private final int dataValueId;
    private Object watchedObject;
    private boolean watching;

    public WatchableObject(int var1, int var2, Object var3) {
        this.dataValueId = var2;
        this.watchedObject = var3;
        this.objectType = var1;
        this.watching = true;
    }

    public int getDataValueId() {
        return this.dataValueId;
    }

    public Object getObject() {
        return this.watchedObject;
    }

    public void setObject(Object var1) {
        this.watchedObject = var1;
    }

    public int getObjectType() {
        return this.objectType;
    }

    public boolean isWatching() {
        return this.watching;
    }

    public void setWatching(boolean var1) {
        this.watching = var1;
    }
}
