package net.minecraft.world.storage;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.IPlayerFileData;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.TagCompound;
import net.minecraft.util.MinecraftException;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.ChunkLoader;
import net.minecraft.world.chunk.IChunkLoader;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler implements IPlayerFileData, ISaveHandler {
    private static final Logger logger = Logger.getLogger("Minecraft");
    private final File worldDir;
    private final File worldPlayersDir;
    private final File worldDataDir;
    private final long lockTime = System.currentTimeMillis();

    public SaveHandler(File saveDirectory, String worldName, boolean createPlayerDirectory) {
        this.worldDir = new File(saveDirectory, worldName);
        this.worldDir.mkdirs();

        this.worldPlayersDir = new File(this.worldDir, "players");
        if (createPlayerDirectory) {
            this.worldPlayersDir.mkdirs();
        }

        this.worldDataDir = new File(this.worldDir, "data");
        this.worldDataDir.mkdirs();

        this.lockSession();
    }

    private void lockSession() {
        try {
            File sessionFile = new File(this.worldDir, "session.lock");

            try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(sessionFile))) {
                outputStream.writeLong(this.lockTime);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    protected File getWorldDir() {
        return this.worldDir;
    }

    @Override
    public void validateSession() throws MinecraftException {
        try {
            File sessionFile = new File(this.worldDir, "session.lock");

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
            File file = new File(this.worldDir, "DIM-1");
            file.mkdirs();
            return new ChunkLoader(file, true);
        }

        return new ChunkLoader(this.worldDir, true);
    }

    @Override
    public WorldInfo loadWorldInfo() {
        File levelFile = new File(this.worldDir, "level.dat");
        if (levelFile.exists()) {
            try {
                TagCompound var7 = CompressedStreamTools.readGzipCompound(new FileInputStream(levelFile));
                TagCompound var8 = var7.getCompoundTag("Data");
                return new WorldInfo(var8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File oldLevelFile = new File(this.worldDir, "level.dat_old");
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
            File newLevelFile = new File(this.worldDir, "level.dat_new");
            File oldLevelFile = new File(this.worldDir, "level.dat_old");
            File levelFile = new File(this.worldDir, "level.dat");
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
            File newLevelFile = new File(this.worldDir, "level.dat_new");
            File oldLevelFile = new File(this.worldDir, "level.dat_old");
            File levelFile = new File(this.worldDir, "level.dat");

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
    @Side(CodeSide.SERVER)
    public void writePlayerData(EntityPlayer var1) {
        try {
            TagCompound var2 = new TagCompound();
            var1.writeToNBT(var2);
            File var3 = new File(this.worldPlayersDir, "_tmp_.dat");
            File var4 = new File(this.worldPlayersDir, var1.username + ".dat");
            CompressedStreamTools.writeGzipCompound(var2, new FileOutputStream(var3));
            if (var4.exists()) {
                var4.delete();
            }

            var3.renameTo(var4);
        } catch (Exception e) {
            logger.warning("Failed to save player data for " + var1.username);
        }

    }

    @Override
    @Side(CodeSide.SERVER)
    public void readPlayerData(EntityPlayer var1) {
        TagCompound var2 = this.getPlayerData(var1.username);
        if (var2 != null) {
            var1.readFromNBT(var2);
        }

    }

    public TagCompound getPlayerData(String var1) {
        try {
            File var2 = new File(this.worldPlayersDir, var1 + ".dat");
            if (var2.exists()) {
                return CompressedStreamTools.readGzipCompound(new FileInputStream(var2));
            }
        } catch (Exception e) {
            logger.warning("Failed to load player data for " + var1);
        }

        return null;
    }

    @Override
    public IPlayerFileData getPlayerData() {
        return this;
    }

    @Override
    public void clearCache() {

    }

    @Override
    public File getFile(String var1) {
        return new File(this.worldDataDir, var1 + ".dat");
    }

}
