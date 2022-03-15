package net.potion.world.storage;

import net.potion.item.MapDataBase;
import net.potion.nbt.CompressedStreamTools;
import net.potion.nbt.Tag;
import net.potion.nbt.TagCompound;
import net.potion.nbt.TagShort;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage {
    private final ISaveHandler saveHandler;
    private final Map<String, MapDataBase> loadedDataMap = new HashMap<>();
    private final List<MapDataBase> loadedDataList = new ArrayList<>();
    private final Map<String, Short> idCounts = new HashMap<>();

    public MapStorage(ISaveHandler saveHandler) {
        this.saveHandler = saveHandler;
        this.loadIdCounts();
    }

    public MapDataBase loadData(Class var1, String var2) {
        MapDataBase var3 = this.loadedDataMap.get(var2);
        if (var3 != null) {
            return var3;
        }

        if (this.saveHandler != null) {
            try {
                File var4 = this.saveHandler.getFile(var2);
                if (var4 != null && var4.exists()) {
                    try {
                        var3 = (MapDataBase) var1.getConstructor(String.class).newInstance(var2);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate " + var1.toString(), e);
                    }

                    FileInputStream var5 = new FileInputStream(var4);
                    TagCompound var6 = CompressedStreamTools.readGzipCompound(var5);
                    var5.close();
                    var3.readFromNBT(var6.getCompoundTag("data"));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (var3 != null) {
            this.loadedDataMap.put(var2, var3);
            this.loadedDataList.add(var3);
        }

        return var3;
    }

    public void setData(String var1, MapDataBase var2) {
        if (var2 == null) {
            throw new RuntimeException("Can't set null data");
        }

        if (this.loadedDataMap.containsKey(var1)) {
            this.loadedDataList.remove(this.loadedDataMap.remove(var1));
        }

        this.loadedDataMap.put(var1, var2);
        this.loadedDataList.add(var2);
    }

    public void saveAllData() {
        for (int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
            MapDataBase var2 = this.loadedDataList.get(var1);
            if (var2.isDirty()) {
                this.saveData(var2);
                var2.setDirty(false);
            }
        }

    }

    private void saveData(MapDataBase var1) {
        if (this.saveHandler != null) {
            try {
                File var2 = this.saveHandler.getFile(var1.mapId);
                if (var2 != null) {
                    TagCompound var3 = new TagCompound();
                    var1.writeToNBT(var3);
                    TagCompound var4 = new TagCompound();
                    var4.setCompoundTag("data", var3);
                    FileOutputStream var5 = new FileOutputStream(var2);
                    CompressedStreamTools.writeGzipCompound(var4, var5);
                    var5.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void loadIdCounts() {
        try {
            this.idCounts.clear();
            if (this.saveHandler == null) {
                return;
            }

            File var1 = this.saveHandler.getFile("idcounts");
            if (var1 != null && var1.exists()) {
                DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
                TagCompound var3 = CompressedStreamTools.readCompound(var2);
                var2.close();

                for (Tag var5 : var3.tags()) {
                    if (var5 instanceof TagShort) {
                        TagShort var6 = (TagShort) var5;
                        String var7 = var6.getKey();
                        short var8 = var6.shortValue;
                        this.idCounts.put(var7, var8);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getUniqueDataId(String var1) {
        Short var2 = this.idCounts.get(var1);
        if (var2 == null) {
            var2 = 0;
        } else {
            var2 = (short) (var2 + 1);
        }

        this.idCounts.put(var1, var2);
        if (this.saveHandler == null) {
            return var2;
        }

        try {
            File var3 = this.saveHandler.getFile("idcounts");
            if (var3 != null) {
                TagCompound var4 = new TagCompound();

                for (String var6 : this.idCounts.keySet()) {
                    short var7 = this.idCounts.get(var6);
                    var4.setShort(var6, var7);
                }

                DataOutputStream var10 = new DataOutputStream(new FileOutputStream(var3));
                CompressedStreamTools.writeCompound(var4, var10);
                var10.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return var2;
    }
}
