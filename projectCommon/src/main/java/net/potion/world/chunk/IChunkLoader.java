package net.potion.world.chunk;

import net.potion.world.World;

import java.io.IOException;

public interface IChunkLoader {
    Chunk loadChunk(World world, int x, int z) throws IOException;

    void saveChunk(World world, Chunk chunk) throws IOException;

    void saveExtraChunkData(World world, Chunk chunk) throws IOException;

    void onUnloadOldest();

    void saveExtraData();
}
