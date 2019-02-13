package net.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class RegionFileCache {
    private static final Map<File, Reference> field_22125_a = new HashMap<>();

    public static synchronized RegionFile func_22123_a(File var0, int var1, int var2) {
        File var3 = new File(var0, "region");
        File var4 = new File(var3, "r." + (var1 >> 5) + "." + (var2 >> 5) + ".mcr");
        Reference var5 = field_22125_a.get(var4);
        if (var5 != null) {
            RegionFile var6 = (RegionFile) var5.get();
            if (var6 != null) {
                return var6;
            }
        }

        if (!var3.exists()) {
            var3.mkdirs();
        }

        if (field_22125_a.size() >= 256) {
            func_22122_a();
        }

        RegionFile var7 = new RegionFile(var4);
        field_22125_a.put(var4, new SoftReference(var7));
        return var7;
    }

    public static synchronized void func_22122_a() {
        for (Reference var1 : field_22125_a.values()) {
            try {
                RegionFile var2 = (RegionFile) var1.get();
                if (var2 != null) {
                    var2.close();
                }
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

        field_22125_a.clear();
    }

    public static int func_22121_b(File var0, int var1, int var2) {
        RegionFile var3 = func_22123_a(var0, var1, var2);
        return var3.getSizeDelta();
    }

    public static DataInputStream func_22124_c(File var0, int var1, int var2) {
        RegionFile var3 = func_22123_a(var0, var1, var2);
        return var3.getChunkDataInputStream(var1 & 31, var2 & 31);
    }

    public static DataOutputStream func_22120_d(File var0, int var1, int var2) {
        RegionFile var3 = func_22123_a(var0, var1, var2);
        return var3.getChunkDataOutputStream(var1 & 31, var2 & 31);
    }
}
