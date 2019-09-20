package net.minecraft.world.chunk;

import net.minecraft.util.IProgressUpdatable;

public interface IChunkProvider {
    boolean chunkExists(int x, int z);

    Chunk provideChunk(int x, int z);

    Chunk prepareChunk(int x, int z);

    void populate(IChunkProvider provider, int x, int z);

    boolean saveChunks(boolean var1, IProgressUpdatable progressUpdatable);

    boolean unload100OldestChunks();

    boolean canSave();

    String makeString();
}
