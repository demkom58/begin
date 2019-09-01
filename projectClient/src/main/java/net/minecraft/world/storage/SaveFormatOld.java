package net.minecraft.world.storage;

import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.WorldInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveFormatOld implements ISaveFormat {
    protected final File worldsDirectory;

    public SaveFormatOld(File worldsDirectory) {
        if (!worldsDirectory.exists()) {
            worldsDirectory.mkdirs();
        }

        this.worldsDirectory = worldsDirectory;
    }

    protected static void removeAll(File[] files) {
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                removeAll(file.listFiles());
            }

            file.delete();
        }
    }

    public String getFormatName() {
        return "Old Format";
    }

    public List<SaveFormatData> readSaveFormatData() {
        final List<SaveFormatData> comparators = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            String name = "World" + (i + 1);
            WorldInfo info = this.readWorldInfo(name);
            if (info != null)
                comparators.add(new SaveFormatData(name, "", info.getLastTimePlayed(), info.getSizeOnDisk(), false));
        }

        return comparators;
    }

    public void flushCache() {
    }

    public WorldInfo readWorldInfo(String worldName) {
        File worldDirectory = new File(this.worldsDirectory, worldName);
        if (!worldDirectory.exists())
            return null;

        File levelFile = new File(worldDirectory, "level.dat");
        if (levelFile.exists()) {
            try {
                TagCompound worldCompound = CompressedStreamTools.readGzipCompound(new FileInputStream(levelFile));
                TagCompound dataCompound = worldCompound.getCompoundTag("Data");
                return new WorldInfo(dataCompound);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File oldLevelFile = new File(worldDirectory, "level.dat_old");
        if (oldLevelFile.exists()) {
            try {
                TagCompound worldCompound = CompressedStreamTools.readGzipCompound(new FileInputStream(oldLevelFile));
                TagCompound dataCompound = worldCompound.getCompoundTag("Data");
                return new WorldInfo(dataCompound);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void setLevelName(String worldName, String levelName) {
        File worldDirectory = new File(this.worldsDirectory, worldName);
        if (!worldDirectory.exists())
            return;

        File levelFile = new File(worldDirectory, "level.dat");
        if (!levelFile.exists())
            return;

        try {
            TagCompound levelCompound = CompressedStreamTools.readGzipCompound(new FileInputStream(levelFile));
            TagCompound dataCompound = levelCompound.getCompoundTag("Data");
            dataCompound.setString("LevelName", levelName);
            CompressedStreamTools.writeGzipCompound(levelCompound, new FileOutputStream(levelFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeWorld(String worldName) {
        File worldDirectory = new File(this.worldsDirectory, worldName);
        if (worldDirectory.exists()) {
            removeAll(worldDirectory.listFiles());
            worldDirectory.delete();
        }
    }

    public ISaveHandler getSaveLoader(String var1, boolean var2) {
        return new SaveHandler(this.worldsDirectory, var1, var2);
    }

    public boolean isOldMapFormat(String var1) {
        return false;
    }

    public boolean convertMapFormat(String var1, IProgressUpdatable updatable) {
        return false;
    }
}
