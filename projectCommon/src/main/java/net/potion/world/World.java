package net.potion.world;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.hypnosis.util.math.MathConstants;
import net.hypnosis.util.math.MathHelper;
import net.hypnosis.util.math.Vec3d;
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
import net.potion.world.gen.biome.BiomeGenBase;
import net.potion.world.storage.ISaveHandler;
import net.potion.world.storage.MapStorage;

import java.util.*;

public class World implements IBlockAccess {
    static int lightingUpdatesScheduled = 0;
    public final WorldProvider worldProvider;
    protected final ISaveHandler saveHandler;
    public boolean scheduledUpdatesAreImmediate = false;
    public List<Entity> loadedEntityList = new ArrayList<>();
    public List<TileEntity> loadedTileEntityList = new ArrayList<>();
    public List<EntityPlayer> playerEntities = new ArrayList<>();
    public List<Entity> weatherEffects = new ArrayList<>();
    public int skylightSubtracted = 0;
    public int field1 = 0;
    public boolean editingBlocks = false;
    public int difficultySetting;
    public Random rand = new Random();
    public boolean isNewWorld;
    public boolean findingSpawnPoint;
    public MapStorage mapStorage;
    public boolean localWorld = false;
    protected int distHashCounter = new Random().nextInt();
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    protected int field2 = 0;
    protected int autosavePeriod = 40;
    protected List<IWorldAccess> worldAccesses = new ArrayList<>();
    protected IChunkProvider chunkProvider;
    protected WorldInfo worldInfo;
    private final List<MetadataChunkBlock> lightingToUpdate = new ArrayList<>();
    private final List<Entity> unloadedEntityList = new ArrayList<>();
    private final TreeSet<NextTickListEntry> scheduledTickTreeSet = new TreeSet<>();
    private final Set<NextTickListEntry> scheduledTickSet = new HashSet<>();
    private final List<TileEntity> tileEntities = new ArrayList<>();
    private long lockTimestamp;
    private boolean allPlayersSleeping;
    private final ArrayList<AxisAlignedBB> collidingBoundingBoxes = new ArrayList<>();
    private boolean updatingTileEntities;
    private int lightingUpdatesCounter = 0;
    private boolean spawnHostileMobs = true;
    private boolean spawnPeacefulMobs = true;
    private final Set<ChunkCoordIntPair> activeChunkSet = new HashSet<>();
    private int ambientTickCountdown = this.rand.nextInt(12000);
    private final List<Entity> entities = new ArrayList<>();

    public World(ISaveHandler saveHandler, String levelName, WorldProvider worldProvider, long randomSeed) {
        this.lockTimestamp = System.currentTimeMillis();
        this.isNewWorld = false;
        this.saveHandler = saveHandler;
        this.worldInfo = new WorldInfo(randomSeed, levelName);
        this.worldProvider = worldProvider;
        this.mapStorage = new MapStorage(saveHandler);
        worldProvider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.calculateInitialSkylight();
        this.resetWeather();
    }

    public World(World world, WorldProvider worldProvider) {
        this.lockTimestamp = System.currentTimeMillis();
        this.isNewWorld = false;
        this.lockTimestamp = world.lockTimestamp;
        this.saveHandler = world.saveHandler;
        this.worldInfo = new WorldInfo(world.worldInfo);
        this.mapStorage = new MapStorage(this.saveHandler);
        this.worldProvider = worldProvider;
        worldProvider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.calculateInitialSkylight();
        this.resetWeather();
    }

    public World(ISaveHandler saveHandler, String levelName, long randomSeed) {
        this(saveHandler, levelName, randomSeed, null);
    }

    public World(ISaveHandler saveHandler, String levelName, long randomSeed, WorldProvider worldProvider) {
        this.lockTimestamp = System.currentTimeMillis();
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
            this.worldInfo.setLevelName(levelName);
        }

        this.worldProvider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        if (isNew) {
            this.generateSpawnPoint();
        }

