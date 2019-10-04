package net.minecraft.world.storage;

import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;

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
    public File func_28113_a(String var1) {
        return null;
    }
}
