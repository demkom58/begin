package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.ChunkLoader;
import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.nbt.TagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.MinecraftException;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;

import java.io.*;
import java.util.List;

public class SaveHandler implements ISaveHandler {
    private final File saveDirectory;
    private final File playersDirectory;
    private final File dataDirectory;
    private final long lockTime = System.currentTimeMillis();

    public SaveHandler(File saveDirectory, String worldName, boolean createPlayerDirectory) {
        this.saveDirectory = new File(saveDirectory, worldName);
        this.saveDirectory.mkdirs();

        this.playersDirectory = new File(this.saveDirectory, "players");
        if (createPlayerDirectory)
            this.playersDirectory.mkdirs();

        this.dataDirectory = new File(this.saveDirectory, "data");
        this.dataDirectory.mkdirs();

        this.writeSession();
    }

    private void writeSession() {
        try {
            File sessionFile = new File(this.saveDirectory, "session.lock");

            try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(sessionFile))) {
                outputStream.writeLong(this.lockTime);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    protected File getSaveDirectory() {
        return this.saveDirectory;
    }

    @Override
    public void validateSession() throws MinecraftException {
        try {
            File sessionFile = new File(this.saveDirectory, "session.lock");

            try (DataInputStream sessionInput = new DataInputStream(new FileInputStream(sessionFile))) {
                if (sessionInput.readLong() != this.lockTime)
                    throw new MinecraftException("The save is being accessed from another location, aborting");
            }

        } catch (IOException e) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider provider) {
        if (provider instanceof WorldProviderHell) {
            File file = new File(this.saveDirectory, "DIM-1");
            file.mkdirs();
            return new ChunkLoader(file, true);
        }

        return new ChunkLoader(this.saveDirectory, true);
    }

    @Override
    public WorldInfo loadWorldInfo() {
        File levelFile = new File(this.saveDirectory, "level.dat");
        if (levelFile.exists()) {
            try {
                TagCompound var7 = CompressedStreamTools.readGzipCompound(new FileInputStream(levelFile));
                TagCompound var8 = var7.getCompoundTag("Data");
                return new WorldInfo(var8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File oldLevelFile = new File(this.saveDirectory, "level.dat_old");
        if (oldLevelFile.exists()) {
            try {
                TagCompound var2 = CompressedStreamTools.readGzipCompound(new FileInputStream(oldLevelFile));
                TagCompound var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players) {
        TagCompound var3 = worldInfo.getNBTTagCompoundWithPlayer(players);
        TagCompound var4 = new TagCompound();
        var4.setTag("Data", var3);

        try {
            File newLevelFile = new File(this.saveDirectory, "level.dat_new");
            File oldLevelFile = new File(this.saveDirectory, "level.dat_old");
            File levelFile = new File(this.saveDirectory, "level.dat");
            CompressedStreamTools.writeGzipCompound(var4, new FileOutputStream(newLevelFile));
            if (oldLevelFile.exists()) {
                oldLevelFile.delete();
            }

            levelFile.renameTo(oldLevelFile);
            if (levelFile.exists()) {
                levelFile.delete();
            }

            newLevelFile.renameTo(levelFile);
            if (newLevelFile.exists()) {
                newLevelFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveWorldInfo(WorldInfo worldInfo) {
        TagCompound var2 = worldInfo.getNBTTagCompound();
        TagCompound var3 = new TagCompound();
        var3.setTag("Data", var2);

        try {
            File newLevelFile = new File(this.saveDirectory, "level.dat_new");
            File oldLevelFile = new File(this.saveDirectory, "level.dat_old");
            File levelFile = new File(this.saveDirectory, "level.dat");

            CompressedStreamTools.writeGzipCompound(var3, new FileOutputStream(newLevelFile));
            if (oldLevelFile.exists()) {
                oldLevelFile.delete();
            }

            levelFile.renameTo(oldLevelFile);
            if (levelFile.exists()) {
                levelFile.delete();
            }

            newLevelFile.renameTo(levelFile);
            if (newLevelFile.exists()) {
                newLevelFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public File func_28113_a(String var1) {
        return new File(this.dataDirectory, var1 + ".dat");
    }
}