        this.calculateInitialSkylight();
        this.resetWeather();
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.worldChunkMgr;
    }

    protected IChunkProvider createChunkProvider() {
        IChunkLoader chunkLoader = this.saveHandler.getChunkLoader(this.worldProvider);
        return new ChunkProvider(this, chunkLoader, this.worldProvider.getChunkProvider());
    }

    protected void generateSpawnPoint() {
        this.findingSpawnPoint = true;

        int x = 0;
        byte y = 64;
        int z = 0;

        while (!this.worldProvider.canCoordinateBeSpawn(x, z)) {
            x += this.rand.nextInt(64) - this.rand.nextInt(64);
            z += this.rand.nextInt(64) - this.rand.nextInt(64);
        }

        this.worldInfo.setSpawn(x, y, z);
        this.findingSpawnPoint = false;
    }

    @Side(CodeSide.CLIENT)
    public void setSpawnLocation() {
        if (this.worldInfo.getSpawnY() <= 0) {
            this.worldInfo.setSpawnY(64);
        }

        int x = this.worldInfo.getSpawnX();
        int z = this.worldInfo.getSpawnZ();
        while (this.getFirstUncoveredBlock(x, z) == 0) {
            x += this.rand.nextInt(8) - this.rand.nextInt(8);
            z += this.rand.nextInt(8) - this.rand.nextInt(8);
        }
        this.worldInfo.setSpawnX(x);
        this.worldInfo.setSpawnZ(z);
    }

    public int getFirstUncoveredBlock(int x, int z) {
        int y = 63;
        while (!this.isAirBlock(x, y + 1, z)) {
            ++y;
        }

        return this.getBlockId(x, y, z);
    }

    @Side(CodeSide.CLIENT)
    public void emptyMethod1() {
    }

    @Side(CodeSide.CLIENT)
    public void spawnPlayerWithLoadedChunks(EntityPlayer player) {
        try {
            TagCompound playerCompound = this.worldInfo.getPlayerNBTTagCompound();
            if (playerCompound != null) {
                player.readFromNBT(playerCompound);
                this.worldInfo.setPlayerNBTTagCompound(null);
            }

            if (this.chunkProvider instanceof ChunkProviderLoadOrGenerate chunkProvider) {
                int x = MathHelper.floor(player.posX) >> 4;
                int z = MathHelper.floor(player.posZ) >> 4;
                chunkProvider.setCurrentChunkOver(x, z);
            }

            this.entityJoinedWorld(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveWorld(boolean forceSave, IProgressUpdatable updatable) {
        if (this.chunkProvider.canSave()) {
            if (updatable != null) {
                updatable.display("Saving level");
            }

            this.saveLevel();
            if (updatable != null) {
                updatable.displayLoadingString("Saving chunks");
            }

            this.chunkProvider.saveChunks(forceSave, updatable);
        }
    }

    private void saveLevel() {
        this.checkSessionLock();
        this.saveHandler.saveWorldInfoAndPlayer(this.worldInfo, this.playerEntities);
        this.mapStorage.saveAllData();
    }

    @Side(CodeSide.CLIENT)
    public boolean save(int saveTime) {
        if (!this.chunkProvider.canSave()) {
            return true;
        }

        if (saveTime == 0) {
            this.saveLevel();
        }

        return this.chunkProvider.saveChunks(false, null);
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        if (x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            if (y < 0) {
                return 0;
            }

            return y >= 128 ? 0 : this.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockID(x & 15, y, z & 15);
        }

        return 0;
    }

    public boolean isAirBlock(int x, int y, int z) {
        return this.getBlockId(x, y, z) == 0;
    }

    public boolean blockExists(int x, int y, int z) {
        return (y >= 0 && y < 128) && this.chunkExists(x >> 4, z >> 4);
    }

    public boolean doChunksNearChunkExist(int x, int y, int z, int r) {
        return this.checkChunksExist(x - r, y - r, z - r, x + r, y + r, z + r);
    }

    public boolean checkChunksExist(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (maxY >= 0 && minY < 128) {
            minX = minX >> 4;
            minY = minY >> 4;
            minZ = minZ >> 4;
            maxX = maxX >> 4;
            maxY = maxY >> 4;
            maxZ = maxZ >> 4;

            for (int var7 = minX; var7 <= maxX; ++var7) {
                for (int var8 = minZ; var8 <= maxZ; ++var8) {
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

    public Chunk getChunkFromBlockCoords(int x, int z) {
        return this.getChunkFromChunkCoords(x >> 4, z >> 4);
    }

    public Chunk getChunkFromChunkCoords(int x, int z) {
        return this.chunkProvider.provideChunk(x, z);
    }

    public boolean setBlockAndMetadata(int x, int y, int z, int blockId, int metadata) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return false;
        }

        if (y < 0) {
            return false;
        }

        if (y >= 128) {
            return false;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk.setBlockIDWithMetadata(x & 15, y, z & 15, blockId, metadata);
    }

    public boolean setBlock(int x, int y, int z, int blockId) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return false;
        }

        if (y < 0) {
            return false;
        }

        if (y >= 128) {
            return false;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk.setBlockID(x & 15, y, z & 15, blockId);

    }

    @Override
    public Material getBlockMaterial(int x, int y, int z) {
        int blockId = this.getBlockId(x, y, z);
        return blockId == 0 ? Material.AIR : Block.BLOCKS_LIST[blockId].blockMaterial;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return 0;
        }

        if (y < 0) {
            return 0;
        }

        if (y >= 128) {
            return 0;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        x = x & 15;
        z = z & 15;
        return chunk.getBlockMetadata(x, y, z);
    }

    public void setBlockMetadataWithNotify(int x, int y, int z, int metadata) {
        if (!this.setBlockMetadata(x, y, z, metadata)) {
            return;
        }

        int blockId = this.getBlockId(x, y, z);
        if (Block.REQUIRES_SELF_NOTIFY[blockId & 255]) {
            this.notifyBlockChange(x, y, z, blockId);
        } else {
            this.notifyBlocksOfNeighborChange(x, y, z, blockId);
        }
    }

    public boolean setBlockMetadata(int x, int y, int z, int metadata) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return false;
        }

        if (y < 0) {
            return false;
        }

        if (y >= 128) {
            return false;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        x = x & 15;
        z = z & 15;
        chunk.setBlockMetadata(x, y, z, metadata);
        return true;

    }

    public boolean setBlockWithNotify(int x, int y, int z, int blockId) {
        if (this.setBlock(x, y, z, blockId)) {
            this.notifyBlockChange(x, y, z, blockId);
            return true;
        }

        return false;
    }

    public boolean setBlockAndMetadataWithNotify(int x, int y, int z, int blockId, int metadata) {
        if (this.setBlockAndMetadata(x, y, z, blockId, metadata)) {
            this.notifyBlockChange(x, y, z, blockId);
            return true;
        }

        return false;
    }

    public void markBlockNeedsUpdate(int x, int y, int z) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockAndNeighborsNeedsUpdate(x, y, z);
        }
    }

    protected void notifyBlockChange(int x, int y, int z, int blockId) {
        this.markBlockNeedsUpdate(x, y, z);
        this.notifyBlocksOfNeighborChange(x, y, z, blockId);
    }

    public void markBlocksDirtyVertical(int x, int z, int minY, int maxY) {
        if (minY > maxY) {
            int tmp = maxY;
            maxY = minY;
            minY = tmp;
        }

        this.markBlocksDirty(x, minY, z, x, maxY, z);
    }

    public void markBlockAsNeedsUpdate(int x, int y, int z) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockRangeNeedsUpdate(x, y, z, x, y, z);
        }
    }

    public void markBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockRangeNeedsUpdate(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    public void notifyBlocksOfNeighborChange(int x, int y, int z, int blockId) {
        this.notifyBlockOfNeighborChange(x - 1, y, z, blockId);
        this.notifyBlockOfNeighborChange(x + 1, y, z, blockId);
        this.notifyBlockOfNeighborChange(x, y - 1, z, blockId);
        this.notifyBlockOfNeighborChange(x, y + 1, z, blockId);
        this.notifyBlockOfNeighborChange(x, y, z - 1, blockId);
        this.notifyBlockOfNeighborChange(x, y, z + 1, blockId);
    }

    private void notifyBlockOfNeighborChange(int x, int y, int z, int blockId) {
        if (!this.editingBlocks && !this.localWorld) {
            Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
            if (block != null) {
                block.onNeighborBlockChange(this, x, y, z, blockId);
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
        return this.getBlockLightValueExt(x, y, z, true);
    }

    public int getBlockLightValueExt(int x, int y, int z, boolean lookNearMax) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return 15;
        }

        if (lookNearMax) {
            int blockId = this.getBlockId(x, y, z);
            if (blockId == Block.STAIR_SINGLE.blockID
                    || blockId == Block.FARMLAND.blockID
                    || blockId == Block.STAIR_COMPACT_COBBLESTONE.blockID
                    || blockId == Block.STAIR_COMPACT_PLANKS.blockID) {
                int lightUp = this.getBlockLightValueExt(x, y + 1, z, false);
                int lightXp = this.getBlockLightValueExt(x + 1, y, z, false);
                int lightXm = this.getBlockLightValueExt(x - 1, y, z, false);
                int lightZp = this.getBlockLightValueExt(x, y, z + 1, false);
                int lightZm = this.getBlockLightValueExt(x, y, z - 1, false);
                if (lightXp > lightUp) {
                    lightUp = lightXp;
                }

                if (lightXm > lightUp) {
                    lightUp = lightXm;
                }

                if (lightZp > lightUp) {
                    lightUp = lightZp;
                }

                if (lightZm > lightUp) {
                    lightUp = lightZm;
                }

                return lightUp;
            }
        }

        if (y < 0) {
            return 0;
        }

        if (y >= 128) {
            y = 127;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        x = x & 15;
        z = z & 15;
        return chunk.getBlockLightValue(x, y, z, this.skylightSubtracted);

    }

    public boolean canExistingBlockSeeTheSky(int x, int y, int z) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return false;
        }

        if (y < 0) {
            return false;
        }

        if (y >= 128) {
            return true;
        }

        if (!this.chunkExists(x >> 4, z >> 4)) {
            return false;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        x = x & 15;
        z = z & 15;
        return chunk.canBlockSeeTheSky(x, y, z);

    }

    public int getHeightValue(int x, int z) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return 0;
        }

        if (!this.chunkExists(x >> 4, z >> 4)) {
            return 0;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk.getHeightValue(x & 15, z & 15);

    }

    public void neighborLightPropagationChanged(EnumSkyBlock skyBlock, int x, int y, int z, int var5) {
        if (this.worldProvider.hasNoSky && skyBlock == EnumSkyBlock.SKY) {
            return;
        }

        if (!this.blockExists(x, y, z)) {
            return;
        }

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

    public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
        if (y < 0) {
            y = 0;
        }

        if (y >= 128) {
            y = 127;
        }

        if (y >= 0 && y < 128 && x >= -32000000 && z >= -32000000 && x < 32000000 && z <= 32000000) {
            int cX = x >> 4;
            int cZ = z >> 4;
            if (!this.chunkExists(cX, cZ)) {
                return 0;
            }

            Chunk chunk = this.getChunkFromChunkCoords(cX, cZ);
            return chunk.getSavedLightValue(skyBlock, x & 15, y, z & 15);
        }

        return skyBlock.lightValue;
    }

    public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int value) {
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return;
        }

        if (y < 0) {
            return;
        }

        if (y >= 128) {
            return;
        }

        if (!this.chunkExists(x >> 4, z >> 4)) {
            return;
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        chunk.setLightValue(skyBlock, x & 15, y, z & 15, value);

        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.markBlockAndNeighborsNeedsUpdate(x, y, z);
        }

    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getBrightness(int x, int y, int z, int minValue) {
        int lightValue = this.getBlockLightValue(x, y, z);
        if (lightValue < minValue) {
            lightValue = minValue;
        }

        return this.worldProvider.lightBrightnessTable[lightValue];
    }

    @Override
    @Side(CodeSide.CLIENT)
    public float getLightBrightness(int x, int y, int z) {
        return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(x, y, z)];
    }

    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }

    public MovingObjectPosition rayTraceBlocks(Vec3d var1, Vec3d var2) {
        return this.rayTraceBlocks(var1, var2, false, false);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3d var1, Vec3d var2, boolean var3) {
        return this.rayTraceBlocks(var1, var2, var3, false);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3d vec1, Vec3d vec2, boolean paramBoolean, boolean paramBoolean2) {
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


            double x = vec1.x;
            double y = vec1.y;
            double z = vec1.z;

            double difX = vec2.x - x;
            double difY = vec2.y - y;
            double difZ = vec2.z - z;

            if (blockMetadata != 0)
                var21 = (d1 - x) / difX;

            if (i4 != 0)
                var23 = (d2 - y) / difY;

            if (i5 != 0)
                var25 = (d3 - z) / difZ;


            byte var33 = 0;

            if (var21 < var23 && var21 < var25) {
                if (x2 > x1) {
                    var33 = 4;
                } else var33 = 5;


                x = d1;
                y += difY * var21;
                z += difZ * var21;
            } else if (var23 < var25) {
                if (y2 > y1) {
                    var33 = 0;
                } else var33 = 1;


                x += difX * var23;
                y = d2;
                z += difZ * var23;
            } else {
                if (z2 > z1) {
                    var33 = 2;
                } else var33 = 3;


                x += difX * var25;
                y += difY * var25;
                z = d3;
            }

            x1 = MathHelper.floor(x);
            if (var33 == 5) {
                --x1;
            }

            y1 = MathHelper.floor(y);
            if (var33 == 1) {
                --y1;
            }

            z1 = MathHelper.floor(z);
            if (var33 == 3) {
                --z1;
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
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.playSound(soundName, x, y, z, volume, pitch);
        }
    }

    public void playRecord(String recordName, int x, int y, int z) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.playRecord(recordName, x, y, z);
        }
    }

    public void spawnParticle(String particleName, double x, double y, double z, double var8, double var10, double var12) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.spawnParticle(particleName, x, y, z, var8, var10, var12);
        }
    }

    public boolean addWeatherEffect(Entity entity) {
        this.weatherEffects.add(entity);
        return true;
    }

    public boolean entityJoinedWorld(Entity entity) {
        int chunkX = MathHelper.floor(entity.posX / 16.0D);
        int chunkZ = MathHelper.floor(entity.posZ / 16.0D);
        boolean isPlayer = entity instanceof EntityPlayer;

        if (!isPlayer && !this.chunkExists(chunkX, chunkZ)) {
            return false;
        }

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

    public void removeEntity(Entity entity) {
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

    @Side(CodeSide.SERVER)
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
                if (!this.blockExists(x, 64, z)) {
                    continue;
                }

                for (int y = minY - 1; y < maxY; ++y) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null) {
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

    public int calculateSkylightSubtracted(float partialTicks) {
        float var2 = this.getCelestialAngle(partialTicks);
        float var3 = 1.0F - (MathHelper.cos(var2 * MathConstants.PI * 2.0F) * 2.0F + 0.5F);

        if (var3 < 0.0F)
            var3 = 0.0F;

        if (var3 > 1.0F)
            var3 = 1.0F;

        var3 = 1.0F - var3;
        var3 = (float) ((double) var3 * (1.0D - (double) (this.getRainStrength(partialTicks) * 5.0F) / 16.0D));
        var3 = (float) ((double) var3 * (1.0D - (double) (this.getThunderStrength(partialTicks) * 5.0F) / 16.0D));
        var3 = 1.0F - var3;

        return (int) (var3 * 11.0F);
    }

    @Side(CodeSide.CLIENT)
    public Vec3d getSkyColor(Entity entity, float angle) {
        float celAngle = this.getCelestialAngle(angle);
        float celCos = MathHelper.cos(celAngle * Math.PI * 2.0F) * 2.0F + 0.5F;

        if (celCos < 0.0F) {
            celCos = 0.0F;
        }

        if (celCos > 1.0F) {
            celCos = 1.0F;
        }

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

        float var19 = this.getThunderStrength(angle);
        if (var19 > 0.0F) {
            float var20 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.2F;
            float var15 = 1.0F - var19 * 0.75F;
            r = r * var15 + var20 * (1.0F - var15);
            g = g * var15 + var20 * (1.0F - var15);
            b = b * var15 + var20 * (1.0F - var15);
        }

        if (this.field1 > 0) {
            float var21 = Math.min((float) this.field1 - angle, 1.0F) * 0.45F;
            r *= (1.0F - var21) + 0.8F * var21;
            g *= (1.0F - var21) + 0.8F * var21;
            b *= (1.0F - var21) + 1.0F * var21;
        }

        return new Vec3d(r, g, b);
    }

    public float getCelestialAngle(float partialTicks) {
        return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
    }

    @Side(CodeSide.CLIENT)
    public Vec3d cloudColor(float partialTicks) {
        float celAngle = this.getCelestialAngle(partialTicks);
        float celCos = MathHelper.cos(celAngle * Math.PI * 2.0F) * 2.0F + 0.5F;

        if (celCos < 0.0F)
            celCos = 0.0F;

        if (celCos > 1.0F)
            celCos = 1.0F;

        long cloudColor = 0xFFFFFF;
        float r = (float) (cloudColor >> 16 & 255L) / 255.0F;
        float g = (float) (cloudColor >> 8 & 255L) / 255.0F;
        float b = (float) (cloudColor & 255L) / 255.0F;

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

        float var14 = this.getThunderStrength(partialTicks);
        if (var14 > 0.0F) {
            float var15 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.2F;
            float var10 = 1.0F - var14 * 0.95F;
            r *= var10 + var15 * (1.0F - var10);
            g *= var10 + var15 * (1.0F - var10);
            b *= var10 + var15 * (1.0F - var10);
        }

        return new Vec3d(r, g, b);
    }

    @Side(CodeSide.CLIENT)
    public Vec3d getFogColor(float partialTicks) {
        float celestialAngle = this.getCelestialAngle(partialTicks);
        return this.worldProvider.getFogColor(celestialAngle, partialTicks);
    }

    public int findTopSolidOrLiquidBlock(int x, int z) {
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

    public int findTopSolidBlock(int x, int z) {
        Chunk chunk = this.getChunkFromBlockCoords(x, z);
        int y = 127;
        x = x & 15;

        for (int iZ = z & 15; y > 0; --y) {
            int blockID = chunk.getBlockID(x, y, iZ);
            if (blockID != 0 && Block.BLOCKS_LIST[blockID].blockMaterial.getIsSolid()) {
                return y + 1;
            }
        }

        return -1;
    }

    @Side(CodeSide.CLIENT)
    public float getStarBrightness(float partialTicks) {
        float celAngle = this.getCelestialAngle(partialTicks);
        float celCos = 1.0F - (MathHelper.cos(celAngle * MathConstants.PI * 2.0F) * 2.0F + 0.75F);

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

        this.updatingTileEntities = true;
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

        this.updatingTileEntities = false;
        if (!this.tileEntities.isEmpty()) {
            for (TileEntity tileEntity : this.tileEntities) {
                if (!tileEntity.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileEntity))
                        this.loadedTileEntityList.add(tileEntity);

                    Chunk chunk = this.getChunkFromChunkCoords(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4);
                    if (chunk != null)
                        chunk.setChunkBlockTileEntity(tileEntity.xCoord & 15, tileEntity.yCoord, tileEntity.zCoord & 15, tileEntity);

                    this.markBlockNeedsUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                }
            }

            this.tileEntities.clear();
        }

    }

    public void addTileEntities(Collection<TileEntity> tileEntities) {
        if (this.updatingTileEntities) {
            this.tileEntities.addAll(tileEntities);
        } else  {
            this.loadedTileEntityList.addAll(tileEntities);
        }
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

    public boolean checkIfAABBIsClear(AxisAlignedBB bb) {
        List<Entity> entities = this.getEntitiesWithinAABBExcludingEntity(null, bb);

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            if (!entity.isDead && entity.preventEntitySpawning)
                return false;
        }

        return true;
    }

    public boolean containsBlock(AxisAlignedBB bb) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.floor(bb.maxX + 1.0D);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.floor(bb.maxY + 1.0D);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.floor(bb.maxZ + 1.0D);
        if (bb.minX < 0.0D) {
            --minX;
        }

        if (bb.minY < 0.0D) {
            --minY;
        }

        if (bb.minZ < 0.0D) {
            --minZ;
        }

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean containsLiquid(AxisAlignedBB bb) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.floor(bb.maxX + 1.0D);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.floor(bb.maxY + 1.0D);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.floor(bb.maxZ + 1.0D);

        if (bb.minX < 0.0D)
            --minX;

        if (bb.minY < 0.0D)
            --minY;

        if (bb.minZ < 0.0D)
            --minZ;

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial.isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean containsBurners(AxisAlignedBB bb) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.floor(bb.maxX + 1.0D);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.floor(bb.maxY + 1.0D);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.floor(bb.maxZ + 1.0D);

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

    public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material material, Entity entity) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.floor(bb.maxX + 1.0D);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.floor(bb.maxY + 1.0D);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.floor(bb.maxZ + 1.0D);

        if (!this.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
            return false;

        boolean handled = false;
        Vec3d vec = new Vec3d(0.0D, 0.0D, 0.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial == material) {
                        double percentAir = (float) (y + 1) - BlockFluid.setFluidHeight(this.getBlockMetadata(x, y, z));
                        if ((double) maxY >= percentAir) {
                            handled = true;
                            vec = block.velocityToAddToEntity(this, x, y, z, entity, vec);
                        }
                    }
                }
            }
        }

        if (vec.length() > 0.0D) {
            vec = MathHelper.normalizeOrZero(vec);
            double var19 = 0.014D;
            entity.motionX += vec.x * var19;
            entity.motionY += vec.y * var19;
            entity.motionZ += vec.z * var19;
        }

        return handled;
    }

    public boolean containsMaterial(AxisAlignedBB bb, Material material) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.floor(bb.maxX + 1.0D);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.floor(bb.maxY + 1.0D);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.floor(bb.maxZ + 1.0D);

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

    public boolean isAABBInMaterial(AxisAlignedBB bb, Material material) {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.floor(bb.maxX + 1.0D);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.floor(bb.maxY + 1.0D);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.floor(bb.maxZ + 1.0D);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
                    if (block != null && block.blockMaterial == material) {
                        int metadata = this.getBlockMetadata(x, y, z);
                        double upY = y + 1;
                        if (metadata < 8)
                            upY = (double) (y + 1) - (double) metadata / 8.0D;

                        if (upY >= bb.minY)
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

    public float calculcateExplosionPower(Vec3d source, AxisAlignedBB bb) {
        float mX = (float) (1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D));
        float mY = (float) (1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D));
        float mZ = (float) (1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D));

        int nulls = 0;
        int total = 0;

        for (float x = 0.0F; x <= 1.0F; x = x + mX) {
            for (float y = 0.0F; y <= 1.0F; y = y + mY) {
                for (float z = 0.0F; z <= 1.0F; z = z + mZ) {
                    double rX = bb.minX + (bb.maxX - bb.minX) * (double) x;
                    double rY = bb.minY + (bb.maxY - bb.minY) * (double) y;
                    double rZ = bb.minZ + (bb.maxZ - bb.minZ) * (double) z;
                    if (this.rayTraceBlocks(new Vec3d(rX, rY, rZ), source) == null) {
                        ++nulls;
                    }

                    ++total;
                }
            }
        }

        return (float) nulls / (float) total;
    }

    public void onBlockHit(EntityPlayer player, int x, int y, int z, int var5) {
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
            this.playEffects(player, 1004, x, y, z, 0);
            this.setBlockWithNotify(x, y, z, 0);
        }

    }

    @Side(CodeSide.CLIENT)
    public Entity queryEntity(Class var1) {
        return null;
    }

    @Side(CodeSide.CLIENT)
    public String entitiesStatistic() {
        return "All: " + this.loadedEntityList.size();
    }

    @Side(CodeSide.CLIENT)
    public String chunkStatistic() {
        return this.chunkProvider.makeString();
    }

    @Override
    public TileEntity getBlockTileEntity(int x, int y, int z) {
        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return chunk != null ? chunk.getChunkBlockTileEntity(x & 15, y, z & 15) : null;
    }

    public void setBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
        if (tileEntity.isInvalid()) {
            return;
        }

        if (this.updatingTileEntities) {
            tileEntity.xCoord = x;
            tileEntity.yCoord = y;
            tileEntity.zCoord = z;
            this.tileEntities.add(tileEntity);
            return;
        }

        this.loadedTileEntityList.add(tileEntity);
        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.setChunkBlockTileEntity(x & 15, y, z & 15, tileEntity);
        }
    }

    public void removeBlockTileEntity(int x, int y, int z) {
        TileEntity tileEntity = this.getBlockTileEntity(x, y, z);
        if (tileEntity != null && this.updatingTileEntities) {
            tileEntity.invalidate();
            return;
        }

        if (tileEntity != null) {
            this.loadedTileEntityList.remove(tileEntity);
        }

        Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            chunk.removeChunkBlockTileEntity(x & 15, y, z & 15);
        }
    }

    @Override
    @Side(CodeSide.CLIENT)
    public boolean isBlockOpaqueCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        return block != null && block.isOpaqueCube();
    }

    @Override
    public boolean isBlockNormalCube(int x, int y, int z) {
        Block block = Block.BLOCKS_LIST[this.getBlockId(x, y, z)];
        if (block == null)
            return false;

        return block.blockMaterial.isTranslucent() && block.isNormalCube();
    }

    @Side(CodeSide.CLIENT)
    public void saveWorldIndirectly(IProgressUpdatable updatable) {
        this.saveWorld(true, updatable);
    }

    public boolean updatingLighting() {
        if (this.lightingUpdatesCounter >= 50) {
            return false;
        }

        ++this.lightingUpdatesCounter;

        try {
            int updatesLeft = 500;

            while (this.lightingToUpdate.size() > 0) {
                --updatesLeft;
                if (updatesLeft <= 0) {
                    return true;
                }

                this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1).method1(this);
            }

            return false;
        } finally {
            --this.lightingUpdatesCounter;
        }
    }

    public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.scheduleLightingUpdate(skyBlock, x1, y1, z1, x2, y2, z2, true);
    }

    public void scheduleLightingUpdate(EnumSkyBlock skyBlock, int x1, int y1, int z1, int x2, int y2, int z2, boolean var8) {
        if (this.worldProvider.hasNoSky && skyBlock == EnumSkyBlock.SKY) {
            return;
        }

        ++lightingUpdatesScheduled;

        try {
            if (lightingUpdatesScheduled != 50) {
                int var9 = (x2 + x1) / 2;
                int var10 = (z2 + z1) / 2;
                if (!this.blockExists(var9, 64, var10)) {
                    return;
                }

                if (this.getChunkFromBlockCoords(var9, var10).isEmptyChunk()) {
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
                        if (var14.skyBlock == skyBlock && var14.method2(x1, y1, z1, x2, y2, z2)) {
                            return;
                        }
                    }
                }

                this.lightingToUpdate.add(new MetadataChunkBlock(skyBlock, x1, y1, z1, x2, y2, z2));
                int maxUpdates = 1000000;
                if (this.lightingToUpdate.size() > maxUpdates) {
                    System.out.println("More than " + maxUpdates + " updates, aborting lighting updates");
                    this.lightingToUpdate.clear();
                }

            }
        } finally {
            --lightingUpdatesScheduled;
        }

    }

    public void calculateInitialSkylight() {
        int var1 = this.calculateSkylightSubtracted(1.0F);
        if (var1 != this.skylightSubtracted) {
            this.skylightSubtracted = var1;
        }

    }

    public void setAllowedSpawnTypes(boolean hostile, boolean peaceful) {
        this.spawnHostileMobs = hostile;
        this.spawnPeacefulMobs = peaceful;
    }

    public void tick() {
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

        SpawnerAnimals.performSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs);

        long newTime = this.worldInfo.getWorldTime() + 1L;
        if (newTime % (long) this.autosavePeriod == 0L) {
            this.saveWorld(false, null);
        }

        this.worldInfo.setWorldTime(newTime);
        this.TickUpdates(false);
    }

    private void resetWeather() {
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

        if (this.field2 > 0)
            --this.field2;

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
            this.rainingStrength += 0.01D;
        } else this.rainingStrength -= 0.01D;

        if (this.rainingStrength < 0.0F) {
            this.rainingStrength = 0.0F;
        }

        if (this.rainingStrength > 1.0F) {
            this.rainingStrength = 1.0F;
        }

        this.prevThunderingStrength = this.thunderingStrength;
        if (this.worldInfo.isThundering()) {
            this.thunderingStrength += 0.01D;
        } else this.thunderingStrength -= 0.01D;

        if (this.thunderingStrength < 0.0F) {
            this.thunderingStrength = 0.0F;
        }

        if (this.thunderingStrength > 1.0F) {
            this.thunderingStrength = 1.0F;
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

        for (EntityPlayer player : this.playerEntities) {
            int chunkX = MathHelper.floor(player.posX / 16.0D);
            int chunkZ = MathHelper.floor(player.posZ / 16.0D);
            byte radius = 9;

            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z)
                    this.activeChunkSet.add(new ChunkCoordIntPair(x + chunkX, z + chunkZ));
            }
        }

        if (this.ambientTickCountdown > 0) {
            --this.ambientTickCountdown;
        }

        for (ChunkCoordIntPair pair : this.activeChunkSet) {
            int endX = pair.chunkXPos * 16;
            int endZ = pair.chunkZPos * 16;

            Chunk chunk = this.getChunkFromChunkCoords(pair.chunkXPos, pair.chunkZPos);
            if (this.ambientTickCountdown == 0) {
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

                        this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
                    }
                }
            }

            if (this.rand.nextInt(100000) == 0 && this.isSmallRain() && this.isBigThunder()) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int compressedCoord = this.distHashCounter >> 2;
                int uncX = endX + (compressedCoord & 15);
                int uncZ = endZ + (compressedCoord >> 8 & 15);

                int topY = this.findTopSolidOrLiquidBlock(uncX, uncZ);
                if (this.canBlockBeRainedOn(uncX, topY, uncZ)) {
                    this.addWeatherEffect(new EntityLightningBolt(this, uncX, topY, uncZ));
                    this.field2 = 2;
                }
            }

            if (this.rand.nextInt(16) == 0) {
                this.distHashCounter = this.distHashCounter * 3 + 1013904223;
                int var19 = this.distHashCounter >> 2;
                int var24 = var19 & 15;
                int var28 = var19 >> 8 & 15;
                int var31 = this.findTopSolidOrLiquidBlock(var24 + endX, var28 + endZ);
                if (this.getWorldChunkManager().getBiomeGenAt(var24 + endX, var28 + endZ).getEnableSnow() && var31 >= 0 && var31 < 128 && chunk.getSavedLightValue(EnumSkyBlock.BLOCK, var24, var31, var28) < 10) {
                    int var33 = chunk.getBlockID(var24, var31 - 1, var28);
                    int var35 = chunk.getBlockID(var24, var31, var28);
                    if (this.isSmallRain() && var35 == 0 && Block.SNOW.canPlaceBlockAt(this, var24 + endX, var31, var28 + endZ) && var33 != 0 && var33 != Block.ICE.blockID && Block.BLOCKS_LIST[var33].blockMaterial.getIsSolid()) {
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
        if (tasksSize != this.scheduledTickSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }

        if (tasksSize > 1000) {
            tasksSize = 1000;
        }

        for (int i = 0; i < tasksSize; ++i) {
            NextTickListEntry entry = this.scheduledTickTreeSet.first();
            if (!var1 && entry.scheduledTime > this.worldInfo.getWorldTime())
                break;

            this.scheduledTickTreeSet.remove(entry);
            this.scheduledTickSet.remove(entry);

            byte height = 8;
            if (this.checkChunksExist(
                    entry.xCoord - height, entry.yCoord - height, entry.zCoord - height,
                    entry.xCoord + height, entry.yCoord + height, entry.zCoord + height
            )) {
                int blockId = this.getBlockId(entry.xCoord, entry.yCoord, entry.zCoord);
                if (blockId == entry.blockID && blockId > 0) {
                    Block.BLOCKS_LIST[blockId].updateTick(this, entry.xCoord, entry.yCoord, entry.zCoord, this.rand);
                }
            }
        }

        return this.scheduledTickTreeSet.size() != 0;
    }

    @Side(CodeSide.CLIENT)
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
                    this.getChunkFromChunkCoords(x, z).getEntitiesOfTypeWithinAABB(var1, bb, entities);
            }
        }

        return entities;
    }

    @Side(CodeSide.CLIENT)
    public List<Entity> getLoadedEntityList() {
        return this.loadedEntityList;
    }

    public void updateTileEntityChunkAndDoNothing(int x, int y, int z, TileEntity tileEntity) {
        if (this.blockExists(x, y, z))
            this.getChunkFromBlockCoords(x, z).setChunkModified();

        for (IWorldAccess worldAccess : this.worldAccesses)
            worldAccess.doNothingWithTileEntity(x, y, z, tileEntity);
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

    public void addLoadedEntities(List<Entity> entities) {
        this.loadedEntityList.addAll(entities);

        for (Entity entity : entities) {
            this.obtainEntitySkin(entity);
        }
    }

    public void addUnloadedEntities(List<Entity> entities) {
        this.unloadedEntityList.addAll(entities);
    }

    @Side(CodeSide.CLIENT)
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

    public PathEntity getEntityPathToXYZ(Entity entity, int x, int y, int z, float var5) {
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
        return new Pathfinder(chunkCache).createEntityPathTo(entity, x, y, z, var5);
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

    public EntityPlayer getClosestPlayerToEntity(Entity entity, double maxDistance) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, maxDistance);
    }

    public EntityPlayer getClosestPlayer(double x, double y, double z, double maxDistance) {
        double var9 = -1.0D;
        EntityPlayer player = null;

        for (EntityPlayer entityPlayer : this.playerEntities) {
            double distanceSq = entityPlayer.getDistanceSq(x, y, z);
            if ((maxDistance < 0.0D || distanceSq < maxDistance * maxDistance) && (var9 == -1.0D || distanceSq < var9)) {
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

    @Side(CodeSide.CLIENT)
    public void sendQuittingDisconnectingPacket() {
    }

    public void checkSessionLock() {
        this.saveHandler.validateSession();
    }

    public void setTime(long time) {
        long var3 = time - this.worldInfo.getWorldTime();

        for (NextTickListEntry var6 : this.scheduledTickSet) {
            var6.scheduledTime += var3;
        }

        this.setWorldTime(time);
    }

    public long getRandomSeed() {
        return this.worldInfo.getRandomSeed();
    }

    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }

    public void setWorldTime(long worldTime) {
        this.worldInfo.setWorldTime(worldTime);
    }

    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
    }

    public void setSpawnPoint(ChunkCoordinates var1) {
        this.worldInfo.setSpawn(var1.x, var1.y, var1.z);
    }

    @Side(CodeSide.CLIENT)
    public void joinEntityInSurroundings(Entity entity) {
        int cenX = MathHelper.floor(entity.posX / 16.0D);
        int cenZ = MathHelper.floor(entity.posZ / 16.0D);

        byte sqRadius = 2;
        for (int x = cenX - sqRadius; x <= cenX + sqRadius; ++x) {
            for (int z = cenZ - sqRadius; z <= cenZ + sqRadius; ++z)
                this.getChunkFromChunkCoords(x, z);
        }

        if (!this.loadedEntityList.contains(entity)) {
            this.loadedEntityList.add(entity);
        }
    }

    public boolean canMineBlock(EntityPlayer player, int x, int y, int z) {
        return true;
    }

    public void sendTrackedEntityStatusUpdatePacket(Entity entity, byte status) {
    }

    @Side(CodeSide.CLIENT)
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

    public void playNoteAt(int x, int y, int z, int instrumentType, int pitch) {
        int blockId = this.getBlockId(x, y, z);
        if (blockId > 0)
            Block.BLOCKS_LIST[blockId].playBlock(this, x, y, z, instrumentType, pitch);
    }

    public ISaveHandler getSaveHandler() {
        return saveHandler;
    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = !this.playerEntities.isEmpty();

        for (EntityPlayer player : this.playerEntities) {
            if (!player.isSleeping()) {
                this.allPlayersSleeping = false;
                break;
            }
        }

    }

    protected void wakeUpAllPlayers() {
        this.allPlayersSleeping = false;

        for (EntityPlayer player : this.playerEntities) {
            if (player.isSleeping())
                player.wakeUpPlayer(false, false, true);
        }

        this.clearWeather();
    }

    public boolean isAllPlayersFullyAsleep() {
        if (this.allPlayersSleeping && !this.localWorld) {
            for (EntityPlayer player : this.playerEntities) {
                if (!player.isPlayerFullyAsleep())
                    return false;
            }

            return true;
        }

        return false;
    }

    public float getThunderStrength(float thunderStrength) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * thunderStrength) * this.getRainStrength(thunderStrength);
    }

    public float getRainStrength(float var1) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * var1;
    }

    public void setRainingStrength(float rainingStrength) {
        this.prevRainingStrength = rainingStrength;
        this.rainingStrength = rainingStrength;
    }

    public boolean isBigThunder() {
        return (double) this.getThunderStrength(1.0F) > 0.9D;
    }

    public boolean isSmallRain() {
        return (double) this.getRainStrength(1.0F) > 0.2D;
    }

    public boolean canBlockBeRainedOn(int x, int y, int z) {
        if (!this.isSmallRain()) {
            return false;
        }

        if (!this.canBlockSeeTheSky(x, y, z)) {
            return false;
        }

        if (this.findTopSolidOrLiquidBlock(x, z) > y) {
            return false;
        }

        BiomeGenBase genBase = this.getWorldChunkManager().getBiomeGenAt(x, z);
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

    public void playEffects(int effectId, int x, int y, int z, int subData) {
        this.playEffects(null, effectId, x, y, z, subData);
    }

    public void playEffects(EntityPlayer player, int effectId, int x, int y, int z, int subData) {
        for (IWorldAccess worldAccess : this.worldAccesses) {
            worldAccess.playEffect(player, effectId, x, y, z, subData);
        }
    }
}
