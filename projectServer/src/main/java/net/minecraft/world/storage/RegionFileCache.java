package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class RegionFileCache {
    private static final Map<File, Reference<RegionFile>> REFERENCE_MAP = new HashMap<>();

    public static synchronized RegionFile getRegionFile(File file, int x, int z) {
        File regFile = new File(file, "region");
        File mcrFile = new File(regFile, "r." + (x >> 5) + "." + (z >> 5) + ".mcr");

        Reference<RegionFile> regionFileReference = REFERENCE_MAP.get(mcrFile);
        if (regionFileReference != null) {
            RegionFile regionFile = regionFileReference.get();
            if (regionFile != null) {
                return regionFile;
            }
        }

        if (!regFile.exists()) {
            regFile.mkdirs();
        }

        if (REFERENCE_MAP.size() >= 256) {
            clear();
        }

        RegionFile regionFileObj = new RegionFile(mcrFile);
        REFERENCE_MAP.put(mcrFile, new SoftReference<>(regionFileObj));
        return regionFileObj;
    }

    public static synchronized void clear() {
        for (Reference<RegionFile> reference : REFERENCE_MAP.values()) {
            try {
                RegionFile regionFile = reference.get();
                if (regionFile != null) {
                    regionFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        REFERENCE_MAP.clear();
    }

    public static int getSizeDelta(File file, int x, int z) {
        return getRegionFile(file, x, z).getSizeDelta();
    }

    public static DataInputStream getChunkInputStream(File file, int x, int z) {
        return getRegionFile(file, x, z).getChunkDataInputStream(x & 31, z & 31);
    }

    public static DataOutputStream getChunkOutputStream(File file, int x, int z) {
        return getRegionFile(file, x, z).getChunkDataOutputStream(x & 31, z & 31);
    }
}
