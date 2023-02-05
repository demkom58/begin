package net.minecraft.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.World;

import java.util.*;

public class ChunkProviderClient implements IChunkProvider {
    private final Chunk blankChunk;
    private final Map<ChunkCoordIntPair, Chunk> chunkMapping = new HashMap<>();
    private final List<Chunk> chunks = new ArrayList<>();
    private final World world;

    public ChunkProviderClient(World world) {
        this.blankChunk = new EmptyChunk(world, new byte[32768], 0, 0);
        this.world = world;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        if (this != null) {
            return true;
        } else {
            ChunkCoordIntPair var3 = new ChunkCoordIntPair(x, z);
            return this.chunkMapping.containsKey(var3);
        }
    }

    public void unloadChunk(int x, int z) {
        Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isEmptyChunk()) {
            chunk.onChunkUnload();
        }

        this.chunkMapping.remove(new ChunkCoordIntPair(x, z));
        this.chunks.remove(chunk);
    }

    @Override
    public Chunk prepareChunk(int x, int z) {
        ChunkCoordIntPair pair = new ChunkCoordIntPair(x, z);
        byte[] data = new byte[32768];
        Chunk chunk = new Chunk(this.world, data, x, z);
        Arrays.fill(chunk.skylightMap.data, (byte) -1);
        this.chunkMapping.put(pair, chunk);
        chunk.isChunkLoaded = true;
        return chunk;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        ChunkCoordIntPair pair = new ChunkCoordIntPair(x, z);
        Chunk chunk = this.chunkMapping.get(pair);
        return chunk == null ? this.blankChunk : chunk;
    }

    @Override
    public boolean saveChunks(boolean forceSave, IProgressUpdatable updatable) {
        return true;
    }

    @Override
    public boolean unload100OldestChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return false;
    }

    @Override
    public void populate(IChunkProvider provider, int x, int z) {
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.size();
    }
}
