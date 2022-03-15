package net.potion.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class RegionFileCache {
    private static final Map<File, Reference<RegionFile>> CACHE = new HashMap<>();

    public static synchronized RegionFile getRegionFile(File var0, int var1, int var2) {
        File var3 = new File(var0, "region");
        File var4 = new File(var3, "r." + (var1 >> 5) + "." + (var2 >> 5) + ".mcr");
        Reference<RegionFile> cached = CACHE.get(var4);
        if (cached != null) {
            RegionFile regionFile = cached.get();
            if (regionFile != null) {
                return regionFile;
            }
        }

        if (!var3.exists()) {
            var3.mkdirs();
        }

        if (CACHE.size() >= 256) {
            clear();
        }

        RegionFile var7 = new RegionFile(var4);
        CACHE.put(var4, new SoftReference<>(var7));
        return var7;
    }

    public static synchronized void clear() {
        for (Reference<RegionFile> var1 : CACHE.values()) {
            try {
                RegionFile var2 = var1.get();
                if (var2 != null) {
                    var2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CACHE.clear();
    }

    public static int getSizeDelta(File var0, int var1, int var2) {
        RegionFile var3 = getRegionFile(var0, var1, var2);
        return var3.getSizeDelta();
    }

    public static DataInputStream getChunkInputStream(File var0, int var1, int var2) {
        RegionFile var3 = getRegionFile(var0, var1, var2);
        return var3.getChunkDataInputStream(var1 & 31, var2 & 31);
    }

    public static DataOutputStream getChunkOutputStream(File var0, int var1, int var2) {
        RegionFile var3 = getRegionFile(var0, var1, var2);
        return var3.getChunkDataOutputStream(var1 & 31, var2 & 31);
    }
}
