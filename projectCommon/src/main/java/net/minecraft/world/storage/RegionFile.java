package net.minecraft.world.storage;

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class RegionFile {
    private static final byte[] EMPTY_SECTOR = new byte[4096];

    private final File file;
    private final int[] offsets = new int[1024];
    private final int[] chunkTimestamps = new int[1024];
    private RandomAccessFile dataFile;
    private BooleanList sectorFree;
    private int sizeDelta;
    private long lastModified = 0L;

    public RegionFile(File file) {
        this.file = file;
        this.debugln("REGION LOAD " + this.file);
        this.sizeDelta = 0;

        try {
            if (file.exists()) {
                this.lastModified = file.lastModified();
            }

            this.dataFile = new RandomAccessFile(file, "rw");
            if (this.dataFile.length() < 4096L) {
                for (int i = 0; i < 1024; ++i) {
                    this.dataFile.writeInt(0);
                }

                for (int i = 0; i < 1024; ++i) {
                    this.dataFile.writeInt(0);
                }

                this.sizeDelta += 8192;
            }

            if ((this.dataFile.length() & 4095L) != 0L) {
                for (int var8 = 0; (long) var8 < (this.dataFile.length() & 4095L); ++var8) {
                    this.dataFile.write(0);
                }
            }

            int sectors = (int) this.dataFile.length() / 4096;
            this.sectorFree = new BooleanArrayList(sectors);

            for (int i = 0; i < sectors; ++i) {
                this.sectorFree.add(true);
            }

            this.sectorFree.set(0, false);
            this.sectorFree.set(1, false);
            this.dataFile.seek(0L);

            for (int i = 0; i < 1024; ++i) {
                int read = this.dataFile.readInt();
                this.offsets[i] = read;
                if (read != 0 && (read >> 8) + (read & 255) <= this.sectorFree.size()) {
                    for (int j = 0; j < (read & 255); ++j) {
                        this.sectorFree.set((read >> 8) + j, false);
                    }
                }
            }

            for (int i = 0; i < 1024; ++i) {
                int timestamp = this.dataFile.readInt();
                this.chunkTimestamps[i] = timestamp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized int resetSizeDelta() {
        int prev = this.sizeDelta;
        this.sizeDelta = 0;
        return prev;
    }

    private void debug(String var1) {
    }

    private void debugln(String var1) {
        this.debug(var1 + "\n");
    }

    private void debug(String var1, int var2, int var3, String var4) {
        this.debug("REGION " + var1 + " " + this.file.getName() + "[" + var2 + "," + var3 + "] = " + var4);
    }

    private void debug(String var1, int var2, int var3, int var4, String var5) {
        this.debug("REGION " + var1 + " " + this.file.getName() + "[" + var2 + "," + var3 + "] " + var4 + "B = " + var5);
    }

    private void debugln(String var1, int var2, int var3, String var4) {
        this.debug(var1, var2, var3, var4 + "\n");
    }

    public synchronized DataInputStream getChunkDataInputStream(int x, int z) {
        if (this.outOfBounds(x, z)) {
            this.debugln("READ", x, z, "out of bounds");
            return null;
        }

        try {
            int offset = this.getOffset(x, z);
            if (offset == 0) {
                return null;
            }

            int seekValue = offset >> 8;
            int var5 = offset & 255;
            if (seekValue + var5 > this.sectorFree.size()) {
                this.debugln("READ", x, z, "invalid sector");
                return null;
            }

            this.dataFile.seek(seekValue * 4096L);
            int length = this.dataFile.readInt();
            if (length > 4096 * var5) {
                this.debugln("READ", x, z, "invalid length: " + length + " > 4096 * " + var5);
                return null;
            }

            byte fileType = this.dataFile.readByte();
            if (fileType == 1) {
                byte[] src = new byte[length - 1];
                this.dataFile.read(src);
                return new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(src)));
            } else if (fileType == 2) {
                byte[] src = new byte[length - 1];
                this.dataFile.read(src);
                return new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(src)));
            } else {
                this.debugln("READ", x, z, "unknown version " + fileType);
                return null;
            }
        } catch (IOException e) {
            this.debugln("READ", x, z, "exception");
            return null;
        }
    }

    public DataOutputStream getChunkDataOutputStream(int x, int z) {
        return this.outOfBounds(x, z)
                ? null
                : new DataOutputStream(new DeflaterOutputStream(new RegionFileChunkBuffer(this, x, z)));
    }

    protected synchronized void write(int x, int z, byte[] value, int len) {
        try {
            int offset = this.getOffset(x, z);
            int seekValue = offset >> 8;

            int var7 = offset & 255;
            int var8 = (len + 5) / 4096 + 1;
            if (var8 >= 256) {
                return;
            }

            if (seekValue != 0 && var7 == var8) {
                this.debug("SAVE", x, z, len, "rewrite");
                this.write(seekValue, value, len);
            } else {
                for (int var9 = 0; var9 < var7; ++var9) {
                    this.sectorFree.set(seekValue + var9, true);
                }

                int var15 = this.sectorFree.indexOf(true);
                int var10 = 0;
                if (var15 != -1) {
                    for (int var11 = var15; var11 < this.sectorFree.size(); ++var11) {
                        if (var10 != 0) {
                            if (this.sectorFree.getBoolean(var11)) {
                                ++var10;
                            } else {
                                var10 = 0;
                            }
                        } else if (this.sectorFree.getBoolean(var11)) {
                            var15 = var11;
                            var10 = 1;
                        }

                        if (var10 >= var8) {
                            break;
                        }
                    }
                }

                if (var10 >= var8) {
                    this.debug("SAVE", x, z, len, "reuse");
                    seekValue = var15;
                    this.setOffset(x, z, var15 << 8 | var8);

                    for (int var17 = 0; var17 < var8; ++var17) {
                        this.sectorFree.set(seekValue + var17, false);
                    }

                    this.write(seekValue, value, len);
                } else {
                    this.debug("SAVE", x, z, len, "grow");
                    this.dataFile.seek(this.dataFile.length());
                    seekValue = this.sectorFree.size();

                    for (int var16 = 0; var16 < var8; ++var16) {
                        this.dataFile.write(EMPTY_SECTOR);
                        this.sectorFree.add(false);
                    }

                    this.sizeDelta += 4096 * var8;
                    this.write(seekValue, value, len);
                    this.setOffset(x, z, seekValue << 8 | var8);
                }
            }

            this.setChunkTimestamp(x, z, (int) (System.currentTimeMillis() / 1000L));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void write(int offset, byte[] value, int len) throws IOException {
        this.debugln(" " + offset);
        this.dataFile.seek(offset * 4096L);
        this.dataFile.writeInt(len + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(value, 0, len);
    }

    private boolean outOfBounds(int x, int z) {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }

    private int getOffset(int x, int z) {
        return this.offsets[x + z * 32];
    }

    public boolean isChunkSaved(int x, int z) {
        return this.getOffset(x, z) != 0;
    }

    public int getSizeDelta() {
        return sizeDelta;
    }

    private void setOffset(int x, int z, int offset) throws IOException {
        this.offsets[x + z * 32] = offset;
        this.dataFile.seek((x + z * 32L) * 4);
        this.dataFile.writeInt(offset);
    }

    private void setChunkTimestamp(int x, int z, int timestamp) throws IOException {
        this.chunkTimestamps[x + z * 32] = timestamp;
        this.dataFile.seek(4096 + (x + z * 32L) * 4);
        this.dataFile.writeInt(timestamp);
    }

    public void close() throws IOException {
        this.dataFile.close();
    }
}
