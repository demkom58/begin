package net.minecraft.world.storage;

import java.io.ByteArrayOutputStream;

class RegionFileChunkBuffer extends ByteArrayOutputStream {
    final RegionFile regionFile;
    private int x;
    private int z;

    public RegionFileChunkBuffer(RegionFile regionFile, int x, int z) {
        super(8096);
        this.regionFile = regionFile;
        this.x = x;
        this.z = z;
    }

    public void close() {
        this.regionFile.write(this.x, this.z, this.buf, this.count);
    }
}
