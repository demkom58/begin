package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.network.NetClientHandler;
import net.minecraft.network.packet.Packet255KickDisconnect;
import net.minecraft.util.MCHash;
import net.minecraft.world.chunk.ChunkCoordinates;
import net.minecraft.world.chunk.ChunkProviderClient;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveHandlerMP;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class WorldClient extends World {
    private LinkedList<WorldBlockPositionType> field_1057_z = new LinkedList<>();
    private NetClientHandler sendQueue;
    private ChunkProviderClient field_20915_C;
    private MCHash field_1055_D = new MCHash();
    private Set<Entity> field_20914_E = new HashSet<>();
    private Set<Entity> field_1053_F = new HashSet<>();

    public WorldClient(NetClientHandler var1, long var2, int var4) {
        super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(var4), var2);
        this.sendQueue = var1;
        this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
        this.mapStorage = var1.field_28118_b;
    }

    public void tick() {
        this.setWorldTime(this.getWorldTime() + 1L);
        int var1 = this.calculateSkylightSubtracted(1.0F);
        if (var1 != this.skylightSubtracted) {
            this.skylightSubtracted = var1;

            for (int i = 0; i < this.worldAccesses.size(); ++i) {
                this.worldAccesses.get(i).updateAllRenderers();
            }
        }

        for (int i = 0; i < 10 && !this.field_1053_F.isEmpty(); ++i) {
            Entity entity = this.field_1053_F.iterator().next();
            if (!this.loadedEntityList.contains(entity)) {
                this.entityJoinedWorld(entity);
            }
        }

        this.sendQueue.processReadPackets();

        for (int i = 0; i < this.field_1057_z.size(); ++i) {
            WorldBlockPositionType var6 = this.field_1057_z.get(i);
            if (--var6.field_1206_d == 0) {
                super.setBlockAndMetadata(var6.field_1202_a, var6.field_1201_b, var6.field_1207_c, var6.field_1205_e, var6.field_1204_f);
                super.markBlockNeedsUpdate(var6.field_1202_a, var6.field_1201_b, var6.field_1207_c);
                this.field_1057_z.remove(i--);
            }
        }

    }

    public void func_711_c(int var1, int var2, int var3, int var4, int var5, int var6) {
        for (int i = 0; i < this.field_1057_z.size(); ++i) {
            WorldBlockPositionType var8 = this.field_1057_z.get(i);
            if (var8.field_1202_a >= var1 && var8.field_1201_b >= var2 && var8.field_1207_c >= var3 && var8.field_1202_a <= var4 && var8.field_1201_b <= var5 && var8.field_1207_c <= var6) {
                this.field_1057_z.remove(i--);
            }
        }

    }

    protected IChunkProvider getChunkProvider() {
        this.field_20915_C = new ChunkProviderClient(this);
        return this.field_20915_C;
    }

    public void setSpawnLocation() {
        this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
    }

    protected void updateBlocksAndPlayCaveSounds() {
    }

    public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
    }

    public boolean TickUpdates(boolean var1) {
        return false;
    }

    public void doPreChunk(int var1, int var2, boolean var3) {
        if (var3) {
            this.field_20915_C.prepareChunk(var1, var2);
        } else {
            this.field_20915_C.func_539_c(var1, var2);
        }

        if (!var3) {
            this.markBlocksDirty(var1 * 16, 0, var2 * 16, var1 * 16 + 15, 128, var2 * 16 + 15);
        }

    }

    public boolean entityJoinedWorld(Entity entity) {
        boolean var2 = super.entityJoinedWorld(entity);
        this.field_20914_E.add(entity);
        if (!var2) {
            this.field_1053_F.add(entity);
        }

        return var2;
    }

    public void setEntityDead(Entity entity) {
        super.setEntityDead(entity);
        this.field_20914_E.remove(entity);
    }

    protected void obtainEntitySkin(Entity entity) {
        super.obtainEntitySkin(entity);
        this.field_1053_F.remove(entity);

    }

    protected void releaseEntitySkin(Entity entity) {
        super.releaseEntitySkin(entity);
        if (this.field_20914_E.contains(entity)) {
            this.field_1053_F.add(entity);
        }

    }

    public void func_712_a(int var1, Entity var2) {
        Entity entity = this.func_709_b(var1);
        if (entity != null) {
            this.setEntityDead(entity);
        }

        this.field_20914_E.add(var2);
        var2.entityId = var1;
        if (!this.entityJoinedWorld(var2)) {
            this.field_1053_F.add(var2);
        }

        this.field_1055_D.addKey(var1, var2);
    }

    public Entity func_709_b(int var1) {
        return (Entity) this.field_1055_D.lookup(var1);
    }

    public Entity removeEntityFromWorld(int var1) {
        Entity entity = (Entity) this.field_1055_D.removeObject(var1);
        if (entity != null) {
            this.field_20914_E.remove(entity);
            this.setEntityDead(entity);
        }

        return entity;
    }

    public boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
        int var5 = this.getBlockId(var1, var2, var3);
        int var6 = this.getBlockMetadata(var1, var2, var3);
        if (super.setBlockMetadata(var1, var2, var3, var4)) {
            this.field_1057_z.add(new WorldBlockPositionType(this, var1, var2, var3, var5, var6));
            return true;
        }

        return false;
    }

    public boolean setBlockAndMetadata(int var1, int var2, int var3, int var4, int var5) {
        int var6 = this.getBlockId(var1, var2, var3);
        int var7 = this.getBlockMetadata(var1, var2, var3);
        if (super.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
            this.field_1057_z.add(new WorldBlockPositionType(this, var1, var2, var3, var6, var7));
            return true;
        }

        return false;
    }

    public boolean setBlock(int var1, int var2, int var3, int var4) {
        int var5 = this.getBlockId(var1, var2, var3);
        int var6 = this.getBlockMetadata(var1, var2, var3);
        if (super.setBlock(var1, var2, var3, var4)) {
            this.field_1057_z.add(new WorldBlockPositionType(this, var1, var2, var3, var5, var6));
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

    public void sendQuittingDisconnectingPacket() {
        this.sendQueue.func_28117_a(new Packet255KickDisconnect("Quitting"));
    }

    protected void updateWeather() {
        if (this.worldProvider.hasNoSky)
            return;

        if (this.field_27168_F > 0) {
            --this.field_27168_F;
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
