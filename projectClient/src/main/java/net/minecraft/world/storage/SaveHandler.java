package net.minecraft.world.storage;

import net.minecraft.world.chunk.ChunkLoader;
import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CompressedStreamTools;
import net.minecraft.util.MinecraftException;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler implements ISaveHandler {
    private static final Logger logger = Logger.getLogger("Minecraft");
    private final File saveDirectory;
    private final File playersDirectory;
    private final File field_28114_d;
    private final long now = System.currentTimeMillis();

    public SaveHandler(File var1, String var2, boolean var3) {
        this.saveDirectory = new File(var1, var2);
        this.saveDirectory.mkdirs();
        this.playersDirectory = new File(this.saveDirectory, "players");
        this.field_28114_d = new File(this.saveDirectory, "data");
        this.field_28114_d.mkdirs();
        if (var3) {
            this.playersDirectory.mkdirs();
        }

        this.func_22154_d();
    }

    private void func_22154_d() {
        try {
            File var1 = new File(this.saveDirectory, "session.lock");
            DataOutputStream var2 = new DataOutputStream(new FileOutputStream(var1));

            try {
                var2.writeLong(this.now);
            } finally {
                var2.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    protected File getSaveDirectory() {
        return this.saveDirectory;
    }

    public void func_22150_b() {
        try {
            File var1 = new File(this.saveDirectory, "session.lock");
            DataInputStream var2 = new DataInputStream(new FileInputStream(var1));

            try {
                if (var2.readLong() != this.now) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            } finally {
                var2.close();
            }

        } catch (IOException e) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader getChunkLoader(WorldProvider var1) {
        if (var1 instanceof WorldProviderHell) {
            File var2 = new File(this.saveDirectory, "DIM-1");
            var2.mkdirs();
            return new ChunkLoader(var2, true);
        } else {
            return new ChunkLoader(this.saveDirectory, true);
        }
    }

    public WorldInfo loadWorldInfo() {
        File var1 = new File(this.saveDirectory, "level.dat");
        if (var1.exists()) {
            try {
                NBTTagCompound var7 = CompressedStreamTools.readGzipCompound(new FileInputStream(var1));
                NBTTagCompound var8 = var7.getCompoundTag("Data");
                return new WorldInfo(var8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        var1 = new File(this.saveDirectory, "level.dat_old");
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

    public void saveWorldInfoAndPlayer(WorldInfo var1, List var2) {
        NBTTagCompound var3 = var1.getNBTTagCompoundWithPlayer(var2);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setTag("Data", var3);

        try {
            File var5 = new File(this.saveDirectory, "level.dat_new");
            File var6 = new File(this.saveDirectory, "level.dat_old");
            File var7 = new File(this.saveDirectory, "level.dat");
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

    public void saveWorldInfo(WorldInfo var1) {
        NBTTagCompound var2 = var1.getNBTTagCompound();
        NBTTagCompound var3 = new NBTTagCompound();
        var3.setTag("Data", var2);

        try {
            File var4 = new File(this.saveDirectory, "level.dat_new");
            File var5 = new File(this.saveDirectory, "level.dat_old");
            File var6 = new File(this.saveDirectory, "level.dat");
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

    public File func_28113_a(String var1) {
        return new File(this.field_28114_d, var1 + ".dat");
    }
}
