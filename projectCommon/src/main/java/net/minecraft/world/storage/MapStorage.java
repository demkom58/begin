package net.minecraft.world.storage;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.minecraft.item.MapDataBase;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.TagShort;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage {
    private final ISaveHandler saveHandler;
    private final Map<String, MapDataBase> loadedDataMap = new HashMap<>();
    private final List<MapDataBase> loadedDataList = new ArrayList<>();
    private final Object2ShortMap<String> idCounts = new Object2ShortOpenHashMap<>();

    public MapStorage(ISaveHandler saveHandler) {
        this.saveHandler = saveHandler;
        this.loadIdCounts();
    }

    public MapDataBase loadData(Class var1, String var2) {
        MapDataBase map = this.loadedDataMap.get(var2);
        if (map != null) {
            return map;
        }

        if (this.saveHandler != null) {
            try {
                File var4 = this.saveHandler.getFile(var2);
                if (var4 != null && var4.exists()) {
                    try {
                        map = (MapDataBase) var1.getConstructor(String.class).newInstance(var2);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate " + var1.toString(), e);
                    }

                    FileInputStream var5 = new FileInputStream(var4);
                    TagCompound var6 = CompressedStreamTools.readGzipCompound(var5);
                    var5.close();
                    map.readFromNBT(var6.getCompoundTag("data"));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (map != null) {
            this.loadedDataMap.put(var2, map);
            this.loadedDataList.add(map);
        }

        return map;
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
        if (this.saveHandler == null) {
            return;
        }

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
                    if (var5 instanceof TagShort var6) {
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
        short var2 = (short) (this.idCounts.getOrDefault(var1, (short) -1) + 1);
        this.idCounts.put(var1, var2);
        if (this.saveHandler == null) {
            return var2;
        }

        try {
            File var3 = this.saveHandler.getFile("idcounts");
            if (var3 != null) {
                TagCompound var4 = new TagCompound();

                for (String var6 : this.idCounts.keySet()) {
                    short var7 = this.idCounts.getShort(var6);
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
