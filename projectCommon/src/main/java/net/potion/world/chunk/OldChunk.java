package net.potion.world.chunk;

import net.hypnosis.util.math.MathHelper;
import net.potion.block.Block;
import net.potion.block.BlockContainer;
import net.potion.block.EnumSkyBlock;
import net.potion.entity.Entity;
import net.potion.tileentity.TileEntity;
import net.potion.util.AxisAlignedBB;
import net.potion.util.NibbleArray;
import net.potion.world.World;

import java.util.*;

public class OldChunk {
    public static boolean isLit;
    public final int xPosition;
    public final int zPosition;
    public byte[] blocks;
    public boolean isChunkLoaded;
    public World world;
    public NibbleArray metadata;
    public NibbleArray skylightMap;
    public NibbleArray blocklightMap;
    public byte[] heightMap;
    public int lowestBlockHeight;
    public Map<ChunkPosition, TileEntity> chunkTileEntityMap;
    public List<Entity>[] entities;
    public boolean terrainPopulated;
    public boolean modified;
    public boolean neverSave;
    public boolean hasEntities;
    public long lastSaveTime;

    public OldChunk(World world, int x, int z) {
        this.chunkTileEntityMap = new HashMap<>();
        this.entities = new List[8];
        this.terrainPopulated = false;
        this.modified = false;
        this.hasEntities = false;
        this.lastSaveTime = 0L;
        this.world = world;
        this.xPosition = x;
        this.zPosition = z;
        this.heightMap = new byte[256];

        for (int i = 0; i < this.entities.length; ++i) {
            this.entities[i] = new ArrayList<>();
        }

    }

    public OldChunk(World world, byte[] blocks, int x, int z) {
        this(world, x, z);
        this.blocks = blocks;
        this.metadata = new NibbleArray(blocks.length);
        this.skylightMap = new NibbleArray(blocks.length);
        this.blocklightMap = new NibbleArray(blocks.length);
    }

    public boolean isAtLocation(int x, int z) {
        return x == this.xPosition && z == this.zPosition;
    }

    public int getHeightValue(int x, int z) {
        return this.heightMap[z << 4 | x] & 255;
    }

    public void generateBlockLightMap() {
    }

