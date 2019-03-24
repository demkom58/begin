package net.minecraft;

import net.minecraft.server.MinecraftServer;
import util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class WorldServer extends World {
    public ChunkProviderServer chunkProviderServer;
    public boolean field_819_z = false;
    public boolean levelSaving;
    private MinecraftServer mcServer;
    private MCHash hash = new MCHash();

    public WorldServer(MinecraftServer mcServer, ISaveHandler saveHandler, String var3, int var4, long var5) {
        super(saveHandler, var3, var5, WorldProvider.getProviderForDimension(var4));
        this.mcServer = mcServer;
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean chunk) {
        if (!this.mcServer.spawnPeacefulMobs && (entity instanceof EntityAnimal || entity instanceof EntityWaterMob)) {
            entity.setEntityDead();
        }

        if (entity.riddenByEntity == null || !(entity.riddenByEntity instanceof EntityPlayer)) {
            super.updateEntityWithOptionalForce(entity, chunk);
        }

    }

    public void superUpdateEntityWithOptionalForce(Entity entity, boolean chunk) {
        super.updateEntityWithOptionalForce(entity, chunk);
    }

    protected IChunkProvider createChunkProvider() {
        IChunkLoader var1 = this.worldFile.func_22092_a(this.worldProvider);
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

    public boolean canMineBlock(EntityPlayer var1, int var2, int var3, int var4) {
        int var5 = (int) MathHelper.abs((float) (var2 - this.worldInfo.getSpawnX()));
        int var6 = (int) MathHelper.abs((float) (var4 - this.worldInfo.getSpawnZ()));
        if (var5 > var6) {
            var6 = var5;
        }

        return var6 > 16 || this.mcServer.configManager.isOp(var1.username);
    }

    protected void obtainEntitySkin(Entity entity) {
        super.obtainEntitySkin(entity);
        this.hash.addKey(entity.entityId, entity);
    }

    protected void releaseEntitySkin(Entity entity) {
        super.releaseEntitySkin(entity);
        this.hash.removeObject(entity.entityId);
    }

    public Entity func_6158_a(int var1) {
        return (Entity) this.hash.lookup(var1);
    }

    public boolean addLightningBolt(Entity var1) {
        if (super.addLightningBolt(var1)) {
            this.mcServer.configManager.sendPacketToPlayersAroundPoint(var1.posX, var1.posY, var1.posZ, 512.0D, this.worldProvider.worldType, new Packet71Weather(var1));
            return true;
        } else {
            return false;
        }
    }

    public void sendTrackedEntityStatusUpdatePacket(Entity var1, byte var2) {
        Packet38EntityStatus var3 = new Packet38EntityStatus(var1.entityId, var2);
        this.mcServer.getEntityTracker(this.worldProvider.worldType).sendPacketToTrackedPlayersAndTrackedEntity(var1, var3);
    }

    public Explosion newExplosion(Entity exploder, double x, double y, double z, float size, boolean flaming) {
        Explosion explosion = new Explosion(this, exploder, x, y, z, size);
        explosion.isFlaming = flaming;
        explosion.doExplosion();
        explosion.doEffects(false);
        this.mcServer.configManager.sendPacketToPlayersAroundPoint(x, y, z, 64.0D, this.worldProvider.worldType, new Packet60Explosion(x, y, z, size, explosion.destroyedBlockPositions));
        return explosion;
    }

    public void playNoteAt(int var1, int var2, int var3, int var4, int var5) {
        super.playNoteAt(var1, var2, var3, var4, var5);
        this.mcServer.configManager.sendPacketToPlayersAroundPoint((double) var1, (double) var2, (double) var3, 64.0D, this.worldProvider.worldType, new Packet54PlayNoteBlock(var1, var2, var3, var4, var5));
    }

    public void func_30006_w() {
        this.worldFile.func_22093_e();
    }

    protected void updateWeather() {
        boolean var1 = this.func_27068_v();
        super.updateWeather();
        if (var1 != this.func_27068_v()) {
            if (var1) {
                this.mcServer.configManager.sendPacketToAllPlayers(new Packet70Bed(2));
            } else {
                this.mcServer.configManager.sendPacketToAllPlayers(new Packet70Bed(1));
            }
        }

    }
}
