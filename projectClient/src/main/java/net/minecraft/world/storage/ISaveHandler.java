package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;

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
