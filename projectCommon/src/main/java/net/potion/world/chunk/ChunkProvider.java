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

public class ChunkProvider implements IChunkProvider {
    private final IntSet droppedChunksSet = new IntRBTreeSet();
    private final Chunk chunk;
    private final IChunkProvider chunkGenerator;
    private final IChunkLoader chunkLoader;
    private final Int2ObjectMap<Chunk> cord2ChunkMap = new Int2ObjectRBTreeMap<>();
    private final List<Chunk> chunks = new ArrayList<>();
    private final World worldObj;

    public ChunkProvider(World var1, IChunkLoader var2, IChunkProvider var3) {
        this.chunk = new EmptyChunk(var1, new byte['\u8000'], 0, 0);
        this.worldObj = var1;
        this.chunkLoader = var2;
        this.chunkGenerator = var3;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return this.cord2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }

    @Override
    public Chunk prepareChunk(int x, int z) {
        int var3 = ChunkCoordIntPair.chunkXZ2Int(x, z);
        this.droppedChunksSet.remove(var3);
        Chunk var4 = this.cord2ChunkMap.get(var3);
        if (var4 == null) {
            var4 = this.loadChunkFromFile(x, z);
            if (var4 == null) {
                if (this.chunkGenerator == null) {
                    var4 = this.chunk;
                } else {
                    var4 = this.chunkGenerator.provideChunk(x, z);
                }
            }

            this.cord2ChunkMap.put(var3, var4);
            this.chunks.add(var4);
            if (var4 != null) {
                var4.method3();
                var4.onChunkLoad();
            }

            if (!var4.terrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
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

        return var4;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        Chunk var3 = this.cord2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return var3 == null ? this.prepareChunk(x, z) : var3;
    }

    private Chunk loadChunkFromFile(int var1, int var2) {
        if (this.chunkLoader == null) {
            return null;
        }

        try {
            Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, var1, var2);
            if (var3 != null) {
                var3.lastSaveTime = this.worldObj.getWorldTime();
            }

            return var3;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveChunkExtra(Chunk var1) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, var1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveChunk(Chunk var1) {
        if (this.chunkLoader != null) {
            try {
                var1.lastSaveTime = this.worldObj.getWorldTime();
                this.chunkLoader.saveChunk(this.worldObj, var1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void populate(IChunkProvider provider, int x, int z) {
        Chunk var4 = this.provideChunk(x, z);
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
            Chunk var5 = this.chunks.get(var4);
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
                Chunk var3 = this.cord2ChunkMap.get(var2);
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
