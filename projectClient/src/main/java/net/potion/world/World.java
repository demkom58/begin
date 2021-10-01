package net.potion.world;

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
import net.potion.nbt.TagCompound;
import net.potion.tileentity.TileEntity;
import net.potion.util.*;
import net.potion.world.chunk.*;
import net.potion.world.gen.BiomeGenBase;
import net.potion.world.storage.ISaveHandler;
import net.potion.world.storage.MapStorage;
import org.joml.Vector3d;

import java.util.*;

public class World implements IBlockAccess {
    static int lightingUpdatesScheduled = 0;
    public final WorldProvider worldProvider;
    protected final int DIST_HASH_MAGIC;
    protected final ISaveHandler saveHandler;
    public boolean scheduledUpdatesAreImmediate;
    public List<Entity> loadedEntityList;
    public List<TileEntity> loadedTileEntityList;
    public List<EntityPlayer> playerEntities;
    public List<Entity> weatherEffects;
    public int skylightSubtracted;
    public int field_27172_i;
    public boolean editingBlocks;
    public int difficultySetting;
    public Random rand;
    public boolean isNewWorld;
    public boolean findingSpawnPoint;
    public MapStorage mapStorage;
    public boolean multiplayerWorld;
    protected int distHashCounter;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    protected int field_27168_F;
    protected int autosavePeriod;
    protected List<IWorldAccess> worldAccesses;
    protected IChunkProvider chunkProvider;
    protected WorldInfo worldInfo;
    private List<MetadataChunkBlock> lightingToUpdate;
    private List<Entity> unloadedEntityList;
    private TreeSet<NextTickListEntry> scheduledTickTreeSet;
    private Set<NextTickListEntry> scheduledTickSet;
    private List<TileEntity> field_30900_E;
    private long cloudColor;
    private long lockTimestamp;
    private boolean allPlayersSleeping;
    private ArrayList<AxisAlignedBB> collidingBoundingBoxes;
    private boolean field_31055_L;
    private int lightingUpdatesCounter;
    private boolean spawnHostileMobs;
    private boolean spawnPeacefulMobs;
    private Set<ChunkCoordIntPair> activeChunkSet;
    private int soundCounter;
    private List<Entity> entities;

    public World(ISaveHandler saveHandler, String levelName, WorldProvider var3, long var4) {
        this.scheduledUpdatesAreImmediate = false;
        this.lightingToUpdate = new ArrayList<>();
        this.loadedEntityList = new ArrayList<>();
        this.unloadedEntityList = new ArrayList<>();
        this.scheduledTickTreeSet = new TreeSet<>();
        this.scheduledTickSet = new HashSet<>();
        this.loadedTileEntityList = new ArrayList<>();
        this.field_30900_E = new ArrayList<>();
        this.playerEntities = new ArrayList<>();
        this.weatherEffects = new ArrayList<>();
        this.cloudColor = 0xFFFFFF;
        this.skylightSubtracted = 0;
        this.distHashCounter = new Random().nextInt();
        this.DIST_HASH_MAGIC = 1013904223;
        this.field_27168_F = 0;
        this.field_27172_i = 0;
        this.editingBlocks = false;
        this.lockTimestamp = System.currentTimeMillis();
        this.autosavePeriod = 40;
        this.rand = new Random();
        this.isNewWorld = false;
        this.worldAccesses = new ArrayList<>();
        this.collidingBoundingBoxes = new ArrayList<>();
        this.lightingUpdatesCounter = 0;
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.activeChunkSet = new HashSet<>();
        this.soundCounter = this.rand.nextInt(12000);
        this.entities = new ArrayList<>();
        this.multiplayerWorld = false;
        this.saveHandler = saveHandler;
        this.worldInfo = new WorldInfo(var4, levelName);
        this.worldProvider = var3;
        this.mapStorage = new MapStorage(saveHandler);
        var3.registerWorld(this);
        this.chunkProvider = this.getChunkProvider();
        this.calculateInitialSkylight();
        this.func_27163_E();
    }

    public World(World var1, WorldProvider var2) {
        this.scheduledUpdatesAreImmediate = false;
        this.lightingToUpdate = new ArrayList<>();
        this.loadedEntityList = new ArrayList<>();
        this.unloadedEntityList = new ArrayList<>();
        this.scheduledTickTreeSet = new TreeSet<>();
        this.scheduledTickSet = new HashSet<>();
        this.loadedTileEntityList = new ArrayList<>();
        this.field_30900_E = new ArrayList<>();
        this.playerEntities = new ArrayList<>();
        this.weatherEffects = new ArrayList<>();
        this.cloudColor = 0xFFFFFF;
        this.skylightSubtracted = 0;
        this.distHashCounter = (new Random()).nextInt();
        this.DIST_HASH_MAGIC = 1013904223;
        this.field_27168_F = 0;
        this.field_27172_i = 0;
        this.editingBlocks = false;
        this.lockTimestamp = System.currentTimeMillis();
        this.autosavePeriod = 40;
        this.rand = new Random();
        this.isNewWorld = false;
        this.worldAccesses = new ArrayList<>();
        this.collidingBoundingBoxes = new ArrayList<>();
        this.lightingUpdatesCounter = 0;
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.activeChunkSet = new HashSet<>();
        this.soundCounter = this.rand.nextInt(12000);
        this.entities = new ArrayList<>();
        this.multiplayerWorld = false;
        this.lockTimestamp = var1.lockTimestamp;
        this.saveHandler = var1.saveHandler;
        this.worldInfo = new WorldInfo(var1.worldInfo);
        this.mapStorage = new MapStorage(this.saveHandler);
        this.worldProvider = var2;
        var2.registerWorld(this);
        this.chunkProvider = this.getChunkProvider();
        this.calculateInitialSkylight();
        this.func_27163_E();
    }

    public World(ISaveHandler saveHandler, String levelName, long randomSeed) {
        this(saveHandler, levelName, randomSeed, null);
    }

    public World(ISaveHandler saveHandler, String levelName, long randomSeed, WorldProvider worldProvider) {
        this.scheduledUpdatesAreImmediate = false;
        this.lightingToUpdate = new ArrayList<>();
        this.loadedEntityList = new ArrayList<>();
        this.unloadedEntityList = new ArrayList<>();
        this.scheduledTickTreeSet = new TreeSet<>();
        this.scheduledTickSet = new HashSet<>();
        this.loadedTileEntityList = new ArrayList<>();
        this.field_30900_E = new ArrayList<>();
        this.playerEntities = new ArrayList<>();
        this.weatherEffects = new ArrayList<>();
        this.cloudColor = 0xFFFFFF;
        this.skylightSubtracted = 0;
        this.distHashCounter = new Random().nextInt();
        this.DIST_HASH_MAGIC = 1013904223;
        this.field_27168_F = 0;
        this.field_27172_i = 0;
        this.editingBlocks = false;
        this.lockTimestamp = System.currentTimeMillis();
        this.autosavePeriod = 40;
        this.rand = new Random();
        this.isNewWorld = false;
        this.worldAccesses = new ArrayList<>();
        this.collidingBoundingBoxes = new ArrayList<>();
        this.lightingUpdatesCounter = 0;
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.activeChunkSet = new HashSet<>();
        this.soundCounter = this.rand.nextInt(12000);
        this.entities = new ArrayList<>();
        this.multiplayerWorld = false;
        this.saveHandler = saveHandler;
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
            this.worldInfo.setWorldName(levelName);
        }

        this.worldProvider.registerWorld(this);
        this.chunkProvider = this.getChunkProvider();
        if (isNew) {
            this.generateSpawnPoint();
        }

