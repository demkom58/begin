package net.potion.world.chunk;

import net.potion.world.World;

import java.io.IOException;

public interface IOldChunkLoader {
    OldChunk loadChunk(World world, int x, int z) throws IOException;

    void saveChunk(World world, OldChunk chunk) throws IOException;

    void saveExtraChunkData(World world, OldChunk chunk) throws IOException;

    void onUnloadOldest();

    void saveExtraData();
}
