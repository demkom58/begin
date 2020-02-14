package net.potion.world.chunk;

import net.potion.util.IProgressUpdatable;
import net.potion.world.World;

import java.io.IOException;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
    int lastQueriedChunkXPos;
    int lastQueriedChunkZPos;
    private Chunk blankChunk;
    private IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    private Chunk[] chunks;
    private World worldObj;
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
        } else if (!this.worldObj.findingSpawnPoint && !this.canChunkExist(x, z)) {
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
                        var6.func_25124_i();
                    }
                }

                this.chunks[var5] = var6;
                var6.func_4143_d();
                if (this.chunks[var5] != null) {
                    this.chunks[var5].onChunkLoad();
                }

                if (!this.chunks[var5].isTerrainPopulated && this.chunkExists(x + 1, z + 1) && this.chunkExists(x, z + 1) && this.chunkExists(x + 1, z)) {
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
                Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, var1, var2);
                if (var3 != null) {
                    var3.lastSaveTime = this.worldObj.getWorldTime();
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
        if (!var4.isTerrainPopulated) {
            var4.isTerrainPopulated = true;
            if (this.chunkProvider != null) {
                this.chunkProvider.populate(provider, x, z);
                var4.setChunkModified();
            }
        }

    }

    @Override
    public boolean saveChunks(boolean var1, IProgressUpdatable progressUpdatable) {
        int var3 = 0;
        int var4 = 0;
        if (progressUpdatable != null) {
            for (int var5 = 0; var5 < this.chunks.length; ++var5) {
                if (this.chunks[var5] != null && this.chunks[var5].needsSaving(var1)) {
                    ++var4;
                }
            }
        }

        int var7 = 0;

        for (int var6 = 0; var6 < this.chunks.length; ++var6) {
            if (this.chunks[var6] != null) {
                if (var1 && !this.chunks[var6].neverSave) {
                    this.saveExtraChunkData(this.chunks[var6]);
                }

                if (this.chunks[var6].needsSaving(var1)) {
                    this.saveChunk(this.chunks[var6]);
                    this.chunks[var6].isModified = false;
                    ++var3;
                    if (var3 == 2 && !var1) {
                        return false;
                    }

                    if (progressUpdatable != null) {
                        ++var7;
                        if (var7 % 10 == 0) {
                            progressUpdatable.setLoadingProgress(var7 * 100 / var4);
                        }
                    }
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
        if (this.chunkLoader != null) {
            this.chunkLoader.func_814_a();
        }

        return this.chunkProvider.unload100OldestChunks();
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "ChunkCache: " + this.chunks.length;
    }
}
