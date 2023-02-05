package net.minecraft.world.storage;

import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.WorldInfo;

import java.util.List;

public interface ISaveFormat {
    String getFormatName();

    ISaveHandler getSaveLoader(String levelName, boolean createPlayerDirectory);

    List<SaveFormatData> readSaveFormatData();

    void flushCache();

    WorldInfo readWorldInfo(String levelName);

    void removeWorld(String levelName);

    void setLevelName(String oldName, String newName);

    boolean isOldMapFormat(String levelName);

    boolean convertMapFormat(String levelName, IProgressUpdatable var2);
}
