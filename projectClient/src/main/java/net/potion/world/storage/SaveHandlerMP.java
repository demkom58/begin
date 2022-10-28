package net.potion.world.storage;

import net.potion.entity.player.IPlayerFileData;
import net.potion.world.WorldInfo;
import net.potion.world.WorldProvider;
import net.potion.world.chunk.IChunkLoader;

import java.io.File;
import java.util.List;

public class SaveHandlerMP implements ISaveHandler {
    @Override
    public WorldInfo loadWorldInfo() {
        return null;
    }

    @Override
    public void validateSession() {
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider provider) {
        return null;
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo worldInfo, List players) {
    }

    @Override
    public void saveWorldInfo(WorldInfo worldInfo) {
    }

    @Override
    public File getFile(String var1) {
        return null;
    }

    @Override
    public void clearCache() {

    }

    @Override
    public IPlayerFileData getPlayerData() {
        return null;
    }
}
