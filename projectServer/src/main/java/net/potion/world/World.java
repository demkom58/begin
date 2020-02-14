package net.potion.world;

import com.demkom58.timings.WorldTimingsHandler;
import net.potion.block.Block;
import net.potion.block.BlockFluid;
import net.potion.block.EnumSkyBlock;
import net.potion.entity.Entity;
import net.potion.entity.EntityLightningBolt;
import net.potion.entity.SpawnerAnimals;
import net.potion.entity.ai.PathEntity;
import net.potion.entity.ai.Pathfinder;
import net.potion.entity.player.EntityPlayer;
import net.potion.item.MapDataBase;
import net.potion.material.Material;
import net.potion.tileentity.TileEntity;
import net.potion.util.*;
import net.potion.world.chunk.*;
import net.potion.world.gen.BiomeGenBase;
import net.potion.world.storage.ISaveHandler;
import net.potion.world.storage.MapStorage;

import java.util.*;

public class World implements IBlockAccess {
    public final WorldTimingsHandler timings;
    private static int lightingUpdatesScheduled = 0;
    public final WorldProvider worldProvider;
    protected final ISaveHandler worldFile;
    public boolean scheduledUpdatesAreImmediate = false;
    public List<Entity> loadedEntityList = new ArrayList<>();
    public List<TileEntity> loadedTileEntityList = new ArrayList<>();
    public List<EntityPlayer> playerEntities = new ArrayList<>();
    public List<Entity> weatherEffects = new ArrayList<>();
    public int skylightSubtracted = 0;
    public int field_27080_i = 0;
    public boolean editingBlocks = false;
    public int difficultySetting;
    public Random rand = new Random();
    public boolean isNewWorld;
    public boolean findingSpawnPoint;
    public MapStorage mapStorage;
    public boolean singleplayerWorld;
    protected int distHashCounter = (new Random()).nextInt();
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    protected int field_27075_F = 0;
    protected int autosavePeriod = 40;
    protected List<IWorldAccess> worldAccesses = new ArrayList<>();
    protected IChunkProvider chunkProvider;
    protected WorldInfo worldInfo;
    private List<MetadataChunkBlock> lightingToUpdate = new ArrayList<>();
    private List<Entity> unloadedEntityList = new ArrayList<>();
    private TreeSet<NextTickListEntry> scheduledTickTreeSet = new TreeSet<>();
    private Set<NextTickListEntry> scheduledTickSet = new HashSet<>();
    private List<TileEntity> field_20912_E = new ArrayList<>();
    private boolean allPlayersSleeping;
    private ArrayList<AxisAlignedBB> collidingBoundingBoxes = new ArrayList<>();
    private boolean field_31048_L;
    private int lightingUpdatesCounter = 0;
    private boolean spawnHostileMobs = true;
    private boolean spawnPeacefulMobs = true;
    private Set<ChunkCoordIntPair> activeChunkSet = new HashSet<>();
    private int ambientTickCountdown;
    private List<Entity> entities;

    public World(ISaveHandler saveHandler, String levelName, long randomSeed, WorldProvider worldProvider) {
        this.ambientTickCountdown = this.rand.nextInt(12000);
        this.entities = new ArrayList<>();
        this.singleplayerWorld = false;
        this.worldFile = saveHandler;
        this.mapStorage = new MapStorage(saveHandler);
        this.worldInfo = saveHandler.loadWorldInfo();
        this.isNewWorld = this.worldInfo == null;
        if (worldProvider != null) {
            this.worldProvider = worldProvider;
        } else if (this.worldInfo != null && this.worldInfo.getDimension() == -1) {
            this.worldProvider = WorldProvider.getProviderForDimension(-1);
        } else {
            this.worldProvider = WorldProvider.getProviderForDimension(0);
        }

        boolean isNew = false;
        if (this.worldInfo == null) {
            this.worldInfo = new WorldInfo(randomSeed, levelName);
            isNew = true;
        } else {
            this.worldInfo.setLevelName(levelName);
        }

        this.worldProvider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        if (isNew) {
            this.generateSpawnPoint();
        }

        this.calculateInitialSkylight();
        this.func_27070_x();
        this.timings = new WorldTimingsHandler(this);
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.worldChunkMgr;
    }

    protected IChunkProvider createChunkProvider() {
        IChunkLoader var1 = this.worldFile.func_22092_a(this.worldProvider);
        return new ChunkProvider(this, var1, this.worldProvider.getChunkProvider());
    }

    protected void generateSpawnPoint() {
        this.findingSpawnPoint = true;
        int var1 = 0;
        byte var2 = 64;

        int var3;
        for (var3 = 0; !this.worldProvider.canCoordinateBeSpawn(var1, var3); var3 += this.rand.nextInt(64) - this.rand.nextInt(64)) {
            var1 += this.rand.nextInt(64) - this.rand.nextInt(64);
        }

        this.worldInfo.setSpawn(var1, var2, var3);
        this.findingSpawnPoint = false;
    }

    public int getFirstUncoveredBlock(int var1, int var2) {
        int var3;
        for (var3 = 63; !this.isAirBlock(var1, var3 + 1, var2); ++var3) {
        }

        return this.getBlockId(var1, var3, var2);
    }

    public void saveWorld(boolean var1, IProgressUpdatable progressUpdate) {
        if (this.chunkProvider.canSave()) {
            timings.worldSave.startTiming();
            if (progressUpdate != null) {
                progressUpdate.display("Saving level");
            }

            this.saveLevel();
            if (progressUpdate != null) {
                progressUpdate.displayLoadingString("Saving chunks");
            }

            timings.worldSaveChunks.startTiming();
            this.chunkProvider.saveChunks(var1, progressUpdate);
            timings.worldSaveChunks.stopTiming();
            timings.worldSave.stopTiming();
        }
    }

