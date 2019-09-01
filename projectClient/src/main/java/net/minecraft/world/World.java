package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.EnumSkyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLightningBolt;
import net.minecraft.entity.SpawnerAnimals;
import net.minecraft.entity.ai.PathEntity;
import net.minecraft.entity.ai.Pathfinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.MapDataBase;
import net.minecraft.material.Material;
import net.minecraft.nbt.TagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.BiomeGenBase;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import util.MathHelper;
import util.Vec3D;

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
    private long field_1019_F;
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
        this.field_1019_F = 16777215L;
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
        this.field_1019_F = 16777215L;
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

    public World(ISaveHandler var1, String var2, long var3) {
        this(var1, var2, var3, null);
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
        this.field_1019_F = 16777215L;
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

    public int getBlockId(int var1, int var2, int var3) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0) {
                return 0;
            } else {
                return var2 >= 128 ? 0 : this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).getBlockID(var1 & 15, var2, var3 & 15);
            }
        } else {
            return 0;
        }
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
        } else {
            return false;
        }
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
            if (var2 < 0) {
                return false;
            } else if (var2 >= 128) {
                return false;
            } else {
                Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                return var5.setBlockID(var1 & 15, var2, var3 & 15, var4);
            }
        } else {
            return false;
        }
    }

    public Material getBlockMaterial(int var1, int var2, int var3) {
        int var4 = this.getBlockId(var1, var2, var3);
        return var4 == 0 ? Material.AIR : Block.BLOCKS_LIST[var4].blockMaterial;
    }

    public int getBlockMetadata(int var1, int var2, int var3) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0) {
                return 0;
            } else if (var2 >= 128) {
                return 0;
            } else {
                Chunk var4 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                var1 = var1 & 15;
                var3 = var3 & 15;
                return var4.getBlockMetadata(var1, var2, var3);
            }
        } else {
            return 0;
        }
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
            if (var2 < 0) {
                return false;
            } else if (var2 >= 128) {
                return false;
            } else {
                Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                var1 = var1 & 15;
                var3 = var3 & 15;
                var5.setBlockMetadata(var1, var2, var3, var4);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
        if (this.setBlock(var1, var2, var3, var4)) {
            this.notifyBlockChange(var1, var2, var3, var4);
            return true;
        } else {
            return false;
        }
    }

    public boolean setBlockAndMetadataWithNotify(int var1, int var2, int var3, int var4, int var5) {
        if (this.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
            this.notifyBlockChange(var1, var2, var3, var4);
            return true;
        } else {
            return false;
        }
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

    public boolean canBlockSeeTheSky(int var1, int var2, int var3) {
        return this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).canBlockSeeTheSky(var1 & 15, var2, var3 & 15);
    }

    public int getFullBlockLightValue(int var1, int var2, int var3) {
        if (var2 < 0)
            return 0;

        if (var2 >= 128) {
            var2 = 127;
        }

        return this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).getBlockLightValue(var1 & 15, var2, var3 & 15, 0);
    }

    public int getBlockLightValue(int var1, int var2, int var3) {
        return this.getBlockLightValue_do(var1, var2, var3, true);
    }

    public int getBlockLightValue_do(int var1, int var2, int var3, boolean var4) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var4) {
                int var5 = this.getBlockId(var1, var2, var3);
                if (var5 == Block.STAIR_SINGLE.blockID || var5 == Block.FARMLAND.blockID || var5 == Block.STAIR_COMPACT_COBBLESTONE.blockID || var5 == Block.STAIR_COMPACT_PLANKS.blockID) {
                    int var6 = this.getBlockLightValue_do(var1, var2 + 1, var3, false);
                    int var7 = this.getBlockLightValue_do(var1 + 1, var2, var3, false);
                    int var8 = this.getBlockLightValue_do(var1 - 1, var2, var3, false);
                    int var9 = this.getBlockLightValue_do(var1, var2, var3 + 1, false);
                    int var10 = this.getBlockLightValue_do(var1, var2, var3 - 1, false);
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

            if (var2 < 0) {
                return 0;
            } else {
                if (var2 >= 128) {
                    var2 = 127;
                }

                Chunk var13 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                var1 = var1 & 15;
                var3 = var3 & 15;
                return var13.getBlockLightValue(var1, var2, var3, this.skylightSubtracted);
            }
        }

        return 15;
    }

    public boolean canExistingBlockSeeTheSky(int var1, int var2, int var3) {
        if (var1 >= -32000000 && var3 >= -32000000 && var1 < 32000000 && var3 <= 32000000) {
            if (var2 < 0) {
                return false;
            } else if (var2 >= 128) {
                return true;
            } else if (!this.chunkExists(var1 >> 4, var3 >> 4)) {
                return false;
            } else {
                Chunk var4 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                var1 = var1 & 15;
                var3 = var3 & 15;
                return var4.canBlockSeeTheSky(var1, var2, var3);
            }
        }

        return false;
    }

    public int getHeightValue(int var1, int var2) {
        if (var1 >= -32000000 && var2 >= -32000000 && var1 < 32000000 && var2 <= 32000000) {
            if (!this.chunkExists(var1 >> 4, var2 >> 4)) {
                return 0;
            } else {
                Chunk var3 = this.getChunkFromChunkCoords(var1 >> 4, var2 >> 4);
                return var3.getHeightValue(var1 & 15, var2 & 15);
            }
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
            } else {
                Chunk var7 = this.getChunkFromChunkCoords(var5, var6);
                return var7.getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
            }
        } else {
            return var1.lightValue;
        }
    }

    public void setLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
        if (var2 >= -32000000 && var4 >= -32000000 && var2 < 32000000 && var4 <= 32000000) {
            if (var3 >= 0) {
                if (var3 < 128) {
                    if (this.chunkExists(var2 >> 4, var4 >> 4)) {
                        Chunk chunk = this.getChunkFromChunkCoords(var2 >> 4, var4 >> 4);
                        chunk.setLightValue(var1, var2 & 15, var3, var4 & 15, var5);

                        for (IWorldAccess worldAccess : this.worldAccesses) {
                            worldAccess.markBlockAndNeighborsNeedsUpdate(var2, var3, var4);
                        }

                    }
                }
            }
        }
    }

    public float getBrightness(int x, int y, int z, int var4) {
        int var5 = this.getBlockLightValue(x, y, z);
        if (var5 < var4) {
            var5 = var4;
        }

        return this.worldProvider.lightBrightnessTable[var5];
    }

    public float getLightBrightness(int var1, int var2, int var3) {
        return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(var1, var2, var3)];
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
                    Block var37 = Block.BLOCKS_LIST[var35];
                    if ((!paramBoolean2 || var37 == null || var37.getCollisionBoundingBoxFromPool(this, m, n, i1) != null) && var35 > 0 && var37.canCollideCheck(var36, paramBoolean)) {
                        MovingObjectPosition var38 = var37.collisionRayTrace(this, m, n, i1, var1, var2);
                        if (var38 != null) {
                            return var38;
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

    public void playSoundAtEntity(Entity var1, String var2, float var3, float var4) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.playSound(var2, var1.posX, var1.posY - (double) var1.yOffset, var1.posZ, var3, var4);
        }
    }

    public void playSoundEffect(double var1, double var3, double var5, String var7, float var8, float var9) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.playSound(var7, var1, var3, var5, var8, var9);
        }
    }

    public void playRecord(String var1, int var2, int var3, int var4) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.playRecord(var1, var2, var3, var4);
        }
    }

    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.spawnParticle(var1, var2, var4, var6, var8, var10, var12);
        }
    }

    public boolean addWeatherEffect(Entity var1) {
        this.weatherEffects.add(var1);
        return true;
    }

    public boolean entityJoinedWorld(Entity var1) {
        int var2 = MathHelper.floor(var1.posX / 16.0D);
        int var3 = MathHelper.floor(var1.posZ / 16.0D);
        boolean var4 = false;
        if (var1 instanceof EntityPlayer) {
            var4 = true;
        }

        if (!var4 && !this.chunkExists(var2, var3)) {
            return false;
        } else {
            if (var1 instanceof EntityPlayer) {
                EntityPlayer var5 = (EntityPlayer) var1;
                this.playerEntities.add(var5);
                this.updateAllPlayersSleepingFlag();
            }

            this.getChunkFromChunkCoords(var2, var3).addEntity(var1);
            this.loadedEntityList.add(var1);
            this.obtainEntitySkin(var1);
            return true;
        }
    }

    protected void obtainEntitySkin(Entity entity) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.obtainEntitySkin(entity);
        }

    }

    protected void releaseEntitySkin(Entity entity) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.releaseEntitySkin(entity);
        }
    }

    public void setEntityDead(Entity entity) {
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

    public void addWorldAccess(IWorldAccess worldAccess) {
        this.worldAccesses.add(worldAccess);
    }

    public void removeWorldAccess(IWorldAccess worldAccess) {
        this.worldAccesses.remove(worldAccess);
    }

    public List getCollidingBoundingBoxes(Entity entity, AxisAlignedBB var2) {
        this.collidingBoundingBoxes.clear();
        int var3 = MathHelper.floor(var2.minX);
        int var4 = MathHelper.floor(var2.maxX + 1.0D);
        int var5 = MathHelper.floor(var2.minY);
        int var6 = MathHelper.floor(var2.maxY + 1.0D);
        int var7 = MathHelper.floor(var2.minZ);
        int var8 = MathHelper.floor(var2.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var7; var10 < var8; ++var10) {
                if (this.blockExists(var9, 64, var10)) {
                    for (int var11 = var5 - 1; var11 < var6; ++var11) {
                        Block var12 = Block.BLOCKS_LIST[this.getBlockId(var9, var11, var10)];
                        if (var12 != null) {
                            var12.getCollidingBoundingBoxes(this, var9, var11, var10, var2, this.collidingBoundingBoxes);
                        }
                    }
                }
            }
        }

        double var14 = 0.25D;
        List<Entity> var15 = this.getEntitiesWithinAABBExcludingEntity(entity, var2.expand(var14, var14, var14));

        for (int var16 = 0; var16 < var15.size(); ++var16) {
            AxisAlignedBB var13 = ((Entity) var15.get(var16)).getBoundingBox();
            if (var13 != null && var13.intersectsWith(var2)) {
                this.collidingBoundingBoxes.add(var13);
            }

            var13 = entity.getCollisionBox((Entity) var15.get(var16));
            if (var13 != null && var13.intersectsWith(var2)) {
                this.collidingBoundingBoxes.add(var13);
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
        var3 = (float) ((double) var3 * (1.0D - (double) (this.func_27162_g(var1) * 5.0F) / 16.0D));
        var3 = (float) ((double) var3 * (1.0D - (double) (this.func_27166_f(var1) * 5.0F) / 16.0D));
        var3 = 1.0F - var3;
        return (int) (var3 * 11.0F);
    }

    public Vec3D func_4079_a(Entity var1, float var2) {
        float var3 = this.getCelestialAngle(var2);
        float var4 = MathHelper.cos(var3 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
        if (var4 < 0.0F) {
            var4 = 0.0F;
        }

        if (var4 > 1.0F) {
            var4 = 1.0F;
        }

        int var5 = MathHelper.floor(var1.posX);
        int var6 = MathHelper.floor(var1.posZ);
        float var7 = (float) this.getWorldChunkManager().getTemperature(var5, var6);
        int var8 = this.getWorldChunkManager().getBiomeGenAt(var5, var6).getSkyColorByTemp(var7);
        float var9 = (float) (var8 >> 16 & 255) / 255.0F;
        float var10 = (float) (var8 >> 8 & 255) / 255.0F;
        float var11 = (float) (var8 & 255) / 255.0F;
        var9 = var9 * var4;
        var10 = var10 * var4;
        var11 = var11 * var4;
        float var12 = this.func_27162_g(var2);
        if (var12 > 0.0F) {
            float var13 = (var9 * 0.3F + var10 * 0.59F + var11 * 0.11F) * 0.6F;
            float var14 = 1.0F - var12 * 0.75F;
            var9 = var9 * var14 + var13 * (1.0F - var14);
            var10 = var10 * var14 + var13 * (1.0F - var14);
            var11 = var11 * var14 + var13 * (1.0F - var14);
        }

        float var19 = this.func_27166_f(var2);
        if (var19 > 0.0F) {
            float var20 = (var9 * 0.3F + var10 * 0.59F + var11 * 0.11F) * 0.2F;
            float var15 = 1.0F - var19 * 0.75F;
            var9 = var9 * var15 + var20 * (1.0F - var15);
            var10 = var10 * var15 + var20 * (1.0F - var15);
            var11 = var11 * var15 + var20 * (1.0F - var15);
        }

        if (this.field_27172_i > 0) {
            float var21 = (float) this.field_27172_i - var2;
            if (var21 > 1.0F) {
                var21 = 1.0F;
            }

            var21 = var21 * 0.45F;
            var9 = var9 * (1.0F - var21) + 0.8F * var21;
            var10 = var10 * (1.0F - var21) + 0.8F * var21;
            var11 = var11 * (1.0F - var21) + 1.0F * var21;
        }

        return Vec3D.createVector(var9, var10, var11);
    }

    public float getCelestialAngle(float var1) {
        return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), var1);
    }

    public Vec3D func_628_d(float var1) {
        float var2 = this.getCelestialAngle(var1);
        float var3 = MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        float var4 = (float) (this.field_1019_F >> 16 & 255L) / 255.0F;
        float var5 = (float) (this.field_1019_F >> 8 & 255L) / 255.0F;
        float var6 = (float) (this.field_1019_F & 255L) / 255.0F;
        float var7 = this.func_27162_g(var1);
        if (var7 > 0.0F) {
            float var8 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.6F;
            float var9 = 1.0F - var7 * 0.95F;
            var4 = var4 * var9 + var8 * (1.0F - var9);
            var5 = var5 * var9 + var8 * (1.0F - var9);
            var6 = var6 * var9 + var8 * (1.0F - var9);
        }

        var4 = var4 * (var3 * 0.9F + 0.1F);
        var5 = var5 * (var3 * 0.9F + 0.1F);
        var6 = var6 * (var3 * 0.85F + 0.15F);
        float var14 = this.func_27166_f(var1);
        if (var14 > 0.0F) {
            float var15 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.2F;
            float var10 = 1.0F - var14 * 0.95F;
            var4 = var4 * var10 + var15 * (1.0F - var10);
            var5 = var5 * var10 + var15 * (1.0F - var10);
            var6 = var6 * var10 + var15 * (1.0F - var10);
        }

        return Vec3D.createVector(var4, var5, var6);
    }

    public Vec3D getFogColor(float var1) {
        float var2 = this.getCelestialAngle(var1);
        return this.worldProvider.func_4096_a(var2, var1);
    }

    public int findTopSolidBlock(int var1, int var2) {
        Chunk var3 = this.getChunkFromBlockCoords(var1, var2);
        int var4 = 127;
        var1 = var1 & 15;

        for (int var8 = var2 & 15; var4 > 0; --var4) {
            int var5 = var3.getBlockID(var1, var4, var8);
            Material var6 = var5 == 0 ? Material.AIR : Block.BLOCKS_LIST[var5].blockMaterial;
            if (var6.getIsSolid() || var6.getIsLiquid()) {
                return var4 + 1;
            }
        }

        return -1;
    }

    public float getStarBrightness(float var1) {
        float var2 = this.getCelestialAngle(var1);
        float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.75F);
        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        return var3 * var3 * 0.5F;
    }

    public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
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

    public void updateEntities() {
        for (int var1 = 0; var1 < this.weatherEffects.size(); ++var1) {
            Entity var2 = this.weatherEffects.get(var1);
            var2.onUpdate();
            if (var2.isDead) {
                this.weatherEffects.remove(var1--);
            }
        }

        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (int var5 = 0; var5 < this.unloadedEntityList.size(); ++var5) {
            Entity var9 = this.unloadedEntityList.get(var5);
            int var3 = var9.chunkCoordX;
            int var4 = var9.chunkCoordZ;
            if (var9.addedToChunk && this.chunkExists(var3, var4)) {
                this.getChunkFromChunkCoords(var3, var4).removeEntity(var9);
            }
        }

        for (int var6 = 0; var6 < this.unloadedEntityList.size(); ++var6) {
            this.releaseEntitySkin(this.unloadedEntityList.get(var6));
        }

        this.unloadedEntityList.clear();

        for (int var7 = 0; var7 < this.loadedEntityList.size(); ++var7) {
            Entity var10 = this.loadedEntityList.get(var7);
            if (var10.ridingEntity != null) {
                if (!var10.ridingEntity.isDead && var10.ridingEntity.riddenByEntity == var10) {
                    continue;
                }

                var10.ridingEntity.riddenByEntity = null;
                var10.ridingEntity = null;
            }

            if (!var10.isDead) {
                this.updateEntity(var10);
            }

            if (var10.isDead) {
                int var13 = var10.chunkCoordX;
                int var16 = var10.chunkCoordZ;
                if (var10.addedToChunk && this.chunkExists(var13, var16)) {
                    this.getChunkFromChunkCoords(var13, var16).removeEntity(var10);
                }

                this.loadedEntityList.remove(var7--);
                this.releaseEntitySkin(var10);
            }
        }

        this.field_31055_L = true;
        Iterator var8 = this.loadedTileEntityList.iterator();

        while (var8.hasNext()) {
            TileEntity var11 = (TileEntity) var8.next();
            if (!var11.isInvalid()) {
                var11.updateEntity();
            }

            if (var11.isInvalid()) {
                var8.remove();
                Chunk var14 = this.getChunkFromChunkCoords(var11.xCoord >> 4, var11.zCoord >> 4);
                if (var14 != null) {
                    var14.removeChunkBlockTileEntity(var11.xCoord & 15, var11.yCoord, var11.zCoord & 15);
                }
            }
        }

        this.field_31055_L = false;
        if (!this.field_30900_E.isEmpty()) {
            for (TileEntity tileEntity : this.field_30900_E) {
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

            this.field_30900_E.clear();
        }

    }

    public void func_31054_a(Collection var1) {
        if (this.field_31055_L) {
            this.field_30900_E.addAll(var1);
        } else {
            this.loadedTileEntityList.addAll(var1);
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
        List var2 = this.getEntitiesWithinAABBExcludingEntity(null, var1);

        for (int var3 = 0; var3 < var2.size(); ++var3) {
            Entity var4 = (Entity) var2.get(var3);
            if (!var4.isDead && var4.preventEntitySpawning) {
                return false;
            }
        }

        return true;
    }

    public boolean isAnyLiquid(AxisAlignedBB var1) {
        int var2 = MathHelper.floor(var1.minX);
        int var3 = MathHelper.floor(var1.maxX + 1.0D);
        int var4 = MathHelper.floor(var1.minY);
        int var5 = MathHelper.floor(var1.maxY + 1.0D);
        int var6 = MathHelper.floor(var1.minZ);
        int var7 = MathHelper.floor(var1.maxZ + 1.0D);
        if (var1.minX < 0.0D) {
            --var2;
        }

        if (var1.minY < 0.0D) {
            --var4;
        }

        if (var1.minZ < 0.0D) {
            --var6;
        }

        for (int var8 = var2; var8 < var3; ++var8) {
            for (int var9 = var4; var9 < var5; ++var9) {
                for (int var10 = var6; var10 < var7; ++var10) {
                    Block var11 = Block.BLOCKS_LIST[this.getBlockId(var8, var9, var10)];
                    if (var11 != null && var11.blockMaterial.getIsLiquid()) {
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
                        double var16 = (float) (var13 + 1) - BlockFluid.getPercentAir(this.getBlockMetadata(var12, var13, var14));
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

    public Explosion createExplosion(Entity var1, double var2, double var4, double var6, float var8) {
        return this.newExplosion(var1, var2, var4, var6, var8, false);
    }

    public Explosion newExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9) {
        Explosion var10 = new Explosion(this, var1, var2, var4, var6, var8);
        var10.isFlaming = var9;
        var10.doExplosionA();
        var10.doExplosionB(true);
        return var10;
    }

    public float func_675_a(Vec3D var1, AxisAlignedBB var2) {
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
                    if (this.rayTraceBlocks(Vec3D.createVector(var14, var16, var18), var1) == null) {
                        ++var9;
                    }

                    ++var10;
                }
            }
        }

        return (float) var9 / (float) var10;
    }

    public void onBlockHit(EntityPlayer var1, int var2, int var3, int var4, int var5) {
        if (var5 == 0) {
            --var3;
        }

        if (var5 == 1) {
            ++var3;
        }

        if (var5 == 2) {
            --var4;
        }

        if (var5 == 3) {
            ++var4;
        }

        if (var5 == 4) {
            --var2;
        }

        if (var5 == 5) {
            ++var2;
        }

        if (this.getBlockId(var2, var3, var4) == Block.FIRE.blockID) {
            this.func_28107_a(var1, 1004, var2, var3, var4, 0);
            this.setBlockWithNotify(var2, var3, var4, 0);
        }

    }

    public Entity func_4085_a(Class var1) {
        return null;
    }

    public String func_687_d() {
        return "All: " + this.loadedEntityList.size();
    }

    public String func_21119_g() {
        return this.chunkProvider.makeString();
    }

    public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
        Chunk var4 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
        return var4 != null ? var4.getChunkBlockTileEntity(var1 & 15, var2, var3 & 15) : null;
    }

    public void setBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
        if (!var4.isInvalid()) {
            if (this.field_31055_L) {
                var4.xCoord = var1;
                var4.yCoord = var2;
                var4.zCoord = var3;
                this.field_30900_E.add(var4);
            } else {
                this.loadedTileEntityList.add(var4);
                Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
                if (var5 != null) {
                    var5.setChunkBlockTileEntity(var1 & 15, var2, var3 & 15, var4);
                }
            }
        }

    }

    public void removeBlockTileEntity(int var1, int var2, int var3) {
        TileEntity tileEntity = this.getBlockTileEntity(var1, var2, var3);
        if (tileEntity != null && this.field_31055_L) {
            tileEntity.invalidate();
        } else {
            if (tileEntity != null) {
                this.loadedTileEntityList.remove(tileEntity);
            }

            Chunk chunk = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
            if (chunk != null) {
                chunk.removeChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
            }
        }

    }

    public boolean isBlockOpaqueCube(int var1, int var2, int var3) {
        Block var4 = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
        return var4 != null && var4.isOpaqueCube();
    }

    public boolean isBlockNormalCube(int var1, int var2, int var3) {
        Block var4 = Block.BLOCKS_LIST[this.getBlockId(var1, var2, var3)];
        if (var4 == null) {
            return false;
        } else {
            return var4.blockMaterial.getIsTranslucent() && var4.renderAsNormalBlock();
        }
    }

    public void saveWorldIndirectly(IProgressUpdatable var1) {
        this.saveWorld(true, var1);
    }

    public boolean updatingLighting() {
        if (this.lightingUpdatesCounter >= 50) {
            return false;
        } else {
            ++this.lightingUpdatesCounter;

            try {
                int var1 = 500;

                while (this.lightingToUpdate.size() > 0) {
                    --var1;
                    if (var1 <= 0) {
                        boolean var2 = true;
                        return var2;
                    }

                    this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1).func_4127_a(this);
                }

                boolean var6 = false;
                return var6;
            } finally {
                --this.lightingUpdatesCounter;
            }
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

            for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
                this.worldAccesses.get(var5).updateAllRenderers();
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
        if (!this.worldProvider.hasNoSky) {
            if (this.field_27168_F > 0) {
                --this.field_27168_F;
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
                for (int z = -radius; z <= radius; ++z) {
                    this.activeChunkSet.add(new ChunkCoordIntPair(x + chunkX, z + chunkZ));
                }
            }
        }

        if (this.soundCounter > 0) {
            --this.soundCounter;
        }

        for (ChunkCoordIntPair var13 : this.activeChunkSet) {
            int var14 = var13.chunkXPos * 16;
            int var15 = var13.chunkZPos * 16;
            Chunk var16 = this.getChunkFromChunkCoords(var13.chunkXPos, var13.chunkZPos);
            if (this.soundCounter == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var17 = this.distHashCounter >> 2;
                int var21 = var17 & 15;
                int var8 = var17 >> 8 & 15;
                int var9 = var17 >> 16 & 127;
                int var10 = var16.getBlockID(var21, var9, var8);
                var21 = var21 + var14;
                var8 = var8 + var15;
                if (var10 == 0 && this.getFullBlockLightValue(var21, var9, var8) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.SKY, var21, var9, var8) <= 0) {
                    EntityPlayer var11 = this.getClosestPlayer((double) var21 + 0.5D, (double) var9 + 0.5D, (double) var8 + 0.5D, 8.0D);
                    if (var11 != null && var11.getDistanceSq((double) var21 + 0.5D, (double) var9 + 0.5D, (double) var8 + 0.5D) > 4.0D) {
                        this.playSoundEffect((double) var21 + 0.5D, (double) var9 + 0.5D, (double) var8 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
                        this.soundCounter = this.rand.nextInt(12000) + 6000;
                    }
                }
            }

            if (this.rand.nextInt(100000) == 0 && this.func_27161_C() && this.func_27160_B()) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var18 = this.distHashCounter >> 2;
                int var23 = var14 + (var18 & 15);
                int var27 = var15 + (var18 >> 8 & 15);
                int var30 = this.findTopSolidBlock(var23, var27);
                if (this.canBlockBeRainedOn(var23, var30, var27)) {
                    this.addWeatherEffect(new EntityLightningBolt(this, var23, var30, var27));
                    this.field_27168_F = 2;
                }
            }

            if (this.rand.nextInt(16) == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var19 = this.distHashCounter >> 2;
                int var24 = var19 & 15;
                int var28 = var19 >> 8 & 15;
                int var31 = this.findTopSolidBlock(var24 + var14, var28 + var15);
                if (this.getWorldChunkManager().getBiomeGenAt(var24 + var14, var28 + var15).getEnableSnow() && var31 >= 0 && var31 < 128 && var16.getSavedLightValue(EnumSkyBlock.BLOCK, var24, var31, var28) < 10) {
                    int var33 = var16.getBlockID(var24, var31 - 1, var28);
                    int var35 = var16.getBlockID(var24, var31, var28);
                    if (this.func_27161_C() && var35 == 0 && Block.SNOW.canPlaceBlockAt(this, var24 + var14, var31, var28 + var15) && var33 != 0 && var33 != Block.ICE.blockID && Block.BLOCKS_LIST[var33].blockMaterial.getIsSolid()) {
                        this.setBlockWithNotify(var24 + var14, var31, var28 + var15, Block.SNOW.blockID);
                    }

                    if (var33 == Block.WATER_STILL.blockID && var16.getBlockMetadata(var24, var31 - 1, var28) == 0) {
                        this.setBlockWithNotify(var24 + var14, var31 - 1, var28 + var15, Block.ICE.blockID);
                    }
                }
            }

            for (int var20 = 0; var20 < 80; ++var20) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var25 = this.distHashCounter >> 2;
                int var29 = var25 & 15;
                int var32 = var25 >> 8 & 15;
                int var34 = var25 >> 16 & 127;
                int var36 = var16.blocks[var29 << 11 | var32 << 7 | var34] & 255;
                if (Block.TICK_ON_LOAD[var36]) {
                    Block.BLOCKS_LIST[var36].updateTick(this, var29 + var14, var34, var32 + var15, this.rand);
                }
            }
        }

    }

    public boolean TickUpdates(boolean var1) {
        int var2 = this.scheduledTickTreeSet.size();
        if (var2 != this.scheduledTickSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }

        if (var2 > 1000) {
            var2 = 1000;
        }

        for (int var3 = 0; var3 < var2; ++var3) {
            NextTickListEntry var4 = this.scheduledTickTreeSet.first();
            if (!var1 && var4.scheduledTime > this.worldInfo.getWorldTime()) {
                break;
            }

            this.scheduledTickTreeSet.remove(var4);
            this.scheduledTickSet.remove(var4);
            byte var5 = 8;
            if (this.checkChunksExist(var4.xCoord - var5, var4.yCoord - var5, var4.zCoord - var5, var4.xCoord + var5, var4.yCoord + var5, var4.zCoord + var5)) {
                int var6 = this.getBlockId(var4.xCoord, var4.yCoord, var4.zCoord);
                if (var6 == var4.blockID && var6 > 0) {
                    Block.BLOCKS_LIST[var6].updateTick(this, var4.xCoord, var4.yCoord, var4.zCoord, this.rand);
                }
            }
        }

        return this.scheduledTickTreeSet.size() != 0;
    }

    public void randomDisplayUpdates(int var1, int var2, int var3) {
        byte var4 = 16;
        Random var5 = new Random();

        for (int var6 = 0; var6 < 1000; ++var6) {
            int var7 = var1 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            int var8 = var2 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            int var9 = var3 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            int var10 = this.getBlockId(var7, var8, var9);
            if (var10 > 0) {
                Block.BLOCKS_LIST[var10].randomDisplayTick(this, var7, var8, var9, var5);
            }
        }

    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB var2) {
        this.entities.clear();
        int var3 = MathHelper.floor((var2.minX - 2.0D) / 16.0D);
        int var4 = MathHelper.floor((var2.maxX + 2.0D) / 16.0D);
        int var5 = MathHelper.floor((var2.minZ - 2.0D) / 16.0D);
        int var6 = MathHelper.floor((var2.maxZ + 2.0D) / 16.0D);

        for (int var7 = var3; var7 <= var4; ++var7) {
            for (int var8 = var5; var8 <= var6; ++var8) {
                if (this.chunkExists(var7, var8)) {
                    this.getChunkFromChunkCoords(var7, var8).getEntitiesWithinAABBForEntity(entity, var2, this.entities);
                }
            }
        }

        return this.entities;
    }

    public ArrayList<Entity> getEntitiesWithinAABB(Class var1, AxisAlignedBB var2) {
        int var3 = MathHelper.floor((var2.minX - 2.0D) / 16.0D);
        int var4 = MathHelper.floor((var2.maxX + 2.0D) / 16.0D);
        int var5 = MathHelper.floor((var2.minZ - 2.0D) / 16.0D);
        int var6 = MathHelper.floor((var2.maxZ + 2.0D) / 16.0D);
        ArrayList<Entity> var7 = new ArrayList<>();

        for (int var8 = var3; var8 <= var4; ++var8) {
            for (int var9 = var5; var9 <= var6; ++var9) {
                if (this.chunkExists(var8, var9)) {
                    this.getChunkFromChunkCoords(var8, var9).getEntitiesOfTypeWithinAAAB(var1, var2, var7);
                }
            }
        }

        return var7;
    }

    public List getLoadedEntityList() {
        return this.loadedEntityList;
    }

    public void updateTileEntityChunkAndDoNothing(int var1, int var2, int var3, TileEntity var4) {
        if (this.blockExists(var1, var2, var3)) {
            this.getChunkFromBlockCoords(var1, var3).setChunkModified();
        }

        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.doNothingWithTileEntity(var1, var2, var3, var4);
        }
    }

    public int countEntities(Class clazz) {
        int count = 0;

        for (Entity entity : this.loadedEntityList) {
            if (clazz.isAssignableFrom(entity.getClass())) {
                ++count;
            }
        }

        return count;
    }

    public void addLoadedEntities(List<Entity> var1) {
        this.loadedEntityList.addAll(var1);

        for (Entity entity : var1) {
            this.obtainEntitySkin(entity);
        }
    }

    public void addUnloadedEntities(List<Entity> entities) {
        this.unloadedEntityList.addAll(entities);
    }

    public void func_656_j() {
        while (this.chunkProvider.unload100OldestChunks()) {
        }

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

    public boolean isBlockGettingPowered(int var1, int var2, int var3) {
        if (this.isBlockProvidingPowerTo(var1, var2 - 1, var3, 0)) {
            return true;
        } else if (this.isBlockProvidingPowerTo(var1, var2 + 1, var3, 1)) {
            return true;
        } else if (this.isBlockProvidingPowerTo(var1, var2, var3 - 1, 2)) {
            return true;
        } else if (this.isBlockProvidingPowerTo(var1, var2, var3 + 1, 3)) {
            return true;
        } else if (this.isBlockProvidingPowerTo(var1 - 1, var2, var3, 4)) {
            return true;
        } else {
            return this.isBlockProvidingPowerTo(var1 + 1, var2, var3, 5);
        }
    }

    public boolean isBlockIndirectlyProvidingPowerTo(int var1, int var2, int var3, int var4) {
        if (this.isBlockNormalCube(var1, var2, var3)) {
            return this.isBlockGettingPowered(var1, var2, var3);
        } else {
            int var5 = this.getBlockId(var1, var2, var3);
            return var5 != 0 && Block.BLOCKS_LIST[var5].isPoweringTo(this, var1, var2, var3, var4);
        }
    }

    public boolean isBlockIndirectlyGettingPowered(int var1, int var2, int var3) {
        if (this.isBlockIndirectlyProvidingPowerTo(var1, var2 - 1, var3, 0)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(var1, var2 + 1, var3, 1)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(var1, var2, var3 - 1, 2)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(var1, var2, var3 + 1, 3)) {
            return true;
        } else if (this.isBlockIndirectlyProvidingPowerTo(var1 - 1, var2, var3, 4)) {
            return true;
        } else {
            return this.isBlockIndirectlyProvidingPowerTo(var1 + 1, var2, var3, 5);
        }
    }

    public EntityPlayer getClosestPlayerToEntity(Entity var1, double var2) {
        return this.getClosestPlayer(var1.posX, var1.posY, var1.posZ, var2);
    }

    public EntityPlayer getClosestPlayer(double var1, double var3, double var5, double var7) {
        double var9 = -1.0D;
        EntityPlayer var11 = null;

        for (EntityPlayer entityPlayer : this.playerEntities) {
            double var14 = entityPlayer.getDistanceSq(var1, var3, var5);
            if ((var7 < 0.0D || var14 < var7 * var7) && (var9 == -1.0D || var14 < var9)) {
                var9 = var14;
                var11 = entityPlayer;
            }
        }

        return var11;
    }

    public EntityPlayer getPlayerEntityByName(String name) {
        for (EntityPlayer playerEntity : this.playerEntities) {
            if (name.equals(playerEntity.username)) {
                return playerEntity;
            }
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

    public void joinEntityInSurroundings(Entity var1) {
        int var2 = MathHelper.floor(var1.posX / 16.0D);
        int var3 = MathHelper.floor(var1.posZ / 16.0D);
        byte var4 = 2;

        for (int var5 = var2 - var4; var5 <= var2 + var4; ++var5) {
            for (int var6 = var3 - var4; var6 <= var3 + var4; ++var6) {
                this.getChunkFromChunkCoords(var5, var6);
            }
        }

        if (!this.loadedEntityList.contains(var1)) {
            this.loadedEntityList.add(var1);
        }

    }

    public boolean canMineBlock(EntityPlayer var1, int var2, int var3, int var4) {
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
            Entity var7 = this.loadedEntityList.get(var6);
            if (var7.ridingEntity != null) {
                if (!var7.ridingEntity.isDead && var7.ridingEntity.riddenByEntity == var7) {
                    continue;
                }

                var7.ridingEntity.riddenByEntity = null;
                var7.ridingEntity = null;
            }

            if (var7.isDead) {
                int var8 = var7.chunkCoordX;
                int var9 = var7.chunkCoordZ;
                if (var7.addedToChunk && this.chunkExists(var8, var9)) {
                    this.getChunkFromChunkCoords(var8, var9).removeEntity(var7);
                }

                this.loadedEntityList.remove(var6--);
                this.releaseEntitySkin(var7);
            }
        }

    }

    public IChunkProvider getIChunkProvider() {
        return this.chunkProvider;
    }

    public void playNoteAt(int var1, int var2, int var3, int var4, int var5) {
        int var6 = this.getBlockId(var1, var2, var3);
        if (var6 > 0) {
            Block.BLOCKS_LIST[var6].playBlock(this, var1, var2, var3, var4, var5);
        }

    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = !this.playerEntities.isEmpty();

        for (EntityPlayer var2 : this.playerEntities) {
            if (!var2.isPlayerSleeping()) {
                this.allPlayersSleeping = false;
                break;
            }
        }

    }

    protected void wakeUpAllPlayers() {
        this.allPlayersSleeping = false;

        for (EntityPlayer var2 : this.playerEntities) {
            if (var2.isPlayerSleeping()) {
                var2.wakeUpPlayer(false, false, true);
            }
        }

        this.stopPrecipitation();
    }

    public boolean isAllPlayersFullyAsleep() {
        if (this.allPlayersSleeping && !this.multiplayerWorld) {
            for (EntityPlayer var2 : this.playerEntities) {
                if (!var2.isPlayerFullyAsleep()) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public float func_27166_f(float var1) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * var1) * this.func_27162_g(var1);
    }

    public float func_27162_g(float var1) {
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
        return (double) this.func_27162_g(1.0F) > 0.2D;
    }

    public boolean canBlockBeRainedOn(int var1, int var2, int var3) {
        if (!this.func_27161_C()) {
            return false;
        } else if (!this.canBlockSeeTheSky(var1, var2, var3)) {
            return false;
        } else if (this.findTopSolidBlock(var1, var3) > var2) {
            return false;
        } else {
            BiomeGenBase var4 = this.getWorldChunkManager().getBiomeGenAt(var1, var3);
            return !var4.getEnableSnow() && var4.canSpawnLightningBolt();
        }
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
        this.func_28107_a(null, var1, var2, var3, var4, var5);
    }

    public void func_28107_a(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.func_28136_a(var1, var2, var3, var4, var5, var6);
        }
    }
}
