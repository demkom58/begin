package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CompressedStreamTools;
import net.minecraft.util.MinecraftException;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.ChunkLoader;
import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.world.storage.ISaveHandler;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class PlayerNBTManager implements IPlayerFileData, ISaveHandler {
    private static final Logger logger = Logger.getLogger("Minecraft");
    private final File worldDir;
    private final File worldFile;
    private final File field_28112_d;
    private final long field_22100_d = System.currentTimeMillis();

    public PlayerNBTManager(File var1, String var2, boolean var3) {
        this.worldDir = new File(var1, var2);
        this.worldDir.mkdirs();
        this.worldFile = new File(this.worldDir, "players");
        this.field_28112_d = new File(this.worldDir, "data");
        this.field_28112_d.mkdirs();
        if (var3) {
            this.worldFile.mkdirs();
        }

        this.func_22098_f();
    }

    private void func_22098_f() {
        try {
            File var1 = new File(this.worldDir, "session.lock");
            DataOutputStream var2 = new DataOutputStream(new FileOutputStream(var1));

            try {
                var2.writeLong(this.field_22100_d);
            } finally {
                var2.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    protected File getWorldDir() {
        return this.worldDir;
    }

    public void func_22091_b() {
        try {
            File var1 = new File(this.worldDir, "session.lock");
            DataInputStream var2 = new DataInputStream(new FileInputStream(var1));

            try {
                if (var2.readLong() != this.field_22100_d) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            } finally {
                var2.close();
            }

        } catch (IOException e) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader func_22092_a(WorldProvider var1) {
        if (var1 instanceof WorldProviderHell) {
            File var2 = new File(this.worldDir, "DIM-1");
            var2.mkdirs();
            return new ChunkLoader(var2, true);
        } else {
            return new ChunkLoader(this.worldDir, true);
        }
    }

    public WorldInfo loadWorldInfo() {
        File var1 = new File(this.worldDir, "level.dat");
        if (var1.exists()) {
            try {
                NBTTagCompound var7 = CompressedStreamTools.readGzipCompound(new FileInputStream(var1));
                NBTTagCompound var8 = var7.getCompoundTag("Data");
                return new WorldInfo(var8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        var1 = new File(this.worldDir, "level.dat_old");
        if (var1.exists()) {
            try {
                NBTTagCompound var2 = CompressedStreamTools.readGzipCompound(new FileInputStream(var1));
                NBTTagCompound var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void saveWorldInfoAndPlayer(WorldInfo var1, List<EntityPlayer> var2) {
        NBTTagCompound var3 = var1.getNBTTagCompoundWithPlayer(var2);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setTag("Data", var3);

        try {
            File var5 = new File(this.worldDir, "level.dat_new");
            File var6 = new File(this.worldDir, "level.dat_old");
            File var7 = new File(this.worldDir, "level.dat");
            CompressedStreamTools.writeGzipCompound(var4, new FileOutputStream(var5));
            if (var6.exists()) {
                var6.delete();
            }

            var7.renameTo(var6);
            if (var7.exists()) {
                var7.delete();
            }

            var5.renameTo(var7);
            if (var5.exists()) {
                var5.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void func_22094_a(WorldInfo var1) {
        NBTTagCompound var2 = var1.getNBTTagCompound();
        NBTTagCompound var3 = new NBTTagCompound();
        var3.setTag("Data", var2);

        try {
            File var4 = new File(this.worldDir, "level.dat_new");
            File var5 = new File(this.worldDir, "level.dat_old");
            File var6 = new File(this.worldDir, "level.dat");
            CompressedStreamTools.writeGzipCompound(var3, new FileOutputStream(var4));
            if (var5.exists()) {
                var5.delete();
            }

            var6.renameTo(var5);
            if (var6.exists()) {
                var6.delete();
            }

            var4.renameTo(var6);
            if (var4.exists()) {
                var4.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writePlayerData(EntityPlayer var1) {
        try {
            NBTTagCompound var2 = new NBTTagCompound();
            var1.writeToNBT(var2);
            File var3 = new File(this.worldFile, "_tmp_.dat");
            File var4 = new File(this.worldFile, var1.username + ".dat");
            CompressedStreamTools.writeGzipCompound(var2, new FileOutputStream(var3));
            if (var4.exists()) {
                var4.delete();
            }

            var3.renameTo(var4);
        } catch (Exception e) {
            logger.warning("Failed to save player data for " + var1.username);
        }

    }

    public void readPlayerData(EntityPlayer var1) {
        NBTTagCompound var2 = this.getPlayerData(var1.username);
        if (var2 != null) {
            var1.readFromNBT(var2);
        }

    }

    public NBTTagCompound getPlayerData(String var1) {
        try {
            File var2 = new File(this.worldFile, var1 + ".dat");
            if (var2.exists()) {
                return CompressedStreamTools.readGzipCompound(new FileInputStream(var2));
            }
        } catch (Exception e) {
            logger.warning("Failed to load player data for " + var1);
        }

        return null;
    }

    public IPlayerFileData func_22090_d() {
        return this;
    }

    public void func_22093_e() {
    }

    public File func_28111_b(String var1) {
        return new File(this.field_28112_d, var1 + ".dat");
    }
}
