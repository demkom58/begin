package net.minecraft.world.storage;

import net.minecraft.util.IProgressUpdatable;

public interface ISaveFormat {
    boolean isOldSaveType(String var1);

    boolean convertMapToMCRegion(String var1, IProgressUpdatable var2);
}
