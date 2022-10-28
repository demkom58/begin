package net.potion.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import net.potion.item.ItemStack;
import net.potion.network.packet.Packet;
import net.potion.world.chunk.ChunkCoordinates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataWatcher {
    private static final Map<Class<?>, Integer> dataTypes = new HashMap<>();

    static {
        dataTypes.put(Byte.class, 0);
        dataTypes.put(Short.class, 1);
        dataTypes.put(Integer.class, 2);
        dataTypes.put(Float.class, 3);
        dataTypes.put(String.class, 4);
        dataTypes.put(ItemStack.class, 5);
        dataTypes.put(ChunkCoordinates.class, 6);
    }

    private final Int2ObjectMap<WatchableObject> watchedObjects = new Int2ObjectRBTreeMap<>();
    private boolean objectChanged;

    public static void writeObjectsInListToStream(List<WatchableObject> var0, DataOutputStream var1) throws IOException {
        if (var0 != null) {
            for (WatchableObject var3 : var0) {
                writeWatchableObject(var1, var3);
            }
        }

        var1.writeByte(127);
    }

    private static void writeWatchableObject(DataOutputStream var0, WatchableObject var1) throws IOException {
        int var2 = (var1.getObjectType() << 5 | var1.getDataValueId() & 31) & 255;
        var0.writeByte(var2);
        switch (var1.getObjectType()) {
            case 0 -> var0.writeByte((Byte) var1.getObject());
            case 1 -> var0.writeShort((Short) var1.getObject());
            case 2 -> var0.writeInt((Integer) var1.getObject());
            case 3 -> var0.writeFloat((Float) var1.getObject());
            case 4 -> Packet.writeString((String) var1.getObject(), var0);
            case 5 -> {
                ItemStack var4 = (ItemStack) var1.getObject();
                var0.writeShort(var4.getItem().shiftedIndex);
                var0.writeByte(var4.stackSize);
                var0.writeShort(var4.getItemDamage());
            }
            case 6 -> {
                ChunkCoordinates var3 = (ChunkCoordinates) var1.getObject();
                var0.writeInt(var3.x);
                var0.writeInt(var3.y);
                var0.writeInt(var3.z);
            }
        }

    }

    public static List<WatchableObject> readWatchableObjects(DataInputStream var0) throws IOException {
        List<WatchableObject> var1 = null;

        for (byte var2 = var0.readByte(); var2 != 127; var2 = var0.readByte()) {
            if (var1 == null) {
                var1 = new ArrayList<>();
            }

            int var3 = (var2 & 224) >> 5;
            int var4 = var2 & 31;
            WatchableObject var5 = null;
            switch (var3) {
                case 0 -> var5 = new WatchableObject(var3, var4, var0.readByte());
                case 1 -> var5 = new WatchableObject(var3, var4, var0.readShort());
                case 2 -> var5 = new WatchableObject(var3, var4, var0.readInt());
                case 3 -> var5 = new WatchableObject(var3, var4, var0.readFloat());
                case 4 -> var5 = new WatchableObject(var3, var4, Packet.readString(var0, 64));
                case 5 -> {
                    short var9 = var0.readShort();
                    byte var10 = var0.readByte();
                    short var11 = var0.readShort();
                    var5 = new WatchableObject(var3, var4, new ItemStack(var9, var10, var11));
                }
                case 6 -> {
                    int var6 = var0.readInt();
                    int var7 = var0.readInt();
                    int var8 = var0.readInt();
                    var5 = new WatchableObject(var3, var4, new ChunkCoordinates(var6, var7, var8));
                }
            }

            var1.add(var5);
        }

        return var1;
    }

    public void addObject(int var1, Object var2) {
        Integer var3 = dataTypes.get(var2.getClass());
        if (var3 == null) {
            throw new IllegalArgumentException("Unknown data type: " + var2.getClass());
        } else if (var1 > 31) {
            throw new IllegalArgumentException("Data value id is too big with " + var1 + "! (Max is " + 31 + ")");
        } else if (this.watchedObjects.containsKey(var1)) {
            throw new IllegalArgumentException("Duplicate id value for " + var1 + "!");
        }

        WatchableObject watchableObject = new WatchableObject(var3, var1, var2);
        this.watchedObjects.put(var1, watchableObject);
    }

    public byte getWatchableObjectByte(int var1) {
        return (Byte) this.watchedObjects.get(var1).getObject();
    }

    public int getWatchableObjectInteger(int var1) {
        return (Integer) this.watchedObjects.get(var1).getObject();
    }

    public String getWatchableObjectString(int var1) {
        return (String) this.watchedObjects.get(var1).getObject();
    }

    public void updateObject(int var1, Object var2) {
        WatchableObject watchableObject = this.watchedObjects.get(var1);
        if (!var2.equals(watchableObject.getObject())) {
            watchableObject.setObject(var2);
            watchableObject.setWatching(true);
            this.objectChanged = true;
        }

    }

    public boolean hasObjectChanged() {
        return this.objectChanged;
    }

    public ArrayList<WatchableObject> getChangedObjects() {
        ArrayList<WatchableObject> list = null;
        if (this.objectChanged) {
            for (WatchableObject wObject : this.watchedObjects.values()) {
                if (wObject.isWatching()) {
                    wObject.setWatching(false);
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    list.add(wObject);
                }
            }
        }

        this.objectChanged = false;
        return list;
    }

    public void writeWatchableObjects(DataOutputStream os) throws IOException {
        for (WatchableObject wo : this.watchedObjects.values()) {
            writeWatchableObject(os, wo);
        }

        os.writeByte(127);
    }


    public void updateWatchedObjectsFromList(List<WatchableObject> watchableObjects) {
        for (WatchableObject watchableObject : watchableObjects) {
            WatchableObject object = this.watchedObjects.get(watchableObject.getDataValueId());
            if (object != null) {
                object.setObject(watchableObject.getObject());
            }
        }

    }
}
