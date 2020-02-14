package net.potion.world.storage;

import net.potion.util.IProgressUpdatable;
import net.potion.world.WorldInfo;

import java.util.List;

public interface ISaveFormat {
    String getFormatName();

    ISaveHandler getSaveLoader(String var1, boolean var2);

    List<SaveFormatData> readSaveFormatData();

    void flushCache();

    WorldInfo readWorldInfo(String var1);

    void removeWorld(String var1);

    void setLevelName(String var1, String var2);

    boolean isOldMapFormat(String var1);

    boolean convertMapFormat(String var1, IProgressUpdatable var2);
}
