package net.potion.world.storage;

import net.potion.nbt.CompressedStreamTools;
import net.potion.nbt.TagCompound;
import net.potion.util.IProgressUpdatable;
import net.potion.world.WorldInfo;

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
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                removeAll(file.listFiles());
            }

            file.delete();
        }
    }

    @Override
    public String getFormatName() {
        return "Old Format";
    }

    @Override
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

    @Override
    public void flushCache() {
    }

    @Override
    public WorldInfo readWorldInfo(String levelName) {
        File worldDirectory = new File(this.worldsDirectory, levelName);
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

    @Override
    public void setLevelName(String oldName, String newName) {
        File worldDirectory = new File(this.worldsDirectory, oldName);
        if (!worldDirectory.exists())
            return;

        File levelFile = new File(worldDirectory, "level.dat");
        if (!levelFile.exists())
            return;

        try {
            TagCompound levelCompound = CompressedStreamTools.readGzipCompound(new FileInputStream(levelFile));
            TagCompound dataCompound = levelCompound.getCompoundTag("Data");
            dataCompound.setString("LevelName", newName);
            CompressedStreamTools.writeGzipCompound(levelCompound, new FileOutputStream(levelFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeWorld(String levelName) {
        File worldDirectory = new File(this.worldsDirectory, levelName);
        if (worldDirectory.exists()) {
            removeAll(worldDirectory.listFiles());
            worldDirectory.delete();
        }
    }

    @Override
    public ISaveHandler getSaveLoader(String levelName, boolean createPlayerDirectory) {
        return new SaveHandler(this.worldsDirectory, levelName, createPlayerDirectory);
    }

    @Override
    public boolean isOldMapFormat(String levelName) {
        return false;
    }

    @Override
    public boolean convertMapFormat(String levelName, IProgressUpdatable updatable) {
        return false;
    }
}
