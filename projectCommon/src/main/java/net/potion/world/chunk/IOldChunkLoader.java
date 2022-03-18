package net.potion.world.chunk;

import net.potion.world.World;

import java.io.IOException;

public interface IOldChunkLoader {
    OldChunk loadChunk(World var1, int var2, int var3) throws IOException;

    void saveChunk(World var1, OldChunk var2) throws IOException;

    void saveExtraChunkData(World var1, OldChunk var2) throws IOException;

    void method1();

    void saveExtraData();
}
