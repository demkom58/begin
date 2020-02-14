package net.potion.entity.player;

import net.potion.nbt.TagCompound;
import net.potion.nbt.CompressedStreamTools;
import net.potion.util.PotionException;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.WorldProviderHell;
import net.potion.world.chunk.ChunkLoader;
import net.potion.world.chunk.IChunkLoader;
import net.potion.world.storage.ISaveHandler;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class PlayerNBTManager implements IPlayerFileData, ISaveHandler {
    private static final Logger logger = Logger.getLogger("Potion");
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

    @Override
    public void func_22091_b() {
        try {
            File var1 = new File(this.worldDir, "session.lock");
            DataInputStream var2 = new DataInputStream(new FileInputStream(var1));

            try {
                if (var2.readLong() != this.field_22100_d) {
                    throw new PotionException("The save is being accessed from another location, aborting");
                }
            } finally {
                var2.close();
            }

        } catch (IOException e) {
            throw new PotionException("Failed to check session lock, aborting");
        }
    }

    @Override
    public IChunkLoader func_22092_a(WorldProvider var1) {
        if (var1 instanceof WorldProviderHell) {
            File var2 = new File(this.worldDir, "DIM-1");
            var2.mkdirs();
            return new ChunkLoader(var2, true);
        } else {
            return new ChunkLoader(this.worldDir, true);
        }
    }

    @Override
    public WorldInfo loadWorldInfo() {
        File var1 = new File(this.worldDir, "level.dat");
        if (var1.exists()) {
            try {
                TagCompound var7 = CompressedStreamTools.readGzipCompound(new FileInputStream(var1));
                TagCompound var8 = var7.getCompoundTag("Data");
                return new WorldInfo(var8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        var1 = new File(this.worldDir, "level.dat_old");
        if (var1.exists()) {
            try {
                TagCompound var2 = CompressedStreamTools.readGzipCompound(new FileInputStream(var1));
                TagCompound var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo var1, List<EntityPlayer> var2) {
        TagCompound var3 = var1.getNBTTagCompoundWithPlayer(var2);
        TagCompound var4 = new TagCompound();
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

    @Override
    public void func_22094_a(WorldInfo var1) {
        TagCompound var2 = var1.getNBTTagCompound();
        TagCompound var3 = new TagCompound();
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

    @Override
    public void writePlayerData(EntityPlayer var1) {
        try {
            TagCompound var2 = new TagCompound();
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

    @Override
    public void readPlayerData(EntityPlayer var1) {
        TagCompound var2 = this.getPlayerData(var1.username);
        if (var2 != null) {
            var1.readFromNBT(var2);
        }

    }

    public TagCompound getPlayerData(String var1) {
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

    @Override
    public IPlayerFileData func_22090_d() {
        return this;
    }

    @Override
    public void func_22093_e() {
    }

    @Override
    public File func_28111_b(String var1) {
        return new File(this.field_28112_d, var1 + ".dat");
    }
}
