package net.potion.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.util.IProgressUpdatable;

public interface IOldChunkProvider {
    boolean chunkExists(int x, int z);

    OldChunk provideChunk(int x, int z);

    OldChunk prepareChunk(int x, int z);

    void populate(IOldChunkProvider provider, int x, int z);

    boolean saveChunks(boolean var1, IProgressUpdatable progressUpdatable);

    boolean unload100OldestChunks();

    boolean canSave();

    @Side(CodeSide.CLIENT)
    String makeString();
}
