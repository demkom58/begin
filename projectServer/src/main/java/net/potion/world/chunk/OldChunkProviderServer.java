package net.potion.world.chunk;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.util.IProgressUpdatable;
import net.potion.world.WorldServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldChunkProviderServer implements IOldChunkProvider {
    public boolean chunkLoadOverride = false;
    private final IntSet unloadList = new IntRBTreeSet();
    private final OldChunk dummyChunk;
    private final IOldChunkProvider serverChunkGenerator;
    private final IOldChunkLoader chunkLoader;
    private final Int2ObjectMap<OldChunk> id2ChunkMap = new Int2ObjectRBTreeMap<>();
    private final List<OldChunk> chunks = new ArrayList<>();
    private final WorldServer world;

    public OldChunkProviderServer(WorldServer worldServer, IOldChunkLoader chunkLoader, IOldChunkProvider chunkProvider) {
        this.dummyChunk = new EmptyOldChunk(worldServer, new byte[32768], 0, 0);
        this.world = worldServer;
        this.chunkLoader = chunkLoader;
        this.serverChunkGenerator = chunkProvider;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return this.id2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }

    public void addForUnload(int x, int z) {
        ChunkCoordinates coordinates = this.world.getSpawnPoint();
        int worldX = x * 16 + 8 - coordinates.x;
        int worldZ = z * 16 + 8 - coordinates.z;
        short size = 128;
        if (worldX < -size || worldX > size || worldZ < -size || worldZ > size) {
            this.unloadList.add(ChunkCoordIntPair.chunkXZ2Int(x, z));
        }

    }

    @Override
    public OldChunk prepareChunk(int x, int z) {
        int chunkXZ2Int = ChunkCoordIntPair.chunkXZ2Int(x, z);
        this.unloadList.remove(chunkXZ2Int);
        OldChunk chunk = this.id2ChunkMap.get(chunkXZ2Int);
        if (chunk == null) {
            chunk = this.loadChunk(x, z);
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
        }

        return chunk;
    }

    @Override
    public OldChunk provideChunk(int x, int z) {
        OldChunk chunk = this.id2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
        if (chunk == null) {
            return !this.world.findingSpawnPoint && !this.chunkLoadOverride ? this.dummyChunk : this.prepareChunk(x, z);
        }

        return chunk;
    }

    private OldChunk loadChunk(int x, int z) {
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

    private void saveExtraData(OldChunk chunk) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.world, chunk);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveChunk(OldChunk chunk) {
        if (this.chunkLoader != null) {
            try {
                chunk.lastSaveTime = this.world.getWorldTime();
                this.chunkLoader.saveChunk(this.world, chunk);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void populate(IOldChunkProvider chunkProvider, int x, int z) {
        OldChunk chunk = this.provideChunk(x, z);
        if (!chunk.terrainPopulated) {
            chunk.terrainPopulated = true;
            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(chunkProvider, x, z);
                chunk.setChunkModified();
            }
        }

    }

    @Override
    public boolean saveChunks(boolean forceSave, IProgressUpdatable progressUpdate) {
        int saved = 0;

        for (int i = 0; i < this.chunks.size(); ++i) {
            OldChunk chunk = this.chunks.get(i);
            if (forceSave && !chunk.neverSave) {
                this.saveExtraData(chunk);
            }

            if (chunk.needsSaving(forceSave)) {
                this.saveChunk(chunk);
                chunk.modified = false;
                ++saved;
                if (saved == 24 && !forceSave) {
                    return false;
                }
            }
        }

        if (forceSave) {
            if (this.chunkLoader == null) {
                return true;
            }

            this.chunkLoader.saveExtraData();
        }

        return true;
    }

    @Override
    public boolean unload100OldestChunks() {
        if (this.world.levelSaving) {
            return this.serverChunkGenerator.unload100OldestChunks();
        }

        for (int i = 0; i < 100; ++i) {
            if (!this.unloadList.isEmpty()) {
                int id = this.unloadList.iterator().nextInt();
                OldChunk chunk = this.id2ChunkMap.get(id);
                chunk.onChunkUnload();
                this.saveChunk(chunk);
                this.saveExtraData(chunk);
                this.unloadList.remove(id);
                this.id2ChunkMap.remove(id);
                this.chunks.remove(chunk);
            }
        }

        if (this.chunkLoader != null) {
            this.chunkLoader.method1();
        }

        return this.serverChunkGenerator.unload100OldestChunks();
    }

    @Override
    public boolean canSave() {
        return !this.world.levelSaving;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String makeString() {
        return "MultiplayerChunkCache: " + this.id2ChunkMap.size();
    }

    public OldChunk getDummyChunk() {
        return dummyChunk;
    }

    public IOldChunkLoader getChunkLoader() {
        return chunkLoader;
    }

    public IOldChunkProvider getServerChunkGenerator() {
        return serverChunkGenerator;
    }

    public Int2ObjectMap<OldChunk> getId2ChunkMap() {
        return id2ChunkMap;
    }

    public IntSet getUnloadList() {
        return unloadList;
    }

    public List<OldChunk> getChunks() {
        return chunks;
    }

    public WorldServer getWorld() {
        return world;
    }

}
