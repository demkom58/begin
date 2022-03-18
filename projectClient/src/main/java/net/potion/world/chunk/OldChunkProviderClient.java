package net.potion.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.renew.chunk.Chunk;
import net.potion.util.IProgressUpdatable;
import net.potion.world.World;

import java.util.*;

public class OldChunkProviderClient implements IOldChunkProvider {
    private final OldChunk blankChunk;
    private final Map<ChunkCoordIntPair, OldChunk> chunkMapping = new HashMap<>();
    private final List<Chunk> chunks = new ArrayList<>();
    private final World worldObj;

    public OldChunkProviderClient(World world) {
        this.blankChunk = new EmptyOldChunk(world, new byte[32768], 0, 0);
        this.worldObj = world;
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
        OldChunk oldChunk = this.provideChunk(x, z);
        if (!oldChunk.isEmptyChunk()) {
            oldChunk.onChunkUnload();
        }

        this.chunkMapping.remove(new ChunkCoordIntPair(x, z));
        this.chunks.remove(oldChunk);
    }

    @Override
    public OldChunk prepareChunk(int x, int z) {
        ChunkCoordIntPair pair = new ChunkCoordIntPair(x, z);
        byte[] data = new byte[32768];
        OldChunk chunk = new OldChunk(this.worldObj, data, x, z);
        Arrays.fill(chunk.skylightMap.data, (byte) -1);
        this.chunkMapping.put(pair, chunk);
        chunk.isChunkLoaded = true;
        return chunk;
    }

    @Override
    public OldChunk provideChunk(int x, int z) {
        ChunkCoordIntPair pair = new ChunkCoordIntPair(x, z);
        OldChunk chunk = this.chunkMapping.get(pair);
        return chunk == null ? this.blankChunk : chunk;
    }

    @Override
    public boolean saveChunks(boolean var1, IProgressUpdatable progressUpdatable) {
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
    public void populate(IOldChunkProvider provider, int x, int z) {
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.size();
    }
}
