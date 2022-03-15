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
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
    }

    @Override
    public void obtainEntitySkin(Entity var1) {
        this.server.getEntityTracker(this.field_28134_b.worldProvider.worldType).trackEntity(var1);
    }

    @Override
    public void releaseEntitySkin(Entity var1) {
        this.server.getEntityTracker(this.field_28134_b.worldProvider.worldType).untrackEntity(var1);
    }

    @Override
    public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
    }

    @Override
    public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
    }

    @Override
    public void updateAllRenderers() {
    }

    @Override
    public void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3) {
        this.server.configManager.markBlockNeedsUpdate(var1, var2, var3, this.field_28134_b.worldProvider.worldType);
    }

    @Override
    public void playRecord(String var1, int var2, int var3, int var4) {
    }

    @Override
    public void doNothingWithTileEntity(int var1, int var2, int var3, TileEntity var4) {
        this.server.configManager.sentTileEntityToPlayer(var1, var2, var3, var4);
    }

    @Override
    public void playEffect(EntityPlayer var1, int var2, int x, int y, int z, int var6) {
        this.server.configManager.func_28171_a(var1, x, y, z, 64.0D, this.field_28134_b.worldProvider.worldType, new Packet61DoorChange(var2, x, y, z, var6));
    }
}
