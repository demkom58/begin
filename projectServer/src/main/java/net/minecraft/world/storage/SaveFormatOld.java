package net.minecraft.world.storage;

import net.minecraft.util.CompressedStreamTools;
import net.minecraft.util.IProgressUpdatable;
import net.minecraft.entity.player.PlayerNBTManager;
import net.minecraft.nbt.TagCompound;
import net.minecraft.world.WorldInfo;

import java.io.File;
import java.io.FileInputStream;

public class SaveFormatOld implements ISaveFormat {
    protected final File field_22106_a;

    public SaveFormatOld(File var1) {
        if (!var1.exists()) {
            var1.mkdirs();
        }

        this.field_22106_a = var1;
    }

    protected static void func_22104_a(File[] var0) {
        for (int var1 = 0; var1 < var0.length; ++var1) {
            if (var0[var1].isDirectory()) {
                func_22104_a(var0[var1].listFiles());
            }

            var0[var1].delete();
        }

    }

    public WorldInfo getWorldInfo(String var1) {
        File var2 = new File(this.field_22106_a, var1);
        if (!var2.exists()) {
            return null;
        } else {
            File var3 = new File(var2, "level.dat");
            if (var3.exists()) {
                try {
                    TagCompound var9 = CompressedStreamTools.readGzipCompound(new FileInputStream(var3));
                    TagCompound var10 = var9.getCompoundTag("Data");
                    return new WorldInfo(var10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            var3 = new File(var2, "level.dat_old");
            if (var3.exists()) {
                try {
                    TagCompound var4 = CompressedStreamTools.readGzipCompound(new FileInputStream(var3));
                    TagCompound var5 = var4.getCompoundTag("Data");
                    return new WorldInfo(var5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public ISaveHandler func_22105_a(String var1, boolean var2) {
        return new PlayerNBTManager(this.field_22106_a, var1, var2);
    }

    public boolean isOldSaveType(String var1) {
        return false;
    }

    public boolean convertMapToMCRegion(String var1, IProgressUpdatable var2) {
        return false;
    }
}
