package net.minecraft.world.chunk;

import java.io.File;
import java.util.regex.Matcher;

public class ChunkFile implements Comparable<ChunkFile> {
    private final File chunkFile;
    private final int x;
    private final int z;

    public ChunkFile(File var1) {
        this.chunkFile = var1;
        Matcher var2 = ChunkFilePattern.PATTERN.matcher(var1.getName());
        if (var2.matches()) {
            this.x = Integer.parseInt(var2.group(1), 36);
            this.z = Integer.parseInt(var2.group(2), 36);
        } else {
            this.x = 0;
            this.z = 0;
        }

    }

    public File getChunkFile() {
        return this.chunkFile;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public int compareTo(ChunkFile var1) {
        int var2 = this.x >> 5;
        int var3 = var1.x >> 5;
        if (var2 == var3) {
            int var4 = this.z >> 5;
            int var5 = var1.z >> 5;
            return var4 - var5;
        } else {
            return var2 - var3;
        }
    }
}
