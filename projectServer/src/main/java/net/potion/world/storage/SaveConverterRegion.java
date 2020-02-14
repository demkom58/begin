package net.potion.world.storage;

import net.potion.util.IProgressUpdatable;
import net.potion.world.WorldInfo;
import net.potion.world.chunk.ChunkFile;
import net.potion.world.chunk.ChunkFilePattern;
import net.potion.world.chunk.ChunkFolderPattern;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class SaveConverterRegion extends SaveFormatOld {
    public SaveConverterRegion(File file) {
        super(file);
    }

    @Override
    public ISaveHandler func_22105_a(String var1, boolean var2) {
        return new SaveOldDir(this.field_22106_a, var1, var2);
    }

    @Override
    public boolean isOldSaveType(String var1) {
        WorldInfo var2 = this.getWorldInfo(var1);
        return var2 != null && var2.getVersion() == 0;
    }

    @Override
    public boolean convertMapToRegion(String var1, IProgressUpdatable progressUpdate) {
        progressUpdate.setLoadingProgress(0);

        ArrayList<ChunkFile> var3 = new ArrayList<>();
        ArrayList<File> var4 = new ArrayList<>();
        ArrayList<ChunkFile> var5 = new ArrayList<>();
        ArrayList<File> var6 = new ArrayList<>();

        File var7 = new File(this.field_22106_a, var1);
        File var8 = new File(var7, "DIM-1");
        System.out.println("Scanning folders...");
        this.func_22108_a(var7, var3, var4);
        if (var8.exists()) {
            this.func_22108_a(var8, var5, var6);
        }

        int count = var3.size() + var5.size() + var4.size() + var6.size();
        System.out.println("Total conversion count is " + count);
        this.func_22107_a(var7, var3, 0, count, progressUpdate);
        this.func_22107_a(var8, var5, var3.size(), count, progressUpdate);
        WorldInfo worldInfo = this.getWorldInfo(var1);
        worldInfo.setVersion(19132);
        ISaveHandler saveHandler = this.func_22105_a(var1, false);
        saveHandler.func_22094_a(worldInfo);
        this.func_22109_a(var4, var3.size() + var5.size(), count, progressUpdate);
        if (var8.exists()) {
            this.func_22109_a(var6, var3.size() + var5.size() + var4.size(), count, progressUpdate);
        }

        return true;
    }

    private void func_22108_a(File root, ArrayList<ChunkFile> chunkFiles, ArrayList<File> files) {
        ChunkFolderPattern folderPattern = new ChunkFolderPattern();
        ChunkFilePattern filePattern = new ChunkFilePattern();
        File[] content = root.listFiles(folderPattern);

        for (File folder : content) {
            files.add(folder);
            File[] folders = folder.listFiles(folderPattern);

            for (File var15 : folders) {
                File[] fs = var15.listFiles(filePattern);

                for (File file : fs) {
                    chunkFiles.add(new ChunkFile(file));
                }
            }
        }

    }

    private void func_22107_a(File var1, List<ChunkFile> var2, int var3, int var4, IProgressUpdatable progressUpdate) {
        Collections.sort(var2);
        byte[] var6 = new byte[4096];

        for (ChunkFile var8 : var2) {
            int var9 = var8.getX();
            int var10 = var8.getZ();
            RegionFile var11 = RegionFileCache.getRegionFile(var1, var9, var10);
            if (!var11.isChunkSaved(var9 & 31, var10 & 31)) {
                try {
                    DataInputStream var12 = new DataInputStream(new GZIPInputStream(new FileInputStream(var8.getChunkFile())));
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
            progressUpdate.setLoadingProgress(var16);
        }

        RegionFileCache.clear();
    }

    private void func_22109_a(ArrayList<File> files, int var2, int var3, IProgressUpdatable progressUpdate) {
        for (File var6 : files) {
            File[] var7 = var6.listFiles();
            func_22104_a(var7);
            var6.delete();
            ++var2;
            int var8 = (int) Math.round(100.0D * (double) var2 / (double) var3);
            progressUpdate.setLoadingProgress(var8);
        }

    }
}
