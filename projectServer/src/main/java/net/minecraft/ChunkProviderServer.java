package net.minecraft;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.io.IOException;
import java.util.*;

public class ChunkProviderServer implements IChunkProvider {
    public boolean chunkLoadOverride = false;
    private IntSet chunkCoords = new IntRBTreeSet();
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    private Int2ObjectMap<Chunk> id2ChunkMap = new Int2ObjectRBTreeMap<>();
    private List<Chunk> chunks = new ArrayList<>();
    private WorldServer world;

    public ChunkProviderServer(WorldServer worldServer, IChunkLoader chunkLoader, IChunkProvider chunkProvider) {
        this.dummyChunk = new EmptyChunk(worldServer, new byte['\u8000'], 0, 0);
        this.world = worldServer;
        this.chunkLoader = chunkLoader;
        this.serverChunkGenerator = chunkProvider;
    }

    public boolean chunkExists(int x, int z) {
        return this.id2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }

    public void func_374_c(int x, int z) {
        ChunkCoordinates coordinates = this.world.getSpawnPoint();
        int worldX = x * 16 + 8 - coordinates.posX;
        int worldZ = z * 16 + 8 - coordinates.posZ;
        short size = 128;
        if (worldX < -size || worldX > size || worldZ < -size || worldZ > size) {
            this.chunkCoords.add(ChunkCoordIntPair.chunkXZ2Int(x, z));
        }

    }

    public Chunk loadChunk(int x, int z) {
        int chunkXZ2Int = ChunkCoordIntPair.chunkXZ2Int(x, z);
        this.chunkCoords.remove(chunkXZ2Int);
        Chunk chunk = this.id2ChunkMap.get(chunkXZ2Int);
        if (chunk == null) {
            chunk = this.func_4063_e(x, z);
            if (chunk == null) {
                if (this.serverChunkGenerator == null) {
                    chunk = this.dummyChunk;
                } else {
                    chunk = this.serverChunkGenerator.provideChunk(x, z);
                }
            }

            this.id2ChunkMap.put(chunkXZ2Int, chunk);
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
        Chunk chunk = this.id2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
        if (chunk == null) {
            return !this.world.worldChunkLoadOverride && !this.chunkLoadOverride ? this.dummyChunk : this.loadChunk(x, z);
        }

        return chunk;
    }

    private Chunk func_4063_e(int x, int z) {
        if (this.chunkLoader == null) {
            return null;
        }

        try {
            Chunk chunk = this.chunkLoader.loadChunk(this.world, x, z);
            if (chunk != null) {
                chunk.lastSaveTime = this.world.getWorldTime();
            }

            return chunk;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void func_375_a(Chunk chunk) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.world, chunk);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveChunk(Chunk chunk) {
        if (this.chunkLoader != null) {
            try {
                chunk.lastSaveTime = this.world.getWorldTime();
                this.chunkLoader.saveChunk(this.world, chunk);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void populate(IChunkProvider chunkProvider, int x, int z) {
        Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isTerrainPopulated) {
            chunk.isTerrainPopulated = true;
            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(chunkProvider, x, z);
                chunk.setChunkModified();
            }
        }

    }

    public boolean saveChunks(boolean var1, IProgressUpdate progressUpdate) {
        int saved = 0;

        for (int i = 0; i < this.chunks.size(); ++i) {
            Chunk chunk = this.chunks.get(i);
            if (var1 && !chunk.neverSave) {
                this.func_375_a(chunk);
            }

            if (chunk.needsSaving(var1)) {
                this.saveChunk(chunk);
                chunk.isModified = false;
                ++saved;
                if (saved == 24 && !var1) {
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

    public boolean func_361_a() {
        if (!this.world.levelSaving) {
            for (int i = 0; i < 100; ++i) {
                if (!this.chunkCoords.isEmpty()) {
                    int id = this.chunkCoords.iterator().nextInt();
                    Chunk chunk = this.id2ChunkMap.get(id);
                    chunk.onChunkUnload();
                    this.saveChunk(chunk);
                    this.func_375_a(chunk);
                    this.chunkCoords.remove(id);
                    this.id2ChunkMap.remove(id);
                    this.chunks.remove(chunk);
                }
            }

            if (this.chunkLoader != null) {
                this.chunkLoader.func_661_a();
            }
        }

        return this.serverChunkGenerator.func_361_a();
    }

    public boolean func_364_b() {
        return !this.world.levelSaving;
    }
}
