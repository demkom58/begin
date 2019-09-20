package net.minecraft.world.chunk;

import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

public class ChunkProvider implements IChunkProvider {
    private Set droppedChunksSet = new HashSet();
    private Chunk field_28064_b;
    private IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    private Map chunkMap = new HashMap();
    private List chunkList = new ArrayList();
    private World field_28066_g;

    public ChunkProvider(World var1, IChunkLoader var2, IChunkProvider var3) {
        this.field_28064_b = new EmptyChunk(var1, new byte['\u8000'], 0, 0);
        this.field_28066_g = var1;
        this.chunkLoader = var2;
        this.chunkProvider = var3;
    }

    public boolean chunkExists(int x, int z) {
        return this.chunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }

    public Chunk prepareChunk(int x, int z) {
        int var3 = ChunkCoordIntPair.chunkXZ2Int(x, z);
        this.droppedChunksSet.remove(var3);
        Chunk var4 = (Chunk) this.chunkMap.get(var3);
        if (var4 == null) {
            var4 = this.loadChunkFromFile(x, z);
            if (var4 == null) {
                if (this.chunkProvider == null) {
                    var4 = this.field_28064_b;
                } else {
                    var4 = this.chunkProvider.provideChunk(x, z);
                }
            }

            this.chunkMap.put(var3, var4);
            this.chunkList.add(var4);
            if (var4 != null) {
                var4.func_4143_d();
                var4.onChunkLoad();
            }

            if (!var4.isTerrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
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

        return var4;
    }

    public Chunk provideChunk(int x, int z) {
        Chunk var3 = (Chunk) this.chunkMap.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return var3 == null ? this.prepareChunk(x, z) : var3;
    }

    private Chunk loadChunkFromFile(int var1, int var2) {
        if (this.chunkLoader == null) {
            return null;
        } else {
            try {
                Chunk var3 = this.chunkLoader.loadChunk(this.field_28066_g, var1, var2);
                if (var3 != null) {
                    var3.lastSaveTime = this.field_28066_g.getWorldTime();
                }

                return var3;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void saveChunkExtra(Chunk var1) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.field_28066_g, var1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveChunk(Chunk var1) {
        if (this.chunkLoader != null) {
            try {
                var1.lastSaveTime = this.field_28066_g.getWorldTime();
                this.chunkLoader.saveChunk(this.field_28066_g, var1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void populate(IChunkProvider provider, int x, int z) {
        Chunk var4 = this.provideChunk(x, z);
        if (!var4.isTerrainPopulated) {
            var4.isTerrainPopulated = true;
            if (this.chunkProvider != null) {
                this.chunkProvider.populate(provider, x, z);
                var4.setChunkModified();
            }
        }

    }

    public boolean saveChunks(boolean var1, IProgressUpdatable progressUpdatable) {
        int var3 = 0;

        for (int var4 = 0; var4 < this.chunkList.size(); ++var4) {
            Chunk var5 = (Chunk) this.chunkList.get(var4);
            if (var1 && !var5.neverSave) {
                this.saveChunkExtra(var5);
            }

            if (var5.needsSaving(var1)) {
                this.saveChunk(var5);
                var5.isModified = false;
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

    public boolean unload100OldestChunks() {
        for (int var1 = 0; var1 < 100; ++var1) {
            if (!this.droppedChunksSet.isEmpty()) {
                Integer var2 = (Integer) this.droppedChunksSet.iterator().next();
                Chunk var3 = (Chunk) this.chunkMap.get(var2);
                var3.onChunkUnload();
                this.saveChunk(var3);
                this.saveChunkExtra(var3);
                this.droppedChunksSet.remove(var2);
                this.chunkMap.remove(var2);
                this.chunkList.remove(var3);
            }
        }

        if (this.chunkLoader != null) {
            this.chunkLoader.func_814_a();
        }

        return this.chunkProvider.unload100OldestChunks();
    }

    public boolean canSave() {
        return true;
    }

    public String makeString() {
        return "ServerChunkCache: " + this.chunkMap.size() + " Drop: " + this.droppedChunksSet.size();
    }
}