    public void generateHeightMap() {
        int lowestHeight = 127;

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int newHeight = 127;

                int cXZ = x << 11 | z << 7;
                while (newHeight > 0 && Block.LIGHT_OPACITY[this.blocks[cXZ + newHeight - 1] & 255] == 0) {
                    --newHeight;
                }

                this.heightMap[z << 4 | x] = (byte) newHeight;
                if (newHeight < lowestHeight) {
                    lowestHeight = newHeight;
                }
            }
        }

        this.lowestBlockHeight = lowestHeight;
        this.modified = true;
    }

    public void generateHeightAndSkyLightMap() {
        int lowestHeight = 127;

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int newHeight = 127;

                int cXZ = x << 11 | z << 7;
                while (newHeight > 0 && Block.LIGHT_OPACITY[this.blocks[cXZ + newHeight - 1] & 255] == 0) {
                    --newHeight;
                }

                this.heightMap[z << 4 | x] = (byte) newHeight;
                if (newHeight < lowestHeight) {
                    lowestHeight = newHeight;
                }

                if (!this.world.worldProvider.hasNoSky) {
                    int skyLight = 15;
                    int y = 127;

                    do {
                        skyLight -= Block.LIGHT_OPACITY[this.blocks[cXZ + y] & 255];
                        if (skyLight > 0) {
                            this.skylightMap.setNibble(x, y, z, skyLight);
                        }

                        --y;
                    } while (y > 0 && skyLight > 0);
                }
            }
        }

        this.lowestBlockHeight = lowestHeight;

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                this.updateSkyLight(x, z);
            }
        }

        this.modified = true;
    }

    public void prepareChunkLoad() {
    }

    private void updateSkyLight(int x, int z) {
        int y = this.getHeightValue(x, z);
        int gX = this.xPosition * 16 + x;
        int gZ = this.zPosition * 16 + z;
        this.updateSkyLight(gX - 1, gZ, y);
        this.updateSkyLight(gX + 1, gZ, y);
        this.updateSkyLight(gX, gZ - 1, y);
        this.updateSkyLight(gX, gZ + 1, y);
    }

    private void updateSkyLight(int x, int y, int z) {
        int heightValue = this.world.getHeightValue(x, y);
        if (heightValue > z) {
            this.world.scheduleLightingUpdate(EnumSkyBlock.SKY, x, z, y, x, heightValue, y);
            this.modified = true;
        } else if (heightValue < z) {
            this.world.scheduleLightingUpdate(EnumSkyBlock.SKY, x, heightValue, y, x, z, y);
            this.modified = true;
        }

    }

    private void relightBlock(int x, int y, int z) {
        int topBlockY = this.heightMap[z << 4 | x] & 255;
        int maxY = Math.max(y, topBlockY);

        int lXZ = x << 11 | z << 7;
        while (maxY > 0 && Block.LIGHT_OPACITY[this.blocks[lXZ + maxY - 1] & 255] == 0) {
            --maxY;
        }

        if (maxY == topBlockY) {
            return;
        }

        this.world.markBlocksDirtyVertical(x, z, maxY, topBlockY);
        this.heightMap[z << 4 | x] = (byte) maxY;
        if (maxY >= this.lowestBlockHeight) {
            int newY = 127;

            for (int iX = 0; iX < 16; ++iX) {
                for (int iZ = 0; iZ < 16; ++iZ) {
                    if ((this.heightMap[iZ << 4 | iX] & 255) < newY) {
                        newY = this.heightMap[iZ << 4 | iX] & 255;
                    }
                }
            }

            this.lowestBlockHeight = newY;
        } else {
            this.lowestBlockHeight = maxY;
        }

        int gX = this.xPosition * 16 + x;
        int gZ = this.zPosition * 16 + z;
        if (maxY < topBlockY) {
            for (int lY = maxY; lY < topBlockY; ++lY) {
                this.skylightMap.setNibble(x, lY, z, 15);
            }
        } else {
            this.world.scheduleLightingUpdate(EnumSkyBlock.SKY, gX, topBlockY, gZ, gX, maxY, gZ);

            for (int lY = topBlockY; lY < maxY; ++lY) {
                this.skylightMap.setNibble(x, lY, z, 0);
            }
        }

        int lightResult = 15;

        int prevMaxY = maxY;
        while (maxY > 0 && lightResult > 0) {
            --maxY;
            int lightOpacity = Block.LIGHT_OPACITY[this.getBlockID(x, maxY, z)];
            if (lightOpacity == 0) {
                lightOpacity = 1;
            }

            lightResult -= lightOpacity;
            if (lightResult < 0) {
                lightResult = 0;
            }
            this.skylightMap.setNibble(x, maxY, z, lightResult);
        }

        while (maxY > 0 && Block.LIGHT_OPACITY[this.getBlockID(x, maxY - 1, z)] == 0) {
            --maxY;
        }

        if (maxY != prevMaxY) {
            this.world.scheduleLightingUpdate(EnumSkyBlock.SKY, gX - 1, maxY, gZ - 1, gX + 1, prevMaxY, gZ + 1);
        }

        this.modified = true;
    }

    public int getBlockID(int x, int y, int z) {
        return this.blocks[x << 11 | z << 7 | y] & 255;
    }

    public boolean setBlockIDWithMetadata(int x, int y, int z, int blockId, int metadata) {
        byte bId = (byte) blockId;
        int topBlockY = this.heightMap[z << 4 | x] & 255;
        int prevBlockId = this.blocks[x << 11 | z << 7 | y] & 255;
        if (prevBlockId == blockId && this.metadata.getNibble(x, y, z) == metadata) {
            return false;
        }

        int gX = this.xPosition * 16 + x;
        int gZ = this.zPosition * 16 + z;
        this.blocks[x << 11 | z << 7 | y] = (byte) (bId & 255);
        if (prevBlockId != 0 && !this.world.localWorld) {
            Block.BLOCKS_LIST[prevBlockId].onBlockRemoval(this.world, gX, y, gZ);
        }

        this.metadata.setNibble(x, y, z, metadata);
        if (!this.world.worldProvider.hasNoSky) {
            if (Block.LIGHT_OPACITY[bId & 255] != 0) {
                if (y >= topBlockY) {
                    this.relightBlock(x, y + 1, z);
                }
            } else if (y == topBlockY - 1) {
                this.relightBlock(x, y, z);
            }

            this.world.scheduleLightingUpdate(EnumSkyBlock.SKY, gX, y, gZ, gX, y, gZ);
        }

        this.world.scheduleLightingUpdate(EnumSkyBlock.BLOCK, gX, y, gZ, gX, y, gZ);
        this.updateSkyLight(x, z);
        this.metadata.setNibble(x, y, z, metadata);
        if (blockId != 0) {
            Block.BLOCKS_LIST[blockId].onBlockAdded(this.world, gX, y, gZ);
        }

        this.modified = true;
        return true;
    }

    public boolean setBlockID(int x, int y, int z, int blockId) {
        byte bId = (byte) blockId;
        int topBlockY = this.heightMap[z << 4 | x] & 255;
        int prevBlockId = this.blocks[x << 11 | z << 7 | y] & 255;
        if (prevBlockId == blockId) {
            return false;
        }

        int gX = this.xPosition * 16 + x;
        int gZ = this.zPosition * 16 + z;
        this.blocks[x << 11 | z << 7 | y] = (byte) (bId & 255);
        if (prevBlockId != 0) {
            Block.BLOCKS_LIST[prevBlockId].onBlockRemoval(this.world, gX, y, gZ);
        }

        this.metadata.setNibble(x, y, z, 0);
        if (Block.LIGHT_OPACITY[bId & 255] != 0) {
            if (y >= topBlockY) {
                this.relightBlock(x, y + 1, z);
            }
        } else if (y == topBlockY - 1) {
            this.relightBlock(x, y, z);
        }

        this.world.scheduleLightingUpdate(EnumSkyBlock.SKY, gX, y, gZ, gX, y, gZ);
        this.world.scheduleLightingUpdate(EnumSkyBlock.BLOCK, gX, y, gZ, gX, y, gZ);
        this.updateSkyLight(x, z);
        if (blockId != 0 && !this.world.localWorld) {
            Block.BLOCKS_LIST[blockId].onBlockAdded(this.world, gX, y, gZ);
        }

        this.modified = true;
        return true;
    }

    public int getBlockMetadata(int x, int y, int z) {
        return this.metadata.getNibble(x, y, z);
    }

    public void setBlockMetadata(int x, int y, int z, int metadata) {
        this.modified = true;
        this.metadata.setNibble(x, y, z, metadata);
    }

    public int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z) {
        if (skyBlock == EnumSkyBlock.SKY) {
            return this.skylightMap.getNibble(x, y, z);
        }

        if (skyBlock == EnumSkyBlock.BLOCK) {
            return this.blocklightMap.getNibble(x, y, z);
        }

        return 0;
    }

    public void setLightValue(EnumSkyBlock skyBlock, int x, int y, int z, int light) {
        this.modified = true;
        if (skyBlock == EnumSkyBlock.SKY) {
            this.skylightMap.setNibble(x, y, z, light);
            return;
        }

        if (skyBlock == EnumSkyBlock.BLOCK) {
            this.blocklightMap.setNibble(x, y, z, light);
        }
    }

    public int getBlockLightValue(int x, int y, int z, int skylightSubtracted) {
        int light = this.skylightMap.getNibble(x, y, z);
        if (light > 0) {
            isLit = true;
        }

        light -= skylightSubtracted;
        int blockLight = this.blocklightMap.getNibble(x, y, z);
        if (blockLight > light) {
            light = blockLight;
        }

        return light;
    }

    public void addEntity(Entity entity) {
        this.hasEntities = true;
        int cX = MathHelper.floor(entity.posX / 16.0D);
        int cZ = MathHelper.floor(entity.posZ / 16.0D);
        if (cX != this.xPosition || cZ != this.zPosition) {
            System.out.println("Wrong location! " + entity);
            Thread.dumpStack();
        }

        int y = MathHelper.floor(entity.posY / 16.0D);
        if (y < 0) {
            y = 0;
        }

        if (y >= this.entities.length) {
            y = this.entities.length - 1;
        }

        entity.addedToChunk = true;
        entity.chunkCoordX = this.xPosition;
        entity.chunkCoordY = y;
        entity.chunkCoordZ = this.zPosition;
        this.entities[y].add(entity);
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAtIndex(entity, entity.chunkCoordY);
    }

    public void removeEntityAtIndex(Entity entity, int listIdx) {
        if (listIdx < 0) {
            listIdx = 0;
        }

        if (listIdx >= this.entities.length) {
            listIdx = this.entities.length - 1;
        }

        this.entities[listIdx].remove(entity);
    }

    public boolean canBlockSeeTheSky(int x, int y, int z) {
        return y >= (this.heightMap[z << 4 | x] & 255);
    }

    public TileEntity getChunkBlockTileEntity(int x, int y, int z) {
        ChunkPosition cPos = new ChunkPosition(x, y, z);
        TileEntity tileEntity = this.chunkTileEntityMap.get(cPos);
        if (tileEntity == null) {
            int blockID = this.getBlockID(x, y, z);
            if (!Block.IS_BLOCK_CONTAINER[blockID]) {
                return null;
            }

            BlockContainer container = (BlockContainer) Block.BLOCKS_LIST[blockID];
            container.onBlockAdded(this.world, this.xPosition * 16 + x, y, this.zPosition * 16 + z);
            tileEntity = this.chunkTileEntityMap.get(cPos);
        }

        if (tileEntity != null && tileEntity.isInvalid()) {
            this.chunkTileEntityMap.remove(cPos);
            return null;
        }

        return tileEntity;
    }

    public void addTileEntity(TileEntity tileEntity) {
        int x = tileEntity.xCoord - this.xPosition * 16;
        int y = tileEntity.yCoord;
        int z = tileEntity.zCoord - this.zPosition * 16;
        this.setChunkBlockTileEntity(x, y, z, tileEntity);
        if (this.isChunkLoaded) {
            this.world.loadedTileEntityList.add(tileEntity);
        }

    }

    public void setChunkBlockTileEntity(int x, int y, int z, TileEntity tileEntity) {
        ChunkPosition cPos = new ChunkPosition(x, y, z);
        tileEntity.worldObj = this.world;
        tileEntity.xCoord = this.xPosition * 16 + x;
        tileEntity.yCoord = y;
        tileEntity.zCoord = this.zPosition * 16 + z;
        if (this.getBlockID(x, y, z) == 0 || !(Block.BLOCKS_LIST[this.getBlockID(x, y, z)] instanceof BlockContainer)) {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
            return;
        }

        tileEntity.validate();
        this.chunkTileEntityMap.put(cPos, tileEntity);
    }

    public void removeChunkBlockTileEntity(int x, int y, int z) {
        ChunkPosition cPos = new ChunkPosition(x, y, z);
        if (!this.isChunkLoaded) {
            return;
        }

        TileEntity tileEntity = this.chunkTileEntityMap.remove(cPos);
        if (tileEntity != null) {
            tileEntity.invalidate();
        }
    }

    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.world.addTileEntities(this.chunkTileEntityMap.values());

        for (int i = 0; i < this.entities.length; ++i) {
            this.world.addLoadedEntities(this.entities[i]);
        }

    }

    public void onChunkUnload() {
        this.isChunkLoaded = false;

        for (TileEntity tileEntity : this.chunkTileEntityMap.values()) {
            tileEntity.invalidate();
        }

        for (int listIdx = 0; listIdx < this.entities.length; ++listIdx) {
            this.world.addUnloadedEntities(this.entities[listIdx]);
        }

    }

    public void setChunkModified() {
        this.modified = true;
    }

    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB bb, List<Entity> result) {
        int start = MathHelper.floor((bb.minY - 2.0D) / 16.0D);
        int end = MathHelper.floor((bb.maxY + 2.0D) / 16.0D);
        if (start < 0) {
            start = 0;
        }

        if (end >= this.entities.length) {
            end = this.entities.length - 1;
        }

        for (int listIdx = start; listIdx <= end; ++listIdx) {
            List<Entity> entityList = this.entities[listIdx];

            for (int i = 0; i < entityList.size(); ++i) {
                Entity iterEntity = entityList.get(i);
                if (iterEntity != entity && iterEntity.boundingBox.intersectsWith(bb)) {
                    result.add(iterEntity);
                }
            }
        }

    }

    public void getEntitiesOfTypeWithinAABB(Class<? extends Entity> type, AxisAlignedBB bb, List<Entity> entities) {
        int start = MathHelper.floor((bb.minY - 2.0D) / 16.0D);
        int end = MathHelper.floor((bb.maxY + 2.0D) / 16.0D);

        if (start < 0) {
            start = 0;
        }

        if (end >= this.entities.length) {
            end = this.entities.length - 1;
        }

        for (int listIdx = start; listIdx <= end; ++listIdx) {
            List<Entity> entityList = this.entities[listIdx];

            for (int i = 0; i < entityList.size(); ++i) {
                Entity entity = entityList.get(i);
                if (type.isAssignableFrom(entity.getClass()) && entity.boundingBox.intersectsWith(bb))
                    entities.add(entity);
            }
        }

    }

    public boolean needsSaving(boolean var1) {
        if (this.neverSave) {
            return false;
        }

        if (var1) {
            if (this.hasEntities && this.world.getWorldTime() != this.lastSaveTime) {
                return true;
            }
        } else if (this.hasEntities && this.world.getWorldTime() >= this.lastSaveTime + 600L) {
            return true;
        }

        return this.modified;
    }

    public int getChunkData(byte[] result, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int offset) {
        int rngX = maxX - minX;
        int rngY = maxY - minY;
        int rngZ = maxZ - minZ;

        if (rngX * rngY * rngZ == this.blocks.length) {
            System.arraycopy(this.blocks, 0, result, offset, this.blocks.length);
            offset = offset + this.blocks.length;
            System.arraycopy(this.metadata.data, 0, result, offset, this.metadata.data.length);
            offset = offset + this.metadata.data.length;
            System.arraycopy(this.blocklightMap.data, 0, result, offset, this.blocklightMap.data.length);
            offset = offset + this.blocklightMap.data.length;
            System.arraycopy(this.skylightMap.data, 0, result, offset, this.skylightMap.data.length);
            offset = offset + this.skylightMap.data.length;
            return offset;
        }

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = x << 11 | z << 7 | minY;
                int rng = maxY - minY;
                System.arraycopy(this.blocks, cXYZ, result, offset, rng);
                offset += rng;
            }
        }

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = (x << 11 | z << 7 | minY) >> 1;
                int rng = (maxY - minY) / 2;
                System.arraycopy(this.metadata.data, cXYZ, result, offset, rng);
                offset += rng;
            }
        }

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = (x << 11 | z << 7 | minY) >> 1;
                int rng = (maxY - minY) / 2;
                System.arraycopy(this.blocklightMap.data, cXYZ, result, offset, rng);
                offset += rng;
            }
        }

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = (x << 11 | z << 7 | minY) >> 1;
                int rng = (maxY - minY) / 2;
                System.arraycopy(this.skylightMap.data, cXYZ, result, offset, rng);
                offset += rng;
            }
        }

        return offset;
    }

    public int setChunkData(byte[] src, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int offset) {
        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = x << 11 | z << 7 | minY;
                int rng = maxY - minY;
                System.arraycopy(src, offset, this.blocks, cXYZ, rng);
                offset += rng;
            }
        }

        this.generateHeightMap();

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = (x << 11 | z << 7 | minY) >> 1;
                int rng = (maxY - minY) / 2;
                System.arraycopy(src, offset, this.metadata.data, cXYZ, rng);
                offset += rng;
            }
        }

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = (x << 11 | z << 7 | minY) >> 1;
                int rng = (maxY - minY) / 2;
                System.arraycopy(src, offset, this.blocklightMap.data, cXYZ, rng);
                offset += rng;
            }
        }

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int cXYZ = (x << 11 | z << 7 | minY) >> 1;
                int rng = (maxY - minY) / 2;
                System.arraycopy(src, offset, this.skylightMap.data, cXYZ, rng);
                offset += rng;
            }
        }

        return offset;
    }

    public Random createSpecialRandom(long var1) {
        return new Random(this.world.getRandomSeed()
                + (this.xPosition * this.xPosition * 4987142L)
                + (this.xPosition * 5947611L)
                + ((long) this.zPosition * this.zPosition) * 4392871L
                + (this.zPosition * 389711L) ^ var1);
    }

    public boolean isEmptyChunk() {
        return false;
    }

    public void checkBlocks() {
        ChunkBlockMap.fix(this.blocks);
    }
}
