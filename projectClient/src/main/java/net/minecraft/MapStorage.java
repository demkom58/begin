package net.minecraft;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage {
    private ISaveHandler field_28191_a;
    private Map loadedDataMap = new HashMap();
    private List loadedDataList = new ArrayList();
    private Map<String, Short> idCounts = new HashMap<>();

    public MapStorage(ISaveHandler var1) {
        this.field_28191_a = var1;
        this.loadIdCounts();
    }

    public MapDataBase loadData(Class var1, String var2) {
        MapDataBase var3 = (MapDataBase) this.loadedDataMap.get(var2);
        if (var3 != null) {
            return var3;
        } else {
            if (this.field_28191_a != null) {
                try {
                    File var4 = this.field_28191_a.func_28113_a(var2);
                    if (var4 != null && var4.exists()) {
                        try {
                            var3 = (MapDataBase) var1.getConstructor(String.class).newInstance(var2);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to instantiate " + var1.toString(), e);
                        }

                        FileInputStream var5 = new FileInputStream(var4);
                        NBTTagCompound var6 = CompressedStreamTools.func_1138_a(var5);
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
    }

    public void setData(String var1, MapDataBase var2) {
        if (var2 == null) {
            throw new RuntimeException("Can't set null data");
        } else {
            if (this.loadedDataMap.containsKey(var1)) {
                this.loadedDataList.remove(this.loadedDataMap.remove(var1));
            }

            this.loadedDataMap.put(var1, var2);
            this.loadedDataList.add(var2);
        }
    }

    public void saveAllData() {
        for (int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
            MapDataBase var2 = (MapDataBase) this.loadedDataList.get(var1);
            if (var2.isDirty()) {
                this.saveData(var2);
                var2.setDirty(false);
            }
        }

    }

    private void saveData(MapDataBase var1) {
        if (this.field_28191_a != null) {
            try {
                File var2 = this.field_28191_a.func_28113_a(var1.field_28168_a);
                if (var2 != null) {
                    NBTTagCompound var3 = new NBTTagCompound();
                    var1.writeToNBT(var3);
                    NBTTagCompound var4 = new NBTTagCompound();
                    var4.setCompoundTag("data", var3);
                    FileOutputStream var5 = new FileOutputStream(var2);
                    CompressedStreamTools.writeGzippedCompoundToOutputStream(var4, var5);
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
            if (this.field_28191_a == null) {
                return;
            }

            File var1 = this.field_28191_a.func_28113_a("idcounts");
            if (var1 != null && var1.exists()) {
                DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
                NBTTagCompound var3 = CompressedStreamTools.func_1141_a(var2);
                var2.close();

                for (NBTBase var5 : var3.tags()) {
                    if (var5 instanceof NBTTagShort) {
                        NBTTagShort var6 = (NBTTagShort) var5;
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
        if (this.field_28191_a == null) {
            return var2.shortValue();
        } else {
            try {
                File var3 = this.field_28191_a.func_28113_a("idcounts");
                if (var3 != null) {
                    NBTTagCompound var4 = new NBTTagCompound();

                    for (String var6 : this.idCounts.keySet()) {
                        short var7 = this.idCounts.get(var6).shortValue();
                        var4.setShort(var6, var7);
                    }

                    DataOutputStream var10 = new DataOutputStream(new FileOutputStream(var3));
                    CompressedStreamTools.func_1139_a(var4, var10);
                    var10.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return var2.shortValue();
        }
    }
}
