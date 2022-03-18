package net.potion.world.chunk;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.util.IProgressUpdatable;
import net.potion.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldChunkProvider implements IOldChunkProvider {
    private final IntSet droppedChunksSet = new IntRBTreeSet();
    private final OldChunk chunk;
    private final IOldChunkProvider chunkGenerator;
    private final IOldChunkLoader chunkLoader;
    private final Int2ObjectMap<OldChunk> cord2ChunkMap = new Int2ObjectRBTreeMap<>();
    private final List<OldChunk> chunks = new ArrayList<>();
    private final World world;

    public OldChunkProvider(World world, IOldChunkLoader chunkLoader, IOldChunkProvider provider) {
        this.chunk = new EmptyOldChunk(world, new byte[32768], 0, 0);
        this.world = world;
        this.chunkLoader = chunkLoader;
        this.chunkGenerator = provider;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return this.cord2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }

    @Override
    public OldChunk prepareChunk(int x, int z) {
        int cXZ = ChunkCoordIntPair.chunkXZ2Int(x, z);
        this.droppedChunksSet.remove(cXZ);
        OldChunk chunk = this.cord2ChunkMap.get(cXZ);
        if (chunk != null) {
            return chunk;
        }

        chunk = this.loadChunkFromFile(x, z);
        if (chunk == null) {
            if (this.chunkGenerator == null) {
                chunk = this.chunk;
            } else {
                chunk = this.chunkGenerator.provideChunk(x, z);
            }
        }

        this.cord2ChunkMap.put(cXZ, chunk);
        this.chunks.add(chunk);
        if (chunk != null) {
            chunk.prepareChunkLoad();
            chunk.onChunkLoad();
        }

        if (!chunk.terrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
            this.populate(this, x, z);
        }

        if (this.chunkExists(x - 1, z) && !this.provideChunk(x - 1, z).terrainPopulated && this.chunkExists(x - 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x - 1, z)) {
            this.populate(this, x - 1, z);
        }

        if (this.chunkExists(x, z - 1) && !this.provideChunk(x, z - 1).terrainPopulated && this.chunkExists(x + 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x + 1, z)) {
            this.populate(this, x, z - 1);
        }

        if (this.chunkExists(x - 1, z - 1) && !this.provideChunk(x - 1, z - 1).terrainPopulated && this.chunkExists(x - 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x - 1, z)) {
            this.populate(this, x - 1, z - 1);
        }

        return chunk;
    }

    @Override
    public OldChunk provideChunk(int x, int z) {
        OldChunk var3 = this.cord2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return var3 == null ? this.prepareChunk(x, z) : var3;
    }

    private OldChunk loadChunkFromFile(int x, int z) {
        if (this.chunkLoader == null) {
            return null;
        }

        try {
            OldChunk chunk = this.chunkLoader.loadChunk(this.world, x, z);
            if (chunk != null) {
                chunk.lastSaveTime = this.world.getWorldTime();
            }

            return chunk;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveChunkExtra(OldChunk chunk) {
        if (this.chunkLoader == null) {
            return;
        }

        try {
            this.chunkLoader.saveExtraChunkData(this.world, chunk);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveChunk(OldChunk chunk) {
        if (this.chunkLoader == null) {
            return;
        }

        try {
            chunk.lastSaveTime = this.world.getWorldTime();
            this.chunkLoader.saveChunk(this.world, chunk);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void populate(IOldChunkProvider provider, int x, int z) {
        OldChunk var4 = this.provideChunk(x, z);
        if (!var4.terrainPopulated) {
            var4.terrainPopulated = true;
            if (this.chunkGenerator != null) {
                this.chunkGenerator.populate(provider, x, z);
                var4.setChunkModified();
            }
        }

    }

    @Override
    public boolean saveChunks(boolean var1, IProgressUpdatable progressUpdatable) {
        int var3 = 0;

        for (int var4 = 0; var4 < this.chunks.size(); ++var4) {
            OldChunk var5 = this.chunks.get(var4);
            if (var1 && !var5.neverSave) {
                this.saveChunkExtra(var5);
            }

            if (var5.needsSaving(var1)) {
                this.saveChunk(var5);
                var5.modified = false;
                ++var3;
                if (var3 == 24 && !var1) {
                    return false;
                }
            }
        }

        if (var1) {
            if (this.chunkLoader == null) {
                return true;
            }

            this.chunkLoader.saveExtraData();
        }

        return true;
    }

    @Override
    public boolean unload100OldestChunks() {
        for (int var1 = 0; var1 < 100; ++var1) {
            if (!this.droppedChunksSet.isEmpty()) {
                Integer var2 = this.droppedChunksSet.iterator().next();
                OldChunk var3 = this.cord2ChunkMap.get(var2);
                var3.onChunkUnload();
                this.saveChunk(var3);
                this.saveChunkExtra(var3);
                this.droppedChunksSet.remove(var2);
                this.cord2ChunkMap.remove(var2);
                this.chunks.remove(var3);
            }
        }

        if (this.chunkLoader != null) {
            this.chunkLoader.method1();
        }

        return this.chunkGenerator.unload100OldestChunks();
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String makeString() {
        return "ServerChunkCache: " + this.cord2ChunkMap.size() + " Drop: " + this.droppedChunksSet.size();
    }
}
