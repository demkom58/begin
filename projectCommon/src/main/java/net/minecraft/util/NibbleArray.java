package net.minecraft.util;

public class NibbleArray {
    public final byte[] data;

    public NibbleArray(int var1) {
        this.data = new byte[var1 >> 1];
    }

    public NibbleArray(byte[] data) {
        this.data = data;
    }

    public int getNibble(int x, int y, int z) {
        int xyz = x << 11 | z << 7 | y;
        int idx = xyz >> 1;
        int var6 = xyz & 1;
        return var6 == 0 ? this.data[idx] & 15 : this.data[idx] >> 4 & 15;
    }

    public void setNibble(int x, int y, int z, int value) {
        int xyz = x << 11 | z << 7 | y;
        int idx = xyz >> 1;
        int var7 = xyz & 1;
        if (var7 == 0) {
            this.data[idx] = (byte) (this.data[idx] & 240 | value & 15);
        } else {
            this.data[idx] = (byte) (this.data[idx] & 15 | (value & 15) << 4);
        }

    }

    public boolean isValid() {
        return this.data != null;
    }
}
