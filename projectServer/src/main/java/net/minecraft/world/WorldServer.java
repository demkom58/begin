package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAnimal;
import net.minecraft.entity.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Explosion;
import net.minecraft.util.Hash;
import net.hypnosis.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkProviderServer;
import net.minecraft.world.chunk.IChunkLoader;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;

import java.util.ArrayList;
import java.util.List;

public class WorldServer extends World {
    public ChunkProviderServer chunkProviderServer;
    public boolean field_819_z = false;
    public boolean levelSaving;
    private MinecraftServer server;
    private Hash hash = new Hash();

    public WorldServer(MinecraftServer server, ISaveHandler saveHandler, String var3, int var4, long var5) {
        super(saveHandler, var3, var5, WorldProvider.getProviderForDimension(var4));
        this.server = server;
    }

    @Override
    public void updateEntityWithOptionalForce(Entity entity, boolean chunk) {
        if (!this.server.spawnPeacefulMobs && (entity instanceof EntityAnimal || entity instanceof EntityWaterMob)) {
            entity.setEntityDead();
        }

        if (!(entity.riddenByEntity instanceof EntityPlayer)) {
            super.updateEntityWithOptionalForce(entity, chunk);
        }

    }

    public void superUpdateEntityWithOptionalForce(Entity entity, boolean chunk) {
        super.updateEntityWithOptionalForce(entity, chunk);
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        IChunkLoader var1 = this.saveHandler.getChunkLoader(this.worldProvider);
        this.chunkProviderServer = new ChunkProviderServer(this, var1, this.worldProvider.getChunkProvider());
        return this.chunkProviderServer;
    }

    public List<TileEntity> getTileEntityList(int var1, int var2, int var3, int var4, int var5, int var6) {
        ArrayList<TileEntity> tileEntities = new ArrayList<>();

        for (int i = 0; i < this.loadedTileEntityList.size(); ++i) {
            TileEntity tileEntity = this.loadedTileEntityList.get(i);
            if (tileEntity.xCoord >= var1 && tileEntity.yCoord >= var2 && tileEntity.zCoord >= var3 && tileEntity.xCoord < var4 && tileEntity.yCoord < var5 && tileEntity.zCoord < var6) {
                tileEntities.add(tileEntity);
            }
        }

        return tileEntities;
    }

    @Override
    public boolean canMineBlock(EntityPlayer var1, int var2, int var3, int var4) {
        int var5 = (int) MathHelper.abs((float) (var2 - this.worldInfo.getSpawnX()));
        int var6 = (int) MathHelper.abs((float) (var4 - this.worldInfo.getSpawnZ()));
        if (var5 > var6) {
            var6 = var5;
        }

        return var6 > 16 || this.server.configManager.isOp(var1.username);
    }

    @Override
    protected void obtainEntitySkin(Entity entity) {
        super.obtainEntitySkin(entity);
        this.hash.addKey(entity.entityId, entity);
    }

    @Override
    protected void releaseEntitySkin(Entity entity) {
        super.releaseEntitySkin(entity);
        this.hash.removeObject(entity.entityId);
    }

    public Entity func_6158_a(int var1) {
        return (Entity) this.hash.lookup(var1);
    }

    @Override
    public boolean addWeatherEffect(Entity var1) {
        if (super.addWeatherEffect(var1)) {
            this.server.configManager.sendPacketToPlayersAroundPoint(var1.posX, var1.posY, var1.posZ, 512.0D, this.worldProvider.worldType, new Packet71Weather(var1));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendTrackedEntityStatusUpdatePacket(Entity entity, byte status) {
        Packet38EntityStatus var3 = new Packet38EntityStatus(entity.entityId, status);
        this.server.getEntityTracker(this.worldProvider.worldType).sendPacketToTrackedPlayersAndTrackedEntity(entity, var3);
    }

    @Override
    public Explosion newExplosion(Entity exploder, double x, double y, double z, float size, boolean flaming) {
        Explosion explosion = new Explosion(this, exploder, x, y, z, size);
        explosion.isFlaming = flaming;
        explosion.doExplosionA();
        explosion.doExplosionB(false);
        this.server.configManager.sendPacketToPlayersAroundPoint(x, y, z, 64.0D, this.worldProvider.worldType, new Packet60Explosion(x, y, z, size, explosion.destroyedBlockPositions));
        return explosion;
    }

    @Override
    public void playNoteAt(int var1, int var2, int var3, int instrumentType, int pitch) {
        super.playNoteAt(var1, var2, var3, instrumentType, pitch);
        this.server.configManager.sendPacketToPlayersAroundPoint(var1, var2, var3, 64.0D, this.worldProvider.worldType, new Packet54PlayNoteBlock(var1, var2, var3, instrumentType, pitch));
    }

    public void clearCache() {
        this.saveHandler.clearCache();
    }

    @Override
    protected void updateWeather() {
        boolean var1 = this.isSmallRain();
        super.updateWeather();
        if (var1 != this.isSmallRain()) {
            if (var1) {
                this.server.configManager.sendPacketToAllPlayers(new Packet70Bed(2));
            } else {
                this.server.configManager.sendPacketToAllPlayers(new Packet70Bed(1));
            }
        }

    }
}
