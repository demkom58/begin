package net.minecraft;

import util.MathHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class SaveConverterMcRegion extends SaveFormatOld {
    public SaveConverterMcRegion(File var1) {
        super(var1);
    }

    public String func_22178_a() {
        return "Scaevolus' McRegion";
    }

    public List<SaveFormatComparator> func_22176_b() {
        List<SaveFormatComparator> loaded = new ArrayList<>();
        File[] files = this.field_22180_a.listFiles();

        if (files == null) {
            return loaded;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String name = file.getName();
                WorldInfo worldInfo = this.func_22173_b(name);
                if (worldInfo != null) {
                    boolean badVersion = worldInfo.getSaveVersion() != 19132;
                    String worldName = worldInfo.getWorldName();
                    if (worldName == null || MathHelper.stringNullOrLengthZero(worldName)) {
                        worldName = name;
                    }

                    loaded.add(new SaveFormatComparator(name, worldName, worldInfo.getLastTimePlayed(), worldInfo.getSizeOnDisk(), badVersion));
                }
            }
        }

        return loaded;
    }

    public void flushCache() {
        RegionFileCache.func_22192_a();
    }

    public ISaveHandler getSaveLoader(String var1, boolean var2) {
        return new SaveOldDir(this.field_22180_a, var1, var2);
    }

    public boolean isOldMapFormat(String var1) {
        WorldInfo var2 = this.func_22173_b(var1);
        return var2 != null && var2.getSaveVersion() == 0;
    }

    public boolean convertMapFormat(String var1, IProgressUpdate var2) {
        var2.setLoadingProgress(0);
        ArrayList var3 = new ArrayList();
        ArrayList var4 = new ArrayList();
        ArrayList var5 = new ArrayList();
        ArrayList var6 = new ArrayList();
        File var7 = new File(this.field_22180_a, var1);
        File var8 = new File(var7, "DIM-1");
        System.out.println("Scanning folders...");
        this.func_22183_a(var7, var3, var4);
        if (var8.exists()) {
            this.func_22183_a(var8, var5, var6);
        }

        int var9 = var3.size() + var5.size() + var4.size() + var6.size();
        System.out.println("Total conversion count is " + var9);
        this.func_22181_a(var7, var3, 0, var9, var2);
        this.func_22181_a(var8, var5, var3.size(), var9, var2);
        WorldInfo var10 = this.func_22173_b(var1);
        var10.setSaveVersion(19132);
        ISaveHandler var11 = this.getSaveLoader(var1, false);
        var11.saveWorldInfo(var10);
        this.func_22182_a(var4, var3.size() + var5.size(), var9, var2);
        if (var8.exists()) {
            this.func_22182_a(var6, var3.size() + var5.size() + var4.size(), var9, var2);
        }

        return true;
    }

    private void func_22183_a(File var1, ArrayList var2, ArrayList var3) {
        ChunkFolderPattern var4 = new ChunkFolderPattern();
        ChunkFilePattern var5 = new ChunkFilePattern();
        File[] var6 = var1.listFiles(var4);

        for (File var10 : var6) {
            var3.add(var10);
            File[] var11 = var10.listFiles(var4);

            for (File var15 : var11) {
                File[] var16 = var15.listFiles(var5);

                for (File var20 : var16) {
                    var2.add(new ChunkFile(var20));
                }
            }
        }

    }

    private void func_22181_a(File var1, List<ChunkFile> var2, int var3, int var4, IProgressUpdate var5) {
        Collections.sort(var2);
        byte[] var6 = new byte[4096];

        for (ChunkFile var8 : var2) {
            int var9 = var8.func_22323_b();
            int var10 = var8.func_22321_c();
            RegionFile var11 = RegionFileCache.func_22193_a(var1, var9, var10);
            if (!var11.func_22202_c(var9 & 31, var10 & 31)) {
                try {
                    DataInputStream var12 = new DataInputStream(new GZIPInputStream(new FileInputStream(var8.func_22324_a())));
                    DataOutputStream var13 = var11.getChunkDataOutputStream(var9 & 31, var10 & 31);
                    int var14 = 0;

                    while ((var14 = var12.read(var6)) != -1) {
                        var13.write(var6, 0, var14);
                    }

                    var13.close();
                    var12.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ++var3;
            int var16 = (int) Math.round(100.0D * (double) var3 / (double) var4);
            var5.setLoadingProgress(var16);
        }

        RegionFileCache.func_22192_a();
    }

    private void func_22182_a(List<File> var1, int var2, int var3, IProgressUpdate var4) {
        for (File var6 : var1) {
            File[] var7 = var6.listFiles();
            func_22179_a(var7);
            var6.delete();
            ++var2;
            int var8 = (int) Math.round(100.0D * (double) var2 / (double) var3);
            var4.setLoadingProgress(var8);
        }

    }
}