        this.calculateInitialSkylight();
        this.func_27163_E();
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.worldChunkMgr;
    }

    protected IChunkProvider getChunkProvider() {
        IChunkLoader chunkLoader = this.saveHandler.getChunkLoader(this.worldProvider);
        return new ChunkProvider(this, chunkLoader, this.worldProvider.getChunkProvider());
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

    public void setSpawnLocation() {
        if (this.worldInfo.getSpawnY() <= 0) {
            this.worldInfo.setSpawnY(64);
        }

        int var1 = this.worldInfo.getSpawnX();

        int var2;
        for (var2 = this.worldInfo.getSpawnZ(); this.getFirstUncoveredBlock(var1, var2) == 0; var2 += this.rand.nextInt(8) - this.rand.nextInt(8)) {
            var1 += this.rand.nextInt(8) - this.rand.nextInt(8);
        }

        this.worldInfo.setSpawnX(var1);
        this.worldInfo.setSpawnZ(var2);
    }

    public int getFirstUncoveredBlock(int var1, int var2) {
        int var3;
        for (var3 = 63; !this.isAirBlock(var1, var3 + 1, var2); ++var3) {
        }

        return this.getBlockId(var1, var3, var2);
    }

    public void emptyMethod1() {
    }

    public void spawnPlayerWithLoadedChunks(EntityPlayer entityPlayer) {
        try {
            TagCompound playerCompound = this.worldInfo.getPlayerNBTTagCompound();
            if (playerCompound != null) {
                entityPlayer.readFromNBT(playerCompound);
                this.worldInfo.setPlayerNBTTagCompound(null);
            }

            if (this.chunkProvider instanceof ChunkProviderLoadOrGenerate) {
                ChunkProviderLoadOrGenerate chunkProvider = (ChunkProviderLoadOrGenerate) this.chunkProvider;
                int var4 = MathHelper.floor((float) ((int) entityPlayer.posX)) >> 4;
                int var5 = MathHelper.floor((float) ((int) entityPlayer.posZ)) >> 4;
                chunkProvider.setCurrentChunkOver(var4, var5);
            }

            this.entityJoinedWorld(entityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveWorld(boolean var1, IProgressUpdatable progressUpdate) {
        if (this.chunkProvider.canSave()) {
            if (progressUpdate != null) {
                progressUpdate.display("Saving level");
            }

            this.saveLevel();
            if (progressUpdate != null) {
                progressUpdate.displayLoadingString("Saving chunks");
            }

            this.chunkProvider.saveChunks(var1, progressUpdate);
        }
    }

    private void saveLevel() {
        this.checkSessionLock();
        this.saveHandler.saveWorldInfoAndPlayer(this.worldInfo, this.playerEntities);
        this.mapStorage.saveAllData();
    }

    public boolean func_650_a(int var1) {
        if (!this.chunkProvider.canSave()) {
            return true;
        }

        if (var1 == 0) {
            this.saveLevel();
        }

        return this.chunkProvider.saveChunks(false, null);
    }

    @Override
    public int getBlockId(int var1, int var2, int var3) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0)
                return 0;

            return var2 >= 128 ? 0 : this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).getBlockID(var1 & 15, var2, var3 & 15);
        }

        return 0;
    }

    public boolean isAirBlock(int var1, int var2, int var3) {
        return this.getBlockId(var1, var2, var3) == 0;
    }

    public boolean blockExists(int var1, int var2, int var3) {
        return (var2 >= 0 && var2 < 128) && this.chunkExists(var1 >> 4, var3 >> 4);
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

    private boolean chunkExists(int var1, int var2) {
        return this.chunkProvider.chunkExists(var1, var2);
    }

    public Chunk getChunkFromBlockCoords(int var1, int var2) {
        return this.getChunkFromChunkCoords(var1 >> 4, var2 >> 4);
    }

    public Chunk getChunkFromChunkCoords(int var1, int var2) {
        return this.chunkProvider.provideChunk(var1, var2);
    }

    public boolean setBlockAndMetadata(int var1, int var2, int var3, int var4, int var5) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0) {
                return false;
            } else if (var2 >= 128) {
                return false;
            } else {
                Chunk var6 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                return var6.setBlockIDWithMetadata(var1 & 15, var2, var3 & 15, var4, var5);
            }
        }

        return false;
    }

    public boolean setBlock(int var1, int var2, int var3, int var4) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0)
                return false;

            if (var2 >= 128)
                return false;

            Chunk chunk = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
            return chunk.setBlockID(var1 & 15, var2, var3 & 15, var4);
        }

        return false;
    }

    @Override
    public Material getBlockMaterial(int var1, int var2, int var3) {
        int var4 = this.getBlockId(var1, var2, var3);
        return var4 == 0 ? Material.AIR : Block.BLOCKS_LIST[var4].blockMaterial;
    }

    @Override
    public int getBlockMetadata(int var1, int var2, int var3) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0)
                return 0;

            if (var2 >= 128)
                return 0;

            Chunk chunk = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
            var1 = var1 & 15;
            var3 = var3 & 15;
            return chunk.getBlockMetadata(var1, var2, var3);
        }

        return 0;
    }

    public void setBlockMetadataWithNotify(int var1, int var2, int var3, int var4) {
        if (this.setBlockMetadata(var1, var2, var3, var4)) {
            int var5 = this.getBlockId(var1, var2, var3);
            if (Block.REQUIRES_SELF_NOTIFY[var5 & 255]) {
                this.notifyBlockChange(var1, var2, var3, var5);
            } else {
                this.notifyBlocksOfNeighborChange(var1, var2, var3, var5);
            }
        }

    }

    public boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0)
                return false;

            if (var2 >= 128)
                return false;

            Chunk chunk = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
            var1 = var1 & 15;
            var3 = var3 & 15;
            chunk.setBlockMetadata(var1, var2, var3, var4);
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

    public void markBlockNeedsUpdate(int var1, int var2, int var3) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockAndNeighborsNeedsUpdate(var1, var2, var3);
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
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockRangeNeedsUpdate(var1, var2, var3, var1, var2, var3);
        }
    }

    public void markBlocksDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockRangeNeedsUpdate(var1, var2, var3, var4, var5, var6);
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
        if (!this.editingBlocks && !this.multiplayerWorld) {
            Block var5 = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
            if (var5 != null) {
                var5.onNeighborBlockChange(this, var1, var2, var3, var4);
            }

        }
    }

    public boolean canBlockSeeTheSky(int x, int y, int z) {
        return this.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15);
    }

    public int getFullBlockLightValue(int x, int y, int z) {
        if (y < 0)
            return 0;

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
                    if (var7 > var6) {
                        var6 = var7;
                    }

                    if (var8 > var6) {
                        var6 = var8;
                    }

                    if (var9 > var6) {
                        var6 = var9;
                    }

                    if (var10 > var6) {
                        var6 = var10;
                    }

                    return var6;
                }
            }

            if (y < 0)
                return 0;

            if (y >= 128) {
                y = 127;
            }

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

    public void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int var5) {
        if (!this.worldProvider.hasNoSky || skyBlock != EnumSkyBlock.SKY) {
            if (this.blockExists(x, y, z)) {
                if (skyBlock == EnumSkyBlock.SKY) {
                    if (this.canExistingBlockSeeTheSky(x, y, z)) {
                        var5 = 15;
                    }
                } else if (skyBlock == EnumSkyBlock.BLOCK) {
                    int var6 = this.getBlockId(x, y, z);
                    if (Block.LIGHT_VALUE[var6] > var5) {
                        var5 = Block.LIGHT_VALUE[var6];
                    }
                }

                if (this.getSavedLightValue(skyBlock, x, y, z) != var5) {
                    this.scheduleLightingUpdate(skyBlock, x, y, z, x, y, z);
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
            if (!this.chunkExists(var5, var6))
                return 0;

            Chunk chunk = this.getChunkFromChunkCoords(var5, var6);
            return chunk.getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
        }

        return var1.lightValue;
    }

    public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int var5) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y >= 0) {
                if (y < 128) {
                    if (this.chunkExists(x >> 4, z >> 4)) {
                        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                        chunk.setLightValue(skyBlock, x & 15, y, z & 15, var5);

                        for (IWorldAccess worldAccess : this.worldAccesses) {
                            worldAccess.markBlockAndNeighborsNeedsUpdate(x, y, z);
                        }

                    }
                }
            }
        }
    }

    @Override
    public float getBrightness(int x, int y, int z, int var4) {
        int var5 = this.getBlockLightValue(x, y, z);
        if (var5 < var4) {
            var5 = var4;
        }

        return this.worldProvider.lightBrightnessTable[var5];
    }

    @Override
    public float getLightBrightness(int var1, int var2, int var3) {
        return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(var1, var2, var3)];
    }

    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }

    public MovingObjectPosition rayTraceBlocks(Vector3d var1, Vector3d var2) {
        return this.rayTraceBlocks(var1, var2, false, false);
    }

    public MovingObjectPosition rayTraceBlocks(Vector3d var1, Vector3d var2, boolean var3) {
        return this.rayTraceBlocks(var1, var2, var3, false);
    }

    public MovingObjectPosition rayTraceBlocks(Vector3d vec1, Vector3d vec2, boolean paramBoolean, boolean paramBoolean2) {
        if (Double.isNaN(vec1.x) || Double.isNaN(vec1.y) || Double.isNaN(vec1.z))
            return null;

        if (Double.isNaN(vec2.x) || Double.isNaN(vec2.y) || Double.isNaN(vec2.z))
            return null;

        int x2 = MathHelper.floor(vec2.x);
        int y2 = MathHelper.floor(vec2.y);
        int z2 = MathHelper.floor(vec2.z);

        int x1 = MathHelper.floor(vec1.x);
        int y1 = MathHelper.floor(vec1.y);
        int z1 = MathHelper.floor(vec1.z);

        int blockId = this.getBlockId(x1, y1, z1);
        int blockMetadata = this.getBlockMetadata(x1, y1, z1);

        Block localBlock1 = Block.BLOCKS_LIST[blockId];
        if ((!paramBoolean2
                || localBlock1 == null
                || localBlock1.getCollisionBoundingBoxFromPool(this, x1, y1, z1) != null)
                && blockId > 0
                && localBlock1.canCollideCheck(blockMetadata, paramBoolean)) {
            MovingObjectPosition pos = localBlock1.collisionRayTrace(this, x1, y1, z1, vec1, vec2);
            if (pos != null)
                return pos;
        }

        blockId = 200;

        while (blockId-- >= 0) {
            if (Double.isNaN(vec1.x) || Double.isNaN(vec1.y) || Double.isNaN(vec1.z))
                return null;

            if (x1 == x2 && y1 == y2 && z1 == z2)
                return null;

            blockMetadata = 1;
            int i4 = 1;
            int i5 = 1;

            double d1 = 999.0D;
            double d2 = 999.0D;
            double d3 = 999.0D;

            if (x2 > x1) {
                d1 = x1 + 1.0D;
            } else if (x2 < x1) {
                d1 = x1 + 0.0D;
            } else blockMetadata = 0;


            if (y2 > y1) {
                d2 = y1 + 1.0D;
            } else if (y2 < y1) {
                d2 = y1 + 0.0D;
            } else i4 = 0;

            if (z2 > z1) {
                d3 = z1 + 1.0D;
            } else if (z2 < z1) {
                d3 = z1 + 0.0D;
            } else i5 = 0;

            double var21 = 999.0D;
            double var23 = 999.0D;
            double var25 = 999.0D;
            double difX = vec2.x - vec1.x;
            double difY = vec2.y - vec1.y;
            double difZ = vec2.z - vec1.z;

            if (blockMetadata != 0)
                var21 = (d1 - vec1.x) / difX;

            if (i4 != 0)
                var23 = (d2 - vec1.y) / difY;

            if (i5 != 0)
                var25 = (d3 - vec1.z) / difZ;


            byte var33 = 0;
            if (var21 < var23 && var21 < var25) {
                if (x2 > x1) {
                    var33 = 4;
                } else var33 = 5;


                vec1.x = d1;
                vec1.y += difY * var21;
                vec1.z += difZ * var21;
            } else if (var23 < var25) {
                if (y2 > y1) {
                    var33 = 0;
                } else var33 = 1;


                vec1.x += difX * var23;
                vec1.y = d2;
                vec1.z += difZ * var23;
            } else {
                if (z2 > z1) {
                    var33 = 2;
                } else var33 = 3;


                vec1.x += difX * var25;
                vec1.y += difY * var25;
                vec1.z = d3;
            }

            Vector3d var34 = new Vector3d(vec1.x, vec1.y, vec1.z);
            x1 = (int) (var34.x = MathHelper.floor(vec1.x));
            if (var33 == 5) {
                --x1;
                ++var34.x;
            }

            y1 = (int) (var34.y = MathHelper.floor(vec1.y));
            if (var33 == 1) {
                --y1;
                ++var34.y;
            }

            z1 = (int) (var34.z = MathHelper.floor(vec1.z));
            if (var33 == 3) {
                --z1;
                ++var34.z;
            }

            int blockId1 = this.getBlockId(x1, y1, z1);
            int blockMetadata1 = this.getBlockMetadata(x1, y1, z1);
            Block block = Block.BLOCKS_LIST[blockId1];
            if ((!paramBoolean2
                    || block == null
                    || block.getCollisionBoundingBoxFromPool(this, x1, y1, z1) != null)
                    && blockId1 > 0
                    && block.canCollideCheck(blockMetadata1, paramBoolean)) {
                MovingObjectPosition pos = block.collisionRayTrace(this, x1, y1, z1, vec1, vec2);
                if (pos != null)
                    return pos;
            }
        }

        return null;

    }

    public void playSoundAtEntity(Entity entity, String soundName, float volume, float pitch) {
        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.playSound(soundName, entity.posX, entity.posY - (double) entity.yOffset, entity.posZ, volume, pitch);
    }

    public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.playSound(soundName, x, y, z, volume, pitch);
    }

    public void playRecord(String var1, int var2, int var3, int var4) {
        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.playRecord(var1, var2, var3, var4);
    }

    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.spawnParticle(var1, var2, var4, var6, var8, var10, var12);
    }

    public boolean addWeatherEffect(Entity entity) {
        this.weatherEffects.add(entity);
        return true;
    }

    public boolean entityJoinedWorld(Entity entity) {
        int chunkX = MathHelper.floor(entity.posX / 16.0D);
        int chunkZ = MathHelper.floor(entity.posZ / 16.0D);
        boolean isPlayer = false;

        if (entity instanceof EntityPlayer)
            isPlayer = true;

        if (!isPlayer && !this.chunkExists(chunkX, chunkZ))
            return false;

        if (entity instanceof EntityPlayer) {
            this.playerEntities.add((EntityPlayer) entity);
            this.updateAllPlayersSleepingFlag();
        }

        this.getChunkFromChunkCoords(chunkX, chunkZ).addEntity(entity);
        this.loadedEntityList.add(entity);
        this.obtainEntitySkin(entity);
        return true;
    }

    protected void obtainEntitySkin(Entity entity) {
        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.obtainEntitySkin(entity);
    }

    protected void releaseEntitySkin(Entity entity) {
        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.releaseEntitySkin(entity);
    }

    public void setEntityDead(Entity entity) {
        if (entity.riddenByEntity != null)
            entity.riddenByEntity.mountEntity(null);

        if (entity.ridingEntity != null)
            entity.mountEntity(null);

        entity.setEntityDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            this.updateAllPlayersSleepingFlag();
        }

    }

    public void addWorldAccess(IWorldAccess worldAccess) {
        this.worldAccesses.add(worldAccess);
    }

    public void removeWorldAccess(IWorldAccess worldAccess) {
        this.worldAccesses.remove(worldAccess);
    }

    public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entity, AxisAlignedBB axis) {
        this.collidingBoundingBoxes.clear();
        int minX = MathHelper.floor(axis.minX);
        int maxX = MathHelper.floor(axis.maxX + 1.0D);
        int minY = MathHelper.floor(axis.minY);
        int maxY = MathHelper.floor(axis.maxY + 1.0D);
        int minZ = MathHelper.floor(axis.minZ);
        int maxZ = MathHelper.floor(axis.maxZ + 1.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                if (this.blockExists(x, 64, z)) {
                    for (int y = minY - 1; y < maxY; ++y) {
                        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                        if (block != null)
                            block.getCollidingBoundingBoxes(this, x, y, z, axis, this.collidingBoundingBoxes);
                    }
                }
            }
        }

        double var14 = 0.25D;
        List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(entity, axis.expand(var14, var14, var14));

        for (int i = 0; i < entities.size(); ++i) {
            AxisAlignedBB listBB = entities.get(i).getBoundingBox();
            if (listBB != null && listBB.intersectsWith(axis))
                this.collidingBoundingBoxes.add(listBB);

            listBB = entity.getCollisionBox(entities.get(i));
            if (listBB != null && listBB.intersectsWith(axis))
                this.collidingBoundingBoxes.add(listBB);
        }

        return this.collidingBoundingBoxes;
    }

    public int calculateSkylightSubtracted(float var1) {
        float var2 = this.getCelestialAngle(var1);
        float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (var3 < 0.0F)
            var3 = 0.0F;

        if (var3 > 1.0F)
            var3 = 1.0F;

        var3 = 1.0F - var3;
        var3 = (float) ((double) var3 * (1.0D - (double) (this.getRainStrength(var1) * 5.0F) / 16.0D));
        var3 = (float) ((double) var3 * (1.0D - (double) (this.func_27166_f(var1) * 5.0F) / 16.0D));
        var3 = 1.0F - var3;

        return (int) (var3 * 11.0F);
    }

    public Vector3d func_4079_a(Entity entity, float angle) {
        float celAngle = this.getCelestialAngle(angle);
        float celCos = MathHelper.cos(celAngle * Math.PI * 2.0F) * 2.0F + 0.5F;

        if (celCos < 0.0F)
            celCos = 0.0F;

        if (celCos > 1.0F)
            celCos = 1.0F;


        int floorX = MathHelper.floor(entity.posX);
        int floorZ = MathHelper.floor(entity.posZ);

        float temperature = (float) this.getWorldChunkManager().getTemperature(floorX, floorZ);
        int skyColor = this.getWorldChunkManager().getBiomeGenAt(floorX, floorZ).getSkyColorByTemp(temperature);

        float r = (float) (skyColor >> 16 & 255) / 255.0F;
        float g = (float) (skyColor >> 8 & 255) / 255.0F;
        float b = (float) (skyColor & 255) / 255.0F;

        r *= celCos;
        g *= celCos;
        b *= celCos;

        float rainStrength = this.getRainStrength(angle);
        if (rainStrength > 0.0F) {
            float var13 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.6F;
            float var14 = 1.0F - rainStrength * 0.75F;
            r *= var14 + var13 * (1.0F - var14);
            g *= var14 + var13 * (1.0F - var14);
            b *= var14 + var13 * (1.0F - var14);
        }

        float var19 = this.func_27166_f(angle);
        if (var19 > 0.0F) {
            float var20 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.2F;
            float var15 = 1.0F - var19 * 0.75F;
            r = r * var15 + var20 * (1.0F - var15);
            g = g * var15 + var20 * (1.0F - var15);
            b = b * var15 + var20 * (1.0F - var15);
        }

        if (this.field_27172_i > 0) {
            float var21 = Math.min((float) this.field_27172_i - angle, 1.0F) * 0.45F;
            r *= (1.0F - var21) + 0.8F * var21;
            g *= (1.0F - var21) + 0.8F * var21;
            b *= (1.0F - var21) + 1.0F * var21;
        }

        return new Vector3d(r, g, b);
    }

    public float getCelestialAngle(float var1) {
        return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), var1);
    }

    public Vector3d cloudColor(float partialTicks) {
        float celAngle = this.getCelestialAngle(partialTicks);
        float celCos = MathHelper.cos(celAngle * Math.PI * 2.0F) * 2.0F + 0.5F;

        if (celCos < 0.0F)
            celCos = 0.0F;

        if (celCos > 1.0F)
            celCos = 1.0F;

        float r = (float) (this.cloudColor >> 16 & 255L) / 255.0F;
        float g = (float) (this.cloudColor >> 8 & 255L) / 255.0F;
        float b = (float) (this.cloudColor & 255L) / 255.0F;

        float rainStrength = this.getRainStrength(partialTicks);
        if (rainStrength > 0.0F) {
            float var8 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.6F;
            float var9 = 1.0F - rainStrength * 0.95F;
            r = r * var9 + var8 * (1.0F - var9);
            g = g * var9 + var8 * (1.0F - var9);
            b = b * var9 + var8 * (1.0F - var9);
        }

        r *= celCos * 0.9F + 0.1F;
        g *= celCos * 0.9F + 0.1F;
        b *= celCos * 0.85F + 0.15F;

        float var14 = this.func_27166_f(partialTicks);
        if (var14 > 0.0F) {
            float var15 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.2F;
            float var10 = 1.0F - var14 * 0.95F;
            r *= var10 + var15 * (1.0F - var10);
            g *= var10 + var15 * (1.0F - var10);
            b *= var10 + var15 * (1.0F - var10);
        }

        return new Vector3d(r, g, b);
    }

    public Vector3d getFogColor(float var1) {
        float celestialAngle = this.getCelestialAngle(var1);
        return this.worldProvider.func_4096_a(celestialAngle, var1);
    }

    public int findTopSolidBlock(int x, int z) {
        Chunk chunk = this.getChunkFromBlockCoords(x, z);
        int y = 127;
        x &= 15;

        for (int iZ = z & 15; y > 0; --y) {
            int blockID = chunk.getBlockID(x, y, iZ);
            Material material = blockID == 0 ? Material.AIR : Block.BLOCKS_LIST[blockID].blockMaterial;
            if (material.getIsSolid() || material.isLiquid())
                return y + 1;
        }

        return -1;
    }

    public float getStarBrightness(float var1) {
        float celAngle = this.getCelestialAngle(var1);
        float celCos = 1.0F - (MathHelper.cos(celAngle * 3.1415927F * 2.0F) * 2.0F + 0.75F);

        if (celCos < 0.0F)
            celCos = 0.0F;

        if (celCos > 1.0F)
            celCos = 1.0F;

        return celCos * celCos * 0.5F;
    }

    public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
        NextTickListEntry entry = new NextTickListEntry(var1, var2, var3, var4);
        byte var7 = 8;

        if (this.scheduledUpdatesAreImmediate) {
            if (this.checkChunksExist(entry.xCoord - var7, entry.yCoord - var7, entry.zCoord - var7, entry.xCoord + var7, entry.yCoord + var7, entry.zCoord + var7)) {
                int var8 = this.getBlockId(entry.xCoord, entry.yCoord, entry.zCoord);
                if (var8 == entry.blockID && var8 > 0)
                    Block.BLOCKS_LIST[var8].updateTick(this, entry.xCoord, entry.yCoord, entry.zCoord, this.rand);
            }
        } else {
            if (this.checkChunksExist(var1 - var7, var2 - var7, var3 - var7, var1 + var7, var2 + var7, var3 + var7)) {
                if (var4 > 0)
                    entry.setScheduledTime((long) var5 + this.worldInfo.getWorldTime());

                if (!this.scheduledTickSet.contains(entry)) {
                    this.scheduledTickSet.add(entry);
                    this.scheduledTickTreeSet.add(entry);
                }
            }

        }
    }

    public void updateEntities() {
        for (int i = 0; i < this.weatherEffects.size(); ++i) {
            Entity entity = this.weatherEffects.get(i);
            entity.onUpdate();
            if (entity.isDead)
                this.weatherEffects.remove(i--);
        }

        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            Entity entity = this.unloadedEntityList.get(i);
            int chunkX = entity.chunkCoordX;
            int chunkZ = entity.chunkCoordZ;
            if (entity.addedToChunk && this.chunkExists(chunkX, chunkZ))
                this.getChunkFromChunkCoords(chunkX, chunkZ).removeEntity(entity);
        }

        for (int i = 0; i < this.unloadedEntityList.size(); ++i)
            this.releaseEntitySkin(this.unloadedEntityList.get(i));

        this.unloadedEntityList.clear();

        for (int i = 0; i < this.loadedEntityList.size(); ++i) {
            Entity entity = this.loadedEntityList.get(i);
            if (entity.ridingEntity != null) {
                if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity)
                    continue;

                entity.ridingEntity.riddenByEntity = null;
                entity.ridingEntity = null;
            }

            if (!entity.isDead)
                this.updateEntity(entity);

            if (entity.isDead) {
                int chunkX = entity.chunkCoordX;
                int chunkZ = entity.chunkCoordZ;

                if (entity.addedToChunk && this.chunkExists(chunkX, chunkZ))
                    this.getChunkFromChunkCoords(chunkX, chunkZ).removeEntity(entity);

                this.loadedEntityList.remove(i--);
                this.releaseEntitySkin(entity);
            }
        }

        this.field_31055_L = true;
        Iterator<TileEntity> tileEntityIterator = this.loadedTileEntityList.iterator();

        while (tileEntityIterator.hasNext()) {
            TileEntity tileEntity = tileEntityIterator.next();
            if (!tileEntity.isInvalid())
                tileEntity.updateEntity();

            if (tileEntity.isInvalid()) {
                tileEntityIterator.remove();
                Chunk chunk = this.getChunkFromChunkCoords(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4);
                if (chunk != null)
                    chunk.removeChunkBlockTileEntity(tileEntity.xCoord & 15, tileEntity.yCoord, tileEntity.zCoord & 15);
            }
        }

        this.field_31055_L = false;
        if (!this.field_30900_E.isEmpty()) {
            for (TileEntity tileEntity : this.field_30900_E) {
                if (!tileEntity.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileEntity))
                        this.loadedTileEntityList.add(tileEntity);

                    Chunk chunk = this.getChunkFromChunkCoords(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4);
                    if (chunk != null)
                        chunk.setChunkBlockTileEntity(tileEntity.xCoord & 15, tileEntity.yCoord, tileEntity.zCoord & 15, tileEntity);

                    this.markBlockNeedsUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                }
            }

            this.field_30900_E.clear();
        }

    }

    public void func_31054_a(Collection<TileEntity> tileEntities) {
        if (this.field_31055_L) {
            this.field_30900_E.addAll(tileEntities);
        } else this.loadedTileEntityList.addAll(tileEntities);
    }

    public void updateEntity(Entity entity) {
        this.updateEntityWithOptionalForce(entity, true);
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean chunk) {
        int floorX = MathHelper.floor(entity.posX);
        int floorZ = MathHelper.floor(entity.posZ);

        byte size = 32;
        if (!chunk || this.checkChunksExist(floorX - size, 0, floorZ - size, floorX + size, 128, floorZ + size)) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
            entity.prevRotationYaw = entity.rotationYaw;
            entity.prevRotationPitch = entity.rotationPitch;
            if (chunk && entity.addedToChunk) {
                if (entity.ridingEntity != null) {
                    entity.updateRidden();
                } else entity.onUpdate();
            }

            if (Double.isNaN(entity.posX) || Double.isInfinite(entity.posX))
                entity.posX = entity.lastTickPosX;

            if (Double.isNaN(entity.posY) || Double.isInfinite(entity.posY))
                entity.posY = entity.lastTickPosY;

            if (Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ))
                entity.posZ = entity.lastTickPosZ;

            if (Double.isNaN(entity.rotationPitch) || Double.isInfinite(entity.rotationPitch))
                entity.rotationPitch = entity.prevRotationPitch;

            if (Double.isNaN(entity.rotationYaw) || Double.isInfinite(entity.rotationYaw))
                entity.rotationYaw = entity.prevRotationYaw;

            int entity16X = MathHelper.floor(entity.posX / 16.0D);
            int entity16Y = MathHelper.floor(entity.posY / 16.0D);
            int entity16Z = MathHelper.floor(entity.posZ / 16.0D);

            if (!entity.addedToChunk || entity.chunkCoordX != entity16X || entity.chunkCoordY != entity16Y || entity.chunkCoordZ != entity16Z) {
                if (entity.addedToChunk && this.chunkExists(entity.chunkCoordX, entity.chunkCoordZ))
                    this.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);

                if (this.chunkExists(entity16X, entity16Z)) {
                    entity.addedToChunk = true;
                    this.getChunkFromChunkCoords(entity16X, entity16Z).addEntity(entity);
                } else entity.addedToChunk = false;
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

    public boolean checkIfAABBIsClear(AxisAlignedBB axis) {
        List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(null, axis);

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            if (!entity.isDead && entity.preventEntitySpawning)
                return false;
        }

        return true;
    }

    public boolean isAnyLiquid(AxisAlignedBB axis) {
        int minX = MathHelper.floor(axis.minX);
        int maxX = MathHelper.floor(axis.maxX + 1.0D);
        int minY = MathHelper.floor(axis.minY);
        int maxY = MathHelper.floor(axis.maxY + 1.0D);
        int minZ = MathHelper.floor(axis.minZ);
        int maxZ = MathHelper.floor(axis.maxZ + 1.0D);

        if (axis.minX < 0.0D)
            --minX;

        if (axis.minY < 0.0D)
            --minY;

        if (axis.minZ < 0.0D)
            --minZ;

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial.isLiquid())
                        return true;
                }
            }
        }

        return false;
    }

    public boolean isBoundingBoxBurning(AxisAlignedBB axis) {
        int minX = MathHelper.floor(axis.minX);
        int maxX = MathHelper.floor(axis.maxX + 1.0D);
        int minY = MathHelper.floor(axis.minY);
        int maxY = MathHelper.floor(axis.maxY + 1.0D);
        int minZ = MathHelper.floor(axis.minZ);
        int maxZ = MathHelper.floor(axis.maxZ + 1.0D);

        if (!this.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
            return false;

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    int blockId = this.getBlockId(x, y, z);
                    if (blockId == Block.FIRE.blockID
                            || blockId == Block.LAVA_MOVING.blockID
                            || blockId == Block.LAVA_STILL.blockID) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2, Entity entity) {
        int minX = MathHelper.floor(var1.minX);
        int maxX = MathHelper.floor(var1.maxX + 1.0D);
        int minY = MathHelper.floor(var1.minY);
        int maxY = MathHelper.floor(var1.maxY + 1.0D);
        int minZ = MathHelper.floor(var1.minZ);
        int maxZ = MathHelper.floor(var1.maxZ + 1.0D);

        if (!this.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
            return false;

        boolean handled = false;
        Vector3d vec = new Vector3d(0.0D, 0.0D, 0.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial == var2) {
                        double percentAir = (float) (y + 1) - BlockFluid.getPercentAir(this.getBlockMetadata(x, y, z));
                        if ((double) maxY >= percentAir) {
                            handled = true;
                            block.velocityToAddToEntity(this, x, y, z, entity, vec);
                        }
                    }
                }
            }
        }

        if (vec.length() > 0.0D) {
            vec = MathHelper.normalizeOrZero(new Vector3d(vec));
            double var19 = 0.014D;
            entity.motionX += vec.x * var19;
            entity.motionY += vec.y * var19;
            entity.motionZ += vec.z * var19;
        }

        return handled;
    }

    public boolean isMaterialInBB(AxisAlignedBB axis, Material material) {
        int minX = MathHelper.floor(axis.minX);
        int maxX = MathHelper.floor(axis.maxX + 1.0D);
        int minY = MathHelper.floor(axis.minY);
        int maxY = MathHelper.floor(axis.maxY + 1.0D);
        int minZ = MathHelper.floor(axis.minZ);
        int maxZ = MathHelper.floor(axis.maxZ + 1.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial == material)
                        return true;
                }
            }
        }

        return false;
    }

    public boolean isAABBInMaterial(AxisAlignedBB axis, Material material) {
        int minX = MathHelper.floor(axis.minX);
        int maxX = MathHelper.floor(axis.maxX + 1.0D);
        int minY = MathHelper.floor(axis.minY);
        int maxY = MathHelper.floor(axis.maxY + 1.0D);
        int minZ = MathHelper.floor(axis.minZ);
        int maxZ = MathHelper.floor(axis.maxZ + 1.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial == material) {
                        int metadata = this.getBlockMetadata(x, y, z);
                        double upY = y + 1;
                        if (metadata < 8)
                            upY = (double) (y + 1) - (double) metadata / 8.0D;

                        if (upY >= axis.minY)
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public Explosion createExplosion(Entity entity, double x, double y, double z, float power) {
        return this.newExplosion(entity, x, y, z, power, false);
    }

    public Explosion newExplosion(Entity entity, double x, double y, double z, float power, boolean flaming) {
        Explosion explosion = new Explosion(this, entity, x, y, z, power);
        explosion.isFlaming = flaming;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public float func_675_a(Vector3d var1, AxisAlignedBB var2) {
        double var3 = 1.0D / ((var2.maxX - var2.minX) * 2.0D + 1.0D);
        double var5 = 1.0D / ((var2.maxY - var2.minY) * 2.0D + 1.0D);
        double var7 = 1.0D / ((var2.maxZ - var2.minZ) * 2.0D + 1.0D);
        int var9 = 0;
        int var10 = 0;

        for (float var11 = 0.0F; var11 <= 1.0F; var11 = (float) ((double) var11 + var3)) {
            for (float var12 = 0.0F; var12 <= 1.0F; var12 = (float) ((double) var12 + var5)) {
                for (float var13 = 0.0F; var13 <= 1.0F; var13 = (float) ((double) var13 + var7)) {
                    double var14 = var2.minX + (var2.maxX - var2.minX) * (double) var11;
                    double var16 = var2.minY + (var2.maxY - var2.minY) * (double) var12;
                    double var18 = var2.minZ + (var2.maxZ - var2.minZ) * (double) var13;
                    if (this.rayTraceBlocks(new Vector3d(var14, var16, var18), var1) == null)
                        ++var9;

                    ++var10;
                }
            }
        }

        return (float) var9 / (float) var10;
    }

    public void onBlockHit(EntityPlayer var1, int x, int y, int z, int var5) {
        if (var5 == 0)
            --y;

        if (var5 == 1)
            ++y;

        if (var5 == 2)
            --z;

        if (var5 == 3)
            ++z;

        if (var5 == 4)
            --x;

        if (var5 == 5)
            ++x;

        if (this.getBlockId(x, y, z) == Block.FIRE.blockID) {
            this.playEffects(var1, 1004, x, y, z, 0);
            this.setBlockWithNotify(x, y, z, 0);
        }

    }

    public Entity func_4085_a(Class var1) {
        return null;
    }

    public String entitiesStatistic() {
        return "All: " + this.loadedEntityList.size();
    }

    public String chunkStatistic() {
        return this.chunkProvider.makeString();
    }

    @Override
    public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
        Chunk var4 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
        return var4 != null ? var4.getChunkBlockTileEntity(var1 & 15, var2, var3 & 15) : null;
    }

    public void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
        if (tileEntity.isInvalid())
            return;

        if (this.field_31055_L) {
            tileEntity.xCoord = x;
            tileEntity.yCoord = y;
            tileEntity.zCoord = z;
            this.field_30900_E.add(tileEntity);
            return;
        }

        this.loadedTileEntityList.add(tileEntity);
        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null)
            chunk.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
    }

    public void removeBlockTileEntity(int x, int y, int z) {
        TileEntity tileEntity = this.getBlockTileEntity(x, y, z);
        if (tileEntity != null && this.field_31055_L) {
            tileEntity.invalidate();
            return;
        }

        if (tileEntity != null)
            this.loadedTileEntityList.remove(tileEntity);

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null)
            chunk.removeChunkBlockTileEntity(x & 15, y, z & 15);
    }

    @Override
    public boolean isBlockOpaqueCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        return block != null && block.isOpaqueCube();
    }

    @Override
    public boolean isBlockNormalCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        if (block == null)
            return false;

        return block.blockMaterial.getIsTranslucent() && block.renderAsNormalBlock();
    }

    public void saveWorldIndirectly(IProgressUpdatable progressUpdatable) {
        this.saveWorld(true, progressUpdatable);
    }

    public boolean updatingLighting() {
        if (this.lightingUpdatesCounter >= 50)
            return false;

        ++this.lightingUpdatesCounter;

        try {
            int var1 = 500;

            while (this.lightingToUpdate.size() > 0) {
                --var1;
                if (var1 <= 0)
                    return true;

                this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1).func_4127_a(this);
            }

            return false;
        } finally {
            --this.lightingUpdatesCounter;
        }
    }

    public void scheduleLightingUpdate(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.scheduleLightingUpdate(var1, var2, var3, var4, var5, var6, var7, true);
    }

    public void scheduleLightingUpdate(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
        if (!this.worldProvider.hasNoSky || var1 != EnumSkyBlock.SKY) {
            ++lightingUpdatesScheduled;

            try {
                if (lightingUpdatesScheduled != 50) {
                    int var9 = (var5 + var2) / 2;
                    int var10 = (var7 + var4) / 2;
                    if (!this.blockExists(var9, 64, var10)) {
                        return;
                    }

                    if (this.getChunkFromBlockCoords(var9, var10).func_21167_h()) {
                        return;
                    }

                    int var11 = this.lightingToUpdate.size();
                    if (var8) {
                        int var12 = 5;
                        if (var12 > var11) {
                            var12 = var11;
                        }

                        for (int var13 = 0; var13 < var12; ++var13) {
                            MetadataChunkBlock var14 = this.lightingToUpdate.get(this.lightingToUpdate.size() - var13 - 1);
                            if (var14.field_1299_a == var1 && var14.func_866_a(var2, var3, var4, var5, var6, var7)) {
                                return;
                            }
                        }
                    }

                    this.lightingToUpdate.add(new MetadataChunkBlock(var1, var2, var3, var4, var5, var6, var7));
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
        int var1 = this.calculateSkylightSubtracted(1.0F);
        if (var1 != this.skylightSubtracted) {
            this.skylightSubtracted = var1;
        }

    }

    public void setAllowedMobSpawns(boolean var1, boolean var2) {
        this.spawnHostileMobs = var1;
        this.spawnPeacefulMobs = var2;
    }

    public void tick() {
        this.updateWeather();
        if (this.isAllPlayersFullyAsleep()) {
            boolean var1 = false;
            if (this.spawnHostileMobs && this.difficultySetting >= 1) {
                var1 = SpawnerAnimals.performSleepSpawning(this, this.playerEntities);
            }

            if (!var1) {
                long var2 = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(var2 - var2 % 24000L);
                this.wakeUpAllPlayers();
            }
        }

        SpawnerAnimals.performSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs);
        this.chunkProvider.unload100OldestChunks();
        int var4 = this.calculateSkylightSubtracted(1.0F);
        if (var4 != this.skylightSubtracted) {
            this.skylightSubtracted = var4;

            for (int i = 0; i < this.worldAccesses.size(); ++i) {
                this.worldAccesses.get(i).updateAllRenderers();
            }
        }

        long var6 = this.worldInfo.getWorldTime() + 1L;
        if (var6 % (long) this.autosavePeriod == 0L) {
            this.saveWorld(false, null);
        }

        this.worldInfo.setWorldTime(var6);
        this.TickUpdates(false);
        this.updateBlocksAndPlayCaveSounds();
    }

    private void func_27163_E() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0F;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0F;
            }
        }

    }

    protected void updateWeather() {
        if (this.worldProvider.hasNoSky)
            return;

        if (this.field_27168_F > 0)
            --this.field_27168_F;

        int thunderTime = this.worldInfo.getThunderTime();
        if (thunderTime <= 0) {
            if (this.worldInfo.isThundering()) {
                this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
            } else this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
        } else {
            --thunderTime;
            this.worldInfo.setThunderTime(thunderTime);
            if (thunderTime <= 0)
                this.worldInfo.setThundering(!this.worldInfo.isThundering());
        }

        int rainTime = this.worldInfo.getRainTime();
        if (rainTime <= 0) {
            if (this.worldInfo.isRaining()) {
                this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
            } else this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
        } else {
            --rainTime;
            this.worldInfo.setRainTime(rainTime);
            if (rainTime <= 0)
                this.worldInfo.setRaining(!this.worldInfo.isRaining());
        }

        this.prevRainingStrength = this.rainingStrength;
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = (float) ((double) this.rainingStrength + 0.01D);
        } else this.rainingStrength = (float) ((double) this.rainingStrength - 0.01D);

        if (this.rainingStrength < 0.0F)
            this.rainingStrength = 0.0F;

        if (this.rainingStrength > 1.0F)
            this.rainingStrength = 1.0F;

        this.prevThunderingStrength = this.thunderingStrength;
        if (this.worldInfo.isThundering()) {
            this.thunderingStrength = (float) ((double) this.thunderingStrength + 0.01D);
        } else this.thunderingStrength = (float) ((double) this.thunderingStrength - 0.01D);

        if (this.thunderingStrength < 0.0F)
            this.thunderingStrength = 0.0F;

        if (this.thunderingStrength > 1.0F)
            this.thunderingStrength = 1.0F;
    }

    private void stopPrecipitation() {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }

    protected void updateBlocksAndPlayCaveSounds() {
        this.activeChunkSet.clear();

        for (EntityPlayer player : this.playerEntities) {
            int chunkX = MathHelper.floor(player.posX / 16.0D);
            int chunkZ = MathHelper.floor(player.posZ / 16.0D);
            byte radius = 9;

            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z)
                    this.activeChunkSet.add(new ChunkCoordIntPair(x + chunkX, z + chunkZ));
            }
        }

        if (this.soundCounter > 0)
            --this.soundCounter;

        for (ChunkCoordIntPair pair : this.activeChunkSet) {
            int endX = pair.chunkXPos * 16;
            int endZ = pair.chunkZPos * 16;

            Chunk chunk = this.getChunkFromChunkCoords(pair.chunkXPos, pair.chunkZPos);
            if (this.soundCounter == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int compressedCoord = this.distHashCounter >> 2;
                int uncX = compressedCoord & 15;
                int uncZ = compressedCoord >> 8 & 15;
                int uncY = compressedCoord >> 16 & 127;
                int blockID = chunk.getBlockID(uncX, uncY, uncZ);
                uncX = uncX + endX;
                uncZ = uncZ + endZ;
                if (blockID == 0
                        && this.getFullBlockLightValue(uncX, uncY, uncZ) <= this.rand.nextInt(8)
                        && this.getSavedLightValue(EnumSkyBlock.SKY, uncX, uncY, uncZ) <= 0) {

                    EntityPlayer closestPlayer = this.getClosestPlayer(
                            (double) uncX + 0.5D,
                            (double) uncY + 0.5D,
                            (double) uncZ + 0.5D,
                            8.0D
                    );

                    if (closestPlayer != null && closestPlayer.getDistanceSq((double) uncX + 0.5D, (double) uncY + 0.5D, (double) uncZ + 0.5D) > 4.0D) {
                        this.playSoundEffect(
                                (double) uncX + 0.5D,
                                (double) uncY + 0.5D,
                                (double) uncZ + 0.5D,
                                "ambient.cave.cave",
                                0.7F,
                                0.8F + this.rand.nextFloat() * 0.2F
                        );

                        this.soundCounter = this.rand.nextInt(12000) + 6000;
                    }
                }
            }

            if (this.rand.nextInt(100000) == 0 && this.func_27161_C() && this.func_27160_B()) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int compressedCoord = this.distHashCounter >> 2;
                int uncX = endX + (compressedCoord & 15);
                int uncZ = endZ + (compressedCoord >> 8 & 15);

                int topY = this.findTopSolidBlock(uncX, uncZ);
                if (this.canBlockBeRainedOn(uncX, topY, uncZ)) {
                    this.addWeatherEffect(new EntityLightningBolt(this, uncX, topY, uncZ));
                    this.field_27168_F = 2;
                }
            }

            if (this.rand.nextInt(16) == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var19 = this.distHashCounter >> 2;
                int var24 = var19 & 15;
                int var28 = var19 >> 8 & 15;
                int var31 = this.findTopSolidBlock(var24 + endX, var28 + endZ);
                if (this.getWorldChunkManager().getBiomeGenAt(var24 + endX, var28 + endZ).getEnableSnow() && var31 >= 0 && var31 < 128 && chunk.getSavedLightValue(EnumSkyBlock.BLOCK, var24, var31, var28) < 10) {
                    int var33 = chunk.getBlockID(var24, var31 - 1, var28);
                    int var35 = chunk.getBlockID(var24, var31, var28);
                    if (this.func_27161_C() && var35 == 0 && Block.SNOW.canPlaceBlockAt(this, var24 + endX, var31, var28 + endZ) && var33 != 0 && var33 != Block.ICE.blockID && Block.BLOCKS_LIST[var33].blockMaterial.getIsSolid()) {
                        this.setBlockWithNotify(var24 + endX, var31, var28 + endZ, Block.SNOW.blockID);
                    }

                    if (var33 == Block.WATER_STILL.blockID && chunk.getBlockMetadata(var24, var31 - 1, var28) == 0) {
                        this.setBlockWithNotify(var24 + endX, var31 - 1, var28 + endZ, Block.ICE.blockID);
                    }
                }
            }

            for (int var20 = 0; var20 < 80; ++var20) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var25 = this.distHashCounter >> 2;
                int var29 = var25 & 15;
                int var32 = var25 >> 8 & 15;
                int var34 = var25 >> 16 & 127;
                int var36 = chunk.blocks[var29 << 11 | var32 << 7 | var34] & 255;
                if (Block.TICK_ON_LOAD[var36]) {
                    Block.BLOCKS_LIST[var36].updateTick(this, var29 + endX, var34, var32 + endZ, this.rand);
                }
            }
        }

    }

    public boolean TickUpdates(boolean var1) {
        int tasksSize = this.scheduledTickTreeSet.size();
        if (tasksSize != this.scheduledTickSet.size())
            throw new IllegalStateException("TickNextTick list out of synch");

        if (tasksSize > 1000)
            tasksSize = 1000;

        for (int i = 0; i < tasksSize; ++i) {
            NextTickListEntry entry = this.scheduledTickTreeSet.first();
            if (!var1 && entry.scheduledTime > this.worldInfo.getWorldTime())
                break;

            this.scheduledTickTreeSet.remove(entry);
            this.scheduledTickSet.remove(entry);

            byte var5 = 8;
            if (this.checkChunksExist(entry.xCoord - var5, entry.yCoord - var5, entry.zCoord - var5, entry.xCoord + var5, entry.yCoord + var5, entry.zCoord + var5)) {
                int blockId = this.getBlockId(entry.xCoord, entry.yCoord, entry.zCoord);
                if (blockId == entry.blockID && blockId > 0) {
                    Block.BLOCKS_LIST[blockId].updateTick(this, entry.xCoord, entry.yCoord, entry.zCoord, this.rand);
                }
            }
        }

        return this.scheduledTickTreeSet.size() != 0;
    }

    public void randomDisplayUpdates(int ofX, int ofY, int ofZ) {
        byte var4 = 16;
        Random random = new Random();

        for (int i = 0; i < 1000; ++i) {
            int x = ofX + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            int y = ofY + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            int z = ofZ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            int blockId = this.getBlockId(x, y, z);

            if (blockId > 0)
                Block.BLOCKS_LIST[blockId].randomDisplayTick(this, x, y, z, random);
        }

    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB bb) {
        this.entities.clear();

        int minX = MathHelper.floor((bb.minX - 2.0D) / 16.0D);
        int maxX = MathHelper.floor((bb.maxX + 2.0D) / 16.0D);
        int minZ = MathHelper.floor((bb.minZ - 2.0D) / 16.0D);
        int maxZ = MathHelper.floor((bb.maxZ + 2.0D) / 16.0D);

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (this.chunkExists(x, z))
                    this.getChunkFromChunkCoords(x, z).getEntitiesWithinAABBForEntity(entity, bb, this.entities);
            }
        }

        return this.entities;
    }

    public List<Entity> getEntitiesWithinAABB(Class var1, AxisAlignedBB bb) {
        int minX = MathHelper.floor((bb.minX - 2.0D) / 16.0D);
        int maxX = MathHelper.floor((bb.maxX + 2.0D) / 16.0D);
        int minZ = MathHelper.floor((bb.minZ - 2.0D) / 16.0D);
        int maxZ = MathHelper.floor((bb.maxZ + 2.0D) / 16.0D);

        List<Entity> entities = new ArrayList<>();
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (this.chunkExists(x, z))
                    this.getChunkFromChunkCoords(x, z).getEntitiesOfTypeWithinAAAB(var1, bb, entities);
            }
        }

        return entities;
    }

    public List getLoadedEntityList() {
        return this.loadedEntityList;
    }

    public void updateTileEntityChunkAndDoNothing(int x, int y, int z, TileEntity tileEntity) {
        if (this.blockExists(x, y, z))
            this.getChunkFromBlockCoords(x, z).setChunkModified();

        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.doNothingWithTileEntity(x, y, z, tileEntity);
    }

    public int countEntities(Class<? extends Entity> clazz) {
        int count = 0;

        for (Entity entity : this.loadedEntityList) {
            if (clazz.isAssignableFrom(entity.getClass()))
                ++count;
        }

        return count;
    }

    public void addLoadedEntities(List<Entity> entities) {
        this.loadedEntityList.addAll(entities);

        for (Entity entity : entities)
            this.obtainEntitySkin(entity);
    }

    public void addUnloadedEntities(List<Entity> entities) {
        this.unloadedEntityList.addAll(entities);
    }

    public void unloadOldChunks() {
        while (this.chunkProvider.unload100OldestChunks()) ;
    }

    public boolean canBlockBePlacedAt(int blockId, int x, int y, int z, boolean var5, int var6) {
        int coordBlockId = this.getBlockId(x, y, z);
        Block locBlock = Block.BLOCKS_LIST[coordBlockId];
        Block placeBlock = Block.BLOCKS_LIST[blockId];
        AxisAlignedBB bb = placeBlock.getCollisionBoundingBoxFromPool(this, x, y, z);

        if (var5)
            bb = null;

        if (bb != null && !this.checkIfAABBIsClear(bb))
            return false;

        if (locBlock == Block.WATER_MOVING
                || locBlock == Block.WATER_STILL
                || locBlock == Block.LAVA_MOVING
                || locBlock == Block.LAVA_STILL
                || locBlock == Block.FIRE
                || locBlock == Block.SNOW) {
            locBlock = null;
        }

        return blockId > 0 && locBlock == null && placeBlock.canPlaceBlockOnSide(this, x, y, z, var6);
    }

    public PathEntity getPathToEntity(Entity from, Entity to, float var3) {
        int x = MathHelper.floor(from.posX);
        int y = MathHelper.floor(from.posY);
        int z = MathHelper.floor(from.posZ);
        int diameter = (int) (var3 + 16.0F);

        int startX = x - diameter;
        int startY = y - diameter;
        int startZ = z - diameter;

        int endX = x + diameter;
        int endY = y + diameter;
        int endZ = z + diameter;

        ChunkCache chunkCache = new ChunkCache(this, startX, startY, startZ, endX, endY, endZ);
        return new Pathfinder(chunkCache).createEntityPathTo(from, to, var3);
    }

    public PathEntity getEntityPathToXYZ(Entity entity, int var2, int var3, int var4, float var5) {
        int floorX = MathHelper.floor(entity.posX);
        int floorY = MathHelper.floor(entity.posY);
        int floorZ = MathHelper.floor(entity.posZ);

        int dif = (int) (var5 + 8.0F);

        int minX = floorX - dif;
        int minY = floorY - dif;
        int minZ = floorZ - dif;

        int maxX = floorX + dif;
        int maxY = floorY + dif;
        int maxZ = floorZ + dif;

        ChunkCache chunkCache = new ChunkCache(this, minX, minY, minZ, maxX, maxY, maxZ);
        return new Pathfinder(chunkCache).createEntityPathTo(entity, var2, var3, var4, var5);
    }

    public boolean isBlockProvidingPowerTo(int x, int y, int z, int var4) {
        int blockId = this.getBlockId(x, y, z);
        return blockId != 0 && Block.BLOCKS_LIST[blockId].isIndirectlyPoweringTo(this, x, y, z, var4);
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
        if (this.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0))
            return true;

        if (this.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1))
            return true;

        if (this.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2))
            return true;

        if (this.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3))
            return true;

        if (this.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4))
            return true;

        return this.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5);
    }

    public EntityPlayer getClosestPlayerToEntity(Entity entity, double var2) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, var2);
    }

    public EntityPlayer getClosestPlayer(double x, double y, double z, double var7) {
        double var9 = -1.0D;
        EntityPlayer player = null;

        for (EntityPlayer entityPlayer : this.playerEntities) {
            double distanceSq = entityPlayer.getDistanceSq(x, y, z);
            if ((var7 < 0.0D || distanceSq < var7 * var7) && (var9 == -1.0D || distanceSq < var9)) {
                var9 = distanceSq;
                player = entityPlayer;
            }
        }

        return player;
    }

    public EntityPlayer getPlayerEntityByName(String name) {
        for (EntityPlayer playerEntity : this.playerEntities) {
            if (name.equals(playerEntity.username))
                return playerEntity;
        }

        return null;
    }

    public void setChunkData(int var1, int var2, int var3, int var4, int var5, int var6, byte[] var7) {
        int var8 = var1 >> 4;
        int var9 = var3 >> 4;
        int var10 = var1 + var4 - 1 >> 4;
        int var11 = var3 + var6 - 1 >> 4;
        int var12 = 0;
        int var13 = var2;
        int var14 = var2 + var5;

        if (var2 < 0)
            var13 = 0;

        if (var14 > 128)
            var14 = 128;

        for (int var15 = var8; var15 <= var10; ++var15) {
            int var16 = var1 - var15 * 16;
            int var17 = var1 + var4 - var15 * 16;
            if (var16 < 0)
                var16 = 0;

            if (var17 > 16)
                var17 = 16;

            for (int var18 = var9; var18 <= var11; ++var18) {
                int var19 = var3 - var18 * 16;
                int var20 = var3 + var6 - var18 * 16;
                if (var19 < 0)
                    var19 = 0;

                if (var20 > 16)
                    var20 = 16;

                var12 = this.getChunkFromChunkCoords(var15, var18).setChunkData(var7, var16, var13, var19, var17, var14, var20, var12);
                this.markBlocksDirty(var15 * 16 + var16, var13, var18 * 16 + var19, var15 * 16 + var17, var14, var18 * 16 + var20);
            }
        }

    }

    public void sendQuittingDisconnectingPacket() {
    }

    public void checkSessionLock() {
        this.saveHandler.validateSession();
    }

    public long getRandomSeed() {
        return this.worldInfo.getRandomSeed();
    }

    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }

    public void setWorldTime(long var1) {
        this.worldInfo.setWorldTime(var1);
    }

    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
    }

    public void setSpawnPoint(ChunkCoordinates var1) {
        this.worldInfo.setSpawn(var1.x, var1.y, var1.z);
    }

    public void joinEntityInSurroundings(Entity entity) {
        int cenX = MathHelper.floor(entity.posX / 16.0D);
        int cenZ = MathHelper.floor(entity.posZ / 16.0D);

        byte sqRadius = 2;
        for (int x = cenX - sqRadius; x <= cenX + sqRadius; ++x) {
            for (int z = cenZ - sqRadius; z <= cenZ + sqRadius; ++z)
                this.getChunkFromChunkCoords(x, z);
        }

        if (!this.loadedEntityList.contains(entity))
            this.loadedEntityList.add(entity);
    }

    public boolean canMineBlock(EntityPlayer player, int x, int y, int z) {
        return true;
    }

    public void sendTrackedEntityStatusUpdatePacket(Entity var1, byte var2) {
    }

    public void updateEntityList() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (Entity entity : this.unloadedEntityList) {
            int x = entity.chunkCoordX;
            int z = entity.chunkCoordZ;
            if (entity.addedToChunk && this.chunkExists(x, z)) {
                this.getChunkFromChunkCoords(x, z).removeEntity(entity);
            }
        }

        for (Entity entity : this.unloadedEntityList) {
            this.releaseEntitySkin(entity);
        }

        this.unloadedEntityList.clear();

        for (int var6 = 0; var6 < this.loadedEntityList.size(); ++var6) {
            Entity entity = this.loadedEntityList.get(var6);
            if (entity.ridingEntity != null) {
                if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity) {
                    continue;
                }

                entity.ridingEntity.riddenByEntity = null;
                entity.ridingEntity = null;
            }

            if (entity.isDead) {
                int var8 = entity.chunkCoordX;
                int var9 = entity.chunkCoordZ;
                if (entity.addedToChunk && this.chunkExists(var8, var9)) {
                    this.getChunkFromChunkCoords(var8, var9).removeEntity(entity);
                }

                this.loadedEntityList.remove(var6--);
                this.releaseEntitySkin(entity);
            }
        }

    }

    public IChunkProvider getIChunkProvider() {
        return this.chunkProvider;
    }

    public void playNoteAt(int x, int y, int z, int var4, int var5) {
        int blockId = this.getBlockId(x, y, z);
        if (blockId > 0)
            Block.BLOCKS_LIST[blockId].playBlock(this, x, y, z, var4, var5);
    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = !this.playerEntities.isEmpty();

        for (EntityPlayer varplayer : this.playerEntities) {
            if (!varplayer.isPlayerSleeping()) {
                this.allPlayersSleeping = false;
                break;
            }
        }

    }

    protected void wakeUpAllPlayers() {
        this.allPlayersSleeping = false;

        for (EntityPlayer player : this.playerEntities) {
            if (player.isPlayerSleeping())
                player.wakeUpPlayer(false, false, true);
        }

        this.stopPrecipitation();
    }

    public boolean isAllPlayersFullyAsleep() {
        if (this.allPlayersSleeping && !this.multiplayerWorld) {
            for (EntityPlayer player : this.playerEntities) {
                if (!player.isPlayerFullyAsleep())
                    return false;
            }

            return true;
        }

        return false;
    }

    public float func_27166_f(float var1) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * var1) * this.getRainStrength(var1);
    }

    public float getRainStrength(float var1) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * var1;
    }

    public void func_27158_h(float var1) {
        this.prevRainingStrength = var1;
        this.rainingStrength = var1;
    }

    public boolean func_27160_B() {
        return (double) this.func_27166_f(1.0F) > 0.9D;
    }

    public boolean func_27161_C() {
        return (double) this.getRainStrength(1.0F) > 0.2D;
    }

    public boolean canBlockBeRainedOn(int var1, int var2, int var3) {
        if (!this.func_27161_C()) {
            return false;
        }

        if (!this.canBlockSeeTheSky(var1, var2, var3)) {
            return false;
        }

        if (this.findTopSolidBlock(var1, var3) > var2) {
            return false;
        }

        BiomeGenBase genBase = this.getWorldChunkManager().getBiomeGenAt(var1, var3);
        return !genBase.getEnableSnow() && genBase.canSpawnLightningBolt();
    }

    public void setItemData(String var1, MapDataBase var2) {
        this.mapStorage.setData(var1, var2);
    }

    public MapDataBase loadItemData(Class var1, String var2) {
        return this.mapStorage.loadData(var1, var2);
    }

    public int getUniqueDataId(String var1) {
        return this.mapStorage.getUniqueDataId(var1);
    }

    public void func_28106_e(int var1, int var2, int var3, int var4, int var5) {
        this.playEffects(null, var1, var2, var3, var4, var5);
    }

    public void playEffects(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.func_28136_a(var1, var2, var3, var4, var5, var6);
        }
    }
}
