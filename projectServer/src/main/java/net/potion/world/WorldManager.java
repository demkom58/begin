package net.potion.world;

import net.potion.entity.Entity;
import net.potion.entity.player.EntityPlayer;
import net.potion.network.packet.Packet61DoorChange;
import net.potion.server.PotionServer;
import net.potion.tileentity.TileEntity;

public class WorldManager implements IWorldAccess {
    private PotionServer server;
    private WorldServer field_28134_b;

    public WorldManager(PotionServer server, WorldServer var2) {
        this.server = server;
        this.field_28134_b = var2;
    }

    @Override
    public void spawnParticle(String particleName, double x, double y, double z, double motionX, double motionY, double motionZ) {
    }

    @Override
    public void obtainEntitySkin(Entity entity) {
        this.server.getEntityTracker(this.field_28134_b.worldProvider.worldType).trackEntity(entity);
    }

    @Override
    public void releaseEntitySkin(Entity entity) {
        this.server.getEntityTracker(this.field_28134_b.worldProvider.worldType).untrackEntity(entity);
    }

    @Override
    public void playSound(String soundCategory, double x, double y, double z, float volume, float pitch) {
    }

    @Override
    public void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    }

    @Override
    public void updateAllRenderers() {
    }

    @Override
    public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
        this.server.configManager.markBlockNeedsUpdate(x, y, z, this.field_28134_b.worldProvider.worldType);
    }

    @Override
    public void playRecord(String recordName, int x, int y, int z) {
    }

    @Override
    public void doNothingWithTileEntity(int x, int y, int z, TileEntity tile) {
        this.server.configManager.sentTileEntityToPlayer(x, y, z, tile);
    }

    @Override
    public void playEffect(EntityPlayer player, int effectId, int x, int y, int z, int subData) {
        this.server.configManager.func_28171_a(player, x, y, z, 64.0D, this.field_28134_b.worldProvider.worldType, new Packet61DoorChange(effectId, x, y, z, subData));
    }
}
