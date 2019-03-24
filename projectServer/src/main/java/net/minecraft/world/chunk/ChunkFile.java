package net.minecraft.world.chunk;

import java.io.File;
import java.util.regex.Matcher;

public class ChunkFile implements Comparable {
    private final File chunkFile;
    private final int x;
    private final int z;

    public ChunkFile(File chunkFile) {
        this.chunkFile = chunkFile;

        Matcher matcher = ChunkFilePattern.PATTERN.matcher(chunkFile.getName());
        if (matcher.matches()) {
            this.x = Integer.parseInt(matcher.group(1), 36);
            this.z = Integer.parseInt(matcher.group(2), 36);
        } else {
            this.x = 0;
            this.z = 0;
        }

    }

    public int compareTo(ChunkFile chunkFile) {
        int var2 = this.x >> 5;
        int var3 = chunkFile.x >> 5;

        if (var2 == var3) {
            int var4 = this.z >> 5;
            int var5 = chunkFile.z >> 5;
            return var4 - var5;
        }

        return var2 - var3;
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
    public int compareTo(Object o) {
        return compareTo((ChunkFile) o);
    }
}
