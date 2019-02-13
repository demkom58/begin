package net.minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class RegionFile {
    private static final byte[] emptySector = new byte[4096];
    private final File fileName;
    private final int[] offsets = new int[1024];
    private final int[] chunkTimestamps = new int[1024];
    private RandomAccessFile dataFile;
    private ArrayList<Boolean> sectorFree;
    private int sizeDelta;
    private long lastModified = 0L;

    public RegionFile(File var1) {
        this.fileName = var1;
        this.debugln("REGION LOAD " + this.fileName);
        this.sizeDelta = 0;

        try {
            if (var1.exists()) {
                this.lastModified = var1.lastModified();
            }

            this.dataFile = new RandomAccessFile(var1, "rw");
            if (this.dataFile.length() < 4096L) {
                for (int var2 = 0; var2 < 1024; ++var2) {
                    this.dataFile.writeInt(0);
                }

                for (int var7 = 0; var7 < 1024; ++var7) {
                    this.dataFile.writeInt(0);
                }

                this.sizeDelta += 8192;
            }

            if ((this.dataFile.length() & 4095L) != 0L) {
                for (int var8 = 0; (long) var8 < (this.dataFile.length() & 4095L); ++var8) {
                    this.dataFile.write(0);
                }
            }

            int var9 = (int) this.dataFile.length() / 4096;
            this.sectorFree = new ArrayList<>(var9);

            for (int var3 = 0; var3 < var9; ++var3) {
                this.sectorFree.add(Boolean.TRUE);
            }

            this.sectorFree.set(0, Boolean.FALSE);
            this.sectorFree.set(1, Boolean.FALSE);
            this.dataFile.seek(0L);

            for (int var10 = 0; var10 < 1024; ++var10) {
                int var4 = this.dataFile.readInt();
                this.offsets[var10] = var4;
                if (var4 != 0 && (var4 >> 8) + (var4 & 255) <= this.sectorFree.size()) {
                    for (int var5 = 0; var5 < (var4 & 255); ++var5) {
                        this.sectorFree.set((var4 >> 8) + var5, Boolean.FALSE);
                    }
                }
            }

            for (int var11 = 0; var11 < 1024; ++var11) {
                int var12 = this.dataFile.readInt();
                this.chunkTimestamps[var11] = var12;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized int getSizeDelta() {
        int var1 = this.sizeDelta;
        this.sizeDelta = 0;
        return var1;
    }

    private void debug(String var1) {
    }

    private void debugln(String var1) {
        this.debug(var1 + "\n");
    }

    private void debug(String var1, int var2, int var3, String var4) {
        this.debug("REGION " + var1 + " " + this.fileName.getName() + "[" + var2 + "," + var3 + "] = " + var4);
    }

    private void debug(String var1, int var2, int var3, int var4, String var5) {
        this.debug("REGION " + var1 + " " + this.fileName.getName() + "[" + var2 + "," + var3 + "] " + var4 + "B = " + var5);
    }

    private void debugln(String var1, int var2, int var3, String var4) {
        this.debug(var1, var2, var3, var4 + "\n");
    }

    public synchronized DataInputStream getChunkDataInputStream(int var1, int var2) {
        if (this.outOfBounds(var1, var2)) {
            this.debugln("READ", var1, var2, "out of bounds");
            return null;
        } else {
            try {
                int var3 = this.getOffset(var1, var2);
                if (var3 == 0) {
                    return null;
                } else {
                    int var4 = var3 >> 8;
                    int var5 = var3 & 255;
                    if (var4 + var5 > this.sectorFree.size()) {
                        this.debugln("READ", var1, var2, "invalid sector");
                        return null;
                    } else {
                        this.dataFile.seek((long) (var4 * 4096));
                        int var6 = this.dataFile.readInt();
                        if (var6 > 4096 * var5) {
                            this.debugln("READ", var1, var2, "invalid length: " + var6 + " > 4096 * " + var5);
                            return null;
                        } else {
                            byte var7 = this.dataFile.readByte();
                            if (var7 == 1) {
                                byte[] var11 = new byte[var6 - 1];
                                this.dataFile.read(var11);
                                DataInputStream var12 = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(var11)));
                                return var12;
                            } else if (var7 == 2) {
                                byte[] var8 = new byte[var6 - 1];
                                this.dataFile.read(var8);
                                DataInputStream var9 = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(var8)));
                                return var9;
                            } else {
                                this.debugln("READ", var1, var2, "unknown version " + var7);
                                return null;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                this.debugln("READ", var1, var2, "exception");
                return null;
            }
        }
    }

    public DataOutputStream getChunkDataOutputStream(int var1, int var2) {
        return this.outOfBounds(var1, var2) ? null : new DataOutputStream(new DeflaterOutputStream(new RegionFileChunkBuffer(this, var1, var2)));
    }

    protected synchronized void write(int var1, int var2, byte[] var3, int var4) {
        try {
            int var5 = this.getOffset(var1, var2);
            int var6 = var5 >> 8;
            int var7 = var5 & 255;
            int var8 = (var4 + 5) / 4096 + 1;
            if (var8 >= 256) {
                return;
            }

            if (var6 != 0 && var7 == var8) {
                this.debug("SAVE", var1, var2, var4, "rewrite");
                this.write(var6, var3, var4);
            } else {
                for (int var9 = 0; var9 < var7; ++var9) {
                    this.sectorFree.set(var6 + var9, Boolean.TRUE);
                }

                int var15 = this.sectorFree.indexOf(Boolean.TRUE);
                int var10 = 0;
                if (var15 != -1) {
                    for (int var11 = var15; var11 < this.sectorFree.size(); ++var11) {
                        if (var10 != 0) {
                            if (this.sectorFree.get(var11)) {
                                ++var10;
                            } else {
                                var10 = 0;
                            }
                        } else if (this.sectorFree.get(var11)) {
                            var15 = var11;
                            var10 = 1;
                        }

                        if (var10 >= var8) {
                            break;
                        }
                    }
                }

                if (var10 >= var8) {
                    this.debug("SAVE", var1, var2, var4, "reuse");
                    var6 = var15;
                    this.setOffset(var1, var2, var15 << 8 | var8);

                    for (int var17 = 0; var17 < var8; ++var17) {
                        this.sectorFree.set(var6 + var17, Boolean.FALSE);
                    }

                    this.write(var6, var3, var4);
                } else {
                    this.debug("SAVE", var1, var2, var4, "grow");
                    this.dataFile.seek(this.dataFile.length());
                    var6 = this.sectorFree.size();

                    for (int var16 = 0; var16 < var8; ++var16) {
                        this.dataFile.write(emptySector);
                        this.sectorFree.add(Boolean.FALSE);
                    }

                    this.sizeDelta += 4096 * var8;
                    this.write(var6, var3, var4);
                    this.setOffset(var1, var2, var6 << 8 | var8);
                }
            }

            this.setChunkTimestamp(var1, var2, (int) (System.currentTimeMillis() / 1000L));
        } catch (IOException var12) {
            var12.printStackTrace();
        }

    }

    private void write(int var1, byte[] var2, int var3) throws IOException {
        this.debugln(" " + var1);
        this.dataFile.seek((long) (var1 * 4096));
        this.dataFile.writeInt(var3 + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(var2, 0, var3);
    }

    private boolean outOfBounds(int var1, int var2) {
        return var1 < 0 || var1 >= 32 || var2 < 0 || var2 >= 32;
    }

    private int getOffset(int var1, int var2) {
        return this.offsets[var1 + var2 * 32];
    }

    public boolean isChunkSaved(int var1, int var2) {
        return this.getOffset(var1, var2) != 0;
    }

    private void setOffset(int var1, int var2, int var3) throws IOException {
        this.offsets[var1 + var2 * 32] = var3;
        this.dataFile.seek((long) ((var1 + var2 * 32) * 4));
        this.dataFile.writeInt(var3);
    }

    private void setChunkTimestamp(int var1, int var2, int var3) throws IOException {
        this.chunkTimestamps[var1 + var2 * 32] = var3;
        this.dataFile.seek((long) (4096 + (var1 + var2 * 32) * 4));
        this.dataFile.writeInt(var3);
    }

    public void close() throws IOException {
        this.dataFile.close();
    }
}
