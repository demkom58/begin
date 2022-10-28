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

    public static synchronized RegionFile getRegionFile(File worldDir, int x, int z) {
        File regionsDir = new File(worldDir, "region");
        File file = new File(regionsDir, "r." + (x >> 5) + "." + (z >> 5) + ".mcr");

        Reference<RegionFile> cached = CACHE.get(file);
        if (cached != null) {
            RegionFile regionFile = cached.get();
            if (regionFile != null) {
                return regionFile;
            }
        }

        if (!regionsDir.exists()) {
            regionsDir.mkdirs();
        }

        if (CACHE.size() >= 256) {
            clear();
        }

        RegionFile regionFile = new RegionFile(file);
        CACHE.put(file, new SoftReference<>(regionFile));
        return regionFile;
    }

    public static synchronized void clear() {
        for (Reference<RegionFile> reference : CACHE.values()) {
            try {
                RegionFile regionFile = reference.get();
                if (regionFile != null) {
                    regionFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CACHE.clear();
    }

    public static int getSizeDelta(File file, int x, int z) {
        RegionFile regionFile = getRegionFile(file, x, z);
        return regionFile.getSizeDelta();
    }

    public static DataInputStream getChunkInputStream(File file, int x, int z) {
        RegionFile regionFile = getRegionFile(file, x, z);
        return regionFile.getChunkDataInputStream(x & 31, z & 31);
    }

    public static DataOutputStream getChunkOutputStream(File file, int x, int z) {
        RegionFile regionFile = getRegionFile(file, x, z);
        return regionFile.getChunkDataOutputStream(x & 31, z & 31);
    }
}
