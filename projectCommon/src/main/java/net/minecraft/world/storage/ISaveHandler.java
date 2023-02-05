package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.IPlayerFileData;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkLoader;

import java.io.File;
import java.util.List;

public interface ISaveHandler {
    WorldInfo loadWorldInfo();

    void validateSession();

    IChunkLoader getChunkLoader(WorldProvider provider);

    void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players);

    void saveWorldInfo(WorldInfo worldInfo);

    File getFile(String var1);

    void clearCache();

    IPlayerFileData getPlayerData();
}
