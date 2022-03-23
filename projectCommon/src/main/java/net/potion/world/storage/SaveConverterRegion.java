package net.potion.world.storage;

import net.potion.util.IProgressUpdatable;
import net.hypnosis.util.math.MathHelper;
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
    public SaveConverterRegion(File worldsFolder) {
        super(worldsFolder);
    }

    @Override
    public String getFormatName() {
        return "Scaevolus' McRegion";
    }

    @Override
    public List<SaveFormatData> readSaveFormatData() {
        List<SaveFormatData> loaded = new ArrayList<>();
        File[] files = this.worldsDirectory.listFiles();

        if (files == null) {
            return loaded;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String name = file.getName();
                WorldInfo worldInfo = this.readWorldInfo(name);

                if (worldInfo != null) {
                    boolean invalidVersion = worldInfo.getVersion() != 19132;
                    String worldName = worldInfo.getLevelName();
                    if (worldName == null || MathHelper.stringNullOrLengthZero(worldName)) {
                        worldName = name;
                    }

                    loaded.add(new SaveFormatData(name, worldName, worldInfo.getLastTimePlayed(), worldInfo.getSizeOnDisk(), invalidVersion));
                }
            }
        }

        return loaded;
    }

    @Override
    public void flushCache() {
        RegionFileCache.clear();
    }

    @Override
    public ISaveHandler getSaveLoader(String levelName, boolean createPlayerDirectory) {
        return new SaveOldDir(this.worldsDirectory, levelName, createPlayerDirectory);
    }

    @Override
    public boolean isOldMapFormat(String levelName) {
        WorldInfo var2 = this.readWorldInfo(levelName);
        return var2 != null && var2.getVersion() == 0;
    }

    @Override
    public boolean convertMapFormat(String levelName, IProgressUpdatable updatable) {
        updatable.setLoadingProgress(0);

        List<ChunkFile> var3 = new ArrayList<>();
        List<File> var4 = new ArrayList<>();
        List<ChunkFile> var5 = new ArrayList<>();
        List<File> var6 = new ArrayList<>();

        File var7 = new File(this.worldsDirectory, levelName);
        File var8 = new File(var7, "DIM-1");
        System.out.println("Scanning folders...");
        this.func_22183_a(var7, var3, var4);
        if (var8.exists()) {
            this.func_22183_a(var8, var5, var6);
        }

        int var9 = var3.size() + var5.size() + var4.size() + var6.size();
        System.out.println("Total conversion count is " + var9);
        this.func_22181_a(var7, var3, 0, var9, updatable);
        this.func_22181_a(var8, var5, var3.size(), var9, updatable);
        WorldInfo var10 = this.readWorldInfo(levelName);
        var10.setVersion(19132);
        ISaveHandler var11 = this.getSaveLoader(levelName, false);
        var11.saveWorldInfo(var10);
        this.func_22182_a(var4, var3.size() + var5.size(), var9, updatable);
        if (var8.exists()) {
            this.func_22182_a(var6, var3.size() + var5.size() + var4.size(), var9, updatable);
        }

        return true;
    }

    private void func_22183_a(File root, List<ChunkFile> chunkFileList, List<File> fileList) {
        ChunkFolderPattern folderPattern = new ChunkFolderPattern();
        ChunkFilePattern filePattern = new ChunkFilePattern();
        File[] listFiles = root.listFiles(folderPattern);

        for (File var10 : listFiles) {
            fileList.add(var10);
            File[] files = var10.listFiles(folderPattern);

            for (File var15 : files) {
                File[] chunkFiles = var15.listFiles(filePattern);

                for (File chunkFile : chunkFiles) {
                    chunkFileList.add(new ChunkFile(chunkFile));
                }
            }
        }

    }

    private void func_22181_a(File var1, List<ChunkFile> var2, int var3, int var4, IProgressUpdatable var5) {
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
            var5.setLoadingProgress(var16);
        }

        RegionFileCache.clear();
    }

    private void func_22182_a(List<File> var1, int var2, int var3, IProgressUpdatable progressUpdate) {
        for (File var6 : var1) {
            File[] var7 = var6.listFiles();
            removeAll(var7);
            var6.delete();
            ++var2;
            int var8 = (int) Math.round(100.0D * (double) var2 / (double) var3);
            progressUpdate.setLoadingProgress(var8);
        }

    }
}
