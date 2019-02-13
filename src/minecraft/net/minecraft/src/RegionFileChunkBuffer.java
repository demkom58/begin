package net.minecraft.src;

import java.io.ByteArrayOutputStream;

class RegionFileChunkBuffer extends ByteArrayOutputStream {
    // $FF: synthetic field
    final RegionFile field_22284_a;
    private int field_22283_b;
    private int field_22285_c;

    public RegionFileChunkBuffer(RegionFile var1, int var2, int var3) {
        super(8096);
        this.field_22284_a = var1;
        this.field_22283_b = var2;
        this.field_22285_c = var3;
    }

    public void close() {
        this.field_22284_a.write(this.field_22283_b, this.field_22285_c, this.buf, this.count);
    }
}
