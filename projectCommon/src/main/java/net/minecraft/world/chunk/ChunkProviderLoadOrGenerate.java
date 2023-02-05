package net.minecraft.world.chunk;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.util.IProgressUpdatable;
import net.minecraft.world.World;

import java.io.IOException;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
    int lastQueriedChunkXPos;
    int lastQueriedChunkZPos;
    private Chunk blankChunk;
    private IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    private Chunk[] chunks;
    private World world;
    private Chunk lastQueriedChunk;
    private int curChunkX;
    private int curChunkY;

    public void setCurrentChunkOver(int var1, int var2) {
        this.curChunkX = var1;
        this.curChunkY = var2;
    }

    public boolean canChunkExist(int var1, int var2) {
        byte var3 = 15;
        return var1 >= this.curChunkX - var3 && var2 >= this.curChunkY - var3 && var1 <= this.curChunkX + var3 && var2 <= this.curChunkY + var3;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        if (!this.canChunkExist(x, z)) {
            return false;
        } else if (x == this.lastQueriedChunkXPos && z == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
            return true;
        } else {
            int var3 = x & 31;
            int var4 = z & 31;
            int var5 = var3 + var4 * 32;
            return this.chunks[var5] != null && (this.chunks[var5] == this.blankChunk || this.chunks[var5].isAtLocation(x, z));
        }
    }

    @Override
    public Chunk prepareChunk(int x, int z) {
        return this.provideChunk(x, z);
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        if (x == this.lastQueriedChunkXPos && z == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
            return this.lastQueriedChunk;
        } else if (!this.world.findingSpawnPoint && !this.canChunkExist(x, z)) {
            return this.blankChunk;
        } else {
            int var3 = x & 31;
            int var4 = z & 31;
            int var5 = var3 + var4 * 32;
            if (!this.chunkExists(x, z)) {
                if (this.chunks[var5] != null) {
                    this.chunks[var5].onChunkUnload();
                    this.saveChunk(this.chunks[var5]);
                    this.saveExtraChunkData(this.chunks[var5]);
                }

                Chunk var6 = this.func_542_c(x, z);
                if (var6 == null) {
                    if (this.chunkProvider == null) {
                        var6 = this.blankChunk;
                    } else {
                        var6 = this.chunkProvider.provideChunk(x, z);
                        var6.checkBlocks();
                    }
                }

                this.chunks[var5] = var6;
                var6.prepareChunkLoad();
                if (this.chunks[var5] != null) {
                    this.chunks[var5].onChunkLoad();
                }

                if (!this.chunks[var5].terrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
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

            this.lastQueriedChunkXPos = x;
            this.lastQueriedChunkZPos = z;
            this.lastQueriedChunk = this.chunks[var5];
            return this.chunks[var5];
        }
    }

    private Chunk func_542_c(int var1, int var2) {
        if (this.chunkLoader == null) {
            return this.blankChunk;
        } else {
            try {
                Chunk var3 = this.chunkLoader.loadChunk(this.world, var1, var2);
                if (var3 != null) {
                    var3.lastSaveTime = this.world.getWorldTime();
                }

                return var3;
            } catch (Exception e) {
                e.printStackTrace();
                return this.blankChunk;
            }
        }
    }

    private void saveExtraChunkData(Chunk var1) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.world, var1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void saveChunk(Chunk var1) {
        if (this.chunkLoader != null) {
            try {
                var1.lastSaveTime = this.world.getWorldTime();
                this.chunkLoader.saveChunk(this.world, var1);
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
            if (this.chunkProvider != null) {
                this.chunkProvider.populate(provider, x, z);
                var4.setChunkModified();
            }
        }

    }

    @Override
    public boolean saveChunks(boolean forceSave, IProgressUpdatable updatable) {
        int var3 = 0;
        int var4 = 0;
        if (updatable != null) {
            for (int var5 = 0; var5 < this.chunks.length; ++var5) {
                if (this.chunks[var5] != null && this.chunks[var5].needsSaving(forceSave)) {
                    ++var4;
                }
            }
        }

        int var7 = 0;

        for (int var6 = 0; var6 < this.chunks.length; ++var6) {
            if (this.chunks[var6] != null) {
                if (forceSave && !this.chunks[var6].neverSave) {
                    this.saveExtraChunkData(this.chunks[var6]);
                }

                if (this.chunks[var6].needsSaving(forceSave)) {
                    this.saveChunk(this.chunks[var6]);
                    this.chunks[var6].modified = false;
                    ++var3;
                    if (var3 == 2 && !forceSave) {
                        return false;
                    }

                    if (updatable != null) {
                        ++var7;
                        if (var7 % 10 == 0) {
                            updatable.setLoadingProgress(var7 * 100 / var4);
                        }
                    }
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
        if (this.chunkLoader != null) {
            this.chunkLoader.onUnloadOldest();
        }

        return this.chunkProvider.unload100OldestChunks();
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    @Side(CodeSide.CLIENT)
    public String makeString() {
        return "ChunkCache: " + this.chunks.length;
    }
}
