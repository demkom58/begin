package net.potion.world.storage;

import net.potion.entity.player.EntityPlayer;
import net.potion.world.chunk.IChunkLoader;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;

import java.io.File;
import java.util.List;

public interface ISaveHandler {
    WorldInfo loadWorldInfo();

    void validateSession();

    IChunkLoader getChunkLoader(WorldProvider provider);

    void saveWorldInfoAndPlayer(WorldInfo worldInfo, List<EntityPlayer> players);

    void saveWorldInfo(WorldInfo worldInfo);

    File func_28113_a(String var1);
}
