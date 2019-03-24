package net.minecraft.world.storage;

import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {
    boolean isOldSaveType(String var1);

    boolean convertMapToMCRegion(String var1, IProgressUpdate var2);
}
