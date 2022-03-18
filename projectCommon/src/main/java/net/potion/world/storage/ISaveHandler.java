package net.potion.world.storage;

import net.potion.entity.player.EntityPlayer;
import net.potion.entity.player.IPlayerFileData;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.chunk.IOldChunkLoader;

import java.io.File;
import java.util.List;

public interface ISaveHandler {
    WorldInfo loadWorldInfo();

    void validateSession();

    IOldChunkLoader getChunkLoader(WorldProvider provider);

    void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players);

    void saveWorldInfo(WorldInfo worldInfo);

    File getFile(String var1);

    void clearCache();

    IPlayerFileData getPlayerData();
}
