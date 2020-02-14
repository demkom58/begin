package net.potion.util;

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
    private static final HashMap<Class, Integer> dataTypes = new HashMap<>();

    static {
        dataTypes.put(Byte.class, 0);
        dataTypes.put(Short.class, 1);
        dataTypes.put(Integer.class, 2);
        dataTypes.put(Float.class, 3);
        dataTypes.put(String.class, 4);
        dataTypes.put(ItemStack.class, 5);
        dataTypes.put(ChunkCoordinates.class, 6);
    }

    private final Map<Integer, WatchableObject> watchedObjects = new HashMap<>();
    private boolean objectChanged;

    public static void writeObjectsInListToStream(List<WatchableObject> var0, DataOutputStream var1) throws IOException {
        if (var0 != null) {
            for (WatchableObject var3 : var0) {
                writeWatchableObject(var1, var3);
            }
        }

        var1.writeByte(127);
    }

    private static void writeWatchableObject(DataOutputStream outputStream,
                                             WatchableObject watchableObject) throws IOException {
        int var2 = (watchableObject.getObjectType() << 5 | watchableObject.getDataValueId() & 31) & 255;
        outputStream.writeByte(var2);
        switch (watchableObject.getObjectType()) {
            case 0:
                outputStream.writeByte((Byte) watchableObject.getObject());
                break;
            case 1:
                outputStream.writeShort((Short) watchableObject.getObject());
                break;
            case 2:
                outputStream.writeInt((Integer) watchableObject.getObject());
                break;
            case 3:
                outputStream.writeFloat((Float) watchableObject.getObject());
                break;
            case 4:
                Packet.writeString((String) watchableObject.getObject(), outputStream);
                break;
            case 5:
                ItemStack var4 = (ItemStack) watchableObject.getObject();
                outputStream.writeShort(var4.getItem().shiftedIndex);
                outputStream.writeByte(var4.stackSize);
                outputStream.writeShort(var4.getItemDamage());
                break;
            case 6:
                ChunkCoordinates var3 = (ChunkCoordinates) watchableObject.getObject();
                outputStream.writeInt(var3.x);
                outputStream.writeInt(var3.y);
                outputStream.writeInt(var3.z);
        }

    }

    public static List readWatchableObjects(DataInputStream inputStream) throws IOException {
        List<WatchableObject> objects = null;

        for (byte var2 = inputStream.readByte(); var2 != 127; var2 = inputStream.readByte()) {
            if (objects == null)
                objects = new ArrayList<>();

            int var3 = (var2 & 224) >> 5;
            int var4 = var2 & 31;
            WatchableObject var5 = null;

            switch (var3) {
                case 0:
                    var5 = new WatchableObject(var3, var4, inputStream.readByte());
                    break;
                case 1:
                    var5 = new WatchableObject(var3, var4, inputStream.readShort());
                    break;
                case 2:
                    var5 = new WatchableObject(var3, var4, inputStream.readInt());
                    break;
                case 3:
                    var5 = new WatchableObject(var3, var4, inputStream.readFloat());
                    break;
                case 4:
                    var5 = new WatchableObject(var3, var4, Packet.readString(inputStream, 64));
                    break;
                case 5:
                    short var9 = inputStream.readShort();
                    byte var10 = inputStream.readByte();
                    short var11 = inputStream.readShort();
                    var5 = new WatchableObject(var3, var4, new ItemStack(var9, var10, var11));
                    break;
                case 6:
                    int var6 = inputStream.readInt();
                    int var7 = inputStream.readInt();
                    int var8 = inputStream.readInt();
                    var5 = new WatchableObject(var3, var4, new ChunkCoordinates(var6, var7, var8));
            }

            objects.add(var5);
        }

        return objects;
    }

    public void addObject(int var1, Object var2) {
        Integer var3 = dataTypes.get(var2.getClass());

        if (var3 == null)
            throw new IllegalArgumentException("Unknown data type: " + var2.getClass());

        if (var1 > 31)
            throw new IllegalArgumentException("Data value id is too big with " + var1 + "! (Max is " + 31 + ")");

        if (this.watchedObjects.containsKey(var1))
            throw new IllegalArgumentException("Duplicate id value for " + var1 + "!");

        WatchableObject var4 = new WatchableObject(var3, var1, var2);
        this.watchedObjects.put(var1, var4);
    }

    public byte getWatchableObjectByte(int var1) {
        return (Byte) this.watchedObjects.get(var1).getObject();
    }

    public int getWatchableObjectInt(int var1) {
        return (Integer) this.watchedObjects.get(var1).getObject();
    }

    public String getWatchableObjectString(int var1) {
        return (String) this.watchedObjects.get(var1).getObject();
    }

    public void updateObject(int var1, Object o) {
        WatchableObject watchableObject = this.watchedObjects.get(var1);
        if (!o.equals(watchableObject.getObject())) {
            watchableObject.setObject(o);
            watchableObject.setWatching(true);
            this.objectChanged = true;
        }

    }

    public void writeWatchableObjects(DataOutputStream outputStream) throws IOException {
        for (WatchableObject var3 : this.watchedObjects.values()) {
            writeWatchableObject(outputStream, var3);
        }

        outputStream.writeByte(127);
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
