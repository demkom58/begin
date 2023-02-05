package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet255KickDisconnect;
import net.minecraft.util.Hash;
import net.minecraft.world.chunk.ChunkCoordinates;
import net.minecraft.world.chunk.ChunkProviderClient;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveHandlerMP;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class WorldClient extends World {
    private LinkedList<WorldBlockPositionType> blocksToReceive = new LinkedList<>();
    private NetClientHandler sendQueue;
    private ChunkProviderClient chunkProviderClient;
    private Hash entityHashSet = new Hash();
    private Set<Entity> entityList = new HashSet<>();
    private Set<Entity> entitySpawnQueue = new HashSet<>();

    public WorldClient(NetClientHandler var1, long var2, int var4) {
        super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(var4), var2);
        this.sendQueue = var1;
        this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
        this.mapStorage = var1.field_28118_b;
    }

    @Override
    public void tick() {
        this.setWorldTime(this.getWorldTime() + 1L);
        int var1 = this.calculateSkylightSubtracted(1.0F);
        if (var1 != this.skylightSubtracted) {
            this.skylightSubtracted = var1;

            for (int i = 0; i < this.worldAccesses.size(); ++i) {
                this.worldAccesses.get(i).updateAllRenderers();
            }
        }

        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            Entity entity = this.entitySpawnQueue.iterator().next();
            if (!this.loadedEntityList.contains(entity)) {
                this.entityJoinedWorld(entity);
            }
        }

        this.sendQueue.processReadPackets();

        for (int i = 0; i < this.blocksToReceive.size(); ++i) {
            WorldBlockPositionType var6 = this.blocksToReceive.get(i);
            if (--var6.acceptCountdown == 0) {
                super.setBlockAndMetadata(var6.posX, var6.posY, var6.posZ, var6.blockId, var6.metadata);
                super.markBlockNeedsUpdate(var6.posX, var6.posY, var6.posZ);
                this.blocksToReceive.remove(i--);
            }
        }

    }

    public void func_711_c(int var1, int var2, int var3, int var4, int var5, int var6) {
        for (int i = 0; i < this.blocksToReceive.size(); ++i) {
            WorldBlockPositionType var8 = this.blocksToReceive.get(i);
            if (var8.posX >= var1 && var8.posY >= var2 && var8.posZ >= var3 && var8.posX <= var4 && var8.posY <= var5 && var8.posZ <= var6) {
                this.blocksToReceive.remove(i--);
            }
        }

    }

    @Override
    protected IChunkProvider createChunkProvider() {
        this.chunkProviderClient = new ChunkProviderClient(this);
        return this.chunkProviderClient;
    }

    @Override
    public void setSpawnLocation() {
        this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
    }

    @Override
    protected void doRandomUpdateTicks() {
    }

    @Override
    public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
    }

    @Override
    public boolean TickUpdates(boolean var1) {
        return false;
    }

    public void doPreChunk(int var1, int var2, boolean var3) {
        if (var3) {
            this.chunkProviderClient.prepareChunk(var1, var2);
        } else {
            this.chunkProviderClient.unloadChunk(var1, var2);
        }

        if (!var3) {
            this.markBlocksDirty(var1 * 16, 0, var2 * 16, var1 * 16 + 15, 128, var2 * 16 + 15);
        }

    }

    @Override
    public boolean entityJoinedWorld(Entity entity) {
        boolean var2 = super.entityJoinedWorld(entity);
        this.entityList.add(entity);
        if (!var2) {
            this.entitySpawnQueue.add(entity);
        }

        return var2;
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        this.entityList.remove(entity);
    }

    @Override
    protected void obtainEntitySkin(Entity entity) {
        super.obtainEntitySkin(entity);
        this.entitySpawnQueue.remove(entity);

    }

    @Override
    protected void releaseEntitySkin(Entity entity) {
        super.releaseEntitySkin(entity);
        if (this.entityList.contains(entity)) {
            this.entitySpawnQueue.add(entity);
        }

    }

    public void func_712_a(int var1, Entity var2) {
        Entity entity = this.func_709_b(var1);
        if (entity != null) {
            this.removeEntity(entity);
        }

        this.entityList.add(var2);
        var2.entityId = var1;
        if (!this.entityJoinedWorld(var2)) {
            this.entitySpawnQueue.add(var2);
        }

        this.entityHashSet.addKey(var1, var2);
    }

    public Entity func_709_b(int var1) {
        return (Entity) this.entityHashSet.lookup(var1);
    }

    public Entity removeEntityFromWorld(int var1) {
        Entity entity = (Entity) this.entityHashSet.removeObject(var1);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }

        return entity;
    }

    @Override
    public boolean setBlockMetadata(int x, int y, int z, int metadata) {
        int var5 = this.getBlockId(x, y, z);
        int var6 = this.getBlockMetadata(x, y, z);
        if (super.setBlockMetadata(x, y, z, metadata)) {
            this.blocksToReceive.add(new WorldBlockPositionType(this, x, y, z, var5, var6));
            return true;
        }

        return false;
    }

    @Override
    public boolean setBlockAndMetadata(int x, int y, int z, int blockId, int metadata) {
        int var6 = this.getBlockId(x, y, z);
        int var7 = this.getBlockMetadata(x, y, z);
        if (super.setBlockAndMetadata(x, y, z, blockId, metadata)) {
            this.blocksToReceive.add(new WorldBlockPositionType(this, x, y, z, var6, var7));
            return true;
        }

        return false;
    }

    @Override
    public boolean setBlock(int x, int y, int z, int blockId) {
        int var5 = this.getBlockId(x, y, z);
        int var6 = this.getBlockMetadata(x, y, z);
        if (super.setBlock(x, y, z, blockId)) {
            this.blocksToReceive.add(new WorldBlockPositionType(this, x, y, z, var5, var6));
            return true;
        }

        return false;
    }

    public boolean func_714_c(int var1, int var2, int var3, int var4, int var5) {
        this.func_711_c(var1, var2, var3, var1, var2, var3);
        if (super.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
            this.notifyBlockChange(var1, var2, var3, var4);
            return true;
        }

        return false;
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        this.sendQueue.func_28117_a(new Packet255KickDisconnect("Quitting"));
    }

    @Override
    protected void updateWeather() {
        if (this.worldProvider.hasNoSky)
            return;

        if (this.field2 > 0) {
            --this.field2;
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
