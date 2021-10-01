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
    private ISaveHandler saveHandler;
    private Map<String, MapDataBase> field_28179_b = new HashMap<>();
    private List<MapDataBase> field_28182_c = new ArrayList<>();
    private Map<String, Short> field_28181_d = new HashMap<>();

    public MapStorage(ISaveHandler saveHandler) {
        this.saveHandler = saveHandler;
        this.func_28174_b();
    }

    public MapDataBase func_28178_a(Class var1, String var2) {
        MapDataBase dataBase = this.field_28179_b.get(var2);
        if (dataBase != null) {
            return dataBase;
        }

        if (this.saveHandler != null) {
            try {
                File var4 = this.saveHandler.func_28111_b(var2);
                if (var4 != null && var4.exists()) {
                    try {
                        dataBase = (MapDataBase) var1.getConstructor(String.class).newInstance(var2);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate " + var1.toString(), e);
                    }

                    FileInputStream var5 = new FileInputStream(var4);
                    TagCompound var6 = CompressedStreamTools.readGzipCompound(var5);
                    var5.close();
                    dataBase.func_28148_a(var6.getCompoundTag("data"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (dataBase != null) {
            this.field_28179_b.put(var2, dataBase);
            this.field_28182_c.add(dataBase);
        }

        return dataBase;
    }

    public void func_28177_a(String var1, MapDataBase var2) {
        if (var2 == null) {
            throw new RuntimeException("Can't set null data");
        } else {
            if (this.field_28179_b.containsKey(var1)) {
                this.field_28182_c.remove(this.field_28179_b.remove(var1));
            }

            this.field_28179_b.put(var1, var2);
            this.field_28182_c.add(var2);
        }
    }

    public void saveAllData() {
        for (int var1 = 0; var1 < this.field_28182_c.size(); ++var1) {
            MapDataBase var2 = this.field_28182_c.get(var1);
            if (var2.func_28150_b()) {
                this.func_28175_a(var2);
                var2.func_28149_a(false);
            }
        }

    }

    private void func_28175_a(MapDataBase var1) {
        if (this.saveHandler != null) {
            try {
                File var2 = this.saveHandler.func_28111_b(var1.field_28152_a);
                if (var2 != null) {
                    TagCompound var3 = new TagCompound();
                    var1.func_28147_b(var3);
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

    private void func_28174_b() {
        try {
            this.field_28181_d.clear();
            if (this.saveHandler == null) {
                return;
            }

            File var1 = this.saveHandler.func_28111_b("idcounts");
            if (var1 != null && var1.exists()) {
                DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
                TagCompound var3 = CompressedStreamTools.readCompound(var2);
                var2.close();

                for (Tag var5 : var3.tags()) {
                    if (var5 instanceof TagShort) {
                        TagShort var6 = (TagShort) var5;
                        String var7 = var6.getKey();
                        short var8 = var6.shortValue;
                        this.field_28181_d.put(var7, var8);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int func_28173_a(String var1) {
        Short var2 = this.field_28181_d.get(var1);
        if (var2 == null) {
            var2 = 0;
        } else {
            var2 = (short) (var2 + 1);
        }

        this.field_28181_d.put(var1, var2);
        if (this.saveHandler == null) {
            return var2;
        } else {
            try {
                File var3 = this.saveHandler.func_28111_b("idcounts");
                if (var3 != null) {
                    TagCompound var4 = new TagCompound();

                    for (String var6 : this.field_28181_d.keySet()) {
                        short var7 = this.field_28181_d.get(var6);
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
}
