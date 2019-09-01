package net.minecraft.world.storage;

import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.world.WorldInfo;
import net.minecraft.world.WorldProvider;

import java.io.File;
import java.util.List;

public class SaveHandlerMP implements ISaveHandler {
    public WorldInfo loadWorldInfo() {
        return null;
    }

    public void validateSession() {
    }

    public IChunkLoader getChunkLoader(WorldProvider provider) {
        return null;
    }

    public void saveWorldInfoAndPlayer(WorldInfo worldInfo, List players) {
    }

    public void saveWorldInfo(WorldInfo worldInfo) {
    }

    public File func_28113_a(String var1) {
        return null;
    }
}
