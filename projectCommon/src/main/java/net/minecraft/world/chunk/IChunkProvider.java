package net.minecraft.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.util.IProgressUpdatable;

public interface IChunkProvider {
    boolean chunkExists(int x, int z);

    Chunk provideChunk(int x, int z);

    Chunk prepareChunk(int x, int z);

    void populate(IChunkProvider provider, int x, int z);

    boolean saveChunks(boolean forceSave, IProgressUpdatable updatable);

    boolean unload100OldestChunks();

    boolean canSave();

    @Side(CodeSide.CLIENT)
    String makeString();
}