    private void saveLevel() {
        this.checkSessionLock();
        this.worldFile.saveWorldInfoAndPlayer(this.worldInfo, this.playerEntities);
        this.mapStorage.saveAllData();
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0) {
                return 0;
            } else {
                return y >= 128 ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockID(x & 15, y, z & 15);
            }
        } else {
            return 0;
        }
    }

    public boolean isAirBlock(int x, int y, int z) {
        return this.getBlockId(x, y, z) == 0;
    }

    public boolean blockExists(int x, int y, int z) {
        return (y >= 0 && y < 128) && this.chunkExists(x >> 4, z >> 4);
    }

    public boolean doChunksNearChunkExist(int var1, int var2, int var3, int var4) {
        return this.checkChunksExist(var1 - var4, var2 - var4, var3 - var4, var1 + var4, var2 + var4, var3 + var4);
    }

    public boolean checkChunksExist(int var1, int var2, int var3, int var4, int var5, int var6) {
        if (var5 >= 0 && var2 < 128) {
            var1 = var1 >> 4;
            var2 = var2 >> 4;
            var3 = var3 >> 4;
            var4 = var4 >> 4;
            var5 = var5 >> 4;
            var6 = var6 >> 4;

            for (int var7 = var1; var7 <= var4; ++var7) {
                for (int var8 = var3; var8 <= var6; ++var8) {
                    if (!this.chunkExists(var7, var8)) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    private boolean chunkExists(int x, int z) {
        return this.chunkProvider.chunkExists(x, z);
    }

    public Chunk getChunkFromBlockCoords(int var1, int var2) {
        return this.getChunkFromChunkCoords(var1 >> 4, var2 >> 4);
    }

    public Chunk getChunkFromChunkCoords(int var1, int var2) {
        return this.chunkProvider.provideChunk(var1, var2);
    }

    public boolean setBlockAndMetadata(int x, int y, int z, int id, int meta) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0)
                return false;

            if (y >= 128)
                return false;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            return chunk.setBlockIDWithMetadata(x & 15, y, z & 15, id, meta);
        }

        return false;
    }

    public boolean setBlock(int x, int y, int z, int id) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0)
                return false;

            if (y >= 128)
                return false;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            return chunk.setBlockID(x & 15, y, z & 15, id);
        }

        return false;
    }

    @Override
    public Material getBlockMaterial(int x, int y, int z) {
        int id = this.getBlockId(x, y, z);
        return id == 0 ? Material.AIR : Block.BLOCKS_LIST[id].blockMaterial;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0)
                return 0;

            if (y >= 128)
                return 0;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            x = x & 15;
            z = z & 15;
            return chunk.getBlockMetadata(x, y, z);
        }

        return 0;
    }

    public void setBlockMetadataWithNotify(int x, int y, int z, int meta) {
        if (this.setBlockMetadata(x, y, z, meta)) {
            int blockId = this.getBlockId(x, y, z);
            if (Block.REQUIRES_SELF_NOTIFY[blockId & 255]) {
                this.notifyBlockChange(x, y, z, blockId);
            } else {
                this.notifyBlocksOfNeighborChange(x, y, z, blockId);
            }
        }

    }

    public boolean setBlockMetadata(int x, int y, int z, int meta) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0)
                return false;

            if (y >= 128)
                return false;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            x = x & 15;
            z = z & 15;
            chunk.setBlockMetadata(x, y, z, meta);
            return true;
        }

        return false;
    }

    public boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
        if (this.setBlock(var1, var2, var3, var4)) {
            this.notifyBlockChange(var1, var2, var3, var4);
            return true;
        }

        return false;
    }

    public boolean setBlockAndMetadataWithNotify(int var1, int var2, int var3, int var4, int var5) {
        if (this.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
            this.notifyBlockChange(var1, var2, var3, var4);
            return true;
        }

        return false;
    }

    public void markBlockNeedsUpdate(int x, int y, int z) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).markBlockNeedsUpdate(x, y, z);
        }

    }

    protected void notifyBlockChange(int var1, int var2, int var3, int var4) {
        this.markBlockNeedsUpdate(var1, var2, var3);
        this.notifyBlocksOfNeighborChange(var1, var2, var3, var4);
    }

    public void markBlocksDirtyVertical(int var1, int var2, int var3, int var4) {
        if (var3 > var4) {
            int var5 = var4;
            var4 = var3;
            var3 = var5;
        }

        this.markBlocksDirty(var1, var3, var2, var1, var4, var2);
    }

    public void markBlockAsNeedsUpdate(int var1, int var2, int var3) {
        for (int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
            this.worldAccesses.get(var4).markBlockRangeNeedsUpdate(var1, var2, var3, var1, var2, var3);
        }

    }

    public void markBlocksDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
        for (int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
            this.worldAccesses.get(var7).markBlockRangeNeedsUpdate(var1, var2, var3, var4, var5, var6);
        }

    }

    public void notifyBlocksOfNeighborChange(int var1, int var2, int var3, int var4) {
        this.notifyBlockOfNeighborChange(var1 - 1, var2, var3, var4);
        this.notifyBlockOfNeighborChange(var1 + 1, var2, var3, var4);
        this.notifyBlockOfNeighborChange(var1, var2 - 1, var3, var4);
        this.notifyBlockOfNeighborChange(var1, var2 + 1, var3, var4);
        this.notifyBlockOfNeighborChange(var1, var2, var3 - 1, var4);
        this.notifyBlockOfNeighborChange(var1, var2, var3 + 1, var4);
    }

    private void notifyBlockOfNeighborChange(int var1, int var2, int var3, int var4) {
        if (!this.editingBlocks && !this.singleplayerWorld) {
            Block block = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
            if (block != null) {
                block.onNeighborBlockChange(this, var1, var2, var3, var4);
            }

        }
    }

    public boolean canBlockSeeTheSky(int x, int y, int z) {
        return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
    }

    public int getBlockLightValueNoChecks(int x, int y, int z) {
        if (y < 0) {
            return 0;
        }

        if (y >= 128) {
            y = 127;
        }

        return this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockLightValue(x & 15, y, z & 15, 0);
    }

    public int getBlockLightValue(int x, int y, int z) {
        return this.getBlockLightValue_do(x, y, z, true);
    }

    public int getBlockLightValue_do(int x, int y, int z, boolean var4) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (var4) {
                int var5 = this.getBlockId(x, y, z);
                if (var5 == Block.STAIR_SINGLE.blockID || var5 == Block.FARMLAND.blockID || var5 == Block.STAIR_COMPACT_COBBLESTONE.blockID || var5 == Block.STAIR_COMPACT_PLANKS.blockID) {
                    int var6 = this.getBlockLightValue_do(x, y + 1, z, false);
                    int var7 = this.getBlockLightValue_do(x + 1, y, z, false);
                    int var8 = this.getBlockLightValue_do(x - 1, y, z, false);
                    int var9 = this.getBlockLightValue_do(x, y, z + 1, false);
                    int var10 = this.getBlockLightValue_do(x, y, z - 1, false);
                    if (var7 > var6)
                        var6 = var7;

                    if (var8 > var6)
                        var6 = var8;

                    if (var9 > var6)
                        var6 = var9;

                    if (var10 > var6)
                        var6 = var10;

                    return var6;
                }
            }

            if (y < 0)
                return 0;

            if (y >= 128)
                y = 127;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            x = x & 15;
            z = z & 15;
            return chunk.getBlockLightValue(x, y, z, this.skylightSubtracted);
        }

        return 15;
    }

    public boolean canExistingBlockSeeTheSky(int x, int y, int z) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0)
                return false;

            if (y >= 128)
                return true;

            if (!this.chunkExists(x >> 4, z >> 4))
                return false;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            x = x & 15;
            z = z & 15;
            return chunk.canBlockSeeTheSky(x, y, z);
        }

        return false;
    }

    public int getHeightValue(int x, int z) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (!this.chunkExists(x >> 4, z >> 4))
                return 0;

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            return chunk.getHeightValue(x & 15, z & 15);
        }

        return 0;
    }

    public void neighborLightPropagationChanged(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
        if (!this.worldProvider.hasNoSky || var1 != EnumSkyBlock.SKY) {
            if (this.blockExists(var2, var3, var4)) {
                if (var1 == EnumSkyBlock.SKY) {
                    if (this.canExistingBlockSeeTheSky(var2, var3, var4)) {
                        var5 = 15;
                    }
                } else if (var1 == EnumSkyBlock.BLOCK) {
                    int var6 = this.getBlockId(var2, var3, var4);
                    if (Block.LIGHT_VALUE[var6] > var5) {
                        var5 = Block.LIGHT_VALUE[var6];
                    }
                }

                if (this.getSavedLightValue(var1, var2, var3, var4) != var5) {
                    this.scheduleLightingUpdate(var1, var2, var3, var4, var2, var3, var4);
                }

            }
        }
    }

    public int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
        if (var3 < 0) {
            var3 = 0;
        }

        if (var3 >= 128) {
            var3 = 127;
        }

        if (var3 >= 0 && var3 < 128 && var2 >= -32000000 && var4 >= -32000000 && var2 < 32000000 && var4 <= 32000000) {
            int var5 = var2 >> 4;
            int var6 = var4 >> 4;
            if (!this.chunkExists(var5, var6)) {
                return 0;
            }

            Chunk chunk = this.getChunkFromChunkCoords(var5, var6);
            return chunk.getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
        }

        return var1.lightValue;
    }

    public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int value) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y >= 0) {
                if (y < 128) {
                    if (this.chunkExists(x >> 4, z >> 4)) {
                        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                        chunk.setLightValue(skyBlock, x & 15, y, z & 15, value);

                        for (int i = 0; i < this.worldAccesses.size(); ++i) {
                            this.worldAccesses.get(i).markBlockNeedsUpdate(x, y, z);
                        }

                    }
                }
            }
        }
    }

    public float getLightBrightness(int x, int y, int z) {
        return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(x, y, z)];
    }

    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }

    public MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
        return this.rayTraceBlocks(var1, var2, false, false);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2, boolean var3) {
        return this.rayTraceBlocks(var1, var2, var3, false);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2, boolean paramBoolean, boolean paramBoolean2) {
        if (!Double.isNaN(var1.xCoord) && !Double.isNaN(var1.yCoord) && !Double.isNaN(var1.zCoord)) {
            if (!Double.isNaN(var2.xCoord) && !Double.isNaN(var2.yCoord) && !Double.isNaN(var2.zCoord)) {
                int i = MathHelper.floor(var2.xCoord);
                int j = MathHelper.floor(var2.yCoord);
                int k = MathHelper.floor(var2.zCoord);

                int m = MathHelper.floor(var1.xCoord);
                int n = MathHelper.floor(var1.yCoord);
                int i1 = MathHelper.floor(var1.zCoord);

                int i2 = this.getBlockId(m, n, i1);
                int i3 = this.getBlockMetadata(m, n, i1);
                Block localBlock1 = Block.BLOCKS_LIST[i2];
                if ((!paramBoolean2 || localBlock1 == null || localBlock1.getCollisionBoundingBoxFromPool(this, m, n, i1) != null) && i2 > 0 && localBlock1.canCollideCheck(i3, paramBoolean)) {
                    MovingObjectPosition localMovingObjectPosition1 = localBlock1.collisionRayTrace(this, m, n, i1, var1, var2);
                    if (localMovingObjectPosition1 != null) {
                        return localMovingObjectPosition1;
                    }
                }

                i2 = 200;

                while (i2-- >= 0) {
                    if (Double.isNaN(var1.xCoord) || Double.isNaN(var1.yCoord) || Double.isNaN(var1.zCoord)) {
                        return null;
                    }

                    if (m == i && n == j && i1 == k) {
                        return null;
                    }

                    i3 = 1;
                    int i4 = 1;
                    int i5 = 1;

                    double d1 = 999.0D;
                    double d2 = 999.0D;
                    double d3 = 999.0D;

                    if (i > m) {
                        d1 = m + 1.0D;
                    } else if (i < m) {
                        d1 = m + 0.0D;
                    } else {
                        i3 = 0;
                    }

                    if (j > n) {
                        d2 = n + 1.0D;
                    } else if (j < n) {
                        d2 = n + 0.0D;
                    } else {
                        i4 = 0;
                    }

                    if (k > i1) {
                        d3 = i1 + 1.0D;
                    } else if (k < i1) {
                        d3 = i1 + 0.0D;
                    } else {
                        i5 = 0;
                    }

                    double var21 = 999.0D;
                    double var23 = 999.0D;
                    double var25 = 999.0D;
                    double var27 = var2.xCoord - var1.xCoord;
                    double var29 = var2.yCoord - var1.yCoord;
                    double var31 = var2.zCoord - var1.zCoord;
                    if (i3 != 0) {
                        var21 = (d1 - var1.xCoord) / var27;
                    }

                    if (i4 != 0) {
                        var23 = (d2 - var1.yCoord) / var29;
                    }

                    if (i5 != 0) {
                        var25 = (d3 - var1.zCoord) / var31;
                    }

                    byte var33 = 0;
                    if (var21 < var23 && var21 < var25) {
                        if (i > m) {
                            var33 = 4;
                        } else {
                            var33 = 5;
                        }

                        var1.xCoord = d1;
                        var1.yCoord += var29 * var21;
                        var1.zCoord += var31 * var21;
                    } else if (var23 < var25) {
                        if (j > n) {
                            var33 = 0;
                        } else {
                            var33 = 1;
                        }

                        var1.xCoord += var27 * var23;
                        var1.yCoord = d2;
                        var1.zCoord += var31 * var23;
                    } else {
                        if (k > i1) {
                            var33 = 2;
                        } else {
                            var33 = 3;
                        }

                        var1.xCoord += var27 * var25;
                        var1.yCoord += var29 * var25;
                        var1.zCoord = d3;
                    }

                    Vec3D var34 = Vec3D.createVector(var1.xCoord, var1.yCoord, var1.zCoord);
                    m = (int) (var34.xCoord = MathHelper.floor(var1.xCoord));
                    if (var33 == 5) {
                        --m;
                        ++var34.xCoord;
                    }

                    n = (int) (var34.yCoord = MathHelper.floor(var1.yCoord));
                    if (var33 == 1) {
                        --n;
                        ++var34.yCoord;
                    }

                    i1 = (int) (var34.zCoord = MathHelper.floor(var1.zCoord));
                    if (var33 == 3) {
                        --i1;
                        ++var34.zCoord;
                    }

                    int var35 = this.getBlockId(m, n, i1);
                    int var36 = this.getBlockMetadata(m, n, i1);
                    Block block = Block.BLOCKS_LIST[var35];
                    if ((!paramBoolean2 || block == null || block.getCollisionBoundingBoxFromPool(this, m, n, i1) != null) && var35 > 0 && block.canCollideCheck(var36, paramBoolean)) {
                        MovingObjectPosition position = block.collisionRayTrace(this, m, n, i1, var1, var2);
                        if (position != null) {
                            return position;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void playSoundAtEntity(Entity entity, String soundName, float volume, float pitch) {
        for (int i = 0; i < this.worldAccesses.size(); ++i)
            this.worldAccesses.get(i).playSound(soundName, entity.posX, entity.posY - (double) entity.yOffset, entity.posZ, volume, pitch);
    }

    public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
        for (int i = 0; i < this.worldAccesses.size(); ++i)
            this.worldAccesses.get(i).playSound(soundName, x, y, z, volume, pitch);
    }

    public void playRecord(String var1, int var2, int var3, int var4) {
        for (int i = 0; i < this.worldAccesses.size(); ++i)
            this.worldAccesses.get(i).playRecord(var1, var2, var3, var4);
    }

    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        for (int i = 0; i < this.worldAccesses.size(); ++i)
            this.worldAccesses.get(i).spawnParticle(var1, var2, var4, var6, var8, var10, var12);
    }

    public boolean addLightningBolt(Entity var1) {
        this.weatherEffects.add(var1);
        return true;
    }

    public boolean entityJoinedWorld(Entity entity) {
        int x = MathHelper.floor(entity.posX / 16.0D);
        int z = MathHelper.floor(entity.posZ / 16.0D);
        boolean isPlayer = false;
        if (entity instanceof EntityPlayer) {
            isPlayer = true;
        }

        if (!isPlayer && !this.chunkExists(x, z)) {
            return false;
        }

        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            this.playerEntities.add(entityPlayer);
            this.updateAllPlayersSleepingFlag();
        }

        this.getChunkFromChunkCoords(x, z).addEntity(entity);
        this.loadedEntityList.add(entity);
        this.obtainEntitySkin(entity);

        return true;
    }

    protected void obtainEntitySkin(Entity entity) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).obtainEntitySkin(entity);
        }

    }

    protected void releaseEntitySkin(Entity entity) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).releaseEntitySkin(entity);
        }

    }

    public void removePlayerForLogoff(Entity entity) {
        if (entity.riddenByEntity != null) {
            entity.riddenByEntity.mountEntity(null);
        }

        if (entity.ridingEntity != null) {
            entity.mountEntity(null);
        }

        entity.setEntityDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            this.updateAllPlayersSleepingFlag();
        }

    }

    public void removePlayer(Entity entity) {
        entity.setEntityDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            this.updateAllPlayersSleepingFlag();
        }

        int chunkCoordX = entity.chunkCoordX;
        int chunkCoordZ = entity.chunkCoordZ;

        if (entity.addedToChunk && this.chunkExists(chunkCoordX, chunkCoordZ)) {
            this.getChunkFromChunkCoords(chunkCoordX, chunkCoordZ).removeEntity(entity);
        }

        this.loadedEntityList.remove(entity);
        this.releaseEntitySkin(entity);
    }

    public void addWorldAccess(IWorldAccess worldAccess) {
        this.worldAccesses.add(worldAccess);
    }

    public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entity, AxisAlignedBB axis) {
        this.collidingBoundingBoxes.clear();
        int var3 = MathHelper.floor(axis.minX);
        int var4 = MathHelper.floor(axis.maxX + 1.0D);
        int var5 = MathHelper.floor(axis.minY);
        int var6 = MathHelper.floor(axis.maxY + 1.0D);
        int var7 = MathHelper.floor(axis.minZ);
        int var8 = MathHelper.floor(axis.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var7; var10 < var8; ++var10) {
                if (this.blockExists(var9, 64, var10)) {
                    for (int var11 = var5 - 1; var11 < var6; ++var11) {
                        Block block = Block.BLOCKS_LIST[this.getBlockId(var9, var11, var10)];
                        if (block != null) {
                            block.getCollidingBoundingBoxes(this, var9, var11, var10, axis, this.collidingBoundingBoxes);
                        }
                    }
                }
            }
        }

        double var14 = 0.25D;
        List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(entity, axis.expand(var14, var14, var14));

        for (int i = 0; i < entities.size(); ++i) {
            AxisAlignedBB bb = entities.get(i).getBoundingBox();
            if (bb != null && bb.intersectsWith(axis)) {
                this.collidingBoundingBoxes.add(bb);
            }

            bb = entity.func_89_d(entities.get(i));
            if (bb != null && bb.intersectsWith(axis)) {
                this.collidingBoundingBoxes.add(bb);
            }
        }

        return this.collidingBoundingBoxes;
    }

    public int calculateSkylightSubtracted(float var1) {
        float var2 = this.getCelestialAngle(var1);
        float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F);
        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        var3 = 1.0F - var3;
        var3 = (float) ((double) var3 * (1.0D - (double) (this.func_27074_d(var1) * 5.0F) / 16.0D));
        var3 = (float) ((double) var3 * (1.0D - (double) (this.func_27065_c(var1) * 5.0F) / 16.0D));
        var3 = 1.0F - var3;
        return (int) (var3 * 11.0F);
    }

    public float getCelestialAngle(float var1) {
        return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), var1);
    }

    public int getTopSolidOrLiquidBlock(int var1, int var2) {
        Chunk var3 = this.getChunkFromBlockCoords(var1, var2);
        int var4 = 127;
        var1 = var1 & 15;

        for (int var8 = var2 & 15; var4 > 0; --var4) {
            int var5 = var3.getBlockID(var1, var4, var8);
            Material var6 = var5 == 0 ? Material.AIR : Block.BLOCKS_LIST[var5].blockMaterial;
            if (var6.getIsSolid() || var6.isLiquid()) {
                return var4 + 1;
            }
        }

        return -1;
    }

    public int findTopSolidBlock(int var1, int var2) {
        Chunk chunk = this.getChunkFromBlockCoords(var1, var2);
        int var4 = 127;
        var1 = var1 & 15;

        for (int var7 = var2 & 15; var4 > 0; --var4) {
            int var5 = chunk.getBlockID(var1, var4, var7);
            if (var5 != 0 && Block.BLOCKS_LIST[var5].blockMaterial.getIsSolid()) {
                return var4 + 1;
            }
        }

        return -1;
    }

    public void scheduleUpdateTick(int var1, int var2, int var3, int var4, int var5) {
        NextTickListEntry var6 = new NextTickListEntry(var1, var2, var3, var4);
        byte var7 = 8;
        if (this.scheduledUpdatesAreImmediate) {
            if (this.checkChunksExist(var6.xCoord - var7, var6.yCoord - var7, var6.zCoord - var7, var6.xCoord + var7, var6.yCoord + var7, var6.zCoord + var7)) {
                int var8 = this.getBlockId(var6.xCoord, var6.yCoord, var6.zCoord);
                if (var8 == var6.blockID && var8 > 0) {
                    Block.BLOCKS_LIST[var8].updateTick(this, var6.xCoord, var6.yCoord, var6.zCoord, this.rand);
                }
            }

        } else {
            if (this.checkChunksExist(var1 - var7, var2 - var7, var3 - var7, var1 + var7, var2 + var7, var3 + var7)) {
                if (var4 > 0) {
                    var6.setScheduledTime((long) var5 + this.worldInfo.getWorldTime());
                }

                if (!this.scheduledTickSet.contains(var6)) {
                    this.scheduledTickSet.add(var6);
                    this.scheduledTickTreeSet.add(var6);
                }
            }

        }
    }

    public void tickEntities() {
        for (int i = 0; i < this.weatherEffects.size(); ++i) {
            Entity entity = this.weatherEffects.get(i);

            entity.tickTimer.startTiming();
            entity.onUpdate();
            entity.tickTimer.stopTiming();

            if (entity.isDead) {
                this.weatherEffects.remove(i--);
            }
        }

        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            Entity entity = this.unloadedEntityList.get(i);
            int coordX = entity.chunkCoordX;
            int coordZ = entity.chunkCoordZ;
            if (entity.addedToChunk && this.chunkExists(coordX, coordZ)) {
                this.getChunkFromChunkCoords(coordX, coordZ).removeEntity(entity);
            }
        }

        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            this.releaseEntitySkin(this.unloadedEntityList.get(i));
        }

        this.unloadedEntityList.clear();

        for (int i = 0; i < this.loadedEntityList.size(); ++i) {
            Entity entity = this.loadedEntityList.get(i);
            if (entity.ridingEntity != null) {
                if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity) {
                    continue;
                }

                entity.ridingEntity.riddenByEntity = null;
                entity.ridingEntity = null;
            }

            if (!entity.isDead) {
                entity.tickTimer.startTiming();
                this.updateEntity(entity);
                entity.tickTimer.stopTiming();
            }

            if (entity.isDead) {
                int coordX = entity.chunkCoordX;
                int coordZ = entity.chunkCoordZ;
                if (entity.addedToChunk && this.chunkExists(coordX, coordZ)) {
                    this.getChunkFromChunkCoords(coordX, coordZ).removeEntity(entity);
                }

                this.loadedEntityList.remove(i--);
                this.releaseEntitySkin(entity);
            }
        }

        this.field_31048_L = true;
        Iterator<TileEntity> entityIterator = this.loadedTileEntityList.iterator();

        while (entityIterator.hasNext()) {
            TileEntity tileEntity = entityIterator.next();
            if (!tileEntity.isInvalid()) {
                tileEntity.tickTimer.startTiming();
                tileEntity.updateEntity();
                tileEntity.tickTimer.stopTiming();
            }

            if (tileEntity.isInvalid()) {
                entityIterator.remove();
                Chunk chunk = this.getChunkFromChunkCoords(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4);
                if (chunk != null) {
                    chunk.removeChunkBlockTileEntity(tileEntity.xCoord & 15, tileEntity.yCoord, tileEntity.zCoord & 15);
                }
            }
        }

        this.field_31048_L = false;
        if (!this.field_20912_E.isEmpty()) {
            for (TileEntity tileEntity : this.field_20912_E) {
                if (!tileEntity.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileEntity)) {
                        this.loadedTileEntityList.add(tileEntity);
                    }

                    Chunk chunk = this.getChunkFromChunkCoords(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4);
                    if (chunk != null) {
                        chunk.setChunkBlockTileEntity(tileEntity.xCoord & 15, tileEntity.yCoord, tileEntity.zCoord & 15, tileEntity);
                    }

                    this.markBlockNeedsUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                }
            }

            this.field_20912_E.clear();
        }

    }

    public void func_31047_a(Collection<TileEntity> tiles) {
        if (this.field_31048_L) {
            this.field_20912_E.addAll(tiles);
        } else {
            this.loadedTileEntityList.addAll(tiles);
        }

    }

    public void updateEntity(Entity entity) {
        this.updateEntityWithOptionalForce(entity, true);
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean chunk) {
        int var3 = MathHelper.floor(entity.posX);
        int var4 = MathHelper.floor(entity.posZ);
        byte var5 = 32;
        if (!chunk || this.checkChunksExist(var3 - var5, 0, var4 - var5, var3 + var5, 128, var4 + var5)) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
            entity.prevRotationYaw = entity.rotationYaw;
            entity.prevRotationPitch = entity.rotationPitch;
            if (chunk && entity.addedToChunk) {
                if (entity.ridingEntity != null) {
                    entity.updateRidden();
                } else {
                    entity.onUpdate();
                }
            }

            if (Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
                entity.posX = entity.lastTickPosX;
            }

            if (Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
                entity.posY = entity.lastTickPosY;
            }

            if (Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
                entity.posZ = entity.lastTickPosZ;
            }

            if (Double.isNaN(entity.rotationPitch) || Double.isInfinite(entity.rotationPitch)) {
                entity.rotationPitch = entity.prevRotationPitch;
            }

            if (Double.isNaN(entity.rotationYaw) || Double.isInfinite(entity.rotationYaw)) {
                entity.rotationYaw = entity.prevRotationYaw;
            }

            int var6 = MathHelper.floor(entity.posX / 16.0D);
            int var7 = MathHelper.floor(entity.posY / 16.0D);
            int var8 = MathHelper.floor(entity.posZ / 16.0D);
            if (!entity.addedToChunk || entity.chunkCoordX != var6 || entity.chunkCoordY != var7 || entity.chunkCoordZ != var8) {
                if (entity.addedToChunk && this.chunkExists(entity.chunkCoordX, entity.chunkCoordZ)) {
                    this.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);
                }

                if (this.chunkExists(var6, var8)) {
                    entity.addedToChunk = true;
                    this.getChunkFromChunkCoords(var6, var8).addEntity(entity);
                } else {
                    entity.addedToChunk = false;
                }
            }

            if (chunk && entity.addedToChunk && entity.riddenByEntity != null) {
                if (!entity.riddenByEntity.isDead && entity.riddenByEntity.ridingEntity == entity) {
                    this.updateEntity(entity.riddenByEntity);
                } else {
                    entity.riddenByEntity.ridingEntity = null;
                    entity.riddenByEntity = null;
                }
            }

        }
    }

    public boolean checkIfAABBIsClear(AxisAlignedBB var1) {
        List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(null, var1);

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            if (!entity.isDead && entity.preventEntitySpawning) {
                return false;
            }
        }

        return true;
    }

    public boolean func_27069_b(AxisAlignedBB var1) {
        int fromX = MathHelper.floor(var1.minX);
        int toX = MathHelper.floor(var1.maxX + 1.0D);
        int fromY = MathHelper.floor(var1.minY);
        int toY = MathHelper.floor(var1.maxY + 1.0D);
        int fromZ = MathHelper.floor(var1.minZ);
        int toZ = MathHelper.floor(var1.maxZ + 1.0D);
        if (var1.minX < 0.0D) {
            --fromX;
        }

        if (var1.minY < 0.0D) {
            --fromY;
        }

        if (var1.minZ < 0.0D) {
            --fromZ;
        }

        for (int x = fromX; x < toX; ++x) {
            for (int y = fromY; y < toY; ++y) {
                for (int z = fromZ; z < toZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isAnyLiquid(AxisAlignedBB var1) {
        int fromX = MathHelper.floor(var1.minX);
        int toX = MathHelper.floor(var1.maxX + 1.0D);
        int fromY = MathHelper.floor(var1.minY);
        int toY = MathHelper.floor(var1.maxY + 1.0D);
        int fromZ = MathHelper.floor(var1.minZ);
        int toZ = MathHelper.floor(var1.maxZ + 1.0D);
        if (var1.minX < 0.0D) {
            --fromX;
        }

        if (var1.minY < 0.0D) {
            --fromY;
        }

        if (var1.minZ < 0.0D) {
            --fromZ;
        }

        for (int x = fromX; x < toX; ++x) {
            for (int y = fromY; y < toY; ++y) {
                for (int z = fromZ; z < toZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial.isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isBoundingBoxBurning(AxisAlignedBB var1) {
        int var2 = MathHelper.floor(var1.minX);
        int var3 = MathHelper.floor(var1.maxX + 1.0D);
        int var4 = MathHelper.floor(var1.minY);
        int var5 = MathHelper.floor(var1.maxY + 1.0D);
        int var6 = MathHelper.floor(var1.minZ);
        int var7 = MathHelper.floor(var1.maxZ + 1.0D);
        if (this.checkChunksExist(var2, var4, var6, var3, var5, var7)) {
            for (int var8 = var2; var8 < var3; ++var8) {
                for (int var9 = var4; var9 < var5; ++var9) {
                    for (int var10 = var6; var10 < var7; ++var10) {
                        int var11 = this.getBlockId(var8, var9, var10);
                        if (var11 == Block.FIRE.blockID || var11 == Block.LAVA_MOVING.blockID || var11 == Block.LAVA_STILL.blockID) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2, Entity var3) {
        int var4 = MathHelper.floor(var1.minX);
        int var5 = MathHelper.floor(var1.maxX + 1.0D);
        int var6 = MathHelper.floor(var1.minY);
        int var7 = MathHelper.floor(var1.maxY + 1.0D);
        int var8 = MathHelper.floor(var1.minZ);
        int var9 = MathHelper.floor(var1.maxZ + 1.0D);
        if (!this.checkChunksExist(var4, var6, var8, var5, var7, var9)) {
            return false;
        }

        boolean var10 = false;
        Vec3D var11 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

        for (int var12 = var4; var12 < var5; ++var12) {
            for (int var13 = var6; var13 < var7; ++var13) {
                for (int var14 = var8; var14 < var9; ++var14) {
                    Block var15 = Block.BLOCKS_LIST[this.getBlockId(var12, var13, var14)];
                    if (var15 != null && var15.blockMaterial == var2) {
                        double var16 = (float) (var13 + 1) - BlockFluid.setFluidHeight(this.getBlockMetadata(var12, var13, var14));
                        if ((double) var7 >= var16) {
                            var10 = true;
                            var15.velocityToAddToEntity(this, var12, var13, var14, var3, var11);
                        }
                    }
                }
            }
        }

        if (var11.lengthVector() > 0.0D) {
            var11 = var11.normalize();
            double var19 = 0.014D;
            var3.motionX += var11.xCoord * var19;
            var3.motionY += var11.yCoord * var19;
            var3.motionZ += var11.zCoord * var19;
        }

        return var10;
    }

    public boolean isMaterialInBB(AxisAlignedBB var1, Material var2) {
        int var3 = MathHelper.floor(var1.minX);
        int var4 = MathHelper.floor(var1.maxX + 1.0D);
        int var5 = MathHelper.floor(var1.minY);
        int var6 = MathHelper.floor(var1.maxY + 1.0D);
        int var7 = MathHelper.floor(var1.minZ);
        int var8 = MathHelper.floor(var1.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var5; var10 < var6; ++var10) {
                for (int var11 = var7; var11 < var8; ++var11) {
                    Block var12 = Block.BLOCKS_LIST[this.getBlockId(var9, var10, var11)];
                    if (var12 != null && var12.blockMaterial == var2) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isAABBInMaterial(AxisAlignedBB var1, Material var2) {
        int var3 = MathHelper.floor(var1.minX);
        int var4 = MathHelper.floor(var1.maxX + 1.0D);
        int var5 = MathHelper.floor(var1.minY);
        int var6 = MathHelper.floor(var1.maxY + 1.0D);
        int var7 = MathHelper.floor(var1.minZ);
        int var8 = MathHelper.floor(var1.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var5; var10 < var6; ++var10) {
                for (int var11 = var7; var11 < var8; ++var11) {
                    Block var12 = Block.BLOCKS_LIST[this.getBlockId(var9, var10, var11)];
                    if (var12 != null && var12.blockMaterial == var2) {
                        int var13 = this.getBlockMetadata(var9, var10, var11);
                        double var14 = var10 + 1;
                        if (var13 < 8) {
                            var14 = (double) (var10 + 1) - (double) var13 / 8.0D;
                        }

                        if (var14 >= var1.minY) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Explosion createExplosion(Entity entity, double x, double y, double z, float size) {
        return this.newExplosion(entity, x, y, z, size, false);
    }

    public Explosion newExplosion(Entity exploder, double x, double y, double z, float size, boolean flaming) {
        Explosion explosion = new Explosion(this, exploder, x, y, z, size);
        explosion.isFlaming = flaming;
        explosion.doExplosion();
        explosion.doEffects(true);
        return explosion;
    }

    public float func_494_a(Vec3D vec3D, AxisAlignedBB axis) {
        double var3 = 1.0D / ((axis.maxX - axis.minX) * 2.0D + 1.0D);
        double var5 = 1.0D / ((axis.maxY - axis.minY) * 2.0D + 1.0D);
        double var7 = 1.0D / ((axis.maxZ - axis.minZ) * 2.0D + 1.0D);
        int var9 = 0;
        int var10 = 0;

        for (float var11 = 0.0F; var11 <= 1.0F; var11 = (float) ((double) var11 + var3)) {
            for (float var12 = 0.0F; var12 <= 1.0F; var12 = (float) ((double) var12 + var5)) {
                for (float var13 = 0.0F; var13 <= 1.0F; var13 = (float) ((double) var13 + var7)) {
                    double var14 = axis.minX + (axis.maxX - axis.minX) * (double) var11;
                    double var16 = axis.minY + (axis.maxY - axis.minY) * (double) var12;
                    double var18 = axis.minZ + (axis.maxZ - axis.minZ) * (double) var13;
                    if (this.rayTraceBlocks(Vec3D.createVector(var14, var16, var18), vec3D) == null) {
                        ++var9;
                    }

                    ++var10;
                }
            }
        }

        return (float) var9 / (float) var10;
    }

    public void onBlockHit(EntityPlayer entityPlayer, int x, int y, int z, int var5) {
        if (var5 == 0) {
            --y;
        }

        if (var5 == 1) {
            ++y;
        }

        if (var5 == 2) {
            --z;
        }

        if (var5 == 3) {
            ++z;
        }

        if (var5 == 4) {
            --x;
        }

        if (var5 == 5) {
            ++x;
        }

        if (this.getBlockId(x, y, z) == Block.FIRE.blockID) {
            this.func_28101_a(entityPlayer, 1004, x, y, z, 0);
            this.setBlockWithNotify(x, y, z, 0);
        }

    }

    @Override
    public TileEntity getBlockTileEntity(int x, int y, int z) {
        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk != null ? chunk.getChunkBlockTileEntity(x & 15, y, z & 15) : null;
    }

    public void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
        if (!tileEntity.isInvalid()) {
            if (this.field_31048_L) {
                tileEntity.xCoord = x;
                tileEntity.yCoord = y;
                tileEntity.zCoord = z;
                this.field_20912_E.add(tileEntity);
            } else {
                this.loadedTileEntityList.add(tileEntity);
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                if (chunk != null) {
                    chunk.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
                }
            }
        }

    }

    public void removeBlockTileEntity(int x, int y, int z) {
        TileEntity tileEntity = this.getBlockTileEntity(x, y, z);
        if (tileEntity != null && this.field_31048_L) {
            tileEntity.invalidate();
        } else {
            if (tileEntity != null) {
                this.loadedTileEntityList.remove(tileEntity);
            }

            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            if (chunk != null) {
                chunk.removeChunkBlockTileEntity(x & 15, y, z & 15);
            }
        }

    }

    public boolean isBlockOpaqueCube(int x, int y, int z) {
        Block var4 = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        return var4 != null && var4.isOpaqueCube();
    }

    @Override
    public boolean isBlockNormalCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        if (block == null) {
            return false;
        } else {
            return block.blockMaterial.getIsOpaque() && block.isACube();
        }
    }

    public boolean updatingLighting() {
        if (this.lightingUpdatesCounter >= 50) {
            return false;
        }

        ++this.lightingUpdatesCounter;

        try {
            int var1 = 500;

            while (this.lightingToUpdate.size() > 0) {
                --var1;
                if (var1 <= 0) {
                    return true;
                }

                this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1).func_4107_a(this);
            }

            return false;
        } finally {
            --this.lightingUpdatesCounter;
        }
    }

    public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.scheduleLightingUpdate(skyBlock, var2, var3, var4, var5, var6, var7, true);
    }

    public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
        if (!this.worldProvider.hasNoSky || skyBlock != EnumSkyBlock.SKY) {
            ++lightingUpdatesScheduled;

            try {
                if (lightingUpdatesScheduled != 50) {
                    int var9 = (var5 + var2) / 2;
                    int var10 = (var7 + var4) / 2;
                    if (!this.blockExists(var9, 64, var10)) {
                        return;
                    }

                    if (this.getChunkFromBlockCoords(var9, var10).func_21101_g()) {
                        return;
                    }

                    int var11 = this.lightingToUpdate.size();
                    if (var8) {
                        int var12 = 5;
                        if (var12 > var11) {
                            var12 = var11;
                        }

                        for (int i = 0; i < var12; ++i) {
                            MetadataChunkBlock chunkBlock = this.lightingToUpdate.get(this.lightingToUpdate.size() - i - 1);
                            if (chunkBlock.field_957_a == skyBlock && chunkBlock.func_692_a(var2, var3, var4, var5, var6, var7)) {
                                return;
                            }
                        }
                    }

                    this.lightingToUpdate.add(new MetadataChunkBlock(skyBlock, var2, var3, var4, var5, var6, var7));
                    int var18 = 1000000;
                    if (this.lightingToUpdate.size() > 1000000) {
                        System.out.println("More than " + var18 + " updates, aborting lighting updates");
                        this.lightingToUpdate.clear();
                    }

                }
            } finally {
                --lightingUpdatesScheduled;
            }

        }
    }

    public void calculateInitialSkylight() {
        int skylight = this.calculateSkylightSubtracted(1.0F);
        if (skylight != this.skylightSubtracted) {
            this.skylightSubtracted = skylight;
        }

    }

    public void setAllowedSpawnTypes(boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
        this.spawnHostileMobs = spawnHostileMobs;
        this.spawnPeacefulMobs = spawnPeacefulMobs;
    }

    public void doTick() {
        this.doRandomUpdateTicks();
        this.updateWeather();

        if (this.isAllPlayersFullyAsleep()) {
            boolean spawned = false;
            if (this.spawnHostileMobs && this.difficultySetting >= 1) {
                spawned = SpawnerAnimals.performSleepSpawning(this, this.playerEntities);
            }

            if (!spawned) {
                long newTime = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(newTime - newTime % 24000L);
                this.wakeUpAllPlayers();
            }
        }


        this.chunkProvider.unload100OldestChunks();
        int light = this.calculateSkylightSubtracted(1.0F);
        if (light != this.skylightSubtracted) {
            this.skylightSubtracted = light;

            for (int i = 0; i < this.worldAccesses.size(); ++i) {
                this.worldAccesses.get(i).updateAllRenderers();
            }
        }

        timings.mobSpawn.startTiming();
        SpawnerAnimals.performSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs);
        timings.mobSpawn.stopTiming();

        long newTime = this.worldInfo.getWorldTime() + 1L;
        if (newTime % (long) this.autosavePeriod == 0L) {
            this.saveWorld(false, null);
        }

        this.worldInfo.setWorldTime(newTime);

        timings.scheduledBlocks.startTiming();
        this.TickUpdates(false);
        timings.scheduledBlocks.stopTiming();
    }

    private void func_27070_x() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0F;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0F;
            }
        }

    }

    protected void updateWeather() {
        if (!this.worldProvider.hasNoSky) {
            if (this.field_27075_F > 0) {
                --this.field_27075_F;
            }

            int var1 = this.worldInfo.getThunderTime();
            if (var1 <= 0) {
                if (this.worldInfo.isThundering()) {
                    this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                } else {
                    this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                }
            } else {
                --var1;
                this.worldInfo.setThunderTime(var1);
                if (var1 <= 0) {
                    this.worldInfo.setThundering(!this.worldInfo.isThundering());
                }
            }

            int var2 = this.worldInfo.getRainTime();
            if (var2 <= 0) {
                if (this.worldInfo.isRaining()) {
                    this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                } else {
                    this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                }
            } else {
                --var2;
                this.worldInfo.setRainTime(var2);
                if (var2 <= 0) {
                    this.worldInfo.setRaining(!this.worldInfo.isRaining());
                }
            }

            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
                this.rainingStrength = (float) ((double) this.rainingStrength + 0.01D);
            } else {
                this.rainingStrength = (float) ((double) this.rainingStrength - 0.01D);
            }

            if (this.rainingStrength < 0.0F) {
                this.rainingStrength = 0.0F;
            }

            if (this.rainingStrength > 1.0F) {
                this.rainingStrength = 1.0F;
            }

            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = (float) ((double) this.thunderingStrength + 0.01D);
            } else {
                this.thunderingStrength = (float) ((double) this.thunderingStrength - 0.01D);
            }

            if (this.thunderingStrength < 0.0F) {
                this.thunderingStrength = 0.0F;
            }

            if (this.thunderingStrength > 1.0F) {
                this.thunderingStrength = 1.0F;
            }

        }
    }

    private void clearWeather() {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }

    protected void doRandomUpdateTicks() {
        this.activeChunkSet.clear();

        for (int i = 0; i < this.playerEntities.size(); ++i) {
            EntityPlayer entityPlayer = this.playerEntities.get(i);
            int var3 = MathHelper.floor(entityPlayer.posX / 16.0D);
            int var4 = MathHelper.floor(entityPlayer.posZ / 16.0D);
            byte var5 = 9;

            for (int var6 = -var5; var6 <= var5; ++var6) {
                for (int var7 = -var5; var7 <= var5; ++var7) {
                    this.activeChunkSet.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
                }
            }
        }

        if (this.ambientTickCountdown > 0) {
            --this.ambientTickCountdown;
        }

        for (ChunkCoordIntPair intPair : this.activeChunkSet) {
            int var14 = intPair.chunkXPos * 16;
            int var15 = intPair.chunkZPos * 16;
            Chunk chunk = this.getChunkFromChunkCoords(intPair.chunkXPos, intPair.chunkZPos);
            if (this.ambientTickCountdown == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var17 = this.distHashCounter >> 2;
                int var21 = var17 & 15;
                int var8 = var17 >> 8 & 15;
                int var9 = var17 >> 16 & 127;
                int var10 = chunk.getBlockID(var21, var9, var8);
                var21 = var21 + var14;
                var8 = var8 + var15;
                if (var10 == 0 && this.getBlockLightValueNoChecks(var21, var9, var8) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.SKY, var21, var9, var8) <= 0) {
                    EntityPlayer closestPlayer = this.getClosestPlayer((double) var21 + 0.5D, (double) var9 + 0.5D, (double) var8 + 0.5D, 8.0D);
                    if (closestPlayer != null && closestPlayer.getDistanceSq((double) var21 + 0.5D, (double) var9 + 0.5D, (double) var8 + 0.5D) > 4.0D) {
                        this.playSoundEffect((double) var21 + 0.5D, (double) var9 + 0.5D, (double) var8 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
                        this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
                    }
                }
            }

            if (this.rand.nextInt(100000) == 0 && this.func_27068_v() && this.func_27067_u()) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var18 = this.distHashCounter >> 2;
                int var23 = var14 + (var18 & 15);
                int var27 = var15 + (var18 >> 8 & 15);
                int var30 = this.getTopSolidOrLiquidBlock(var23, var27);
                if (this.canLightningStrikeAt(var23, var30, var27)) {
                    this.addLightningBolt(new EntityLightningBolt(this, var23, var30, var27));
                    this.field_27075_F = 2;
                }
            }

            if (this.rand.nextInt(16) == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var19 = this.distHashCounter >> 2;
                int var24 = var19 & 15;
                int var28 = var19 >> 8 & 15;
                int var31 = this.getTopSolidOrLiquidBlock(var24 + var14, var28 + var15);
                if (this.getWorldChunkManager().getBiomeGenAt(var24 + var14, var28 + var15).getEnableSnow() && var31 >= 0 && var31 < 128 && chunk.getSavedLightValue(EnumSkyBlock.BLOCK, var24, var31, var28) < 10) {
                    int var33 = chunk.getBlockID(var24, var31 - 1, var28);
                    int var35 = chunk.getBlockID(var24, var31, var28);
                    if (this.func_27068_v() && var35 == 0 && Block.SNOW.canPlaceBlockAt(this, var24 + var14, var31, var28 + var15) && var33 != 0 && var33 != Block.ICE.blockID && Block.BLOCKS_LIST[var33].blockMaterial.getIsSolid()) {
                        this.setBlockWithNotify(var24 + var14, var31, var28 + var15, Block.SNOW.blockID);
                    }

                    if (var33 == Block.WATER_STILL.blockID && chunk.getBlockMetadata(var24, var31 - 1, var28) == 0) {
                        this.setBlockWithNotify(var24 + var14, var31 - 1, var28 + var15, Block.ICE.blockID);
                    }
                }
            }

            for (int i = 0; i < 80; ++i) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var25 = this.distHashCounter >> 2;
                int var29 = var25 & 15;
                int var32 = var25 >> 8 & 15;
                int var34 = var25 >> 16 & 127;
                int var36 = chunk.blocks[var29 << 11 | var32 << 7 | var34] & 255;
                if (Block.TICK_ON_LOAD[var36]) {
                    Block.BLOCKS_LIST[var36].updateTick(this, var29 + var14, var34, var32 + var15, this.rand);
                }
            }
        }
    }

    public boolean TickUpdates(boolean var1) {
        int var2;

        var2 = this.scheduledTickTreeSet.size();
        if (var2 != this.scheduledTickSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }

        if (var2 > 1000) {
            var2 = 1000;
        }

        for (int var3 = 0; var3 < var2; ++var3) {
            NextTickListEntry entry = this.scheduledTickTreeSet.first();
            if (!var1 && entry.scheduledTime > this.worldInfo.getWorldTime()) {
                break;
            }

            this.scheduledTickTreeSet.remove(entry);
            this.scheduledTickSet.remove(entry);
            byte var5 = 8;
            if (this.checkChunksExist(
                    entry.xCoord - var5, entry.yCoord - var5, entry.zCoord - var5,
                    entry.xCoord + var5, entry.yCoord + var5, entry.zCoord + var5
            )) {
                int blockId = this.getBlockId(entry.xCoord, entry.yCoord, entry.zCoord);
                if (blockId == entry.blockID && blockId > 0) {
                    Block.BLOCKS_LIST[blockId].updateTick(this, entry.xCoord, entry.yCoord, entry.zCoord, this.rand);
                }
            }
        }

        return this.scheduledTickTreeSet.size() != 0;
    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB axis) {
        this.entities.clear();
        int fromX = MathHelper.floor((axis.minX - 2.0D) / 16.0D);
        int toX = MathHelper.floor((axis.maxX + 2.0D) / 16.0D);
        int fromZ = MathHelper.floor((axis.minZ - 2.0D) / 16.0D);
        int toZ = MathHelper.floor((axis.maxZ + 2.0D) / 16.0D);

        for (int x = fromX; x <= toX; ++x) {
            for (int z = fromZ; z <= toZ; ++z) {
                if (this.chunkExists(x, z)) {
                    this.getChunkFromChunkCoords(x, z).getEntitiesWithinAABBForEntity(entity, axis, this.entities);
                }
            }
        }

        return this.entities;
    }

    public List<Entity> getEntitiesWithinAABB(Class clazz, AxisAlignedBB axis) {
        int var3 = MathHelper.floor((axis.minX - 2.0D) / 16.0D);
        int var4 = MathHelper.floor((axis.maxX + 2.0D) / 16.0D);
        int var5 = MathHelper.floor((axis.minZ - 2.0D) / 16.0D);
        int var6 = MathHelper.floor((axis.maxZ + 2.0D) / 16.0D);
        ArrayList<Entity> entities = new ArrayList<>();

        for (int var8 = var3; var8 <= var4; ++var8) {
            for (int var9 = var5; var9 <= var6; ++var9) {
                if (this.chunkExists(var8, var9)) {
                    this.getChunkFromChunkCoords(var8, var9).getEntitiesOfTypeWithinAAAB(clazz, axis, entities);
                }
            }
        }

        return entities;
    }

    public void updateTileEntityChunkAndDoNothing(int var1, int var2, int var3, TileEntity var4) {
        if (this.blockExists(var1, var2, var3)) {
            this.getChunkFromBlockCoords(var1, var3).setChunkModified();
        }

        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).doNothingWithTileEntity(var1, var2, var3, var4);
        }

    }

    public int countEntities(Class clazz) {
        int count = 0;

        for (int i = 0; i < this.loadedEntityList.size(); ++i) {
            Entity entity = this.loadedEntityList.get(i);
            if (clazz.isAssignableFrom(entity.getClass())) {
                ++count;
            }
        }

        return count;
    }

    public void addLoadedEntities(List<Entity> entities) {
        this.loadedEntityList.addAll(entities);

        for (int i = 0; i < entities.size(); ++i) {
            this.obtainEntitySkin(entities.get(i));
        }

    }

    public void addUnloadedEntities(List<Entity> entities) {
        this.unloadedEntityList.addAll(entities);
    }

    public boolean canBlockBePlacedAt(int var1, int var2, int var3, int var4, boolean var5, int var6) {
        int var7 = this.getBlockId(var2, var3, var4);
        Block var8 = Block.BLOCKS_LIST[var7];
        Block var9 = Block.BLOCKS_LIST[var1];
        AxisAlignedBB var10 = var9.getCollisionBoundingBoxFromPool(this, var2, var3, var4);
        if (var5) {
            var10 = null;
        }

        if (var10 != null && !this.checkIfAABBIsClear(var10)) {
            return false;
        }

        if (var8 == Block.WATER_MOVING || var8 == Block.WATER_STILL || var8 == Block.LAVA_MOVING || var8 == Block.LAVA_STILL || var8 == Block.FIRE || var8 == Block.SNOW) {
            var8 = null;
        }

        return var1 > 0 && var8 == null && var9.canPlaceBlockOnSide(this, var2, var3, var4, var6);
    }

    public PathEntity getPathToEntity(Entity var1, Entity var2, float var3) {
        int var4 = MathHelper.floor(var1.posX);
        int var5 = MathHelper.floor(var1.posY);
        int var6 = MathHelper.floor(var1.posZ);
        int var7 = (int) (var3 + 16.0F);
        int var8 = var4 - var7;
        int var9 = var5 - var7;
        int var10 = var6 - var7;
        int var11 = var4 + var7;
        int var12 = var5 + var7;
        int var13 = var6 + var7;
        ChunkCache var14 = new ChunkCache(this, var8, var9, var10, var11, var12, var13);
        return (new Pathfinder(var14)).createEntityPathTo(var1, var2, var3);
    }

    public PathEntity getEntityPathToXYZ(Entity var1, int var2, int var3, int var4, float var5) {
        int var6 = MathHelper.floor(var1.posX);
        int var7 = MathHelper.floor(var1.posY);
        int var8 = MathHelper.floor(var1.posZ);
        int var9 = (int) (var5 + 8.0F);
        int var10 = var6 - var9;
        int var11 = var7 - var9;
        int var12 = var8 - var9;
        int var13 = var6 + var9;
        int var14 = var7 + var9;
        int var15 = var8 + var9;
        ChunkCache var16 = new ChunkCache(this, var10, var11, var12, var13, var14, var15);
        return (new Pathfinder(var16)).createEntityPathTo(var1, var2, var3, var4, var5);
    }

    public boolean isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
        int var5 = this.getBlockId(var1, var2, var3);
        return var5 != 0 && Block.BLOCKS_LIST[var5].isIndirectlyPoweringTo(this, var1, var2, var3, var4);
    }

    public boolean isBlockGettingPowered(int x, int y, int z) {
        if (this.isBlockProvidingPowerTo(x, y - 1, z, 0))
            return true;

        if (this.isBlockProvidingPowerTo(x, y + 1, z, 1))
            return true;

        if (this.isBlockProvidingPowerTo(x, y, z - 1, 2))
            return true;

        if (this.isBlockProvidingPowerTo(x, y, z + 1, 3))
            return true;

        if (this.isBlockProvidingPowerTo(x - 1, y, z, 4))
            return true;

        return this.isBlockProvidingPowerTo(x + 1, y, z, 5);
    }

    public boolean isBlockIndirectlyProvidingPowerTo(int x, int y, int z, int var4) {
        if (this.isBlockNormalCube(x, y, z))
            return this.isBlockGettingPowered(x, y, z);

        int var5 = this.getBlockId(x, y, z);
        return var5 != 0 && Block.BLOCKS_LIST[var5].isPoweringTo(this, x, y, z, var4);
    }

    public boolean isBlockIndirectlyGettingPowered(int x, int y, int z) {
        if (this.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4)) {
            return true;
        } else {
            return this.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5);
        }
    }

    public EntityPlayer getClosestPlayerToEntity(Entity entity, double var2) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, var2);
    }

    public EntityPlayer getClosestPlayer(double var1, double var3, double var5, double var7) {
        double var9 = -1.0D;
        EntityPlayer var11 = null;

        for (int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
            EntityPlayer var13 = this.playerEntities.get(var12);
            double var14 = var13.getDistanceSq(var1, var3, var5);
            if ((var7 < 0.0D || var14 < var7 * var7) && (var9 == -1.0D || var14 < var9)) {
                var9 = var14;
                var11 = var13;
            }
        }

        return var11;
    }

    public EntityPlayer getPlayerEntityByName(String name) {
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            if (name.equals(this.playerEntities.get(i).username)) {
                return this.playerEntities.get(i);
            }
        }

        return null;
    }

    public byte[] getChunkData(int var1, int var2, int var3, int var4, int var5, int var6) {
        byte[] var7 = new byte[var4 * var5 * var6 * 5 / 2];
        int var8 = var1 >> 4;
        int var9 = var3 >> 4;
        int var10 = var1 + var4 - 1 >> 4;
        int var11 = var3 + var6 - 1 >> 4;
        int var12 = 0;
        int var13 = var2;
        int var14 = var2 + var5;
        if (var2 < 0) {
            var13 = 0;
        }

        if (var14 > 128) {
            var14 = 128;
        }

        for (int var15 = var8; var15 <= var10; ++var15) {
            int var16 = var1 - var15 * 16;
            int var17 = var1 + var4 - var15 * 16;
            if (var16 < 0) {
                var16 = 0;
            }

            if (var17 > 16) {
                var17 = 16;
            }

            for (int var18 = var9; var18 <= var11; ++var18) {
                int var19 = var3 - var18 * 16;
                int var20 = var3 + var6 - var18 * 16;
                if (var19 < 0) {
                    var19 = 0;
                }

                if (var20 > 16) {
                    var20 = 16;
                }

                var12 = this.getChunkFromChunkCoords(var15, var18).getChunkData(var7, var16, var13, var19, var17, var14, var20, var12);
            }
        }

        return var7;
    }

    public void checkSessionLock() {
        this.worldFile.func_22091_b();
    }

    public void func_32005_b(long var1) {
        long var3 = var1 - this.worldInfo.getWorldTime();

        for (NextTickListEntry var6 : this.scheduledTickSet) {
            var6.scheduledTime += var3;
        }

        this.setWorldTime(var1);
    }

    public long getRandomSeed() {
        return this.worldInfo.getRandomSeed();
    }

    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }

    public void setWorldTime(long time) {
        this.worldInfo.setWorldTime(time);
    }

    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
    }

    public boolean canMineBlock(EntityPlayer var1, int var2, int var3, int var4) {
        return true;
    }

    public void sendTrackedEntityStatusUpdatePacket(Entity var1, byte var2) {
    }

    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }

    public void playNoteAt(int var1, int var2, int var3, int var4, int var5) {
        int var6 = this.getBlockId(var1, var2, var3);
        if (var6 > 0) {
            Block.BLOCKS_LIST[var6].playBlock(this, var1, var2, var3, var4, var5);
        }

    }

    public ISaveHandler getWorldFile() {
        return this.worldFile;
    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = !this.playerEntities.isEmpty();

        for (EntityPlayer var2 : this.playerEntities) {
            if (!var2.isSleeping()) {
                this.allPlayersSleeping = false;
                break;
            }
        }

    }

    protected void wakeUpAllPlayers() {
        this.allPlayersSleeping = false;

        for (EntityPlayer entityPlayer : this.playerEntities) {
            if (entityPlayer.isSleeping()) {
                entityPlayer.wakeUpPlayer(false, false, true);
            }
        }

        this.clearWeather();
    }

    public boolean isAllPlayersFullyAsleep() {
        if (this.allPlayersSleeping && !this.singleplayerWorld) {
            for (EntityPlayer entityPlayer : this.playerEntities) {
                if (!entityPlayer.isPlayerFullyAsleep()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public float func_27065_c(float var1) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * var1) * this.func_27074_d(var1);
    }

    public float func_27074_d(float var1) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * var1;
    }

    public boolean func_27067_u() {
        return (double) this.func_27065_c(1.0F) > 0.9D;
    }

    public boolean func_27068_v() {
        return (double) this.func_27074_d(1.0F) > 0.2D;
    }

    public boolean canLightningStrikeAt(int var1, int var2, int var3) {
        if (!this.func_27068_v()) {
            return false;
        } else if (!this.canBlockSeeTheSky(var1, var2, var3)) {
            return false;
        } else if (this.getTopSolidOrLiquidBlock(var1, var3) > var2) {
            return false;
        } else {
            BiomeGenBase biomeGenAt = this.getWorldChunkManager().getBiomeGenAt(var1, var3);
            return !biomeGenAt.getEnableSnow() && biomeGenAt.canSpawnLightningBolt();
        }
    }

    public void setItemData(String var1, MapDataBase var2) {
        this.mapStorage.func_28177_a(var1, var2);
    }

    public MapDataBase loadItemData(Class var1, String var2) {
        return this.mapStorage.func_28178_a(var1, var2);
    }

    public int getUniqueDataId(String var1) {
        return this.mapStorage.func_28173_a(var1);
    }

    public void func_28097_e(int var1, int var2, int var3, int var4, int var5) {
        this.func_28101_a(null, var1, var2, var3, var4, var5);
    }

    public void func_28101_a(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
        for (int i = 0; i < this.worldAccesses.size(); ++i) {
            this.worldAccesses.get(i).func_28133_a(var1, var2, var3, var4, var5, var6);
        }

    }
}
