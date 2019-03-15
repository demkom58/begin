package net.minecraft;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChunkProvider implements IChunkProvider {
    private IntSet chunksCords = new IntRBTreeSet();
    private Chunk chunk;
    private IChunkProvider chunkGenerator;
    private IChunkLoader loader;
    private Int2ObjectMap<Chunk> cord2ChunkMap = new Int2ObjectRBTreeMap<>();
    private List<Chunk> chunks = new ArrayList<>();
    private World worldObj;

    public ChunkProvider(World var1, IChunkLoader var2, IChunkProvider var3) {
        this.chunk = new EmptyChunk(var1, new byte['\u8000'], 0, 0);
        this.worldObj = var1;
        this.loader = var2;
        this.chunkGenerator = var3;
    }

    public boolean chunkExists(int var1, int var2) {
        return this.cord2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
    }

    public Chunk loadChunk(int x, int z) {
        int chunkXZ2Int = ChunkCoordIntPair.chunkXZ2Int(x, z);
        this.chunksCords.remove(chunkXZ2Int);
        Chunk chunk = this.cord2ChunkMap.get(chunkXZ2Int);
        if (chunk == null) {
            chunk = this.rawChunkLoad(x, z);
            if (chunk == null) {
                if (this.chunkGenerator == null) {
                    chunk = this.chunk;
                } else {
                    chunk = this.chunkGenerator.provideChunk(x, z);
                }
            }

            this.cord2ChunkMap.put(chunkXZ2Int, chunk);
            this.chunks.add(chunk);
            if (chunk != null) {
                chunk.func_4053_c();
                chunk.onChunkLoad();
            }

            if (!chunk.isTerrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
                this.populate(this, x, z);
            }

            if (this.chunkExists(x - 1, z) && !this.provideChunk(x - 1, z).isTerrainPopulated && this.chunkExists(x - 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x - 1, z)) {
                this.populate(this, x - 1, z);
            }

            if (this.chunkExists(x, z - 1) && !this.provideChunk(x, z - 1).isTerrainPopulated && this.chunkExists(x + 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x + 1, z)) {
                this.populate(this, x, z - 1);
            }

            if (this.chunkExists(x - 1, z - 1) && !this.provideChunk(x - 1, z - 1).isTerrainPopulated && this.chunkExists(x - 1, z - 1) && this.chunkExists(x, z - 1) && this.chunkExists(x - 1, z)) {
                this.populate(this, x - 1, z - 1);
            }
        }

        return chunk;
    }

    public Chunk provideChunk(int x, int z) {
        Chunk chunk = this.cord2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return chunk == null ? this.loadChunk(x, z) : chunk;
    }

    private Chunk rawChunkLoad(int x, int z) {
        if (this.loader == null) {
            return null;
        }

        try {
            Chunk chunk = this.loader.loadChunk(this.worldObj, x, z);
            if (chunk != null) {
                chunk.lastSaveTime = this.worldObj.getWorldTime();
            }

            return chunk;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveChunkExtra(Chunk chunk) {
        if (this.loader != null) {
            try {
                this.loader.saveExtraChunkData(this.worldObj, chunk);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveChunk(Chunk chunk) {
        if (this.loader != null) {
            try {
                chunk.lastSaveTime = this.worldObj.getWorldTime();
                this.loader.saveChunk(this.worldObj, chunk);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void populate(IChunkProvider chunkProvider, int x, int z) {
        Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isTerrainPopulated) {
            chunk.isTerrainPopulated = true;
            if (this.chunkGenerator != null) {
                this.chunkGenerator.populate(chunkProvider, x, z);
                chunk.setChunkModified();
            }
        }

    }

    public boolean saveChunks(boolean var1, IProgressUpdate progressUpdate) {
        int var3 = 0;

        for (int var4 = 0; var4 < this.chunks.size(); ++var4) {
            Chunk chunk = this.chunks.get(var4);
            if (var1 && !chunk.neverSave) {
                this.saveChunkExtra(chunk);
            }

            if (chunk.needsSaving(var1)) {
                this.saveChunk(chunk);
                chunk.isModified = false;
                ++var3;
                if (var3 == 24 && !var1) {
                    return false;
                }
            }
        }

        if (var1) {
            if (this.loader == null) {
                return true;
            }

            this.loader.saveExtraData();
        }

        return true;
    }

    public boolean func_361_a() {
        for (int i = 0; i < 100; ++i) {
            if (!this.chunksCords.isEmpty()) {
                int cord = this.chunksCords.iterator().nextInt();
                Chunk chunk = this.cord2ChunkMap.get(cord);

                chunk.onChunkUnload();
                this.saveChunk(chunk);
                this.saveChunkExtra(chunk);

                this.chunksCords.remove(cord);
                this.cord2ChunkMap.remove(cord);
                this.chunks.remove(chunk);
            }
        }

        if (this.loader != null) {
            this.loader.func_661_a();
        }

        return this.chunkGenerator.func_361_a();
    }

    public boolean canSave() {
        return true;
    }
}
