package net.minecraft;

import it.unimi.dsi.fastutil.ints.*;

import java.io.IOException;
import java.util.*;

public class ChunkProvider implements IChunkProvider {
    private IntSet field_28062_a = new IntRBTreeSet();
    private Chunk field_28061_b;
    private IChunkProvider chunkGenerator;
    private IChunkLoader field_28066_d;
    private Int2ObjectMap<Chunk> field_28065_e = new Int2ObjectRBTreeMap<>();
    private List<Chunk> field_28064_f = new ArrayList<>();
    private World worldObj;

    public ChunkProvider(World var1, IChunkLoader var2, IChunkProvider var3) {
        this.field_28061_b = new EmptyChunk(var1, new byte['\u8000'], 0, 0);
        this.worldObj = var1;
        this.field_28066_d = var2;
        this.chunkGenerator = var3;
    }

    public boolean chunkExists(int var1, int var2) {
        return this.field_28065_e.containsKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
    }

    public Chunk loadChunk(int var1, int var2) {
        int chunkXZ2Int = ChunkCoordIntPair.chunkXZ2Int(var1, var2);
        this.field_28062_a.remove(chunkXZ2Int);
        Chunk chunk = this.field_28065_e.get(chunkXZ2Int);
        if (chunk == null) {
            chunk = this.func_28058_d(var1, var2);
            if (chunk == null) {
                if (this.chunkGenerator == null) {
                    chunk = this.field_28061_b;
                } else {
                    chunk = this.chunkGenerator.provideChunk(var1, var2);
                }
            }

            this.field_28065_e.put(chunkXZ2Int, chunk);
            this.field_28064_f.add(chunk);
            if (chunk != null) {
                chunk.func_4053_c();
                chunk.onChunkLoad();
            }

            if (!chunk.isTerrainPopulated && this.chunkExists(var1 + 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 + 1, var2)) {
                this.populate(this, var1, var2);
            }

            if (this.chunkExists(var1 - 1, var2) && !this.provideChunk(var1 - 1, var2).isTerrainPopulated && this.chunkExists(var1 - 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 - 1, var2)) {
                this.populate(this, var1 - 1, var2);
            }

            if (this.chunkExists(var1, var2 - 1) && !this.provideChunk(var1, var2 - 1).isTerrainPopulated && this.chunkExists(var1 + 1, var2 - 1) && this.chunkExists(var1, var2 - 1) && this.chunkExists(var1 + 1, var2)) {
                this.populate(this, var1, var2 - 1);
            }

            if (this.chunkExists(var1 - 1, var2 - 1) && !this.provideChunk(var1 - 1, var2 - 1).isTerrainPopulated && this.chunkExists(var1 - 1, var2 - 1) && this.chunkExists(var1, var2 - 1) && this.chunkExists(var1 - 1, var2)) {
                this.populate(this, var1 - 1, var2 - 1);
            }
        }

        return chunk;
    }

    public Chunk provideChunk(int var1, int var2) {
        Chunk chunk = this.field_28065_e.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
        return chunk == null ? this.loadChunk(var1, var2) : chunk;
    }

    private Chunk func_28058_d(int var1, int var2) {
        if (this.field_28066_d == null) {
            return null;
        } else {
            try {
                Chunk chunk = this.field_28066_d.loadChunk(this.worldObj, var1, var2);
                if (chunk != null) {
                    chunk.lastSaveTime = this.worldObj.getWorldTime();
                }

                return chunk;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void func_28060_a(Chunk chunk) {
        if (this.field_28066_d != null) {
            try {
                this.field_28066_d.saveExtraChunkData(this.worldObj, chunk);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void func_28059_b(Chunk chunk) {
        if (this.field_28066_d != null) {
            try {
                chunk.lastSaveTime = this.worldObj.getWorldTime();
                this.field_28066_d.saveChunk(this.worldObj, chunk);
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

        for (int var4 = 0; var4 < this.field_28064_f.size(); ++var4) {
            Chunk chunk = this.field_28064_f.get(var4);
            if (var1 && !chunk.neverSave) {
                this.func_28060_a(chunk);
            }

            if (chunk.needsSaving(var1)) {
                this.func_28059_b(chunk);
                chunk.isModified = false;
                ++var3;
                if (var3 == 24 && !var1) {
                    return false;
                }
            }
        }

        if (var1) {
            if (this.field_28066_d == null) {
                return true;
            }

            this.field_28066_d.saveExtraData();
        }

        return true;
    }

    public boolean func_361_a() {
        for (int var1 = 0; var1 < 100; ++var1) {
            if (!this.field_28062_a.isEmpty()) {
                Integer var2 = this.field_28062_a.iterator().next();
                Chunk var3 = this.field_28065_e.get(var2);
                var3.onChunkUnload();
                this.func_28059_b(var3);
                this.func_28060_a(var3);
                this.field_28062_a.remove(var2);
                this.field_28065_e.remove(var2);
                this.field_28064_f.remove(var3);
            }
        }

        if (this.field_28066_d != null) {
            this.field_28066_d.func_661_a();
        }

        return this.chunkGenerator.func_361_a();
    }

    public boolean func_364_b() {
        return true;
    }
}
