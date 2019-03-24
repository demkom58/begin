package net.minecraft.world.chunk;

import net.minecraft.world.World;

import java.io.IOException;

public interface IChunkLoader {
    Chunk loadChunk(World world, int x, int z) throws IOException;

    void saveChunk(World world, Chunk chunk) throws IOException;

    void saveExtraChunkData(World world, Chunk chunk) throws IOException;

    void func_661_a();

    void saveExtraData();
}
