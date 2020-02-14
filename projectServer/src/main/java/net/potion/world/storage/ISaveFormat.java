package net.potion.world.storage;

import net.potion.util.IProgressUpdatable;

public interface ISaveFormat {
    boolean isOldSaveType(String var1);

    boolean convertMapToRegion(String var1, IProgressUpdatable var2);
}
