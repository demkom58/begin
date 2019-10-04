package net.minecraft.world.chunk;

import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.World;

import java.util.*;

public class ChunkProviderClient implements IChunkProvider {
    private Chunk blankChunk;
    private Map chunkMapping = new HashMap();
    private List field_889_c = new ArrayList();
    private World worldObj;

    public ChunkProviderClient(World var1) {
        this.blankChunk = new EmptyChunk(var1, new byte['\u8000'], 0, 0);
        this.worldObj = var1;
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

    public void func_539_c(int var1, int var2) {
        Chunk var3 = this.provideChunk(var1, var2);
        if (!var3.func_21167_h()) {
            var3.onChunkUnload();
        }

        this.chunkMapping.remove(new ChunkCoordIntPair(var1, var2));
        this.field_889_c.remove(var3);
    }

    @Override
    public Chunk prepareChunk(int x, int z) {
        ChunkCoordIntPair var3 = new ChunkCoordIntPair(x, z);
        byte[] var4 = new byte['\u8000'];
        Chunk var5 = new Chunk(this.worldObj, var4, x, z);
        Arrays.fill(var5.skylightMap.data, (byte) -1);
        this.chunkMapping.put(var3, var5);
        var5.isChunkLoaded = true;
        return var5;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        ChunkCoordIntPair var3 = new ChunkCoordIntPair(x, z);
        Chunk var4 = (Chunk) this.chunkMapping.get(var3);
        return var4 == null ? this.blankChunk : var4;
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
    public void populate(IChunkProvider provider, int x, int z) {
    }

    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.size();
    }
}
